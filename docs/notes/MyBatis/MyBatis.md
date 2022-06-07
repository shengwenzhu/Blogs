



























# MyBatis 面试

## MyBatis 如何防止 SQL 注入

**sql 注入发生的阶段在 SQL 预编译阶段**，编译完成的 SQL 不会产生 SQL 注入；

mybatis 底层实现了预编译，底层通过 prepareStatement 预编译实现类对当前传入的 sql 语句进行了预编译；

通过 “#{xxx}” 格式给 sql 语句传入参数时，使用的是已经编译完成的 sql 语句；

如果通过 “**$**{xxx}” 传入参数时，会直接参与SQL编译，从而不能避免注入攻击；



## MyBatis 与 Hibernate 区别

+ MyBatis 是半自动映射，Hibernate 是全自动映射

  hibernate完全可以通过对象关系模型实现对数据库的操作，拥有完整的 JavaBean 对象与数据库的映射结构来自动生成sql；

  mybatis仅有基本的字段映射，对象数据以及对象实际关系仍然需要通过手写sql来实现和管理；

+ sql 直接优化上，mybatis要比hibernate方便很多

  mybatis 需要手动编写 sql，可以直接通过 优化 sql 语句优化性能，hibernate自动生成sql，不能通过 sql 语句进行优化；

+ hibernate数据库移植性远大于mybatis

+ hibernate拥有完整的日志系统，mybatis则欠缺一些

+ hibernate 功能更加强大，但使用起来复杂；mybatis 简单高效；























MyBatis 是持久层框架，支持自定义 SQL、存储过程以及高级映射；







> 持久层：向一个或多个数据存储器存储或获取数据的组件，作用是持久化数据；





MyBatis：MyBatis 是支持定制化SQL、存储过程以及高级映射的优秀的**持久层框架**，也是一种**半自动映射**框架；

MyBatis 包含三个部分：

+ SQL
+ 映射关系（XML或者注解）
+ POJO

MyBatis 是一个基于`SqlSessionFactory `构建的框架。`SqlSessionFactory` 接口的作用是生成SqISession 接口对象。**`SqlSession `接口对象是MyBatis 操作的核心**；



# 一、MyBatis 的基本构成

## 1. SqlSessionFactoryBuilder

作用：用于生成 SqlSessionFactory接口对象；

生命周期：一旦构建了SqlSessionFactory，作用就已经完结，就应该将其回收；

## 2. SqlSessionFactory

作用：用于生成`SqlSession`接口对象；

生命周期：应用程序需要访问数据库时，就要通过SqlSessionFactory创建SqlSession，所以SqlSessionFactory应该在MyBatis应用的整个生命周期中；

**每一个数据库对应一个SqlSessionFactory**；

MyBatis 提供了两种方式去创建`SqlSessionFactory`：

+ 一种是 XML 配置的方式（优先选择）；
+ 代码方式；

### 1）使用 XML 方式构建

第一步：配置mybatis-config.xml文件（resources文件下）

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<!-- MyBatis的主配置文件 -->
<configuration>
    <!-- 配置环境 -->
    <environments default="development">
        <!-- 配置MySQL环境 -->
        <environment id="development">
            <!-- 采用jdbc事务管理 -->
            <transactionManager type="JDBC"/>
            <!-- 配置数据源/连接池 -->
            <dataSource type="POOLED">
                <!-- 配置连接数据库的4个基本信息  -->
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/test?useSSL=false&amp;serverTimezone=GMT"/>
                <property name="username" value="root"/>
                <property name="password" value="18813138213zsw"/>
            </dataSource>
        </environment>
    </environments>
    <!-- 定义映射器 -->
    <mappers>
        <mapper resource="cn/edu/zhu/StudentsMapper.xml"/>
    </mappers>
