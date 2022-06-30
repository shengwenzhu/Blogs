# Redis 数据类型

## 1. 字符串

### 🚩1）使用

Redis 字符串可以存储 3 种类型的值：字符串、整数、浮点数；

使用场景：

+ 计数器

  ```
  // 统计一个视频的播放次数
  incr(video::playCount)
  ```

+ **限速器**

  建议浏览：http://doc.redisfans.com/string/incr.html

  典型用法是限制公开 API 的请求次数，以下是一个限速器实现示例，它将 API 的最大请求数限制在每个 IP 地址每秒钟十个之内：

  ```java
  FUNCTION LIMIT_API_CALL(ip)
  	ts = CURRENT_UNIX_TIME()
  	keyname = ip+":"+ts		//每秒钟为每个 IP 地址使用一个不同的计数器
  	current = GET(keyname)
  
  	IF current != NULL AND current > 10 THEN
      	ERROR "too many requests per second"
  	END
  
  	IF current == NULL THEN
      	MULTI
          	INCR(keyname, 1)
          	EXPIRE(keyname, 1)	// 用 EXPIRE 命令设置生存时间
      	EXEC
  	ELSE
      	INCR(keyname, 1)
  	END
  
  	PERFORM_API_CALL()
  ```

  ```java
  FUNCTION LIMIT_API_CALL(ip):
  current = GET(ip)	// 只使用单个计数器，它的生存时间为一秒钟，如果在一秒钟内，这个计数器的值大于 10 的话，那么访问就会被禁止
  IF current != NULL AND current > 10 THEN
      ERROR "too many requests per second"
  ELSE
      value = INCR(ip)
      IF value == 1 THEN
          EXPIRE(ip,1)
      END
      PERFORM_API_CALL()
  END
  ```



### 2）编码方式

字符串类型的编码方式有 3 种：

+ int：8个字节的长整型
+ embstr：小于等于39个字节的字符串
+ raw：大于39个字节的字符串

```bash
127.0.0.1:6379> set num 3.1415
OK
127.0.0.1:6379> object encoding num
"embstr"
127.0.0.1:6379> set num2 1234
OK
127.0.0.1:6379> object encoding num2
"int"
```



## 2. 列表

列表用于存储多个有序的字符串；

### 🚩1）使用

常用操作：

+ 支持从列表两端插入(push)或弹出(pop)元素；

  ```bash
  127.0.0.1:6379> rpush list a b c d
  (integer) 4
  127.0.0.1:6379> rpop list
  "d"
  127.0.0.1:6379> lrange list 0 -1
  1) "a"
  2) "b"
  3) "c"
  ```

+ 支持**阻塞式(blocking)弹出**：当给定列表内没有任何元素可供弹出的时候，操作将被阻塞，直到等待超时或发现可弹出元素为止；

  ```bash
  127.0.0.1:6379> exists list
  (integer) 0
  127.0.0.1:6379> blpop list 5	# key 不存在，操作被阻塞，等待超时后退出
  (nil)
  (5.09s)
  127.0.0.1:6379> blpop list 120	# 此时打开另一个客户端对列表进行插入操作
  1) "list"	# 弹出元素所属的列表
  2) "hello"
  (31.41s)
  ```

  **多个客户端被同一个 key 同时阻塞**：不同的客户端被放进一个队列中，按『先阻塞先服务』的顺序执行操作；

<span style="color:red;font-weight:bold;">使用场景：充当栈或队列的角色</span>

