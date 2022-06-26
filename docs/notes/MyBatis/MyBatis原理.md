# 备注

+ 学习方式
  + 官网文档（建议直接看官网文档，不需要看别的，官网文档很详细）
    + https://mybatis.org/mybatis-3/zh/index.html#

# 一、MyBatis 介绍

官方介绍如下：

+ MyBatis 是一款优秀的**持久层框架**，它支持**自定义 SQL、存储过程以及高级映射**。
+ MyBatis 免除了几乎所有的 JDBC 代码以及设置参数和获取结果集的工作。MyBatis 可以通过简单的 XML 或注解来配置和映射原始类型、接口和 Java POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录。

# 二、MyBatis 基本构成

## 1. SqlSessionFactory

每个基于 MyBatis 的应用都是以一个 `SqlSessionFactory` 的实例为核心的。

`SqlSessionFactory` 的作用：获取 `SqlSession` 实例。

`SqlSessionFactory` 的实例通过 `SqlSessionFactoryBuilder` 获得， SqlSessionFactoryBuilder 可以从 XML 配置文件或一个预先配置的 Configuration 实例来构建出 SqlSessionFactory 实例。

`SqlSessionFactory` 实例生命周期：在应用的运行期间一直存在。

## 2. SqlSession

`SqlSession` 相当于 JDBC 的一个 Connection 对象，其作用主要有两个：

+ 获取映射器，映射器通过命名空间和方法名称找到对应的SQL，发送给数据库执行后返回结果。

  ```java
  try(SqlSession sqlSession = sqlSessionFactory.openSession()) {
      CustomerMapper customerMapper = sqlSession.getMapper(CustomerMapper.class);
    Customer customer= customerMapper.selectByPrimaryKey(100);
  }
  ```
  
+ SqlSession 提供了在数据库执行 SQL 命令所需的所有方法，可以通过 SqlSession 实例来直接执行已映射的 SQL 语句。（iBatis 留下的方式，不推荐使用）

  ```java
  Customer customer = sqlSession.selectOne("org.example.dao.CustomerMapper.selectByPrimaryKey", 1);
  System.out.println(customer);
  ```

`SqlSession` 实例的生命周期：每个线程都应该有它自己的 SqlSession 实例，因为 SqlSession 的实例不是线程安全的，不能被共享。**每次使用完后需要关闭**。

## 3. 映射器

映射器定义了数据库表和 POJO 的映射关系、定义了 SQL语句与接口方法的映射关系。

映射器有两种实现方式：

+ XML 文件配置方式实现
+ 注解实现

### 1）XML文件配置方式实现

第一步：根据数据库表定义POJO

第二步：定义映射器接口

```java
public interface CustomerMapper {
    Customer selectByPrimaryKey(Short customerId);
}
```

第三步：定义 xml 映射文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="org.example.dao.CustomerMapper" >
  <resultMap id="BaseResultMap" type="org.example.entity.Customer" >
    <id column="customer_id" property="customerId" jdbcType="SMALLINT" />
    <result column="store_id" property="storeId" jdbcType="TINYINT" />
    <result column="first_name" property="firstName" jdbcType="VARCHAR" />
    <result column="last_name" property="lastName" jdbcType="VARCHAR" />
    <result column="email" property="email" jdbcType="VARCHAR" />
    <result column="address_id" property="addressId" jdbcType="SMALLINT" />
    <result column="active" property="active" jdbcType="BIT" />
    <result column="create_date" property="createDate" jdbcType="TIMESTAMP" />
    <result column="last_update" property="lastUpdate" jdbcType="TIMESTAMP" />
  </resultMap>
    
  <sql id="Base_Column_List" >
    customer_id, store_id, first_name, last_name, email, address_id, active, create_date, 
    last_update
  </sql>
    
  <select id="selectByPrimaryKey" resultMap="BaseResultMap" parameterType="java.lang.Short" >
    select 
    <include refid="Base_Column_List" />
    from customer
    where customer_id = #{customerId,jdbcType=SMALLINT}
  </select>

</mapper>
```

### 2）注解方式实现

第一步：根据数据库表定义POJO

第二步：定义映射器接口

```java
public interface CustomerMapper {
    @Select("select * from customer where customer_id = #{customerId}")
    Customer select(Short customerId);
}
```

> 使用注解来映射简单语句会使代码显得简洁，但无法使用注解实现高级映射（如动态 SQL）
>



# 三、MyBatis 配置

> 详情见官方文档：https://mybatis.org/mybatis-3/zh/configuration.html#%E9%85%8D%E7%BD%AE



# 四、XML 映射器

XML 映射器由两部分组成：

+ Dao接口（即Mapper接口）
+ XML 映射文件

Dao 接口示例如下：

```java
package org.example.dao;