</configuration>
```

第二步：构建SqlSessionFactory

```java
// 将生成SqlSession封装到一个类中
public class SqlSessionFactoryUtil
{
    // SqlSessionFactory对象
    private static SqlSessionFactory sqlSessionFactory = null;
    // 类线程锁
    private static final Class CLASS_LOCK = SqlSessionFactory.class;
    // 私有化构造参数
    private SqlSessionFactoryUtil() {}
    // 构建SqlSessionFactory：使用单例模式
    public static SqlSessionFactory initSqlSessionFactory()
    {
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try
        {
            inputStream =Resources.getResourceAsStream(resource);
        }
        catch (IOException e)
        {
            Logger.getLogger(SqlSessionFactoryUtil.class.getName()).log(Level.SEVERE, null, e);
        }
        synchronized(CLASS_LOCK)
        {
            if(sqlSessionFactory == null)
            {
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            }
        }
        return sqlSessionFactory;
    }
    // 构建 SqlSession
    public static SqlSession openSqlSession()
    {
        if(sqlSessionFactory == null)
            initSqlSessionFactory();
        return sqlSessionFactory.openSession();
    }
}
```

### 2）使用代码方式构建

采用代码方式一般是需要加入自己特性的时候才会用到，比如要求数据源配置的信息是加密的；

## 3. 构建SqlSession

使用结束后需要关闭 SqlSession 对象；

```java
SqlSession session = factory.openSession();
```

SqlSession 的作用主要有两个：

+ **获得映射器**，让映射器通过命名空间和方法名称找到对应的SQL，发送给数据库执行后返回结果；

  ```java
  // StudentsMapper为映射接口
  StudentsMapper studentsMapper = sqlSession.getMapper(StudentsMapper.class);
  // findAll()为映射接口方法
  List<Students> students = studentsMapper.findAll();
  ```

  

+ 直接通过命名信息去执行SQL返回结果，通过调用update、insert、select、delete等方法，带上SQL的id来操作在XML中配置的SQL，它也支持事务，可以通过调用commit、rollback等方法提交或回滚事务；

## 4. 映射器

映射器是由`JAVA接口`和`XML文件`（或注解）共同组成的；

它的作用是如下：

+ 定义参数类型；
+ 描述缓存；
+ 描述SQL语句；
+ 定义查询结果和POJO的映射关系；

映射器的实现方式有两种：

+ 一种是通过XML方式实现（首选）；
+ 一种是通过代码方式实现（Java注解）；

### 1）XML 文件配置方式实现 Mapper

第一步：给出JAVA 接口

```java
package cn.edu.zhu;
import cn.edu.entity.Students;
import java.util.List;
public interface StudentsMapper
{
    // 查询数据表所有记录
    List<Students> find(int id);
    // 查询数据表所有记录个数
    int count();
}
```

第二步：给出一个映射XML文件（位于resources文件下，映射XML文件位置必须和映射接口的包结构相同）

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- namespace的值为映射器接口的类全名 -->
<mapper namespace="cn.edu.zhu.StudentsMapper">
    <!-- 查询所有记录 -->
    <!-- id为接口方法名，resultType为返回的类型，parameterType为传递给SQL的参数类型 -->
    <select id="find" parameterType="int" resultType="cn.edu.entity.Students">
        select * from students where id = #{id}
    </select>
    <!-- 记录总条数 -->
    <select id="count" resultType="int" >
        select count(*) from students
    </select>
</mapper>
```

### 2）注解方式实现 Mapper

第一步：定义映射接口

```java
package cn.edu.zhu;

import cn.edu.entity.Students;

import java.util.List;
import org.apache.ibatis.annotations.Select;

public interface StudentsMapper
{
    // 查询数据表所有记录
    @Select(value = "select * from students where id = #{id}")
    List<Students> findAll(int id);
    // 查询数据表所有记录个数
    @Select(value = "select count(*) from students")
    int count();
}
```

使用了 @Select 注解，该接口就成了一个映射器；

## 5. POJO

简单的 Java 对象（Plain Ordinary Java Object，POJO），与数据表对应；

```java
// Students数据表对应的POJO
package cn.edu.entity;

public class Students
{
    private int id;
    private String name;
    private char sex;
    private int classnum;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public char getSex()
    {
        return sex;
    }
    
    public void setSex(char sex)
    {
        this.sex = sex;
    }
    
    public int getClass_num()
    {
        return classnum;
    }
    
    public void setClass_num(int class_num)
    {
        this.classnum = class_num;
    }
    
    @Override
    public String toString()
    {
        return "Students{" + "id=" + id +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", classnum=" + classnum +
                '}';
    }
}
```







；

### 3）SqlSession

SqlSession就是一个会话，相当于JDBC中的Connection对象；

线程不安全对象，在多线程中操作数据库应该注意其隔离级别、数据库锁等高级特性；

生命周期是在请求数据库处理事务的过程中；

每次创建的SqlSession都必须及时关闭；

### 4）Mapper

