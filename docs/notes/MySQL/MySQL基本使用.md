# 一、MySQL 数据类型

MySQL支持的数据类型非常多：数值类型、日期时间类型、字符串类型。

## 1.1 数值类型

+ **整数类型**

  MySQL支持标准SQL整数类型：`INTEGER`（与`INT`同义）、`SMALLINT`。

  并进一步扩展，支持`TINYINT`、`MEDIUMINT`和`BIGINT`。

  | 整数类型  | 存储大小（单位：字节） |
  | --------- | ---------------------- |
  | TINYINT   | 1                      |
  | SMALLINT  | 2                      |
  | MEDIUMINT | 3                      |
  | INT       | 4                      |
  | BIGINT    | 8                      |

  MySQL对于每个整数类型都提供了可选的 `unsigned` 属性，表示无符号整数。

  MySQL可以为整数类型指定宽度，如`int(5)`，宽度在大多数场景是没有意义的，它不会限制值的合法范围，只是规定了MySQL的一些交互工具（如MySQL命令行客户端）中显示字符的个数。对于存储和计算来说，`int(1)`和`int(5)`是相同的。

  > 整数类型的选择决定了数据在内存和磁盘中如何存储，但是整数计算时都会转换为BIGINT进行计算，即使在32位机器上也是如此。

+ **实数类型**

  此处，实数表示带有小数部分的数字。

  MySQL对于带有小数部分的数字支持两种存储方式：精确类型（DECIMAL）、近似类型（FLOAT和DOUBLE）。

  | 类型    | 存储大小（单位：字节）                   |
  | ------- | ---------------------------------------- |
  | FLOAT   | 4                                        |
  | DOUBLE  | 8                                        |
  | DECIMAL | 对DECIMAL(M,D) ，如果M>D，为M+2否则为D+2 |

  > MySQL 使用 `DOUBLE` 作为内部浮点计算的类型，选择`DECIMAL`类型只是选择了存储类型，但是计算时`DECIMAL`类型会转换为 `DOUBLE` 类型。
  >
  > `DECIMAL`类型通常会比浮点类型使用更多的空间，所以尽量只在对小数进行精确存储时使用。

  浮点类型和 Decimal 类型都可以指定精度，从而控制小数点前后允许的最大位数。

  ```mysql
  decimal(11,5)	// 11表示值总共可存储的位数，5表示小数点后可存储的位数
  double(11,5)	// 11表示值总共可存储的位数，5表示小数点后可存储的位数
  ```

## 1.2 日期时间类型

每个日期时间类型都存在一个有效表示范围。

| 类型      | 存储大小 ( 单位：字节) | 范围                                               | 格式                | 备注                     |
| :-------- | :--------------------- | -------------------------------------------------- | :------------------ | :----------------------- |
| DATE      | 3                      | 1000-01-01 到 9999-12-31'                          | YYYY-MM-DD          | 只有日期部分没有时间部分 |
| TIME      | 3                      |                                                    | HH:MM:SS            |                          |
| YEAR      | 1                      |                                                    | YYYY                |                          |
| DATETIME  | 8                      | 1000-01-01 00:00:00 到 9999-12-31 23:59:59         | YYYY-MM-DD HH:MM:SS | 与时区无关               |
| TIMESTAMP | 4                      | 1970-01-01 00:00:00 UTC 到 2038-01-19 03:14:07 UTC | YYYYMMDD HHMMSS     | 显示的值与时区有关       |

### DATETIME 和 TIMESTAMP 类型

MySQL将TIMESTAMP值从当前时区转换为UTC来存储，然后从UTC转换回当前时区以进行检索。

+ **时间值的小数秒部分**

  MySQL5.7开始，DATETIME 和 TIMESTAMP 类型支持存储小数秒部分（即毫秒、微妙），精度最高可达微妙。——《MySQL5.7官方参考手册》

  ```mysql
  # 存储小数秒示例
  1000-01-01 00:00:00.000000
  9999-12-31 23:59:59.999999
  ```

  要定义包含小数秒部分的列，使用语法`type_name(fsp)`，其中type_name可以是TIME、DATETIME或TIMESTAMP，而fsp是小数秒精度（0=<fsp<=6）。

  ```mysql
  create table time_test (t1 timestamp(3), t2 datetime(6));
  ```

  将带有小数秒部分的值插入到相同类型的列中，如果插入列允许的小数位数较少，MySQL会进行舍入。

  > MySQL5.7之前，如何存储比秒更小粒度的时间值？
  >
  > + 方式一：使用BIGINT类型存储微妙级别的时间戳
  > + 方式二：使用DUOBLE存储秒之后的小数部分

