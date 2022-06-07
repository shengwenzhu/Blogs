JDBC（Java Database Connectivity，Java 数据库连接）：通过一组通用的 API 访问不同的数据库管理系统 。

> JDBC 由 Sun 公司制定，**只是接口**，没有提供实现类，实现类由各数据库厂商提供实现，这些实现类就是数据库驱动程序。使用 JDBC 时只要面向标准的 JDBC API 编程即可，当需要连接某个特定的数据库时，必须有相应的数据库驱动程序；
>
> **API 文档地址：https://docs.oracle.com/javase/8/docs/api/index.html**

#  一、JDBC 3 个基本功能

+ 建立与数据库的连接；

+ 执行SQL 语句；

+ 获得SQL 语句的执行结果；

# 二、JDBC 常用接口和类

## 1. DriverManager 类

用于管理 JDBC 驱动的服务类；

该类的主要功能是获取 Connection 对象；

```java
public static Connection getConnection(String url,
        String user, String password) throws SQLException
```

## 2. Connection 接口

Connection 对象代表一个物理连接会话；

Connection 接口作用：

+ 获取 Statement 对象用于执行 SQL 语句；
+ 获取数据库相关信息，通过调用方法 getMetaData 实现；
+ 设置数据库为自动提交模式或者事务模式（此时需要显式调用 commit 方法提交事务）

### 1）获取Statement 对象

```java
/*
* 没有参数的SQL语句通常使用Statement对象执行；
* 如果一条SQL语句被执行多次，使用PreparedStatement可能更加高效；
*/
Statement createStatement() throws SQLException;

/*
* PreparedStatement对象存储的SQL语句已经被预编译，执行SQL语句时需要传入参数（如果有参数）
*/
PreparedStatement prepareStatement(String sql) throws SQLException;

/*
* CallableStatement对象用于调用存储过程
*/
CallableStatement prepareCall(String sql) throws SQLException;
```

## 3. Statement 接口

提供多个方法用于执行 SQL 语句；

```java
// 执行查询语句，并返回查询结果对应的ResultSet对象，该方法只能用于执行查询语句
ResultSet executeQuery(String sql) throws SQLException

// 用于执行DML语句，并返回受影响的行数,该方法也可用于执行DDL语句，执行DDL语句将返回0
int executeUpdate(String sql) throws SQLException
 
// 可执行任何SQL语句。如果执行后第一个结果为ResultSet对象，则返回true; 如果执行后第一个结果为受影响的行数或没有任何结果，则返回false。
boolean execute(String sql) throws SQLException
    // Statement提供了如下两个方法来获取执行结果：
    // 获取该Statement 执行查询语句所返回的ResultSet对象：
    ResultSet getResultSet() throws SQLException 
    // 获取该StatementO执行DML语句所影响的记录行数
    int getUpdateCount() throws SQLException
```

Java 8 为 Statement 新增了多个重载的 `executeLargeUpdate()` 方法，这些方法相当于增强版的executeUpdate() 方法， 返回值类型为long 。也就是说， 当 DML 语句影响的记录条数超过 Integer.MAX_VALUE 时， 就应该使用 executeLargeUpdate() 方法。但遗憾的是， 目前最新的MySQL 驱动暂不支持该方法。

### 1）PreparedStatement

表示预编译的 SQL语句；

创建 PreparedStatement 对象时需要传入一条 SQL 语句，该 SQL 语句将会先传输到数据库进行预编译，预编译后的 SQL 语句被存储在 PreparedStatement 对象中。然后可以使用该对象多次高效地执行该SQL语句。

PreparedStatement 也提供了execute()、executeUpdate() 、executeQuery() 三个方法来执行 SQL 语句，不过这三个方法无须参数， 因为 PreparedStatement 己存储了预编译的SQL 语句。

使用 PreparedStatement 预编译SQL 语句时， 该 SQL 语句可以带占位符参数（?）， 因此在执行 SQL 语句之前必须为这些参数传入参数值， PreparedStatement 提供了一系列的 `setXxx(int parameterIndex , Xxx value)`方法来传入参数值。注：如果程序不清楚预编译SQL 语句中各参数的类型，则可以使用`setObject()`方法来传入参数， 由PreparedStatement来负责类型转换。

注：SQL 语句中的占住符参数只能代替普通值，不要使用占住符参数代替表名等数据库对象，更不要用占位符参
数代替 SQL 语句中的 select 等关键字。

使用 PreparedStatement 比使用 Statement 多如下三个好处：