public interface CustomerMapper {

    /**
     * 根据 customerId 查询对应的 Customer
     *
     * @param customerId
     * @return
     */
    Customer selectByCustomerId(Short customerId);

}
```

XML 映射文件示例如下：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="org.example.dao.CustomerMapper" >
    
  <resultMap id="BaseResultMap" type="org.example.entity.Customer" >
    <id column="customer_id" property="customerId" jdbcType="SMALLINT" />
    ...
  </resultMap>
    
  <sql id="Base_Column_List" >
    customer_id, store_id, first_name, last_name, email, address_id, active, create_date, last_update
  </sql>
    
  <select id="selectByCustomerId" parameterType="java.lang.Short" resultMap="BaseResultMap">
    select * from customer
    where customer_id = #{customerId,jdbcType=SMALLINT}
  </select>
  
</mapper>
```

## 1. XML 映射文件中的主要元素

XML 映射文件中可以定义如下元素：

| 元素名称 | 描述                      |
| -------- | ------------------------- |
| select   | 映射查询语句              |
| insert   | 映射插入语句              |
| update   | 映射更新语句              |
| delete   | 映射删除语句              |
| sql      | 定义可重用的 SQL 代码片段 |
|          |                           |

## 2. select 元素

用于映射一条 SQL 查询语句。

select 元素提供了多个属性用于配置每条语句的行为细节。

```xml
<select id="selectByCustomerId" parameterType="java.lang.Short" resultMap="BaseResultMap">
    select * from customer
    where customer_id = #{customerId,jdbcType=SMALLINT}
</select>
```

+ id 属性：指定 SQL 语句在命名空间中唯一的标识符；
+ parameterType 属性：指定传入 SQL 语句的参数的类全限定名或别名；
+ resultMap 属性：指定查询结果与 pojo 之间的映射规则

## 3. insert 元素

用于映射一条 SQL 插入语句。

insert 元素同样提供了多个属性来配置每条语句的行为细节。

```xml
<insert id="insertCustomer" parameterType="org.example.entity.Customer">
    insert into customer
    (store_id, first_name, last_name)
    values (#{storeId,jdbcType=TINYINT}, #{firstName,jdbcType=VARCHAR},
    #{lastName,jdbcType=VARCHAR})
</insert>
```

### 1）主键回填

如果数据库中的主键设置为自增字段，可以通过设置 `useGeneratedKeys` 属性和 `keyProperty` 属性获取数据库自动生成的主键值。

```xml
<insert id="insertCustomer" parameterType="org.example.entity.Customer" useGeneratedKeys="true" keyProperty="customerId">
    insert into customer
    (store_id, first_name, last_name)
    values (#{storeId,jdbcType=TINYINT}, #{firstName,jdbcType=VARCHAR},
    #{lastName,jdbcType=VARCHAR})
</insert>
```

```java
try (SqlSession sqlSession = SqlSessionFactoryUtil.openSqlSession()) {
    CustomerMapper customerMapper = sqlSession.getMapper(CustomerMapper.class);
    Customer customer = new Customer();
    customer.setStoreId((byte) 1);
    customer.setFirstName("zhu");
    customer.setLastName("shengwen");
    customerMapper.insertCustomer(customer);
    // 插入、更新、删除数据都需要执行commit，不然修改不会生效
    sqlSession.commit();
    System.out.println(customer.getCustomerId());	// 数据库中自动生成的主键回填
}
```

### 2）插入多行记录

```java
public interface CustomerMapper {

    /**
     * 插入多条  Customer 记录
     *
     * @param customers
     * @return
     */
    int insertCustomers(Customer[] customers);

}
```

```xml
<insert id="insertCustomers">
    insert into customer
    (store_id, first_name, last_name)
    values
    <foreach item="customer" collection="array" separator=",">
        (#{customer.storeId}, #{customer.firstName}, #{customer.lastName})
    </foreach>
</insert>
```

## 4. sql 元素

用于定义可重用的 SQL 代码片段，以便在其他 SQL 语句中直接引用。 

 `<sql>` 元素定义示例如下：

```xml
<sql id="Base_Column_List">
    customer_id, store_id, first_name, last_name, email, address_id, active, create_date, last_update
</sql>
```

在其他 SQL 语句中通过 `<include>` 元素引用已定义的代码片段：

```xml
<include refid="Base_Column_List"/>
```

## 5. #{}：参数映射 

