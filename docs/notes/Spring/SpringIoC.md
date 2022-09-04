

首先需要清楚以下两个容易混淆的术语：

- 控制反转（Inversion of Control，IoC）：传统编程中，如果一个对象 A 依赖于另一个对象 B，那么对象 A 就需要在其初始化时或者运行某个方法时主动创建对象 B，每个对象负责管理其所依赖的对象的引用，这将会导致对象之间的高度耦合。<span style="color:red;font-weight:bold;">IoC 的核心思想</span>是通过引入第三方组件（即IoC容器）负责对象实例的创建、管理对象的整个生命周期、管理对象之间的依赖关系，降低对象之间的耦合。

- 依赖注入（Dependency Injection，DI）：假如对象 A 依赖于对象 B，由 IoC容器负责创建对象 B 并将对象 B 注入到对象 A 中。

控制反转与依赖注入**本质上是从不同的角度对同一件事情进行了描述**，控制翻转侧重于原理，依赖注入侧重于实现。

# 1. IoC 容器

在 Spring 应用中，**IoC 容器负责创建对象、建立对象之间的依赖关系、管理对象的整个生命周期**。

Spring 自带了多个容器实现，可以将其分为两类：

+ BeanFactory

  类：org.springframework.beans.factory.BeanFactory

  最简单的容器接口，提供了基本的依赖注入支持，Spring 中所有的容器都需要实现该接口。

+ ApplicationContext（应用上下文）

  > org.springframework.context.ApplicationContext

  基于 BeanFactory 构建，提供应用框架级别的服务。

Spring 提供了多种类型的应用上下文：

+ AnnotationConfigApplicationContext ：从 Java 配置类中加载已被定义的 bean；
+ ClassPathXmlApplicationContext：从类路径下的 XML 配置文件中加载已被定义的 bean；
+ FileSystemXmlApplicationContext：从文件系统下的 XML 配置文件中加载已被定义的 bean，需要提供XML 文件的完整路径。

```java
/**
 * StudentConfig 配置类
 */
@Configuration
public class StudentConfig {

    /**
     * 声明 student bean
     */
    @Bean("student")
    public Student createStudent() {
        Student student = new Student();
        student.setName("zhu");
        student.setSex('男');
        return student;
    }
}

/**
 * 通过AnnotationConfigApplicationContext加载定义的bean
 */
public void test() {
    ApplicationContext context = new AnnotationConfigApplicationContext(StudentConfig.class);
    Student student = (Student) context.getBean("student");
    logger.info(student.getName());
}
```

# 2. 定义 bean

定义 bean，即告诉 Spring 要创建哪些 bean

## 2.1 @Component 注解

第一步：使用 @Component 注解声明 bean

```java
@Component
public class StudentService implements UserService {

    @Override
    public void message() {
        System.out.println(UserEnum.Student.getMessage());
    }
}
```

第二步：扫描 bean，存在两种方式

- 方式一：在配置类中使用 @ComponentScan 注解扫描 bean
- 方式二：在 xml 文件中配置

```java
// 方式一
@Configuration
@ComponentScan("com.example.zsw")
public class ScriptConfig {
}

// 方式二：applicationContext.xml文件中配置
<context:component-scan base-package="com.example.zsw"/>
```

第三步：测试

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ScriptConfig.class)
// @ContextConfiguration(locations = "classpath:applicationContext.xml")
public class MyTest {

    @Autowired
    StudentService studentService;

    @Test
    public void test() {
        studentService.message();
    }
}
```

### @Component 注解的扩展

当一个注解被`@Component` 注解修饰，然后使用该注解修饰其他类时，Spring也会为这些类创建bean，所以，这些被 `@Component` 注解修饰的注解可以看作是 `@Component` 注解的扩展。

在spring中，存在以下几个 `@Component` 注解的扩展：

+ `@Controller`：标记控制层组件
+ `@Service`：标记业务层组件
+ `@Repository`：标记数据访问组件，即DAO组件

### Bean 的名字

Spring 应用上下文中的所有 bean 都会有一个唯一的 name

如果没有显式指定 bean 的 name，默认情况下 Spring 会将类名的第一个字母变为小写后作为 bean 的 name。

可以显式指定bean的name，如 `@Component("studentService")`

### @ComponentScan

声明 bean 后，需要显式配置 Spring 扫描带有 `@Component` 注解的类，并为其创建 bean。

 `@ComponentScan` 注解默认扫描其注解的类所在包以及这个包下的所有子包。

可以通过显式指定 `@ComponentScan` 的 `value` 属性（或 `basePackages` 属性，这两个属性等价）指定需要扫描的包。

```java
// 设置扫描单个包
@ComponentScan("com.example.zsw")
// 设置扫描多个包
@ComponentScan(value={"com.example.zsw.service", "com.example.zsw.service.config"})
// 设置扫描的类或接口，进而扫描该类或接口所在的包
@ComponentScan(basePackageClasses=StudentService.class)
```

## 2.2 xml 声明

```xml
<!-- 声明一个最简单的bean：id属性指定bean的名字，class属性指定了bean的类（需要使用全限定类名）-->
<!-- 当使用如下方式声明bean时，Spring会调用该类的无参构造器创建bean -->
<bean id="studentService" class="com.example.zsw.service.impl.StudentService"/>
```

## 2.3 @Bean 注解

在配置类中使用 @Bean 注解声明bean

默认情况下，创建的 bean 的 name 与方法名相同，可以通过显式设置 @Bean 注解的 name 属性指定bean的name，如`@Bean(name="xxx")`

```java
@Configuration
public class StudentConfig {
  