+ PreparedStatement 预编译 SQL 语句，性能更好。
+ PreparedStatement 无须"拼接" SQL 语句，编程更简单。
+ PreparedStatement 可以防止 SQL 注入，安全性更好。

```java
try
(
    Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test?			useSSL=false&serverTimezone=UTC", "root", "18813138213zsw");
    PreparedStatement ps = conn.prepareStatement("INSERT INTO students(id, name, sex,class) VALUES(?,?,?,?)")
)
{
    ps.setInt(1,2023);
    ps.setString(2,"娄玉爽");
    ps.setString(3, "男");
    ps.setInt(4, 4);
    int rs = ps.executeUpdate();
    System.out.println(rs);
}
```

### 2）CallableStatement

用于调用存储过程；

创建 CallableStatement 对象需要传入调用存储过程的 SQL 语句。

调用存储过程的 SQL 语句总是这种格式: `call 过程名(?,?,...)` ，其中的问号作为存储过程参数的占位符；

存储过程的参数既有传入参数，也有传出参数。所谓传入参数就是Java 程序必须为这些参数传入值，可以通过`CallableStatement .setXxx()`方法为传入参数设置值；所谓传出参数就是Java 程序可以通过该参数获取存储过程里的值， CallableStatement 需要调用`registerOutParameter()`方法来注册该参数。

```java
CallableStatement cs = conn.prepareCall(...);
// 注册CallableStatement 的第三个参数为int 类型
cs.registerOutParameter(3, Types.INTEGER);
// 为传入参数赋值
cs.setXxx(index, value);
...
// 执行该SQL语句
cs.execute();
// 得到结果
Xxx result = cs.getXxx(...);
```

## 2.4 ResultSet 接口

数据库查询结果集；

ResultSet 对象维护了一个指向其当前数据行的游标（记录指针），最初游标定位在第一行之前。

默认的 ResultSet 对象是不可更新的，并且游标只能向前移动（只能从第一行迭代到最后一行）。如果希望 ResultSet 对象是可以滚动的或者可以更新的，需要在创建 Statement 对象或者 PreparedStatement 时传入其他的参数；

### 1）可滚动结果集

调用`next()、previous()、absolute(int row)、afterLast()、beforeFirst()、first()、last()`方法可以移动游标位置，可以自由移动游标的ResultSet 被称为**可滚动的结果集**。

```java
// 将结果集的记录指针移动到第row行，如果row是负数，则移动到倒数第row行，如果row为零，则移动到第一行的前面。如果移动后的记录指针指向一条有效记录，则该方法返回true 
boolean absolute(int row)
    
// 将ResultSet 的记录指针定位到下一行，如果移动后的记录指针指向一条有效记录，则该方法返回true 。
boolean next() throws SQLException

// 将ResultSet 的记录指针定位到首行。如果移动后的记录指针指向一条有效记录，则该方法返回true 。
boolean first() throws SQLException
```

### 2）可更新结果集

可以通过修改结果集中的数据进而修改数据库中对应数据；

Connection 在创建Statement 或PreparedStatement 时可额外传入如下两个参数：

+ resultSetType: 控制ResultSet 的类型，该参数可以取如下三个值：
  + ResultSet.TYPE_FORWARD_ONLY：该常量控制记录指针只能向前移动。
  + ResultSet.TYPE_SCROLL_INSENSITIVE： 该常量控制记录指针可以自由移动(可滚动结果集) ，但底层数据的改变不会影响ResultSet 的内容。
  + ResultSet.TYPE_SCROLL_SENSITIVE：该常量控制记录指针可以自由移动(可滚动结果集) ,而且底层数据的改变会影响ResultSet 的内容。
+ resultSetConcurrency: 控制ResultSet 的并发类型，该参数可以接收如下两个值。
  + ResultSet.CONCUR_READ_ONLY：该常量指示ResultSet 是只读的并发模式(默认)。
  + ResultSet.CONCUR_UPDATABLE：该常量指示ResultSet 是可更新的并发模式。

可更新的结果集还需要满足如下两个条件：

+ 所有数据都应该来自一个表。
+ 选出的数据集必须包含主键列。

如果一个ResultSet是可更新的，调用`updateXxx(int columnIndex, Xxx value)`方法就可以修改记录指针所指行特定列的值，最后调用`updateRow()`方法提交修改；