可以使用 `#{}` 为 SQL 语句定义参数，MyBatis 会创建 `PreparedStatement` 参数占位符，并通过占位符安全地设置参数，从而 **防止 SQL 注入**。

```xml
<select id="selectByCustomerId" parameterType="java.lang.Short" resultMap="BaseResultMap">
    select * from customer
    where customer_id = #{customerId}
</select>
```

可以使用 `parameterType` 属性设置传入参数的类型。

> 可以省略 `parameterType` 属性设置的场景：
>
> + 基本数据类型
> + 原始类型或简单数据类型（比如 Integer 和 String）因为没有其它属性，会用它们的值来作为参数，不需要设置 parameterType。
> + 使用注解 `@Param` 传递多个参数进行查询
>
> 不可以省略：
>
> + 传入的参数是一个拥有多个属性的对象
> + 传入的参数是一个map（此时`parameterType`参数类型为`map`，SQL 语句中参数名称是 map 中的key）

```xml
<!-- 最简单的参数定义 -->
#{customerId}

<!-- 可以显示指定参数类型:
 	 注1：MyBatis可以通过类型处理器（TypeHandler）推断出传入参数的类型，所以一般不需要设置javaType，参数类型为HashMap时除外
	 注2：如果一个列允许使用null值，并且会使用值为null的参数，就必须要指定JDBC类型（jdbcType）
-->
#{customerId,javaType=short,jdbcType=SMALLINT}

<!-- 对于数值类型，还可以设置 numericScale 指定小数点后保留的位数 -->
#{height,javaType=double,jdbcType=NUMERIC,numericScale=2}
```

传递多个参数：

+ 方式一：通过 map 传递多个参数（此时需要设置 `parameterType`参数类型为`map`）

  ```java
  /**
   * 通过map传入多个参数
   *
   * @param paramMap
   * @return
   */
  Customer selectByMap(Map<String, Object> paramMap);
  ```

  ```xml
  <select id="selectByMap" parameterType="map" resultMap="BaseResultMap">
      select
      <include refid="Base_Column_List"></include>
      from customer
      where ${column} = #{value}
  </select>
  ```

+ 方式二：将多个参数封装到一个 `javaBean` 中

+ 方式三：使用注解 `@Param` 

  ```java
  /**
   * 动态指定列名进行查询
   *
   * @param column
   * @param value
   * @return
   */
  Customer selectByColumn(@Param("column") String column, @Param("value") String value);
  ```

  ```xml
  <select id="selectByColumn" resultMap="BaseResultMap">
      select
      <include refid="Base_Column_List"></include>
      from customer
      where ${column} = #{value}
  </select>
  ```

## 6. ${}：字符串替换

可以使用 `${}` 为 SQL 语句动态的插入字符串，这种方法会导致潜在的 SQL 注入攻击。

```xml
<!-- 示例：动态指定列名然后进行查找 -->
<select id="selectByColumn" resultMap="BaseResultMap">
    select
    <include refid="Base_Column_List"></include>
    from customer
    where ${column} = #{value}
</select>
```

## 7. resultMap 元素：结果映射

```xml
<resultMap id="BaseResultMap" type="org.example.entity.Customer">
    <id column="customer_id" property="customerId" jdbcType="SMALLINT"/>
    <result column="store_id" property="storeId" jdbcType="TINYINT"/>
    <result column="first_name" property="firstName" jdbcType="VARCHAR"/>
    <result column="last_name" property="lastName" jdbcType="VARCHAR"/>
</resultMap>
```

> `resultMap` 属性意义：
>
> + id：当前命名空间中的一个唯一标识，用于标识一个结果映射；
> + type：类的完全限定名, 或者一个类型别名；
>
> id 和 result 元素都将一个列的值映射到一个简单数据类型（String, int, double, Date 等）的属性，两者不同点在于：id 元素对应的属性会被标记为对象的标识符，在比较对象实例时使用，这样可以提高整体的性能，尤其是进行缓存和嵌套结果映射（也就是连接映射）的时候。
>
> 支持的 JDBC 类型：https://mybatis.org/mybatis-3/zh/sqlmap-xml.html#%E6%94%AF%E6%8C%81%E7%9A%84-jdbc-%E7%B1%BB%E5%9E%8B



# 五、动态 SQL

使用动态 SQL 机制可以解决根据不同条件拼接 SQL 语句带来的繁琐。

详情参考官网：https://mybatis.org/mybatis-3/zh/dynamic-sql.html

## 1. if：条件判断

语法格式如下：

```xml
<if test="判断条件">
	SQL语句
</if>
```

