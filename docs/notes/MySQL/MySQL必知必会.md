










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

















# 1. InnoDB 存储引擎





## 1.2 InnoDB存储引擎的4大特性（尚未修正）

- 插入缓冲（insert buffer)

  插入缓存并不是内存缓存池的一部分，而是物理页的一部分；

  通常应用程序中行记录的插入顺序是按照主键递增的顺序进行插入的，因此，插入聚集索引一般是顺序的，不需要磁盘的随机读写；

  对于非聚集索引的插入或者更新操作，数据页的存放还是按照主键顺序存放，但是对于非聚集索引叶子节点的插入不是顺序的，这时就需要离散的访问非聚集索引页，随机读取会导致插入性能下降；

  对于非聚集索引的插入或者更新操作，不是每一次直接插入到索引页中，而是先判断插入的非聚集索引页是否在缓冲池中，如在直接插入，若不在，先将其放入到插入缓存中，然后再以一定的频率对插入缓存和非聚集索引叶子节点的合并；

  **通过将多个插入操作合并到一个操作中，可以提高对于非聚集索引插入的性能**；

  Insert Buffer的使用需要同时满足以下两个条件：

  + 索引是辅助索引；
  + 索引不是唯一索引；

  

- 两次写(double write)

  double write由两部分组成：一部分是内存中的doublewrite buffer，一部分是物理磁盘上共享表空间中连续的128页；

  对缓冲池的脏页进行刷新时，并不直接写磁盘，而是将脏页先复制到内存中的doublewrite buffer（2MB）中，之后通过 doublewrite buffer再分两次，每次 1MB顺序的写入共享表空间的物理磁盘上；

  如果操作系统在将页写入磁盘的过程中崩溃了，在恢复过程中，innodb 存储引擎可以从共享表空间的doublewrite中找到该页的副本，将其复制到表空间文件，再应用重做日志；

  通过缓存从缓冲池中刷新到磁盘的数据页，可以保证数据页的可靠性；

  

- 自适应哈希索引(AHI)

  哈希是一种非常快的查找方法，一般仅需要一次查找就能找到数据；B+树查找次数取决于B+树的高度；

  Innodb存储引擎会监控对表上各索引页的查询，根据访问的频率自动的为某些热点页建立哈希索引；

  

- 预读(read ahead)

  InnoDB使用两种预读算法来提高I/O性能：线性预读（linear read-ahead）和随机预读（randomread-ahead）；

  线性预读着眼于将下一个extent提前读取到buffer pool中，而随机预读着眼于将当前extent中的剩余的page提前读取到buffer pool中；











## 1.4 约束

数据库通过约束机制完成对数据的校验，保证数据库中数据的完整性；

常用的约束有 4 种：

+ not null：非空约束，指定某列不为空
+ unique：唯一约束，指定某列或几列组合的数据不能重复
+ primary key：主键约束，指定某列的数据不能重复，不能为null；

+ foreign key：外键约束；



### 1.4.1 外键约束（Foreign Key）

被引用的表称为父表，引用的表称为子表；

与主键约束一起使用，为两个表的数据建立连接，保证两个表中数据的一致性和完整性；

**InnoDB 存储引擎在外键建立时会自动对该列增加一个索引**；

创建外键规则：

- 主表必须已经存在于数据库中，或者是当前正在创建的表。如果是后一种情况，则主表与从表是同一个表，这样的表称为自参照表，这种结构称为自参照完整性；
- **必须为主表定义主键**；
- 外键中列的数目必须和主表的主键中列的数目相同；

对于参照完整性约束，外键能起到很好的作用，但导入数据时，外键约束的检查会花费大量时间；可以通过命令在导入数据前关闭外键的检查：

```sql
SET foreign_key_checks = 0;
-- 导入数据后再设置为1
```

+ 创建外键

  有两种方式创建外键：一种是创建表时创建外键，另一种是表创建后通过 ALTER TABLE 创建外键；

  ```sql
  -- 创建表时创建外键
  [CONSTRAINT 外键名]					-- 为外键约束定义名称,如果省略，MySQL将自动生成一个名称
  FOREIGN KEY 字段1 [,字段2,...]) 	
  REFERENCES 主表(字段1 [,...])
  ON DELETE action	-- 定义当父表中的记录被删除时，子表的记录怎样执行操作。
  ON UPDATE action	-- 定义当父表中的记录更新时，子表中的记录会怎样执行操作
  
  -- 通过 ALTER TABLE 创建外键
  -- 添加外键约束的前提：从表中外键列中的数据必须与主表中主键列中的数据一致或者是没有数据
  ALTER TABLE 表名 ADD 
  CONSTRAINT 外键名
  FOREIGN KEY (字段1 [,...])
  REFERENCES 主表(字段1 [,...])
  ON DELETE action
  ON UPDATE action
  ```