生命周期是在一个SqlSession事务方法之内；













JDBC、Hibernate、MyBatis 三种访问数据库的方法

+ 分析优缺点；
+ 区别；
+ 适用场景；

# 一、三种访问数据库的方法

## 1. JDBC

使用传统的 JDBC 操作数据库的步骤大致分为以下几步：

+ 第一步：加载数据库驱动
  调用`Class.forName()`静态方法来加载驱动。

  ```java
  // 加载mysql 驱动
  Class.forName("com.mysql.cj.jdbc.Driver");
  ```

+ 第二步：获取数据库连接

  ```java
  // DriverManager提供了如下方法获取数据库连接
  DriverManager.getConnection(String url, String user, String password);
  
  // mysql url写法：
  "jdbc:mysql://127.0.0.1:3306/test?useSSL=false&serverTimezone=UTC"
  //test 表示数据库名，useSSL=false表示不使用安全连接；
  ```

+ 第三步：通过Connection 对象创建Statement 对象；

  ```java
  Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test?useSSL=false&serverTimezone=UTC", "root", "18813138213zsw");
  Statement s = conn.createStatement();
  ```

+ 第四步：使用Statement 执行SQL 语句；

  ```java
  ResultSet resultSet = s.executeQuery("SELECT * FROM students");
  ```

+ 第五步：操作结果集
  如果执行的SQL 语句是查询语句，则执行结果将返回一个ResultSet 对象，该对象里保存了SQL 语句查询的结果。程序可以通过操作该ResultSet 对象来取出查询结果。

+ 第六步：关闭数据库资源，包括关闭ResultSet、Statement 和Connection 等资源；

### 缺点

+ 工作量相对较大；
+ 要对JDBC 编程可能产生的异常进行捕捉处理并正确关闭资源；

## 2. ORM 模型

ORM（Object Relational Mapping，对象关系映射）

ORM 模型是数据库的表和简单的 Java 对象（Plain Ordinary Java Object，POJO）的映射关系模型，主要解决数据库数据和POJO 对象的相互映射；

注：**ORM 模型都是基于 JDBC 封装的**；

![ORM映射模型](image/ORM映射模型.PNG)

## 3. Hibernate





# 三、MyBatis配置

## 1. MyBatis 配置 XML 文件层次结构

```xml
<?xml version="1.0" endcoding="UTF-8"?>
<configuration>		<!-- 配置-->
    <properties> </properties>		<!-- 属性-->
    <setting> </setting>			<!-- 设置-->
    <typeAliases> </typeAliases>	<!-- 类型别名-->
    <typeHandlers> </typeHandlers>	<!-- 类型处理器-->
    <objectFactory> </objectFactory>	<!-- 对象工厂-->
    <plugins> </plugins>			<!-- 插件-->
    <enviroments>		<!-- 配置环境-->
        <transactionManager> </transactionManager>	<!-- 事务管理器-->
        <dataSource> </dataSource>		<!-- 数据源-->
    </enviroments>	
    <databaseIdProvider> </databaseIdProvider>	<!-- 数据库厂商标识-->
    <mappers> </mappers>	<!-- 映射器-->
</configuration>
```

以上就是MyBatis的所有配置元素，这些层次是不能颠倒顺序的；

## 2. properties元素

配置属性，可以在配置文件的上下文使用；

有三种配置方式：

+ property子元素
+ properties配置文件
+ 程序传递参数

### 1）property子元素

```xml
<!-- property子元素配置 -->
<property name="driver" value="com.mysql.cj.jdbc.Driver"/>
<property name="url" value="jdbc:mysql://localhost:3306/testuseSSL=false&amp;serverTimezone=GMT"/>
<property name="username" value="root"/>
<property name="password" value="18813138213zsw"/>
```

```xml
<!-- 配置属性上下文使用 -->
<dataSource type="POOLED">
    <property name="driver" value="${driver}"/>
    <property name="url" value="${url}"/>
    <property name="username" value="${username}"/>
    <property name="password" value="${password}"/>
</dataSource>
```

### 2）properties配置文件（推荐）

可以在多个配置文件中重复使用，方便维护；

```properties
# 配置文件：xxx.properties
driver=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://localhost:3306/testuseSSL=false&amp;serverTimezone=GMT
...
```

将该配置文件放在【resources】文件夹下，使用该配置文件的其他配置文件只需要导入这个配置文件即可：

