# 第一步：添加maven依赖 

在`pom.xml` 文件中添加redis依赖

```xml
<!-- redis依赖-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<!-- 如果要使用lettuce连接池，必须添加commons-pool2依赖 -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>
```

## Jedis/Lettuce

Jedis 和 Lettuce 都是 Redis 的 client，都可用于连接 Redis Server。

SpringBoot 框架在 1.x.x 版本时默认使用 Jedis 客户端，在 2.x.x 版本时默认使用 Lettuce 客户端。

+ Jedis

  Jedis 

  





# 第二步：Redis配置

在 `application.yml` 文件中配置Redis

```yml

```