+ **自动初始化和自动更新**

  对于表中的任何TIMESTAMP或DATETIME列，可以指定初始默认值或自动更新值为当前时间戳。

  > 当插入行时未指定值时使用初始默认值；
  >
  > 当对行中的其他列值进行更新时，自动更新时间到当前时间戳。

  ```mysql
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  ```

  > CURRENT_TIMESTAMP的同义词（即可以使用其他词进行代替）：CURRENT_TIMESTAMP(), NOW(), LOCALTIME, LOCALTIME(), LOCALTIMESTAMP, and
  > LOCALTIMESTAMP()

  > 系统变量： `explicit_defaults_for_timestamp`（该系统变量已被弃用，其控制的时间戳行为也被弃用）
  >
  > 该变量用于控制TIMESTAMP列的默认值和空值处理。
  >
  > ```mysql
  > mysql> show variables like 'explicit_defaults_for_timestamp';
  > +---------------------------------+-------+
  > | Variable_name                   | Value |
  > +---------------------------------+-------+
  > | explicit_defaults_for_timestamp | OFF   |
  > +---------------------------------+-------+
  > ```
  >
  > 该系统变量的默认值为OFF，此时将按照如下方式处理TIMESTAMP列：
  >
  > + 如果没有显式指定TIMESTAMP列可以包含NULL值，TIMESTAMP列将自动声明为 NOT NULL 列，
  > + 表中的第一个TIMESTAMP列，如果没有显式地用NULL属性或显式的DEFAULT或ON UPDATE属性声明，则会自动地用DEFAULT  CURRENT_TIMESTAMP和ON UPDATE CURRENT_TIMESTAMP属性声明。

## 1.3 字符串类型

MySQL支持多种字符串类型。

从MySQL 4.1 开始，每个字符串列可以定义自己的字符集和排序规则，这些在很大程度上会影响性能。













| 类型       | 空间大小（与编码无关）              | 备注                         |
| :--------- | :---------------------------------- | :--------------------------- |
| CHAR       | 最多 255 个字符                     | 定长字符串                   |
| VARCHAR    | 最多 65535 个字符                   | 变长字符串                   |
| TINYBLOB   | 最多 255 个字符                     | 二进制字符串                 |
| TINYTEXT   | 最多 255 个字符                     | 短文本字符串                 |
| BLOB       | 最多 65535 个字符，即 `2^16-1`      | 二进制形式的长文本数据       |
| TEXT       | 最多 65535 个字符                   | 长文本数据                   |
| MEDIUMBLOB | 最多 16777215 个字符，即 `2^24-1`   | 二进制形式的中等长度文本数据 |
| MEDIUMTEXT | 最多 16777215 个字符                | 中等长度文本数据             |
| LONGBLOB   | 最多 4294967295 个字符，即 `2^32-1` | 二进制形式的极大文本数据     |
| LONGTEXT   | 最多 4294967295 个字符              | 极大文本数据                 |

### 1.1 varchar 和 char 的使用

`char(n)` 和 `varchar(n)` 括号中 `n` 代表字符的个数，并不代表字节个数；

`char` 表示定长字符串，长度是固定的，如果插入数据的长度小于 char 的固定长度，使用空格填充；

varchar与char的区别：

+ 由于长度固定，`char`的存取速度要比 `varchar` 快得多，方便程序的存储与查找，但也因为其长度固定，所以可能会占用多余的空间；
+ 存储字符数量的限制：对于`char`来说，最多能存放的字符个数为255，和编码无关；对于 `varchar` 来说，最多能存放的字符个数为65532；

















## 优化数据类型

+ 尽量使用能够正确存储数据的最小数据类型；

  > 更小的数据类型通常处理速度更快，因为占用更少的磁盘、内存和CPU缓存。
  >
  > 如果存储的数据量很大，后期修改数据类型是一个非常耗时的操作。

+ 选择合适的数据类型
  + 整型比字符串的操作代价更低；
  + 使用日期时间类型存储日期和时间，而不是字符串；
  + 使用整型存储IP地址。




































## 2. SQL 语句分类

DDL：数据定义语言；
		创建数据库中的各种对象-----表、视图、索引、同义词、聚簇等；

DCL：数据控制语言；

​		授予访问数据库的权限，控制事务的提交或回滚；

DML：数据操纵语言；增删改查操作；

## 3. MySQL 服务器

```mysql
-- 启动 MySQL服务器  
	net start mysql
-- 连接 MySQL服务器
	mysql -h 地址 -P 端口 -u 用户名 -p 密码
-- 显示运行的线程  
	SHOW PROCESSLIST;
-- 显示系统变量信息
	SHOW VARIABLES;
```

## 4. 数据库操作

```mysql
-- 创建数据库
	CREATE DATABASE[ IF NOT EXISTS] 数据库名 数据库选项
    -- 数据库选项
        [DEFAULT] CHARACTER SET [=] charset_name
        [DEFAULT] COLLATE [=] collation_name
        DEFAULT ENCRYPTION [=] {'Y' | 'N'}
-- 查看已有数据库  
	SHOW DATABASES;
-- 使用数据库
	USE 数据库名;
-- 检索数据库中已有的数据表
	SHOW TABLES;
-- 查看当前数据库
	SELECT DATABASE();
-- 显示用户名
	SELECT user();
-- 显示数据库版本
	SELECT version();
-- 修改数据库的选项信息
	ALTER DATABASE 库名 选项信息
-- 删除数据库
	DROP DATABASE[ IF EXISTS] 数据库名
```