```java
// update方法有两种使用方式：
// 方式1：更新某行的列值
try (
    Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test?useSSL=false&serverTimezone=UTC", "root", "18813138213zsw");
    PreparedStatement ps = conn.prepareStatement("SELECT * FROM students", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    ResultSet rs = ps.executeQuery())
    {
        rs.absolute(5);
        rs.updateString(2, "小可爱");
        rs.updateRow();
    }
// 方式2：插入行
rs.moveToInsertRow();
rs.updateInt(1, 2030);
rs.updateString(2, "小公举");
rs.updateString(3, "男");
rs.insertRow();
```

### 3）从结果集检索列值

ResultSet接口提供了从当前行检索列值的getter方法(getBoolean、getLong等)。可以使用列的索引号或列的名称检索值。通常，使用列索引会更有效。列从1开始编号；

对于getter方法，JDBC驱动程序尝试将mysql中的数据转换为Java中合适的数据类型；

### 4）使用ResultSetMetaData 分析结果集

ResultSet 无法知道结果集里包含哪些数据列，以及每个数据列的数据类型等；

可以通过调用`getMetaDate()`方法返回一个ResultSetMetaData对象，ResultSetMetaData对象可以得到ResultSet对象列的信息；

```java
ResultSetMetaData getMetaData() throws SQLException
```

ResultSetMetaData 常用的方法有如下三个：

+ int getColumnCount()： 返回该ResultSet 的列数量；

+ String getColumnName(int column)： 返回指定索引的列名；

+ int getColumnType(int column)：返回指定索引的列类型；

## 5. RowSet接口

RowSet 接口继承了ResultSet 接口，与ResultSet 相比， **RowSet 默认是可滚动、可更新、可序列化的结果集**，而且可以作为JavaBean 使用，因此能方便地在网络上传输；

RowSet 接口下包含JdbcRowSet 、CachedRowSet 、FilteredRowSet 、JoinRowSet 和WebRowSet 常用子接口。除JdbcRowSet 需要保持与数据库的连接之外，其余4 个子接口都是离线的RowSet ，无须保持与数据库的连接。对于离线RowSet 而言，程序在创建RowSet 时己把数据从底层数据库读取到了内存；

### 1）创建RowSet 对象

Java 7 新增了 RowSetProvider  类和 RowSetFactory  接口，其中RowSetProvider 负责创建RowSetFactory ，而RowSetFactorγ 则提供了如下方法来创建RowSet 实例：

```java
CachedRowSet createCachedRowSet(): 创建一个默认的CachedRowSet 。
FilteredRowSet createFilteredRowSet(): 创建一个默认的FilteredRowSet 。
JdbcRowSet createJdbcRowSet(): 创建一个默认的JdbcRowSet。
JoinRowSet createJoinRowSet(): 创建一个默认的JoinRowSet 。
WebRowSet createWebRowSet(): 创建一个默认的WebRowSet 。
```

使用RowSetFactory 创建的RowSet 并没有装填数据；

### 2）使用 RowSet 获取数据库数据

为RowSet 设置数据库的URL、用户名、密码等连接信息；

```java
void setUrl(String url)		
void setUsername(String name)		
void setPassword(String password)	
```

为 RowSet 设置SQL 查询命令，该命令的查询结果将装填该RowSet；

```java
void setCommand(String sql)
```

执行SQL查询命令：

```java
void execute() // 执行该命令后，RowSet中将包含查询结果集
```

### 3）离线RowSet

对于ResultSet结果集，Connection关闭后将不能再访问该结果集；在这种模式下，假如应用程序架构被分为两层：数据访问层和视图显示层，当应用程序在数据访问层查询得到ResultSet 之后，对ResultSet 的处理有两种常见方式：使用迭代访问ResultSet 里的记录，并将这些记录转换成Java Bean ，再将多个Java Bean 封装成一个List 集合，也就是完成"ResultSet→ Java Bean 集合"的转换。转换完成后可以关闭Connection等资源，然后将Java Bean 集合传到视图显示层，视图显示层可以显示查询得到的数据。第二种方式是直接将ResultSet 传到视图显示层，这要求当视图显示层显示数据时，底层Connection 必须一直处于打开状态， 否则ResultSet 无法读取记录。第一手中方式比较安全，但编程十分烦琐； 第二种方式则需要Connection 一直处于打开状态，这不仅不安全，而且对程序性能也有较大的影响。使用离线RowSet 可以十分"优雅"地处理上面的问题。

离线RowSet 将底层数据读入内存中，封装成RowSet 对象，Connection关闭后依然可以读取、修改RowSet 中的记录；

离线RowSet 增加了一个方法`void populate(ResultSet data)`可以使用ResultSet结果集中的数据来填充自身；