+ **消息队列**

  使用 **lpush+brpop** 命令可以实现消息队列，生产者客户端通过 lpush 命令从列表左侧插入消息，消费者客户端通过使用 brpop 命令阻塞式的从列表右侧取得消息；

  存在的问题：上述实现的消息队列不安全，存在一个客户端在取出一个消息后崩溃，导致消息未处理并且丢失；

  优化：安全的队列，使用 [RPOPLPUSH](http://doc.redisfans.com/list/rpoplpush.html#rpoplpush) 命令(或者 [BRPOPLPUSH](http://doc.redisfans.com/list/brpoplpush.html#brpoplpush) )可以解决这个问题，通过该命令可以从列表中取得一条消息，并且还将这个消息添加到一个备份列表中，如果一切正常的话，当一个客户端完成某个消息的处理之后，可以用 [*LREM*](http://doc.redisfans.com/list/lrem.html#lrem) 命令将这个消息从备份表删除；最后，还可以添加一个客户端专门用于监视备份表，它自动地将超过一定处理时限的消息重新放入队列中去(负责处理该消息的客户端可能已经崩溃)，这样就不会丢失任何消息了。

### 2）内部编码

Redis 3.0 版本之前内部编码有两种：

+ ziplist（压缩列表）
+ linkedlist（链表）

Redis 3.2 版本之后内部编码只有一种：quicklist

```bash
127.0.0.1:6379> object encoding list
"quicklist"
```

### 3）底层实现

Redis 3.2 版本之前列表由 **压缩列表** 或 **链表** 实现；在 3.2 版本之后，引入了 **QuickList** 数据结构，列表的底层都由 QuickList 实现；



## 3. 哈希

### 1）使用

用于存储键值对；

+ 常用操作

  ```bash
  127.0.0.1:6379> hmset xiaohu name liyuanhao age 23 sex male
  OK
  127.0.0.1:6379> hget xiaohu age
  "23"
  127.0.0.1:6379> hkeys xiaohu
  1) "name"
  2) "age"
  3) "sex"
  127.0.0.1:6379> hvals xiaohu
  1) "liyuanhao"
  2) "23"
  3) "male"
  ```

### 2）编码

+ ziplist：当元素个数小于 hash-max-ziplist-entrie （默认512个）、同时所有元素的存储大小都小于 hash-max-ziplist-value（默认64字节） 时，使用 ziplist 做为哈希的底层实现；

  ```bash
  127.0.0.1:6379> object encoding xiaohu
  "ziplist"
  ```

+ hashtable：当不满足ziplist的配置条件时，使用 hashtable 作为哈希的底层实现；

> ```
> # redis 配置项
> 127.0.0.1:6379> config get hash*
> 1) "hash-max-ziplist-entries"
> 2) "512"
> 3) "hash-max-ziplist-value"
> 4) "64"
> ```



## 4. 集合

集合用于存储多个字符串，集合中不允许有重复元素；

### 1）使用

Redis 除了支持集合内的增删改查，同时支持多个集合取交集、并集、差集；

### 2）内部编码

集合类型的内部编码有两种：

+ 整数集合：当集合中的元素都是整数且元素个数小于 `set-max-intset-entries` 配置时，使用整数集合做为集合的底层实现；
+ 哈希表：当不满足整数集合的条件时，使用哈希表做为集合的底层实现；



## 5. 有序集合

在 Redis 集合的基础上，可以对集合中的元素进行排序；

有序集合给每个元素设置了一个分值（score），按照分值大小对集合元素进行排序；

### 1）内部编码

有序集合支持两种编码方式：

+ 压缩列表：当有序集合的元素个数小于 zset-max-ziplist-entries 配置值，且每个元素的长度小于 zset-max-ziplist-value 配置值（默认64字节）时，使用压缩列表做为有序集合的底层实现；
+ 跳跃表：当不满足压缩列表时，使用跳跃表做为有序集合的底层实现；

注：使用跳跃表作为底层实现时，有序集合通过维护一个字典建立元素到分值的映射，方便通过元素进行快速查询；

```c
// 有序集合结构定义
typedef struct zset {
    // 字典用于快速查找指定元素对应的分值
    dict *dict;
    // 跳跃表用于根据分值大小进行快速查询
    zskiplist *zsl;
} zset;
```

