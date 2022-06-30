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





















