## 5. 数据表操作

### 创建表

```mysql
-- 语法：
	CREATE [TEMPORARY] TABLE [IF NOT EXISTS] 表名
    (
        col_name  数据类型  [NULL | NOT NULL]  [DEFAULT default_value]  [AUTO_INCREMENT], 
        col_name  ...  ,
        ...,
        [PRIMARY KEY(...)]
    )
    [表选项]
    
    -- 使用注意事项：
        TEMPORARY ：临时表，会话结束时表自动消失
        AUTO_INCREMENT：每个表只允许一个AUTO_INCREMENT列，而且它必须被索引
    -- 表选项
        -- 字符集
            CHARSET = charset_name
            如果表没有设定，则使用数据库字符集
        -- 存储引擎
            ENGINE = engine_name
            -- 显示存储引擎的状态信息       
            SHOW ENGINES
            -- 显示存储引擎的日志或状态信息
            SHOW ENGINE 引擎名 {LOGS|STATUS}
        -- 自增起始数
            AUTO_INCREMENT = 行数
        -- 数据文件目录
            DATA DIRECTORY = '目录'
        -- 索引文件目录
            INDEX DIRECTORY = '目录'
        -- 表注释
            COMMENT = 'string'
        -- 分区选项
            PARTITION BY ... 
    -- 示例：   
    CREATE TABLE `templet` (
        `No.` mediumint(9) NOT NULL AUTO_INCREMENT,
        `Name` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
        `Age` int(5) DEFAULT NULL,
        PRIMARY KEY (`No.`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 STATS_AUTO_RECALC=1;
```

### 复制表(创建表的另一种形式)

```mysql
-- 只复制表结构
	-- 方式1：不会复制主键类型，索引等信息
	CREATE TABLE 新表 SELECT * FROM 旧表 WHERE 1=2;
	-- 方式2：全部复制
	CREATE TABLE 新表 LIKE 旧表;
-- 复制表结构和数据
	CREATE TABLE 新表 [AS] SELECT * FROM 旧表;
	注：其实只是把select语句的结果建一个表，所以新表不会有主键，索引；
-- 只复制表数据
	-- 两个表结构一样
	INSERT INTO 新表 SELECT * FROM 旧表;
	-- 两个表结构不一样
	INSERT INTO 新表(column1,column2...) SELECT column1,column2... FROM 旧表;	
```

### 检索数据库中数据表

```mysql
SHOW TABLES[ LIKE 'pattern']
SHOW TABLES FROM  库名
```

### 查看表结构

```mysql
-- 方式1：以SQL语句的形式呈现
	SHOW CREATE TABLE 表名
-- 方式2：以表格的形式呈现
	DESC|DESCRIBE|EXPLAIN 表名
	SHOW COLUMNS FROM 表名
```

### 修改表结构（ALTER）

```mysql
-- 语法格式：
	ALTER TABLE 表名 [修改选项]
	
	常见的修改选项：
	-- 修改表名
		RENAME TO <新表名>
	-- 修改字段数据类型
		MODIFY COLUMN <列名> <类型>
	-- 修改字段名
		CHANGE COLUMN <旧字段名> <新字段名> <新数据类型>
		注：如果不需要修改字段的数据类型，可以将新数据类型设置成与原来一样，但数据类型不能为空
	-- 增加新字段
		ADD [COLUMN] <新字段名> <数据类型> [约束条件] [FIRST|AFTER 已存在的字段名]；
		注：默认将新添加的字段设置为数据表的最后列，FIRST 作用是将新添加的字段设置为表的第一个字段；AFTER 作用是将新添加的字段添加到指定的已存在的字段			名的后面；
	-- 删除字段
		DROP [COLUMN] <字段名>
	-- 修改存储引擎
		engine = myisam;
	-- 修改字段的相对位置
		MODIFY 字段名 数据类型 FIRST|AFTER 字段名2
	-- 创建主键
		ADD PRIMARY KEY(字段名)
	-- 删除主键(删除主键前需删除其AUTO_INCREMENT属性)
		DROP PRIMARY KEY
	-- 创建外键
		ADD CONSTRAINT 外键名
        FOREIGN KEY (字段1 [,...])
        REFERENCES 主表(字段1 [,...])
	-- 删除外键约束
		DROP FOREIGN KEY 键名
```

### 删除表

```mysql
DROP TABLE [IF EXISTS] 表名 [, 表2名, ...] 
```

### 清空表数据

```mysql
TRUNCATE [TABLE] 表名
```



## 6. insert/delete/update

