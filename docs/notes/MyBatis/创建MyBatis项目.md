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

```

> `com.mysql.jdbc.Driver` 和 `com.mysql.cj.jdbc.Driver` 区别：参考博客https://blog.csdn.net/superdangbo/article/details/78732700

第二步：读取配置文件构建SqlSessionFactory

MyBatis 包含了一个名叫 `Resources` 的工具类，它包含一些实用方法，使得从类路径或其它位置加载资源文件更加容易。

```java

```

### 方式二：通过配置类构建

一般不建议采用这种方式。

## 3. 创建SqlSession实例

























