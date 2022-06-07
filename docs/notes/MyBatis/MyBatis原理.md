# 备注

+ 学习方式
  + 官网文档
    + https://mybatis.org/mybatis-3/zh/index.html#

# 一、MyBatis 介绍

**官方定义**：MyBatis 是一款优秀的持久层框架，它支持**自定义 SQL**、**存储过程**以及**高级映射**。MyBatis 免除了几乎所有的 JDBC 代码以及设置参数和获取结果集的工作。MyBatis 可以通过简单的 XML 或注解来配置和映射原始类型、接口和 Java POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录。

# 二、MyBatis 基本原理

## 1. SqlSessionFactory

每个基于 MyBatis 的应用都是以一个 `SqlSessionFactory` 的实例为核心的。

`SqlSessionFactory` 的作用：获取 `SqlSession` 实例。

`SqlSessionFactory` 的实例通过 `SqlSessionFactoryBuilder` 获得， SqlSessionFactoryBuilder 可以从 XML 配置文件或一个预先配置的 Configuration 实例来构建出 SqlSessionFactory 实例。

## 2. SqlSession

`SqlSession` 相当于 JDBC 的一个 Connection 对象，其作用主要有两个：

+ 获取映射器，映射器通过命名空间和方法名称找到对应的SQL，发送给数据库执行后返回结果。

  ```java
  
  ```

  





构建 SqlSession 实例：

```java
SqlSession session = sqlSessionFactory.openSession();
```

SqlSession 实例使用结束后需要及时关闭：

```java

```

SqlSession 的作用有两个：

+ 获取映射器。

  ```java
  
  ```

  

+ `SqlSession` 提供了在数据库执行 SQL 命令所需的所有方法，

  + **获得映射器**，让映射器通过命名空间和方法名称找到对应的SQL，发送给数据库执行后返回结果；

    ```java
    // StudentsMapper为映射接口
    StudentsMapper studentsMapper = sqlSession.getMapper(StudentsMapper.class);
    // findAll()为映射接口方法
    List<Students> students = studentsMapper.findAll();
    ```

    

  + 直接通过命名信息去执行SQL返回结果，通过调用update、insert、select、delete等方法，带上SQL的id来操作在XML中配置的SQL，它也支持事务，可以通过调用commit、rollback等方法提交或回滚事务；



























