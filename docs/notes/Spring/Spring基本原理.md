# 特别备注

+ 学习方式

  + 官方文档：https://docs.spring.io/spring-framework/docs/current/reference/html/index.html

  + 《Spring实战》第四版

    > 电子版地址：https://potoyang.gitbook.io/spring-in-action-v4/



# 一、Spring概述

Spring框架的提出是为了解决企业级应用开发的复杂性，简化Java开发。

Spring的优势：

+ 最小侵入性编程

  > 侵入式编程：使用框架时，框架会强制用户应用继承它们的类或实现它们的接口。
  >
  > 优点：可以使用户跟框架更好的结合，更容易更充分的利用框架提供的功能；
  >
  > 缺点：将应用与框架绑死，应用不能脱离框架使用，不利于代码的复用。
  >
  > 非侵入式编程：不需要用户代码引入框架代码的信息，从类的编写者角度来看，察觉不到框架的存在。

+ 通过依赖注入和面向接口实现具有依赖关系的对象之间的解耦

  > 通过依赖注入，对象的依赖关系可以由系统中负责协调各对象的第三方组件在创建对象的时候进行设定，对象无需自行创建或管理它们的依赖关系。
  >
  > 如果一个对象只通过接口来表明依赖关系，那么这种依赖就能够在对象本身毫不知情的情况下，用不同的具体实现进行替换。

+ 基于切面和惯例进行声明式编程；

  > 系统由许多不同的组件组成，每一个组件负责一个特定功能。这些组件除了实现自身核心的功能之外，还经常承担着额外的职责，如日志记录、事务管理、安全控制等。如果在各个组件中都去实现这些额外的职责，代码将变得很混乱，以及同样的代码出现在多个组件中。
  >
  > 面向切面编程可以将遍布应用各处的辅助功能分离出来形成可重用的组件，通过预编译方式或者运行期动态代理的方式，将其运用到业务组件中。
  >
  > 优势：每个业务组件只需要关注自身的业务逻辑。

+ 通过切面和模板减少样板式代码。



# 二、Spring核心特性

Spring的实现依赖两个核心特性：

+ 控制反转（Inversion of Control，IoC）

  > 控制反转也可称为依赖注入（Dependency Injection，DI）

+ 面向切面编程（Aspect-Oriented Programming，AOP）

# 三、IoC

在面向对象的程序开发中，要实现一个功能，一般需要多个类进行协作才能完成。

按照传统的做法，如果一个对象 A 依赖于另一个对象 B，那么对象 A 就需要在其初始化时或者运行某个方法时主动创建对象 B。每个对象负责管理与自己相互协作的对象（即它所依赖的对象）的引用，这将会导致对象之间的高度耦合。

<span style="color:red;font-weight:bold;">IoC 的核心思想：</span>通过引入第三方组件（即IoC容器）负责对象的创建、管理对象的整个生命周期、通过依赖注入的方式建立对象之间的依赖关系。

## 1. IoC 容器

**IoC 容器负责创建对象、建立对象之间的依赖关系、管理对象的整个生命周期**。

Spring 自带了多个容器实现，可以将其分为两类：

+ BeanFactory

  > org.springframework.beans.factory.BeanFactory

  最简单的容器接口，提供了基本的依赖注入支持，Spring 中所有的容器都需要实现该接口。

+ ApplicationContext（应用上下文）

  > org.springframework.context.ApplicationContext

  基于 BeanFactory 构建，提供应用框架级别的服务。

Spring 提供了多种类型的应用上下文：

+ AnnotationConfigApplicationContext ：从Java配置类中加载已被定义的 bean；
+ ClassPathXmlApplicationContext：从类路径下的 XML 配置文件中加载已被定义的 bean；
+ FileSystemXmlApplicationContext：从文件系统下的 XML 配置文件中加载已被定义的 bean，需要提供XML 文件的完整路径。