+ 插入

  可以通过四种方式插入数据：

  + 插入完整的行；
  + 插入行的一部分；
  + 插入多行；
  + 插入查询的结果；

  ```sql
  -- 插入完整的行
  	INSERT INTO tbl_name
  	[(col_name, ...)]	-- 应该尽量避免省略这一部分，否则将高度依赖于表中列的定义次序，以及需要给每一列给出一个值
  	VALUES(value_1, value_2, ... );
  	
  -- 插入行的一部分
  	插入一行数据时只给其中一部分列提供值；
  	省略的列必须满足以下某个条件：
  		该列允许NULL值（无值或空值）；
  		在表定义中给出默认值；
  	
  -- 插入多个行
  	单条 INSERT语句有多组值，每组值用一对圆括号括起来，用逗号分隔；
  	VALUE (...),(...), ...;
  
  -- 插入查询的结果
  	INSERT INTO tbl_name
  	(col_name, ...)
  	SELECT (col_name, ...)
  	FROM tbl_name;
  ```

+ 更新数据

  ```sql
  -- UPDATE 语句由3部分组成：1.要更新的表；2.列名和他们的新值；3.确定要更新行的过滤条件
  -- 语法格式：
      UPDATE tbl_name
      SET col_name = value_1 [, col2_name = value_2, ...]		-- 更新多个列时，只需要使用单个SET命令，每个“列=值”对之间用逗号分隔
      WHERE ...		-- 省略WHERE子句将更新所有的行
  -- 使用注意事项：
  	UPDATE 语句中可以使用子查询检索出的数据更新列数据；
  	如果用 UPDATE语句更新多行，在更新这些行中的一行或多行时出现错误，则整个 UPDATE操作被取消（错误发生前更新的所有行被恢复到它们原来的值）。如果在即使发生错误的情况下也继续进行更新，可使用IGNORE关键字： UPDATE IGNORE tbl_name…
  	可以使用 UPDATE来删除某个列的值，设置该列值为 NULL;
  ```

+ 删除数据

  ```sql
  DELETE FROM tbl_name
  WHERE ...		
  -- 省略WHERE子句将删除所有的行；如果要删除所有的行，可使用TRUNCATE TABLE语句，它完成相同的工作，但速度更快
  ```

  

## 7. 视图（MySQL 5）

1. 什么是视图

   视图是虚拟表，**由一个 SQL 查询定义**，可以当做表使用；

   视图的数据是从其他表中检索出来的，在添加或更改这些表中的数据时，视图将返回改变后的数据；

2. 视图作用

   + 简化复杂查询：
   + 提高了数据库的安全性：数据库视图允许创建只读视图；争对不同的用户设定不同的视图；
   + 重用SQL语句；
   + 格式化基础数据；

   视图缺点：性能（从数据库视图查询数据可能会很慢）、表依赖关系（每当更改与其相关联的表的结构时，都必须更改视图）；

```sql
-- 创建视图
	-- 视图名必须唯一
	-- 视图不能索引，也不能有关联的触发器
	CREATE [OR REPLACE]
    VIEW view_name
    [(column_list)]
    AS select_statement		
    -- OR REPLACE 子句：如果视图不存在，创建一个视图，如果视图存在，代替那个视图
    -- 示例
    CREATE VIEW productcustomers AS
    SELECT cust_name, cust_contact, prod_id
    FROM customers, orders, orderitems
    WHERE customers.cust_id = orders.cust_id
    AND orderitems.order_num = orders.order_num;
    
-- 删除视图
	DROP VIEW view_name;
	
-- 更新视图
	视图通常是可更新的（即可以对其进行 insert、update、delete操作）；
	更新一个视图将对其关联的基表进行相应的更新；
	如果 MySQL不能正确地确定被更新的基数据，则不允许更新，如果视图包含如下操作将不允许更新：分组、联结、子查询、并、聚集函数、DISTINCT、导出列；
	一般，应该将视图用于检索而不是更新；
```



## 8. 触发器（MySQL 5）

触发器的作用是在执行 insert、delete 和 update命令之前或之后自动调用 SQL 命令或存储过程；

```sql
/*
 创建触发器
 在创建触发器时，需要给出4条信息：
 	触发器名
 	触发器关联的表
 	触发事件（DELETE、INSERT或UPDATE）
 	触发器何时执行（DELETE、INSERT、UPDATE 操作之前或之后）
*/
	CREATE TRIGGER 触发器名  
    AFTER[BEFORE] INSERT[DELETE|UPDATE] 
    ON 表名 
    FOR EACH ROW 
    自动执行的语句（触发体）
-- 注：每个表中每个事件只允许存在一个触发器，因此，每个表最多支持6个触发器（每条INSERT、UPDATE和DELETE的之前和之后）
-- 如果 BEFORE触发器失败， MySQL将不执行触发事件的操作
-- 如果触发器需要执行多条语句，需要使用BEGIN和END作为开始和结束的标志，

-- 删除触发器
	DROP TRIGGER 触发器名;
```

