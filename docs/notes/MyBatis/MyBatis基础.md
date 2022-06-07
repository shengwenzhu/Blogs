# 一、ORM 模型

ORM（Object Relational Mapping，对象关系映射）

ORM 模型：将数据库的表与简单 Java 对象（POJO）建立一个映射关系；

注：ORM 模型都是基于 JDBC 进行封装；

## 1. Hibernate

+ **全表映射**

  通过 xml 映射文件建立 POJO 与数据库表的映射；

  比如更新字段值时需要发送所有的字段；

+ 不需要编写 SQL 语句（使用 HQL 语句）

  无法优化 SQL；

## 2. MyBatis

+ **半自动映射**

  需要提供 POJO、SQL、映射关系（全表映射只需要提供 POJO、映射关系）

+ 支持自定义 SQL、存储过程以及高级映射



# 二、MyBatis 的基本构成

## 1. SqlSessionFactoryBuilder

用于生成 SqlSessionFactory 实例；

> SqlSessionFactoryBuilder 实例生命周期：创建 SqlSessionFactory 实例后作用就结束了，就可以被回收了；

## 2. SqlSessionFactory

用于生成 SqlSession 实例；

> SqlSessionFactory 实例生命周期：应用程序需要访问数据库时，就要通过 SqlSessionFactory 创建 SqlSession，所以 SqlSessionFactory 存在于 MyBatis 应用的整个生命周期中；

**每个基于 MyBatis 的应用都是以一个 SqlSessionFactory 的实例为核心的**；

**每一个数据库对应一个 SqlSessionFactory**；

可以通过 XML 配置文件或 Java 代码生成 SqlSessionFactory 实例；

### 1）通过 XML 配置文件构建 SqlSessionFactory 实例

建议使用类路径下的资源文件进行配置， 但也可以使用任意的输入流（InputStream）实例，比如用文件路径字符串或 file:// URL 构造的输入流。MyBatis 包含一个名叫 Resources 的工具类，它包含一些实用方法，使得从类路径或其它位置加载资源文件更加容易；

第一步：创建 mybatis-config.xml 文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!-- 配置环境 -->
    <environments default="mysql">
        <!-- 配置MySQL环境 -->
        <environment id="mysql">
            <!-- 配置事务管理器 -->
            <transactionManager type="JDBC"/>
            <!-- 配置数据源/连接池 -->
            <dataSource type="POOLED">
                <!-- 配置连接数据库的4个基本信息  -->
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3305/test?useSSL=false&amp;serverTimezone=GMT"/>
                <property name="username" value="root"/>
                <property name="password" value="123456"/>
            </dataSource>
        </environment>
    </environments>

    <!-- 指定XML映射文件的位置 -->
    <mappers>
        <mapper resource="mapper/StudentMapper.xml"/>
    </mappers>
