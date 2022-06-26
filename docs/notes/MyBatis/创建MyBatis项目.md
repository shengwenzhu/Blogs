# 创建 MyBatis 项目

注：本项目通过 Maven 进行构建

## 1. 在 pom.xml 文件中添加依赖

```xml
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.10</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.22</version>
</dependency>
```

## 2. 创建 SqlSessionFactory 实例

存在两种构建方式：

+ XML配置文件
+ 配置类

### 方式一：通过 XML 构建

第一步：在 `resources` 文件下创建 `mybatis-config.xml` 配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
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
                <property name="url" value="jdbc:mysql://localhost:3305/sakila?serverTimezone=Asia/Shanghai&amp;useSSL=false"/>
                <property name="username" value="root"/>
                <property name="password" value="123456"/>
            </dataSource>
        </environment>
    </environments>
    <!-- 定义映射器 -->
    <mappers>
        <mapper resource="mapper/CustomerMapper.xml"/>
    </mappers>
</configuration>
```

> `com.mysql.jdbc.Driver` 和 `com.mysql.cj.jdbc.Driver` 区别：参考博客https://blog.csdn.net/superdangbo/article/details/78732700

第二步：读取配置文件构建SqlSessionFactory

MyBatis 包含了一个名叫 `Resources` 的工具类，它包含一些实用方法，使得从类路径或其它位置加载资源文件更加容易。

```java
/**
 * 使用单例模式构建SqlSessionFactory的实例
 */
public static SqlSessionFactory initSqlSessionFactory() {
    String resource = "mybatis-config.xml";
    InputStream inputStream = null;
    try {
        inputStream = Resources.getResourceAsStream(resource);
        if (sqlSessionFactory == null) {
            synchronized (CLASS_LOCK) {
                if (sqlSessionFactory == null) {
                    sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
                }
            }
        }
    } catch (IOException e) {
        logger.error(e.getMessage());
    }
    return sqlSessionFactory;
}
```

### 方式二：通过配置类构建

一般不建议采用这种方式。

## 3. 创建SqlSession实例

```java
/**
 * 构建SqlSession实例
 * @return
 */
public static SqlSession openSqlSession() {
    if (sqlSessionFactory == null)
        initSqlSessionFactory();
    return sqlSessionFactory.openSession();
}
```

## 4. 根据数据表创建实体类（POJO）

```java
package org.example.entity;

public class Customer {
    private Short customerId;

    private Byte storeId;

    private String firstName;

    private String lastName;

    private String email;

    private Short addressId;

    private Boolean active;

    private Date createDate;

    private Date lastUpdate;

    /* getter and setter 方法 */
    ....
}
```

## 5. 创建映射接口

```java
package org.example.dao;

public interface CustomerMapper {
    Customer selectByPrimaryKey(Short customerId);
}
```

## 6. 创建映射xml文件

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
    customer_id, store_id, first_name, last_name, email, address_id, active, create_date, last_update
  </sql>
    
  <select id="selectByPrimaryKey" resultMap="BaseResultMap" parameterType="java.lang.Short" >
    select 
    <include refid="Base_Column_List" />
    from customer
    where customer_id = #{customerId,jdbcType=SMALLINT}
  </select>
  
</mapper>
```

## 7. 在mybatis-config.xml配置文件中注册映射文件

```xml
<mappers>
	<mapper resource="mapper/CustomerMapper.xml"/>
</mappers>
```

## 8. 测试

```java
public static void main(String[] args) {
    try (SqlSession sqlSession = SqlSessionFactoryUtil.openSqlSession()) {
        CustomerMapper customerMapper = sqlSession.getMapper(CustomerMapper.class);
        Short key = 1;
        Customer customer = customerMapper.selectByPrimaryKey(key);
        System.out.println(customer);
    }
}
```