### INSERT 触发器

在 INSERT触发器中，可引用一个名为NEW的虚拟表，访问被插入的行；

在 BEFORE INSERT 触发器中，NEW 中的值也可以被更新（允许更改被插入的值）；

对于 AUTO_INCREMENT 列，NEW 在 INSERT 执行之前包含0，在INSERT 执行之后包含新的自动生成值；

```mysql
CREATE TRIGGER neworder AFTER INSERT ON orders FOR EACH ROW SELECT NEW.order_num INTO @变量；
-- 每次执行插入操作后都会将order.num赋值给变量；SELECT @变量 就可以查询变量值
```

### DELETE 触发器

在DELETE触发器中，可引用一个名为OLD的虚拟表，访问被删除的行；

OLD中的值全都是只读的，不能更新；

```mysql
CREATE TRIGGER deleteorder BEFORE DELETE ON orders
FOR EACH ROW
BEGIN
	INSERT INTO archive_orders(order_num, order_date, cust_id)
	VALUES(OLD.order_num, OLD.order_date, OLD.cust_id)
END;
-- 触发器使用BEGIN END 语句标记触发体，可以在触发体中执行多条语句
```

### UPDATE 触发器

在UPDATE触发器中，可以引用一个名为OLD的虚拟表访问更新前的值，引用一个名为NEW的虚拟表访问更新后的值；

在BEFORE UPDATE触发器中，NEW中的值可能也被更新（允许更改将要用于UPDATE语句中的值）；

OLD中的值全都是只读的，不能更新；



## 9. 存储过程(MySQL 5)

存储过程：**为了完成特定功能的SQL语句集，经编译后保存在数据库中，用户可通过指定存储过程的名字并给定参数(需要时)来调用执行**；（相当于一个函数）

存储过程作用：

+ 增强SQL语句的功能和灵活性：可以使用流程控制语句，有很强的灵活性，可以完成复杂的判断和较复杂的运算；
+ 提高数据库的访问效率：因为SQL语句已经预编绎过了，因此运行的速度比较快；
+ 提高数据库的安全性和数据的完整性；

存储过程实现了 SQL语言层面的代码封装与重用；

1. 创建存储过程

   ```sql
   -- 创建存储过程
       CREATE PROCEDURE 存储过程名(无参或者有参)
       BEGIN
           mysql语句
       END
   
   -- 声明存储过程语句结束符：mysql命令行程序的分隔符为';'，mysql语句的分隔符也为`;`，所以在命令行程序中创建存储过程时应该临时更改命令行程序的分隔符
       DELIMITER //	-- 使用//作为命令行程序语句分隔符
       创建存储过程
       //				-- 表示创建存储过程结束
       DELIMITER ;		-- 恢复原来的语句分隔符
       
   -- 调用存储过程
   	CALL 存储过程名(参数);
   	
   -- 删除存储过程
   	DROP PROCEDURE 存储过程名;	-- 如果指定的过程不存在，将会产生一个错误，所以可以使用 IF EXISTS 子句
   ```

2. 存储过程的参数

   存储过程参数用于向存储过程传递值或者保存存储过程结果；

   有三种参数类型：IN、OUT、INOUT；

   + IN 输入参数：表示调用者向过程传入值（传入值可以是字面量或变量）；
   + OUT 输出参数：表示过程向调用者传出值(可以返回多个值)（传出值只能是变量）；
   + INOUT 输入输出参数：既表示调用者向过程传入值，又表示过程向调用者传出值（值只能是变量）；

   ```sql
   -- 创建存储过程时需要为参数指定数据类型；
   -- 将存储过程的结果赋值给参数使用`INTO`关键字；
   CREATE PROCEDURE productpricing
   (
   	OUT price_min DECIMAL(8,2),
   	OUT price_max DECIMAL(8,2),
   	OUT price_avg DECIMAL(8,2)
   )
   BEGIN
   	SELECT Min(prod_price) INTO price_min FROM products;
   	SELECT Max(prod_price) INTO price_max FROM products;
   	SELECT Avg(prod_price) INTO price_avg FROM products;
   END
   ```

3. 存储过程中使用变量

   ```sql
   -- 声明变量
   	declare 变量名 数据类型 [DEFAULT value];
   	
   -- 变量赋值
   	SET 变量名 = 值 [,variable_name = expression ...]
   	
   -- 用户变量名一般以@开头
   SELECT 'Hello World' into @message;
   select @message;
   set @message = "study";
   ```