</configuration>
```

+ 第二步：构建 SqlSessionFactory 实例

  可以将其封装到一个类中；

  ```java
  public class SqlSessionFactoryUtil
  {
      // SqlSessionFactory 对象
      private static SqlSessionFactory sqlSessionFactory = null;
      // 类线程锁
      private static final Class CLASS_LOCK = SqlSessionFactory.class;
      
      // 私有化构造参数
      private SqlSessionFactoryUtil() {}
      
      // 构建SqlSessionFactory
      public static SqlSessionFactory initSqlSessionFactory()
      {
          String resource = "mybatis-config.xml";
          InputStream inputStream = null;
          try
          {
              inputStream = Resources.getResourceAsStream(resource);
              synchronized(CLASS_LOCK)
              {
                  if (sqlSessionFactory == null)
                  {
                      sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
                  }
              }
              return sqlSessionFactory;
          } catch (IOException e)
          {
              e.printStackTrace();
          }
          return null;
      }
      
      // 构建 SqlSession
      public static SqlSession openSqlSession()
      {
          if (sqlSessionFactory == null) initSqlSessionFactory();
          return sqlSessionFactory.openSession();
      }
  }
  ```

### 2）通过 Java 代码构建 SqlSessionFactory 实例

采用代码方式一般是需要加入自己特性的时候才会用到，比如要求数据源配置的信息是加密的；

> 了解详情查看：https://mybatis.org/mybatis-3/zh/getting-started.html



## 3. SqlSession

SqlSession 的作用主要有两个：

+ **获取映射器**，让映射器通过命名空间和方法名称找到对应的SQL，发送给数据库执行后返回结果；

  ```java
  StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);
  List<Student> students = studentMapper.findAll();
  ```

+ 通过 SqlSession 实例来直接执行已映射的 SQL 语句

  ```java
  sqlSession = SqlSessionFactoryUtil.openSqlSession();
  Student student = sqlSession.selectOne("com.zhu.mybatis.dao.StudentMapper.selectByPrimaryKey", 2);
  System.out.println(student.getId() + "," + student.getName() + "," + student.getScore());
  ```

  注：这是 iBatis 版本留下的方式，SqlSession 定义了在数据库执行 SQL 命令所需的很多方法： SqlSession 实例通过调用 update、insert、select、delete等方法，带上 SQL 的 id 来操作在 XML 中配置的 SQL，这种方法也支持事务，可以通过调用commit、rollback等方法提交或回滚事务；

> SqlSession 的实例不是线程安全的；
>
> SqlSession 使用结束后需要显式关闭；



## 4. 映射器

映射器由两部分组成：Java 接口、XML 文件（或注解）

### 1）通过 XML 映射文件实现 Mapper

第一步：定义接口

```java
package com.zhu.mybatis.dao;
import com.zhu.mybatis.entity.Student;
public interface StudentMapper
{
    Student selectByPrimaryKey(Integer id);
    int insert(Student record);
}
```

第二步：定义 XML 映射文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.zhu.mybatis.dao.StudentMapper" >
  <resultMap id="BaseResultMap" type="com.zhu.mybatis.entity.Student" >
    <id column="id" property="id" jdbcType="INTEGER" />
    <result column="name" property="name" jdbcType="VARCHAR" />
    <result column="score" property="score" jdbcType="SMALLINT" />
    <result column="subject" property="subject" jdbcType="CHAR" />
  </resultMap>
  <sql id="Base_Column_List" >
    id, name, score, subject
  </sql>
    
  <select id="selectByPrimaryKey" resultMap="BaseResultMap" parameterType="java.lang.Integer" >
    select 
    <include refid="Base_Column_List" />
    from student
    where id = #{id,jdbcType=INTEGER}
  </select>
    
  <insert id="insert" parameterType="com.zhu.mybatis.entity.Student" >
    insert into student (id, name, score, 
      subject)
    values (#{id,jdbcType=INTEGER}, #{name,jdbcType=VARCHAR}, #{score,jdbcType=SMALLINT}, 
      #{subject,jdbcType=CHAR})
  </insert>

</mapper>
```

### 2）注解方式实现 Mapper

第一步：定义接口

```java
package com.zhu.mybatis.dao;

import org.apache.ibatis.annotations.Select;

public interface StudentMapper
{    
    @Select("select count(*) from student")
    int count();
}
```



# 三、MyBatis 配置

## 1. MyBatis XML 配置文件结构

注：各种配置不能颠倒顺序

```xml
<?xml version="1.0" endcoding="UTF-8"?>
<configuration>
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

## 2. properties









配置属性，可以在配置文件的上下文使用；

有三种配置方式：

+ property子元素
+ properties配置文件
+ 程序传递参数

1）property子元素

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

2）properties配置文件（推荐）

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

3）优先级

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

1）系统定义别名

2）自定义别名

## 5. typeHandler类型处理器

MyBatis 在预处理语句（PreparedStatement）中设置一个参数时，或者从结果集（ResultSet）中取出一个值时，都会用注册了的 typeHandler 进行处理；typeHandler 将参数从 javaType 转化为 jdbcType，从数据库取出结果时将 jdbcType 转化为 javaType;

typeHandler 也分为系统定义和用户自定义两种，使用系统定义就可以实现大部分的功能；

1）系统定义的typeHandler

2）自定义 typeHandler

3）枚举类型 typeHandler

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

MyBatis 的真正强大在于它的语句映射；

## 1. XML 映射文件的主要元素

+ `cache` – 该命名空间的缓存配置。
+ `cache-ref` – 引用其它命名空间的缓存配置。
+ `resultMap` – 描述如何从数据库结果集中加载对象，是最复杂也是最强大的元素。
+ `sql` – 可被其它语句引用的可重用语句块。
+ `insert` – 映射插入语句。
+ `update` – 映射更新语句。
+ `delete` – 映射删除语句。
+ `select` – 映射查询语句。



## 2. select

select 元素用于映射查询语句；

```xml
<select id="selectByPrimaryKey" parameterType="java.lang.Integer" resultMap="BaseResultMap">
    select 
    <include refid="Base_Column_List" />
    from student
    where id = #{id,jdbcType=INTEGER}
</select>
```





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



































MyBatis：MyBatis 是支持定制化SQL、存储过程以及高级映射的优秀的**持久层框架**，也是一种**半自动映射**框架；

MyBatis 包含三个部分：

+ SQL
+ 映射关系（XML或者注解）
+ POJO

MyBatis 是一个基于`SqlSessionFactory `构建的框架。`SqlSessionFactory` 接口的作用是生成SqISession 接口对象。**`SqlSession `接口对象是MyBatis 操作的核心**；























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