```java
/**
 * StudentConfig 配置类
 */
@Configuration
public class StudentConfig {

    /**
     * 声明student bean
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

## 2. 依赖注入

**Spring通过依赖注入的方式创建对象之间的依赖关系**。

依赖注入的过程分两步进行：1）声明bean，即告诉Spring要创建哪些bean；2）装配bean，将具有依赖关系的对象装配在一起。

Spring 提供了三种装配 bean 的方式：

+ 在 xml 配置文件中显式配置
+ 在 Java 配置类中显式配置
+ 自动化装配（优先推荐使用）

### 2.1 自动化装配 bean

自动化装配bean的过程分为三步进行：

+ 第一步：声明 bean

  使用 `@Component` 注解修饰类，用于告诉 Spring 为这个类创建 bean；

  >  **`@Component` 注解的扩展**
  >
  > 当一个注解被`@Component` 注解修饰，然后使用该注解修饰其他类时，Spring也会为这些类创建bean；所以，这些被 `@Component` 注解修饰的注解可以看作是 `@Component` 注解的扩展。
  >
  > 在spring中，存在以下几个 `@Component` 注解的扩展：
  >
  > + `@Controller`：标记控制层组件
  > + `@Service`：标记业务层组件
  > + `@Repository`：标记数据访问组件，即DAO组件

  ```java
  /*
   * Spring应用上下文中的所有bean都会有一个唯一的name；
   * 如果没有显式指定bean的name，默认情况下Spring会将类名的第一个字母变为小写后作为bean的name；
   * 也可以显式指定bean的name，如@Component("newStudent")
   */
  @Component
  public class Student {
      
      private String name;
      private int age;
      private char sex;
      ......
  }
  ```

+ 第二步：启动组件扫描

  声明bean后，需要显式配置Spring扫描带有 `@Component` 注解的类，并为其创建bean。

  使用 `@ComponentScan` 注解启用组件扫描， `@ComponentScan` 注解默认扫描其修饰的类所在包以及这个包下的所有子包。

  可以通过指定 `@ComponentScan` 的 `value` 属性（或 `basePackages` 属性，这两个属性等价）指定需要扫描的包。

  ```java
  // 设置扫描单个包,如果只需要设置value属性，可以省略“value=”部分
  @ComponentScan("cn.edu.learn")
  // 设置扫描多个包
  @ComponentScan(value={"cn.edu.learn.entity", "cn.edu.learn.config"})
  // 设置扫描的类或接口，进而扫描该类或接口所在的包
  @ComponentScan(basePackageClasses={Student.class, StudentConfig.class})
  ```

+ 第三步：使用 `@Autowired` 或 `@Resource` 注解注入bean

   `@Autowired` 注解可以用于修饰字段、构造器、方法等，相应的，可以将使用 `@Autowired` 注解注入bean的方法分为三种：Field 注入、构造器注入、Setter方法注入。

  + Field 注入（虽然这种方式很简单，但是Spring官方不推荐这种方式）

    ```java
    public class Teacher {
        
        @Autowired
        private Student student;
        ......
    }
    ```

  + 构造器注入

    ```java
    public class Teacher {
    
        private Student student;
        
        @Autowired
        public Teacher(Student student) {
            this.student = student;
        }
    }
    ```

  + Setter方法注入

    ```java
    public class Teacher {
    
        private Student student;
    
        @Autowired
        public void setStudent(Student student) {
            this.student = student;
        }
    }
    ```

三种注入方法对比：

+ 在 Spring 3.x 刚推出的时候，Spring 官方在对比构造器注入和 Setter 注入时，推荐使用 Setter 方法注入

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

### 2.2 @Autowired与@Resource对比

`@Autowired`注解与`@Resource`都可用于注入bean。

`@Autowired`是Spring中定义的注解，`@Resource`是JSR-250中提供的注解，即Java提供的注解。

`@Autowired` 注解按byType的注入策略注入对象，

`@Resource` 注解默认按byName的注入策略注入对象。













@Autowired注解按byType的注入策略注入对象，默认情况下要求注入的对象必须存在，如果允许注入的对象不存在，需要设置@Autowired注解的required属性为false，即`@Autowired(required=false)` 。

> 如果想要使用byName的注入策略，可以将@Autowired注解与@Qualifier注解结合使用。
>
> 如果Spring上下文中存在多个类型相同的bean，使用@Qualifier注解可以指定bean的名字。

@Autowired注解可以用于构造器、方法、参数、字段等。

如果找不到匹配的bean或者找到多个类型相同的bean，Spring都会抛出异常。

```java
// 用于构造器时，表示将一个CompactDisc类型的bean注入到CDPlayer之中
@Autowired
public CDPlayer(CompactDisc cd) {
    this.cd = cd;
}

// 用于方法
@Autowired
public void setCompactDisc(CompactDisc cd){
  this.cd = cd;
}
```

> 
>
> 
>
> 使用@Resource注解时，可以指定两个属性：name和type，Spring会将指定的name属性值解析为待装配对象的名字，将type属性值解析为bean的类型。
>
> @Resource装配过程：
>
> + 如果同时指定了name和type，则从Spring上下文中找到唯一匹配的bean进行装配，找不到则抛出异常；
> + 如果只指定了name，使用byName的注入策略，从上下文中查找名称匹配的bean进行装配，找不到则抛出异常；
> + 如果指定了type，则使用byType的注入策略，从上下文中找到类型匹配的唯一bean进行装配，找不到或者找到多个，都会抛出异常；
> + 如果name和type属性都没有指定，则使用byName的注入策略（此时用该注解修饰的字段名查找bean）。如果找不到名称匹配的bean，使用byType的注入策略。

## 2. 通过Java代码装配bean

> 有时候无法使用自动化装配bean：如将第三方库中的组件装配到Spring应用中，此时可以使用Java代码或xml配置文件装配bean；

**第一步**：创建配置类（使用@Configuration 注解修饰类）

**第二步**：在配置类中声明bean

编写一个方法，这个方法创建并返回所需类型的实例，然后给这个方法添加@Bean注解。

@Bean注解用于告诉Spring将该方法返回的对象注册为Spring应用上下文中的bean。

默认情况下，创建的bean的name与方法名相同，可以通过设置@Bean注解的name属性显式指定bean的name，如`@Bean(name="...")`

```java
@Configuration
public class StudentConfig {
    @Bean
    public Student createStudent() {
        Student student = new Student();
        student.setName("zhu");
        student.setSex('男');
        return student;
    }
}
```

**第三步**：装配bean

```java
// 装配方式一：使用@Resource或@Autowired注解
    @Resource(name = "createStudent")
    Student student;