```xml
<properties resource="xxx.properties"/>
```

### 3）优先级

这3种配置方式可能同时出现，并且属性可能还会重复配置；

这3种方式是存在优先级的，将按照如下的顺序加载（之后读取的同名属性值将覆盖之前的）：

+ property子元素；
+ properties配置文件；
+ 程序传递参数；

注意事项：尽量不要使用混合的方式；

## 3. setting

## 4. 别名（typeAliases）

类全限定名过长，赋予一个简短的别名；

别名分为系统定义别名和自定义别名两种；

### 1）系统定义别名

### 2）自定义别名

## 5. typeHandler类型处理器

MyBatis 在预处理语句（PreparedStatement）中设置一个参数时，或者从结果集（ResultSet）中取出一个值时，都会用注册了的 typeHandler 进行处理；typeHandler 将参数从 javaType 转化为 jdbcType，从数据库取出结果时将 jdbcType 转化为 javaType;

typeHandler 也分为系统定义和用户自定义两种，使用系统定义就可以实现大部分的功能；

### 1）系统定义的typeHandler

### 2）自定义 typeHandler

### 3）枚举类型 typeHandler

MyBatis 内部提供了两个转化枚举类型的typeHandler：

+ EnumTypeHandler（使用枚举字符串名称作为参数传递）
+ EnumOrdinalTypeHandler（使用整数下标作为参数传递）

## 6. ObjectFactory 对象工厂

## 7. 插件

## 8. environments 配置环境

```xml
<environments default="mysql">		<!-- 表明在缺省的情况下启用哪个数据源配置 -->
    <environment id="mysql">		<!-- 配置一个数据源的开始 -->
        <!-- 配置数据库事务，type属性有3种配置方式：JDBC，MANAGED，自定义 -->
        <transactionManager type="JDBC">	
            <property name="autoCommit" value="false"/>
        </transactionManager>
        <!-- 配置数据源连接信息 -->
        <dataSource type="POOLED">		<!-- 连接池数据库 -->
            <!-- 配置连接数据库的4个基本信息  -->
            <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
            <property name="url" value="jdbc:mysql://localhost:3306/test?useSSL=false&amp;serverTimezone=GMT"/>
            <property name="username" value="root"/>
            <property name="password" value="18813138213zsw"/>
        </dataSource>
    </environment>
</environments>
```

配置环境可以注册多个数据源（dataSource），每一个数据源分为两大部分：一个是数据库源的配置，一个是数据库事务（transactionManager）的配置；

## 9. 引入映射器

引入映射器的方法很多：

```xml
<!-- 使用文件路径引入 -->
<mappers>
    <mapper resource="cn/edu/mapper/StudentsMapper.xml"/>
</mappers>

<!-- 使用包名引入 -->
<mappers>
    <package name="cn.edu.mapper"/>
</mappers>

<!-- 使用映射接口引入 -->
<mappers>
    <mapper class="cn.edu.mapper.StudentsMapper"/>
</mappers>

<!-- 使用映射配置文件引入 -->
<mappers>
    <mapper url="F:/MyBatisXML/src/main/resources/cn/edu/mapper/StudentsMapper.xml"/>
</mappers>
```