    @Bean(name = "student")
    public Student createStudent() {
        Student student = new Student();
        return student;
    }
}
```

# 3. 注入 bean

Spring通过依赖注入的方式创建对象之间的依赖关系。

## 3.1 @Autowired 注解

`@Autowired` 注解通过 bean 的类型注入 bean，如果 Spring 上下文中存在多个类型相同的 bean，将会抛出异常。

如果希望按照 bean 的 name 注入 bean，需要配合 @Qualifier 注解使用。

```java
@Autowired
@Qualifier("studentService")
private UserService userService;
```

默认情况下要求注入的 bean 必须存在，如果允许注入的 bean 不存在，需要设置@Autowired注解的required属性为false，即`@Autowired(required=false)` 。

`@Autowired` 注解可以用于修饰字段、构造器、方法等，相应的，可以将使用 `@Autowired` 注解注入bean的方法分为三种：Field 注入、构造器注入、Setter方法注入。

- Field 注入（虽然这种方式很简单，但是Spring官方不推荐这种方式）

  ```java
  @Component
  public class StudentService implements UserService {
  
      @Autowired
      WorkService workService;
      
      ......
  }
  
  ```

- 构造器注入

  ```java
  @Component
  public class StudentService implements UserService {
  
      private WorkService workService;
  
      @Autowired
      public StudentService(WorkService workService) {
          this.workService = workService;
      }
    
      ......
  }
  
  ```

- Setter 方法注入

  ```java
  @Component
  public class StudentService implements UserService {
  
      private WorkService workService;
  
      @Autowired
      public void setWorkService(WorkService workService) {
          this.workService = workService;
      }
  
      ......
  }
  ```

### 三种注入方式对比

+ 在 Spring 3.x 推出的时候，Spring 官方在对比构造器注入和 Setter 注入时，推荐使用 Setter 方法注入

  > 官方解释：
  >
  > The Spring team generally advocates setter injection, because large numbers of constructor arguments can get unwieldy, especially when properties are optional. Setter methods also make objects of that class amenable to reconfiguration or re-injection later. Management through JMX MBeans is a compelling use case.
  >
  > Some purists favor constructor-based injection. Supplying all object dependencies means that the object is always returned to client (calling) code in a totally initialized state. The disadvantage is that the object becomes less amenable to reconfiguration and re-injection.

+ Spring 4.x 的时候，Spring 官方在对比构造器注入和 Setter 注入时，推荐使用构造器注入方式

  > The Spring team generally advocates constructor injection as it enables one to implement application components as immutable objects and to ensure that required dependencies are not null. Furthermore constructor-injected components are always returned to client (calling) code in a fully initialized state. As a side note, a large number of constructor arguments is a bad code smell, implying that the class likely has too many responsibilities and should be refactored to better address proper separation of concerns.
  >
  > Setter injection should primarily only be used for optional dependencies that can be assigned reasonable default values within the class. Otherwise, not-null checks must be performed everywhere the code uses the dependency. One benefit of setter injection is that setter methods make objects of that class amenable to reconfiguration or re-injection later. Management through JMX MBeans is therefore a compelling use case for setter injection.

选择构造器注入还是setter方法注入？一般对强依赖使用构造器注入，对可选的依赖使用setter方法注入。

## 3.2 @Resource 注解

不同于`@Autowired `是Spring中定义的注解，`@Resource`是 JSR-250 中定义的注解。

@Resource 注解默认按照 bean 的 name 注入 bean。

## 3.3 xml 注入

使用 xml 注入 bean 时，也存在两种注入方式：构造器注入、Setter 方法注入

方式一：构造器注入

```xml
<bean id="teacher" class="cn.edu.learn.entity.Teacher">
    <!-- value 表示将给定的值以字面量的形式注入到构造器之中 -->
    <constructor-arg value="wen"/>
    <!-- ref 表示将一个名为assistant的 bean 注入到构造器中 -->
    <constructor-arg ref="assistant"/>
    <!-- 将集合注入到构造器中 -->
    <constructor-arg>
        <list>
            <ref bean="assistant"/>
            <ref bean="student1"/>
        </list>
    </constructor-arg>
</bean>
```

方式二：setter 方法注入

```xml
<bean id="teacher" class="cn.edu.learn.entity.Teacher">
    <!-- name 指定了要注入的字段，value 属性表示以字面量的形式注入 -->
    <property name="name" value="yu"/>
    <!-- ref属性表示将一个名为student的bean注入 -->
    <property name="student" ref="student"/>
</bean>
```



