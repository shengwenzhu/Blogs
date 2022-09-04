# @RunWith 注解

@RunWith 注解用于指定测试类的运行器：

- @RunWith(JUnit4.class)：用 JUnit4 来运行
- @RunWith(SpringJUnit4ClassRunner.class)：让测试在 Spring 容器环境下执行



# 单元测试示例

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class StudentServiceTest {

    @Autowired
    StudentService studentService;

    @Test
    public void message() {
        studentService.message();
    }
}
```