4. 存储过程的控制语句

   + if-then-else 语句

     ```sql
     CREATE PROCEDURE max(IN NUM1 INT, in num2 int, out max_num int)
     begin
     	if(num1>num2) then
     		select num1 into max_num;
     	else
     		select num2 into max_num;
     	end if;
     end;	
     ```

   + case语句

     ```
     CREATE PROCEDURE proc(IN num INT, out num2 int)
     begin
     	case num
     	when 0 then
     		select 0 into num2;
     	when 1 then
     		select 1 into num2;
     	end case;
     end;
     ```

   + while 循环

     ```
     CREATE PROCEDURE proc(IN num INT, out num2 int)
     begin
     	declare temp int;
     	set temp = 0;
     	while num<10 do
     		set temp = temp*2;
     	end while;
     	select temp into num2;
     end;
     ```


## 10. 游标（MySQL 5）

游标存储了MySQL查询结果集，使用游标可以对MySQL检索得到的数据集逐行进行处理；

**MySQL 游标只能用于存储过程**；

1. 使用游标步骤

   - 创建游标
     使用`DECLARE`关键字创建；

     ```mysql
     DECLARE 游标名 CURSOR
     FOR
     SELECT 查询语句;
     ```

   - 打开游标
     执行SELECT 语句得到查询结果集；

     ```mysql
     OPEN 游标名;
     ```

   - 对结果集逐行处理

     使用FETCH 关键字访问每一行；

     ```mysql
     CREATE PROCEDURE students_name()
     BEGIN
     	-- 定义局部变量
     	DECLARE stu_name STRING;
     	-- 定义游标
     	DECLARE name_cursor CURSOR
     	FOR
     	SELECT name FROM students;
     	-- 打开游标
     	OPEN name_cursor;
     	-- 将第一行数据赋值给局部变量
     	FETCH name INTO stu_name;
     	-- 关闭游标
     	CLOSE name_cursor
     END;
     ```

   - 关闭游标

     ```mysql
     CLOSE 游标名;
     ```

     在一个游标关闭后，如果没有重新打开，则不能使用它；

     如果没有关闭游标，MySQL将会在执行到存储过程END语句时自动关闭它。





# 一、MySQL 数据查询及查询性能优化

















```mysql
-- 语法格式
SELECT
	[ALL | DISTINCT | DISTINCTROW ]		-- 默认为ALL,全部记录；
    select_expr [, select_expr ...]
    [FROM table_references
    [WHERE where_condition]
    [GROUP BY {col_name | expr | position}, ... [WITH ROLLUP]]
    [HAVING where_condition]
    [ORDER BY {col_name | expr | position}
      [ASC | DESC], ... [WITH ROLLUP]]
    [LIMIT {[offset,] row_count | row_count OFFSET offset}]
	
	-- DISTINCT 表示查询不重复字段数据；
		如果查询的列具有NULL值，将保留一个NULL值；
		不能部分使用 DISTINCT, DISTINCT关键字应用于全部列而不仅是前置他的列，如 DISTINCT 字段1,字段2,... 表示使用多个列的组合确定是否重复；
		可以将聚合函数(例如 SUM， AVG和 COUNT)和 DISTINCT子句配合使用，如：SELECT COUNT(DISTINCT vend_id) FROM products;
	-- select_expr
		-- 可以使用 * 表示所有字段
		-- 一个或者多个字段
		-- 可以使用表达式（计算公式、函数调用）
		-- 可以使用 AS 关键字为每个select_expr设置别名
```

### where子句

指定查询条件；

```sql
-- where子句操作符
	-- 比较操作符
		=(注意只有一个等号), <>或!=（不等于）, <, <=, >, >=, BETWEEN AND
		使用等号操作符匹配字符串时不区分大小写，如’zhu’与’Zhu’、’zHu’等相等;
		匹配字符串时，需要给字符串加单引号;
	-- 逻辑操作符
		AND, OR，用于连接多个条件
	-- IN操作符
		IN(值1, 值2, ...)
        NOT IN（否定之后的条件，可配合 IN,BETWEEN,EXISTS使用）
	-- 通配符(匹配字符串)
		%（匹配0个、1个或者多个字符）
        _（只匹配单个字符）
        配合 LIKE关键字使用；
        eg: SELECT name FROM student WHERE name like 'Bob%';
    -- NULL值检查
    	IS NULL
	-- 正则表达式(匹配文本)
		配合关键字 REGEXP 使用，
		语法格式：WHERE 匹配的列 REGEXP 正则表达式
		-- 正则表达式特殊字符：
        	.	匹配任意字符
            |	与OR操作符相同
            []	匹配[]里的任何单一字符,可以配合^使用,如[^123]匹配除1、2、3以外的任何字符
            -	配合[]使用，匹配范围内的任意一个字符，'[1-9]'表示匹配1到9的数字；
            匹配字符类	
            	[:alnum:] 任意字母和数字（同[a-zA-Z0-9]）
                [:alpha:] 任意字符（同[a-zA-Z]）
                [:blank:] 空格和制表（同[\\t]）
                [:cntrl:] ASCII控制字符（ASCII 0到31和127）
                [:digit:] 任意数字（同[0-9]）
                [:graph:] 与[:print:]相同，但不包括空格
                [:lower:] 任意小写字母（同[a-z]）
                [:print:] 任意可打印字符
                [:punct:] 既不在[:alnum:]又不在[:cntrl:]中的任意字符
                [:space:] 包括空格在内的任意空白字符（同[\\f\\n\\r\\t\\v]）
                [:upper:] 任意大写字母（同[A-Z]）
                [:xdigit:] 任意十六进制数字（同[a-fA-F0-9]）
           匹配多个字符
                * 	0个或多个匹配
                + 	1个或多个匹配（等于{1,}）
                ? 	0个或1个匹配（等于{0,1}）
                {n} 指定数目的匹配
                {n,} 不少于指定数目的匹配
                {n,m} 匹配数目的范围（m不超过255）
          定位符
          		^ 文本的开始
                $ 文本的结尾
                [[:<:]] 词的开始
                [[:>:]] 词的结尾
                匹配上述特殊字符，使用转移字符转义，MySQL使用两个反斜杠\\进行转义；
-- LIKE和 REGEXP 区别：
	LIKE将带有通配符的字符串与整个列值进行匹配，整个列值需要匹配带有通配符的字符串才能被检索出来；
	REGEXP是对列值的子串进行匹配，只要该列值的某一个连续子串匹配该正则表达式就会被检索出来；
```