为了将程序对离线RowSet 所做的修改同步到底层数据库，程序可以调用RowSet 的`acceptChanges()`方法；

```java
CachedRowSet cach = cachedRowSet();
cach.absolute(4);
cach.updateString(2, "娄大");
cach.updateRow();       // 只是更新了结果集中的列值，并没有更新底层数据库中的数据
System.out.println(cach.getString(2));  //娄大

// 将程序对离线RowSet 所做的修改同步到底层数据库
Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test?useSSL=false&serverTimezone=UTC", "root", "18813138213zsw");
conn.setAutoCommit(false);
cach.acceptChanges(conn);
```

+ 查询分页
  由于离线RowSet会将数据记录直接装载到内存中，因此如果SQL 查询返回的记录过大，CachedRowSet 将会占用大量的内存；
  离线RowSet 提供了分页功能。所谓分页功能就是一次只装载ResultSet 里的某几条记录，这样就可以避免CachedRowSet 占用内存过大的问题。
  CachedRowSet 提供了如下方法来控制分页：

  ```java
  //使用给定的ResultSet数据装填RowSet，从ResultSet的第startRow条记录开始装填。
  void populate(ResultSet rs, int startRow) 
  // 设置离线RowSet的页面大小，即每次包括多少条记录。
  void setPageSize(int size)
  // 获取下一个页面大小的记录
  boolean nextPage()
  // 获取上一个页面大小的记录
  boolean previousPage()
  ```

  ```java
  public static CachedRowSet cachedRowSet() throws SQLException, ClassNotFoundException
  {
      Class.forName("com.mysql.cj.jdbc.Driver");
      try (
          Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test?useSSL=false&serverTimezone=UTC", "root", "18813138213zsw");
          PreparedStatement ps = conn.prepareStatement("SELECT * FROM students", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          ResultSet rs = ps.executeQuery())
      {
          RowSetFactory factory = RowSetProvider.newFactory();
          CachedRowSet cach = factory.createCachedRowSet();
          cach.setPageSize(3);
          cach.populate(rs,2);
          return cach;
      }
  }
  public static void main(String[] args) throws SQLException, ClassNotFoundException
  {
      CachedRowSet cach = cachedRowSet();
      while(cach.next()) System.out.println(cach.getInt(1)); 		//只输出从第二行开始的三条记录
  }
  ```

##  6. 事务处理

JDBC 开启事务由Connection 接口控制；

Connection 默认打开自动提取，即关闭事务；

使用事务处理的步骤如下所示：

```mysql
-- 1.创建连接
	Connection conn = DriverManager.getConnection(url, user, password);
-- 2.调用 Connection.setAutoCommit() 方法开启事务
	conn.setAutoCommit(false);
-- 3.创建Statement对象
	Statement st = conn.createStatement();
-- 4.执行SQL语句
	st.executeUpdate(sql);
-- 5.调用 Connection.commit() 方法提交事务
	conn.commit();
	
-- 如果任意一条SQL语句执行失败抛出异常，当该异常未捕获时，事务会自动回滚，如果程序捕获了异常，在异常处理块中可以显式回滚事务；
	conn.rollback();
	
-- 设置保留点：Connection 接口定义了如下方法：
	Savepoint setSavapoint()：在当前事务中创建一个保存点，并返回代表该保存点的Savepoint对象；
-- 回滚到保存点：
	Connection.rollback(Savepoint savepoint)
```

### 批量更新（Java8）

使用批量更新时， 多条SQL 语句将作为一批操作被同时收集，并同时提交。

批量更新必须得到底层数据库的支持，可以通过调用 DatabaseMetaData.supportBatchUpdates()方法来查看底层数据库是否支持批量更新；

使用批量更新的步骤：

```mysql
-- 1. 创建一个Statement对象
	Statement st = conn.createStatement();
-- 2. 调用 addBatch() 方法收集多条SQL语句
	st.addBatch(sql);
	st.addBatch(sql2);
	...
-- 3. 调用executeBatch() 方法执行
	st.executeBatch();	-- 执行该方法将返回一个 long[] 数组，用于表示执行每条SQL语句的影响记录数量
```

如果批量更新在执行过程中失败，则应该回滚到批量操作开始之前的状态。为了达到这个目的，应该在开始批量操作之前先关闭自动提交，然后开始收集更新语句，当批量操作执行结束后，提交事务，并回复之前的自动提交模式；

## 7. DatabaseMetaData 接口

使用连接池管理连接







# 三、JDBC 编程步骤

```

```

