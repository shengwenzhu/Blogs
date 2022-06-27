# SpringBoot 项目整合 MyBatis

## 1. 在 pom.xml 文件中添加依赖

```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.2.2</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```

## 2. 数据源配置

方式一：`application.properties` 文件中配置

```properties
# 数据源配置
spring.datasource.url=jdbc:mysql://localhost:3305/sakila?serverTimezone=Asia/Shanghai&useSSL=false
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=123456
```

## 3. MyBatis 映射文件生成（XML+接口）

## 4. MyBatis配置

方式一：在 `application.properties` 文件中配置

```properties
# mybatis配置
# XML映射文件路径
mybatis.mapper-locations=classpath:mapper/*.xml
```

## 5. 注册映射器

**方式一：使用 `@Mapper` 注解**

在 mapper 接口中添加 `@Mapper` 注解，默认情况下，`mybatis-spring-boot-starter` 会自动扫描带用了 `@Mapper` 注解的 mapper 接口，并将其注册为 bean。

```java
@Mapper
public interface CustomerMapper {
    Customer queryCustomerById(String customerId);
}
```

**方式二：使用  `@MapperScan` 注解**

如果要在每个 mapper 接口中添加 `@Mapper` 注解会比较繁琐。

可以使用  `@MapperScan` 注解配置要扫描的包，然后包下所有的所有 mapper 接口都会被注册为 bean。

使用方式：在 SpringBoot 应用的启动类中使用 `@MapperScan` 注解。

```java
@MapperScan(basePackages = {"com.example.demo.dao"})
@SpringBootApplication
public class SpringbootDemoApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SpringbootDemoApplication.class, args);
    }

}
```