// 装配方式二：调用创建bean的方法（不推荐）
// 特别备注：虽然如下代码中的Student对象看似是通过调用createStudent()方法得到的，但并不是每次调用createStudent()就创建一个新的对象，createStudent()方法由@Bean注解修饰，Spring会拦截所有对该方法的调用，并确保直接返回该方法所创建的bean，所以每次调用该方法都返回同一个对象
	String name = createStudent().getName();

// 装配方式三：构造器或者setter方法注入
// 当Spring调用构造器创建Teacher类的bean时，会自动装配一个Student类的bean
    @Bean
    public Teacher(Student student) {
        return new Teacher(student)
    }
```

## 3. 通过 XML 装配 bean

最简单的Spring XML 配置如下所示：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- 声明一个最简单的bean：id属性指定bean的名字，class属性指定了bean的类（需要使用全限定类名）-->
    <!-- 当使用如下方式声明bean时，Spring会调用该类的无参构造器创建bean -->
    <bean id="student" class="cn.edu.learn.entity.Student"/>

</beans>
```

使用xml配置文件声明bean后，Spring并不会主动加载该配置文件，需要通过@ImportResource注解导入配置文件，进而让配置文件里面的内容生效。

```java
@SpringBootApplication
// 导入xml配置文件
@ImportResource(locations = {"classpath:config/*.xml"})
public class LearnApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearnApplication.class, args);
    }
}
```

### 3.1 使用构造器注入装配bean

```java
class Teacher {
    private String name;
    private Student assistant;
    private List<Student> students;

    public Teacher(String name, Student assistant, List<Student> students) {
        this.name = name;
        this.assistant = assistant;
        this.students = students;
    }
}
```

```xml
<bean id="teacher" class="cn.edu.learn.entity.Teacher">
    <!-- constructor-arg元素的value属性表明将给定的值以字面量的形式注入到构造器之中 -->
    <constructor-arg value="wen"/>
    <!-- constructor-arg元素的ref属性用于告诉Spring将一个名为assistant的bean注入到构造器中 -->
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

### 3.2 使用Setter方法注入

```java
public class Teacher {
    private String name;
    private Student assistant;
    private List<Student> students;
    
    /** setter and getter 方法 */
    ......
}
```

```xml
<bean id="teacher" class="cn.edu.learn.entity.Teacher">
    <!-- property元素的name属性指定了要注入的类属性，此处即通过Teacher类的name属性的setter方法注入，value属性表示以字面量的形式注入 -->
    <property name="name" value="yu"/>
    <!-- ref属性表示将一个名为student的bean注入 -->
    <property name="student" ref="student"/>
</bean>
```

## 4. 字段注入、构造器注入、Setter方法注入对比







# 三、bean的生命周期

bean 在 Spring 容器中从创建到销毁要经历若干阶段，其中每个阶段都可以进行个性化定制。

![bean生命周期](image/bean生命周期.jpg)

+ Spring 对 bean 进行实例化；
+ Spring 将值和 bean 的引用注入到 bean 对应的属性中；
+ 如果 bean 实现了 BeanNameAware 接口，Spring 将 bean 的 ID 传递给 setBeanName()方法；
+ 如果 bean 实现了 BeanFactoryAware 接口，Spring 将调用  setBeanFactory() 方法，将 BeanFactory 容器实例传入；
+ 如果 bean 实现了 ApplicationContextAware 接口，Spring 将调用 setApplicationContext() 方法，将 bean 所在的应用上下文的引用传入进来；
+ 如果 bean 实现了 BeanPostProcessor 接口，Spring 将调用它们的 postProcessBefore-Initialization() 方法；
+ 如果 bean 实现了 InitializingBean 接口，Spring 将调用它们的 afterPropertiesSet() 方法。类似地，如果 bean 使用 initmethod 声明了初始化方法，该方法也会被调用；
+ 如果 bean 实现了 BeanPostProcessor 接口，Spring 将调用它们的 postProcessAfter-Initialization() 方法；
+ 此时，bean 已经准备就绪，可以被应用程序使用了，它们将一直驻留在应用上下文中，直到该应用上下文被销毁；
+ 如果 bean 实现了 DisposableBean 接口，Spring 将调用它的 destroy() 接口方法。





















