### GROUP BY 子句

根据一个或多个列将数据分为多个逻辑组，以便能对每个组进行聚集计算；

```mysql
GROUP BY {col_name | expr | position}, ... [WITH ROLLUP]
	
	-- 使用WITH ROLLUP关键字，可以实现在分组统计数据基础上再进行相同的统计
	eg: SELECT CLASS, COUNT(CLASS) FROM test GROUP BY CLASS;
```

### HAVING 子句

`HAVING`子句用来对分组或者聚合组指定过滤条件；

通常配合`GROUP BY`子句一起使用，如果省略`GROUP BY`子句，`HAVING`子句的行为与`WHERE`子句类似；

HAVING 子句与 WHERE 子句功能、用法相同，**执行时机不同**：WHERE 子句在数据分组前对记录进行过滤，HAVING 子句在数据分组后对分组进行过滤；

eg：`SELECT CLASS,COUNT(CLASS) AS TOTAL FROM test GROUP BY CLASS HAVING TOTAL>4;`







### 计算字段

存储在数据库中的数据一般不是应用程序所需要的格式。为了满足应用程序需要的格式，我们需要将数据库中的数据进行转换、计算或者格式化，这些**经过加工的数据就叫做计算字段**；

计算字段可以看作数据表中新增的一列，但计算字段并不实际存在于数据库表中。

注：使用数据库对需要的数据进行转换、计算或格式化再传递给其他程序比在其他程序中完成这些操作要快得多；

+ 算术运算

  MySQL支持加减乘除算术运算；

+ 拼接字段

  将多个列的值连接到一起构成一个字符串。

  多数DBMS使用` + `或者` ||` 实现拼接，MySQL使用`Concat()`函数来实现。

  `concat(字段1, 字段2, …)`：如果有任何一个字段值为NULL，返回值为NULL；

  `concat_ws(separator, str1, str2, ...)`：使用指定分隔符连接字段值；分隔符可以是一个字符串，也可以是其它参数；如果分隔符为NULL，则结果为NULL。和concat函数不同的是，除了分隔符以外的参数为NULL时结果不为NULL。

+ 文本处理函数

  ```mysql
  Length()		获取字符串字节长度
  char_length()	获取字符串字符个数
  Upper()			将串转换为大写
  Lower()			将串转换为小写
  Locate(str1,str2)	返回子串str1在字符串str2中的位置 		
  Left(str,n) 	截取str左边n个字符
  Right() 		截取str右边n个字符
  substring(str,index,len)	从str的index位置截取len个字符
  LTrim(str) 		去掉串左边的空格
  RTrim(str) 		去掉串右边的空格
  trim()			去除字符串str两边的空格
  ```

+ 日期和时间函数

  ```mysql
  CurDate()	返回当前日期	// 2020-10-19
  CurTime()	返回当前时间	// 14:58:37
  Now() 		返回当前日期和时间	// 2020-10-19 14:59:22
  Date()	Time() 返回日期时间的日期和时间
  Year(date)	Month(date)/MonthName(date) Day(date) 	//获取日期的年、月、日，可以接受日期时间类型的参数
  Hour(time) 	Minute(time)	Second(time)		// 获取时间的时、分、秒，可以接受日期时间类型的参数
  weekday(date)/dayname(date)	//获取星期几
  ```

+ 数值处理函数

  ```mysql
  Abs() 返回一个数的绝对值
  Mod() 返回除操作的余数
  Rand() 返回一个随机数
  Sqrt() 返回一个数的平方根
  ```