当判断条件为 true 时，才会拼接其中的 SQL 语句。

示例如下：

```java
/**
 * 根据firstName和lastName查询顾客
 *
 * @param firstName 不能为空
 * @param lastName
 * @return
 */
List<Customer> selectCustomers(@NotNull @Param("firstName") String firstName,
                               @Nullable @Param("lastName") String lastName);
```

```xml
<select id="selectCustomers" resultMap="BaseResultMap">
    select
    <include refid="Base_Column_List"></include>
    from customer
    where first_name = #{firstName}
    <if test="lastName != null">
        and last_name = #{lastName}
    </if>
</select>
```

## 2. choose、when、otherwise

语法格式如下：

```xml
<choose>
    <when test="判断条件1">
        SQL语句1
    </when>
    <when test="判断条件2">
        SQL语句2
    </when>
    <otherwise>
        SQL语句3
    </otherwise>
</choose>
```

从上往下选择第一个判断条件为 true 的 SQL 语句进行拼接。（从多个条件中只选择一个）

## 3. trim、where、set

> <span style = "color:red; font-weight:bold">这三个元素用于消除拼接 SQL 语句时遇到的问题：</span>
>
> + where 子句没有判断条件：`SELECT * FROM BLOG WHERE`
> + where 子句判断条件以 `AND` 开头：`SELECT * FROM BLOG WHERE AND title like ‘someTitle’`

+ **where 元素**

  只会在子元素返回任何内容的情况下才插入 “WHERE” 子句。而且，若子句的开头为 “AND” 或 “OR”，where元素也会将它们去除。

  ```xml
  <select id="selectCustomers" resultMap="BaseResultMap">
      select
      <include refid="Base_Column_List"></include>
      from customer
      <where>
          <if test="firstName != null">
              first_name = #{firstName}
          </if>
          <if test="lastName != null">
              and last_name = #{lastName}
          </if>
      </where>
  </select>
  ```

+ **trim 元素**

  一般用于给 SQL 语句添加或删除后缀。

  语法格式如下：

  ```xml
  <trim prefix="待添加的前缀" suffix="待添加的后缀" prefixOverrides="待删除的前缀" suffixOverrides="待删除的后缀">
      SQL语句
  </trim>
  ```

  示例如下：

  ```xml
  <insert id="insertSelective" parameterType="org.example.entity.Customer">
      insert into customer
      <trim prefix="(" suffix=")" suffixOverrides=",">
          <if test="storeId != null">
              store_id,
          </if>
          <if test="firstName != null">
              first_name,
          </if>
          <if test="lastName != null">
              last_name,
          </if>
      </trim>
      <trim prefix="values (" suffix=")" suffixOverrides=",">
          <if test="storeId != null">
              #{storeId,jdbcType=TINYINT},
          </if>
          <if test="firstName != null">
              #{firstName,jdbcType=VARCHAR},
          </if>
          <if test="lastName != null">
              #{lastName,jdbcType=VARCHAR},
          </if>
      </trim>
  </insert>
  ```

+ **set 元素**

  用于动态更新，可以为 SQL 语句动态的添加 set 关键字，剔除追加到条件末尾多余的逗号。

  ```xml
  <update id="updateByPrimaryKeySelective" parameterType="org.example.entity.Customer">
      update customer
      <set>
          <if test="storeId != null">
              store_id = #{storeId,jdbcType=TINYINT},
          </if>
          <if test="firstName != null">
              first_name = #{firstName,jdbcType=VARCHAR},
          </if>
          <if test="lastName != null">
              last_name = #{lastName,jdbcType=VARCHAR}
          </if>
      </set>
      where customer_id = #{customerId,jdbcType=SMALLINT}
  </update>
  ```

## 4. foreach：遍历集合

用于遍历集合。

语法格式：

```xml
<foreach item="item" index="index" collection="list|array|map" open="(" separator="," close=")">
    参数值
</foreach>
```

> 属性解释：
>
> + item 属性：表示集合中每一个元素进行迭代时的别名；
> + index 属性：表示在迭代过程中每次迭代到的位置；
> + collection 属性：可迭代集合；
> + open 属性：开头字符串；
> + separator 属性：集合项迭代之间的分隔符；
> + close 属性：结尾字符串；

示例如下：

```xml
<insert id="insertCustomers">
    insert into customer
    (store_id, first_name, last_name)
    values
    <foreach item="customer" collection="array" separator=",">
        (#{customer.storeId}, #{customer.firstName}, #{customer.lastName})
    </foreach>
</insert>
```



# 其他

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