# 四、映射器

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="cn.edu.mapper.StudentsMapper">
    <select id="getStudent" parameterType="int" resultType="cn.edu.entity.Students">
        select * from students where id = #{id}
    </select>
    <insert id="insertStudent" parameterType="cn.edu.entity.Students">
        insert into students(id, name, sex, classnum) values (#{id}, #{name}, #{sex}, #{classnum})
    </insert>
    <delete id="deleteStudent" parameterType="int" >
        delete from students where id = #{id}
    </delete>
</mapper>
```

## 1. 映射器的主要元素

| 元素名称  | 描述                         | 备注                         |
| --------- | ---------------------------- | ---------------------------- |
| select    | 查询语句                     | 可以自定义参数，返回结果集等 |
| insert    | 插入语句                     | 执行后返回插入的条数         |
| update    | 更新语句                     | 执行后返回更新的条数         |
| delete    | 删除语句                     | 执行后返回更新的条数         |
| sql       | 定义sql语句，在其他地方引用  |                              |
| resultMap | 自定义映射规则               | 提供映射规则                 |
| cache     | 给定命名空间的缓存配置       |                              |
| cache-ref | 其他命名空间的缓存配置的引用 |                              |

## 2. select

### 1）select子元素

| 元素名称      | 说明                                                         | 备注 |
| ------------- | ------------------------------------------------------------ | ---- |
| id            |                                                              |      |
| parameterType | 类的全命名或者别名、或者int、double等                        |      |
| resultType    | 类的全路径，如"cn.edu.entity.Students"，结果集将通过JavaBean的规范映射；<br />也可以定义为int、double等参数；<br />也可以使用别名；<br />不可以和resultMap一起使用； |      |
| resultMap     |                                                              |      |
| flushCache    |                                                              |      |
| useCache      |                                                              |      |
| timeout       |                                                              |      |
| fetchSize     |                                                              |      |
| statementType |                                                              |      |
| resultSetType |                                                              |      |

### 2）自动映射

MyBatis配置文件settings 元素下有一个属性autoMappingBehavior，该属性有3个属性值：

+ None：取消自动映射
+ PARTIAL：自动映射没有定义嵌套结果集映射的结果集；（默认值）
+ FULL

MyBatis自动映射：只要返回的SQL列名和JavaBean的属性一致，MyBatis将自动回填这些字段而无需进行其他配置；

数据库规范要求每个单词使用下划线分隔，Java规范要求使用驼峰命名法命名，可以使用列的别名使得MyBatis自动映射，或者在MyBatis配置文件settings 元素下设置mapUnderscoreToCamelCase参数：

```xml
select id,name,sex,class_num as classNum from students where id = #{id}
```

### 3）传递多个参数

有三种方式：

+ 使用Map传递参数（实际应用中不使用）

+ 使用注解方式传递参数（传递参数少于5个时最佳）

  ```java
  // 映射接口方法定义
  public List<Students> findStudent(@Param("classnum") int classNum, @Param("sex") char sex);
  ```

  ```xml
  <!-- 映射器配置无需定义参数类型 -->
  <select id=“findStudent" resultMap="studentsMapper">
  	select id,name,sex,classnum from students where classnum = #{classnum} and sex=#{sex}
  </select>
  ```

+ 使用 JavaBean 传递参数（传递参数大于5个时）

### 4）resultMap映射结果集

```xml
<resultMap id="studentResultMap"  type="cn.edu.entity.Students">    <!-- type属性定义对应哪个JavaBean -->
    <id property = "id" column="id"/>       <!-- id元素定义主键，result元素定义普通列的映射关系 -->
    <result property = "name" column="name"/>
    <result property = "sex" column="sex"/>
    <result property = "classNum" column="class_num"/>
</resultMap>

<select id="getStudent" parameterType="int" resultMap = "studentResultMap">
    select id,name,sex,class_num from students where id = #{id}
</select>
```

resultmap用于自定义映射规则；

一般用于复杂、级联这些关联的配置；

## 3. insert

执行插入之后返回一个整数，标识插入的记录数；

### 1）insert 子元素

| 属性名称         | 描述                                                         |
| ---------------- | ------------------------------------------------------------ |
| id               |                                                              |
| parameterType    |                                                              |
| flushCache       |                                                              |
| timeout          |                                                              |
| statementType    |                                                              |
| keyProperty      | 设置哪个列作为主键，不能和keyColumn同时使用                  |
| keyColumn        | 设置第几列是主键，不能和keyProperty同时使用                  |
| useGeneratedKeys | 令MyBatis使用JDBC的getGeneratedKeys方法来取出由数据库内部生成的主键，例如自动递增字段，使用时必须要给keyProperty或者keyColumn赋值，取值为true或者false。默认为false； |

### 2）主键回填和自定义

对于主键自增字段或其他需要根据特殊规则生成的主键，如何在插入后获得这个主键？

首先使用keyProperty属性指定哪个是主键字段，然后使用useGeneratedKeys属性告诉MyBatis这个主键是否使用数据库内置策略生成；

```xml
<!-- 如数据表中主键id设置为自增字段，插入记录时传入的Students对象无需设置id的值，MyBatis使用数据库内置策略生成id的值 -->
<insert id ="insertStudent" parameterType="cn.edu.entity.Students"
       keyProperty = "id" useGeneratedKeys="true">
    insert into students(name, sex, class_num) values (#{name}, #{sex}, #{classNum})
</insert>
```

JAVA程序在调用insertStudent()方法后可以调用getId()方法来获得插入的id；

可以使用selectKey自定义生成主键的特殊规则：

```xml
<insert id ="insertStudent" parameterType="cn.edu.entity.Students"
       keyProperty = "id" useGeneratedKeys="true">
    <selectKey keyProperty="id" resultType="int" order="BEFORE">
        select if(max(id) is null, 1, max(id)+2) as newId from students    
    </selectKey>
    insert into students(id, name, sex, class_num) values (#{id}, #{name}, #{sex}, #{classNum})
</insert>
```

## 3. update

```xml
<update id="updateStudent" parameterType="cn.edu.entity.Students">
	update students set
    sex="男"
    where name="蒋松"
</update>
```

## 4. 参数

定义参数属性的时候不允许换行；

参数配置可以指定参数的类型以确定哪个typeHandler处理该参数，也可以指定哪个typeHandler处理该参数；

```xml
#{id, javaType=int, jdbcType = int}
#{sex, javaType=char, jdbcType=char, typeHandler=...}
```

大部分情况下mybatis会推断返回数据的类型，无须配置参数类型和结果类型，需要配置的往往是可能返回为空的字段类型数据，MyBatis无法判断null的类型；

### 存储过程支持

存储过程存在三种参数：输入参数（IN）、输出参数（OUT）、输入输出参数（INOUT）；

可以通过mode属性确定参数是哪一种参数，

```
#{name, mode=OUT, javaType=String, jdbcType=varchar}
```

### 特殊字符串替换（$，非参数）

用于传递sql语句自身的一部分

```xml
select ${columns} from students
```





# 动态SQL

如果使用 JDBC，很多时候需要拼装 SQL，MyBatis 提供了对 SQL  语句动态的组装能力；

| 动态 SQL 元素            | 作用                        | 备注                     |
| ------------------------ | --------------------------- | ------------------------ |
| if                       | 判断语句                    | 单条件分支判断           |
| choose(when 、otherwise) | 相当于Java 的case when 语句 | 多条件分支判断           |
| trim( where、 set)       | 辅助元素                    | 处理一些Sql 拼装问题     |
| foreach                  | 循环语句                    | 在 in 语句等列举条件常用 |

```xml
<!-- if元素：用于判断，常与test属性联合使用，判断为true时表示拼接，判断为false时表示不拼接 -->
  <insert id="insertSelective" parameterType="cn.edu.bupt.test.pojo.second.StreamAnalyse" >
    insert into stream_analyse
    <trim prefix="(" suffix=")" suffixOverrides="," >
      <if test="packetId != null" >
        packet_id,
      </if>
      <if test="packetLength != null" >
        packet_length,
      </if>
      ...
  </insert>

<!-- choose、when、otherwise元素：相当于switch...case...default语句 -->
    <select id="findRoles" parameterType="role" resultMap="roleResultMap">
        select role_no, role_name, note from t_role
        where 1=1
        <choose>
            <when test="roleNo != null and roleNo != ''">
            and role_no = #{roleName}
            </when>
            <when test="roleName != null and roleName != ''">
            and role_name = #{roleName}
            <otherwise>
            and note is not null
            </otherwise>
        </choose>
    </select>

<!-- trim、where、set元素：相当于switch...case...default语句 -->
    <!-- where 元素内的条件成立的时候才会加入where这个关键字组装到Sql语句里面 -->
    <select id="findRoles" parameterType="role" resultMap="roleResultMap">
        select role_no, role_name, note from t_role
        <where>  
            <if test="roleNo != null and roleNo != ''">
                and role_no = #{roleNo}
            </if>
            <if test="roleName != null and roleName != ''">
                and role_name = #{roleName}
            </if>
        </where>
	</select>
    
    <!-- trim 用于去除一些特殊的字符串，prefix表示添加到语句的前缀，prefixOverrides表示加入前缀前去除的字符串，后缀suffix同理 -->       
    <select id="findRoles" parameterType="role" resultMap="roleResultMap">
        select role_no, role_name, note from t_role
        <trim prefix="where" prefixOverrides="and">
            <if test="roleNo != null and roleNo != ''">
                and role_no = #{roleNo}
            </if>
            <if test="roleName != null and roleName != ''">
                and role_name = #{roleName}
            </if>
        </trim>
	</select>
   
    <!-- set用于更新某些满足条件的值 -->
    <update id="updateRole" parameterType="role">
        update t_role
        <set>
            <if test="roleName != null and roleName != ''">
                role_name = #{roleName}
            </if>
            <if test="note != null and roleName != ''">
                note = #{note}
            </if>
        </set>           
	</select>
        
<!-- foreach：循环语句，作用是遍历集合>
```







































# MyBatis 开发

## 1. 基于XML 配置文件开发

### 1）Maven 依赖配置（pom.xml）

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>MyBatisTest</artifactId>
    <version>1.0-SNAPSHOT</version>

    <!-- 属性 -->
    <properties>
        <java.version>8</java.version>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>

    <!-- 依赖关系 -->
    <dependencies>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.6</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.22</version>
        </dependency>
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.17</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13-beta-3</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

</project>
```

### 2）`resources`中创建MyBatis 全局配置文件（mybatis-config.xml）

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<!-- MyBatis的主配置文件 -->
<configuration>
    <!-- 配置环境 -->
    <environments default="mysql">
        <!-- 配置MySQL环境 -->
        <environment id="mysql">
            <!-- 配置事务类型 -->
            <transactionManager type="JDBC"/>
            <!-- 配置数据源/连接池 -->
            <dataSource type="POOLED">
                <!-- 配置连接数据库的4个基本信息  -->
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/test?useSSL=false&amp;serverTimezone=GMT"/>
                <property name="username" value="root"/>
                <property name="password" value="18813138213zsw"/>
            </dataSource>
        </environment>
    </environments>

    <!-- 指定映射器配置文件的位置 -->
    <mappers>
        <mapper resource="cn/edu/mapper/StudentsMapper.xml"/>
    </mappers>

</configuration>
```

### 3）创建数据表

### 4）基于数据表创建实体类（src/main/java下）

```java
package cn.edu.entity;

public class Students
{
    private int id;
    private String name;
    private char sex;
    private int classnum;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public char getSex()
    {
        return sex;
    }
    
    public void setSex(char sex)
    {
        this.sex = sex;
    }
    
    public int getClass_num()
    {
        return classnum;
    }
    
    public void setClass_num(int class_num)
    {
        this.classnum = class_num;
    }
    
    @Override
    public String toString()
    {
        return "Students{" + "id=" + id +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", classnum=" + classnum +
                '}';
    }
}
```

### 5）创建数据表映射接口

```java
package cn.edu.mapper;

import cn.edu.entity.Students;

public interface StudentsMapper
{
    // 查询记录
    Students getStudent(int id);
    // 插入记录
    int insertStudent(Students students);
    // 删除记录
    int deleteStudent(int id);
}
```

### 6）创建映射配置文件

MyBatis的映射配置文件位置必须和映射接口的包结构相同，包在cn.edu.mapper，那么xml也必须在resources中的cn.edu.mapper；

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="cn.edu.mapper.StudentsMapper">
    <select id="getStudent" parameterType="int" resultType="cn.edu.entity.Students">
        select * from students where id = #{id}
    </select>
    <insert id="insertStudent" parameterType="cn.edu.entity.Students">
        insert into students(id, name, sex, classnum) values (#{id}, #{name}, #{sex}, #{classnum})
    </insert>
    <delete id="deleteStudent" parameterType="int" >
        delete from students where id = #{id}
    </delete>
</mapper>
```

### 6）注册映射文件

在MyBatis 全局配置文件（mybatis-config.xml）中；

```xml
<!-- 指定映射器配置文件的位置 -->
    <mappers>
        <mapper resource="cn/edu/mapper/StudentsMapper.xml"/>
    </mappers>
```

### 7）测试

```java
package cn.edu.main;

import cn.edu.entity.Students;
import cn.edu.mapper.StudentsMapper;
import cn.edu.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;

public class MyBatisMain
{
    public static void main(String[] args)
    {
        SqlSession sqlSession = null;
        try
        {
            sqlSession = SqlSessionFactoryUtil.openSqlSession();
            // 创建映射器
            StudentsMapper studentsMapper = sqlSession.getMapper(StudentsMapper.class);
            Students students = new Students();
            students.setId(2034);
            students.setName("蒋松");
            students.setSex('女');
            students.setClass_num(3);
            // 数据表插入记录
            studentsMapper.insertStudent(students);
            studentsMapper.deleteStudent(2022);
            studentsMapper.getStudent(2024);
            sqlSession.commit();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            sqlSession.rollback();
        }
        finally
        {
            if(sqlSession != null)
                sqlSession.close();
        }
    }
}
```