+ 聚集函数

  ```java
  AVG()
    返回指定列的平均值；
    只适用于数值列；
    忽略列值为NULL的行；
  COUNT()
    计算行的数目；
    COUNT(*) 对表中所有行进行计数，不管字段值是空值（NULL）还是非空值。
    COUNT(column)  忽略NULL值。
  MAX()
  MIN()
  SUM() 
  `DISTINCT`：可以通过该关键字实现聚集不重复行的功能；
  
  eg:`SELECT COUNT(DISTINCT CLASS) FROM test;`
  ```



select ip,url from test where ip in (select ip from（select ip,count(ip) as num from test group by ip order by num desc limit 3）as count);



### 子查询

嵌套在其他查询中的查询；

子查询必须位于括号中；

1. 子查询在WHERE子句中

   ```mysql
   -- 当子查询只返回一个值时，可以使用比较远算符=、<、> 等将子查询返回的单个值和WHERE子句中的表达式进行比较；
   	SELECT name FROM test WHERE class = (SELECT class FROM test LIMIT 1);
   -- 当子查询返回一列值时，可以在WHERE子句中使用IN或NOT IN运算符等其他运算符
   	SELECT name FROM test WHERE class IN (SELECT class FROM test GROUP BY class HAVING COUNT(class) > 4);
   ```

2. FROM子句中的MySQL子查询

   ```mysql
   -- 子查询返回的结果集将用作临时表,必须给子查询结果取个别名;
   	SELECT MAX(num) FROM (SELECT class,COUNT(class) AS num FROM test GROUP BY class) AS class_num;
   ```



### 联结表（join）

**联结是数据库查询操作中最重要的操作；**

join 用于联结多个数据表并从中读取数据；

在联结两个表时，**实际上做的是将第一个表中的每一行与第二个表中的每一行配对**，所以检索出的行的数目将是第一个表中的行数乘以第二个表中的行数，所以在使用联结时，联结条件很关键；

联结可以为两种：

+ inner join（内部联结，等值联结）
+ outer join（外部联结：又分为左联结、右联结）

**1）inner join**

基于两个表之间的相等关系；

```mysql
mysql> select * from students;
+----+-------+-----+------------+
| id | name  | sex | teacher_id |
+----+-------+-----+------------+
|  1 | zhu   | 男  |       1111 |
|  2 | sheng | 女  |       2222 |
|  3 | wen   | 男  |       3333 |
|  4 | lou   | 女  |       1111 |
+----+-------+-----+------------+

mysql> select * from teachers;
+------+------+
| id   | name |
+------+------+
| 1111 | 张三 |
| 2222 | 李四 |
| 4444 | 王五 |
+------+------+

-- 查询 students表与 teachers表 teacher_id字段相同的行中的数据
mysql> SELECT s.name, t.name 
FROM students s INNER JOIN teachers t
ON s.teacher_id = t.id		-- 联结条件用 ON 子句给出
ORDER BY s.name;
+-------+------+
| name  | name |
+-------+------+
| lou   | 张三 |
| sheng | 李四 |
| zhu   | 张三 |
+-------+------+
```

**2）left join**

会读取左边数据表的全部数据，即便右边表无对应数据；

```sql
mysql> SELECT s.name, t.name 
FROM students s LEFT OUTER JOIN teachers t 
ON s.teacher_id = t.id;
+-------+------+
| name  | name |
+-------+------+
| zhu   | 张三 |
| lou   | 张三 |
| sheng | 李四 |
| wen   | NULL |
+-------+------+
```

**3）right join**

会读取右边数据表的全部数据，即便左边表无对应数据；

```SQL
mysql> SELECT s.name, t.name 
FROM students s RIGHT OUTER JOIN teachers t 
ON s.teacher_id = t.id;
+-------+------+
| name  | name |
+-------+------+
| zhu   | 张三 |
| sheng | 李四 |
| lou   | 张三 |
| NULL  | 王五 |
+-------+------+
```



### 联合查询（UNION）

将多条 select 语句的查询结果合并为单个查询结果集返回；

```mysql
-- 使用UNION，将多条SELECT语句使用UNION关键字连接起来；
(SELECT ...) UNION (SELECT ...) UNION ...

-- 使用 UNION 注意事项：
	每个SELECT 查询语句必须包含相同的列、表达式或者聚集函数；
	列数据类型必须兼容：类型不必完全相同，但必须是DBMS可以隐含地转换的类型
	UNION从查询结果集中自动去除了重复的行，如果不要去除重复行，应该使用 UNION ALLL；

-- 对组合查询结果排序
	在最后一条 SELECT查询语句后使用 ORDER BY 语句；
```

```mysql
SELECT vend_id, prod_id, prod_price
FROM products
WHERE prod_price <= 5
UNION
SELECT vend_id, prod_id, prod_price
FROM products
WHERE vend_id IN (1001, 1002);

-- 完成相同工作的WHERE子句
SELECT vend_id, prod_id, prod_price
FROM products
WHERE prod_price <= 5
	OR vend_id IN (1001, 1002);
```