+ 删除主键

  ```sql
  ALTER TABLE table_name 
  DROP FOREIGN KEY 外键名;
  ```

  

## 1.4 InnoDB 逻辑存储结构

+ 表空间

  InnoDB 存储引擎中，所有数据都被逻辑地存放在表空间中；

  表空间由段（segment）、区（extent）、页（page）组成；

+ 段

  表空间由各个段组成；

  常见的段有数据段、索引段、回滚段等；

+ 区

  区是由连续页组成的空间；

  任何情况下每个区的大小都为 1MB；

  默认情况下，InnoDB 存储引擎页的大小为 16KB，所有一个区中一共有 64 个连续的页；

+ 页

  **页是 InnoDB 存储引擎管理数据库的最小单位**；

  每页默认大小为 16KB，InnoDB 1.0.x 版本引入压缩页，每个页的大小可以通过参数 `KEY_BLOCK_SIZE` 设置为 2K、4K、8K；InnoDB 1.2.x 版本新增参数 innodb_page_size，通过该参数可以将默认页的大小设置为4K、8K；

+ 行

  页中的数据是按行进行存放；

  MySQL 目前存在四种行记录格式：Redundant、Compact、Dynamic 和 Compressed；

  > 其中 Redundant 为了兼容之前版本而保留；
  >
  > MySQL 5.1 开始，默认的行记录格式为 Compact；
  >
  > MySQL 5.7 开始，默认的行记录格式为 Dynamic；

  ```sql
  -- 通过如下命令查看当前表使用的行格式，其中 row_format 属性表示当前表所使用的行记录格式
  SHOW TABLE STATUS LIKE '表名';
  ```



### 1.4.1 行记录格式

+ Compact 行记录格式

  设计目的是为了高效的存储数据，一个页中存放的行数据越多，其性能就越高；

+ Redundant 行记录格式

  MySQL 5.0 版本之前 InnoDB存储引擎的行记录存储方式；

+ 行溢出数据

  行记录占用的空间大小最多为 65535 字节；

  默认每页的大小为 16KB（16384 字节），每页至少需要存储 2 行记录；

  **InnoDB 存储引擎会将占用存储空间非常大的列拆开，在当前列中只存储该列的前 768 个字节的数据和一个指向溢出页（Uncompressed BLOB Page 类型）的地址；**

+ Compressed 和 Dynamic 行记录格式

  InnoDB 1.0.x 版本引入了新的文件格式，之前支持的 Compact 和 Redundant 行记录格式称为 Antelope 文件格式，新的文件格式称为 Barracuda 文件格式，该文件格式下拥有两种新的行记录格式：Compressed 和 Dynamic；

  **新的两种行记录格式对于存放在BLOB 中的数据采用完全的行溢出格式**，**在数据页中只存放20个字节的指针**；Compressed 行记录格式可以对存储在其中的行数据以zlib算法进行压缩，对于BLOB、TEXT、VARCHAR大长度类型的数据能够进行非常有效的存储；



























# 数据库优化

## 大表数据优化

+ **字段优化**

  优先选择符合存储需要的最小的数据类型；

  将字符串转换为数值类型存储；（如将 IP 地址转换为数值类型存储）

  时间使用 TimeStamp 存储而非字符串；

  尽可能的把所有列定义为 not null；（很难查询优化且占用额外索引空间）

  避免使用 Text、BLOB 数据类型；

+ **索引优化**

  选择合适的字段作为索引：频繁作为where、order by后的字段；

  > 不适合创建索引的字段：更新频繁的字段；不能有效区分数据的列（如性别：男女）；

  对于字符串列建立索引，使用前缀索引；

  避免索引冗余；

  > 冗余索引指的是索引的功能相同，如索引（a,b) 和索引（a）这两个索引就是冗余索引

  尽量不使用外键：

+ **查询 SQL 优化**

  开启慢查询日志来找出执行速度较慢的SQL；

  通过 explain 命令获取查询语句是如何运行的：

  > 通过EXPLAIN 可以得到：查询使用了哪些索引，可以使用哪些索引，有多少行被查询等信息；

+ **主从复制或者读写分离**

+ **使用缓冲，比如 Redis**

+ **分库分表、冷热数据分离**







## 插入优化

+ 批量插入，一条 sql 语句插入多条数据；

  原因：日志量减少了，降低了日志刷盘的数据量和频率；同时也能减少SQL语句解析的次数；

  注：SQL语句有长度限制，mysql5.7的客户端默认是 16M，服务端默认是 4M；

+ 在事务中进行插入

  进行一次 INSERT 操作时，MySQL内部会建立一个事务，在事务内才进行插入操作。通过使用事务可以减少创建事务的消耗，所有插入都在执行后才进行提交操作；

+ 数据按主键有序插入

  数据库插入时，需要维护索引数据；有序的数据插入减少了索引节点的合并和分裂；



















