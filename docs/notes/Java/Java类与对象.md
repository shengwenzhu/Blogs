# 一、类与对象

Java 是一门面向对象的程序设计语言。

## 1. 定义类

定义类语法格式：

```java
[修饰符] class 类名
{
	成员变量（静态变量、实例变量）
    构造器    
    方法（静态方法、实例方法）
}
```

+ 修饰符

  + 访问控制符：public

    > 定义类时有public和没有public修饰的区别：
    >
    > 有public修饰时：
    >
    > + Java文件名必须和该类名相同
    > + 每个Java文件只能有一个public类
    > + 可以被其他包访问
    >
    > 没有public修饰：
    >
    > + Java文件名可以和类名不同
    > + 只能在同一个包中被访问

  + 抽象类：abstract

  + final类：final

## 2. 成员变量

定义成员变量语法格式：

```java
[修饰符] 类型 成员变量名 [=默认值]
```

+ 修饰符
  + 访问控制符：public、protected、private
  + 静态变量：static
  + final变量：final

成员变量分为两种：静态变量（使用static修饰）、实例变量。

```java
// 访问静态变量
类名.静态变量
// 访问实例变量
实例.实例变量
```

**成员变量无须显式初始化即可使用**：当系统加载类或创建类的实例时，系统将为成员变量分配内存空间，并自动为成员变量指定默认初始值。

## 3. 构造器

构造器用于创建实例以及初始化实例。

定义构造器语法格式：

```java
[修饰符] 构造器名(形参列表) {
    ...;
}
```

+ 修饰符
  + 访问控制符：public、protected、private

当定义类时没有定义构造器时，系统会为该类提供一个默认无参构造器。（如果定义了构造器，系统将不再提供默认构造器）

**构造器重载**：在一个类中定义多个构造器，要求多个构造器的形参列表不同。

调用其他重载构造器：使用this关键字，必须作为构造器执行体的第一条语句。

```java
public class Student
{
    String name;
    int age;
    char sex;
    
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public Student(String name, int age, char sex) {
        this(name, age);
        this.sex = sex;
    }
}
```

## 4. 方法

定义方法语法格式：

```java
[修饰符] 返回值类型 方法名(形参列表) {
    代码块;
}
```

+ 修饰符：
  + 访问控制符：public、protected、private
  + 静态方法：static
  + final方法：final
  + 抽象方法：abstract





### 5.3.1 参数传递机制

Java方法的参数传递方式只有一种：值传递。

值传递：**只对基本数据类型变量而言**，将实际参数的副本传入方法中，不管方法中对这个副本如何操作，实际参数值本身不会受到任何影响（对于引用类型的值传递，因为引用类型的参数存储的是地址，值传递后新的参数也存储的是地址，所以会对实际参数对应的值进行操作）；

```
// 使用 Java 如何交换两个基本数据类型变量的值？
第一种方法：将这两个值封装到一个对象中；
第二种方法：使用数组存储这两个变量值；
```

### 5.3.2 形参数量可变

定义方法时，在最后一个形参的类型后增加三点(…)，则表明该形参可以接受多个参数值，多个参数值被当成数组传入；

个数可变的形参既可以传入多个参数，也可以传入一个数组；

```java
public int sum(int... nums)
{
    int count = 0;
    for (int num : nums)
        count += num;
    return count;
}
```

### 5.3.3 方法重载

形参列表不同；（只与形参列表有关）











## 面向对象的三个基本特征

### 封装

Java 通过 private、protected、public 三个访问控制符实现封装。

### 继承

### 多态











## 5.2 对象的 this 引用

this 用于引用调用该方法的对象，可以省略；

```java
class Dog
{
    public void run()
    {
        System.out.println(“我会跑”);
    }
    public void inf()
    {
        //使用this引用调用 inf 方法的对象；
        this.run();
        // 可以省略this；
        run();
    }
}
```











## 5.6 初始化块

两种初始化块：初始化块、静态初始化块；

初始化块语法格式：

```
静态初始化块：
static{ ... }

初始化块：
{ ... }
```

+ 静态初始化块执行

  在类初始化阶段执行静态初始化块，因此静态初始化块总是比初始化块先执行；

  系统在类初始化阶段执行静态初始化块时，不仅会执行本类的静态初始化块，而且还会一直上溯到java.lang.Object 类，先执行 java.lang.Object  类的静态初始化块(如果有)，然后执行其父类的静态初始化块…… 最后才执行该类的静态初始化块；

+ 初始化块执行

  在创建Java对象时隐式执行，而且在执行构造器之前执行；

  创建一个 Java 对象时，不是仅执行该类的初始化块和构造器，而是会一直上溯到java.lang.Object 类，先执行java.lang.Object 类的初始化块，再执行java.lang.Object 的构造器，依次向下执行其父类的初始化块，再执行其父类的构造器……最后才执行该类的初始化块和构造器，返回该类的对象。

  ```java
  // 父类
  class People
  {
      // 父类初始化块
      static
      {
          System.out.println("父类的静态初始化块");
      }
      
      // 父类初始化块
      {
          System.out.println("父类的初始化块");
      }
      
      public People()
      {
          System.out.println("父类的构造器");
      }
  }
  
  // 子类
  class Student extends People
  {
      // 类初始化块
      static
      {
          System.out.println("子类的静态初始化块");
      }
      
      // 初始化块
      {
          System.out.println("子类的初始化块");
      }
      
      public Student()
      {
          System.out.println("子类的构造器");
      }
  }
  
  
  public class Test
  {
      public static void main(String[] args)
      {
          new Student();
          // 父类的静态初始化块
          // 子类的静态初始化块
          // 父类的初始化块
          // 父类的构造器
          // 子类的初始化块
          // 子类的构造器
      }
  }
  ```

  

## 5.7 封装和访问控制符

+ 封装

  将对象的状态信息隐藏在对象内部，不允许外部程序直接访问对象内部信息，而是通过类提供的方法实现对内部信息的操作和访问；

+ 封装实现

  通过 Java 提供的访问控制符；

+ 访问控制符

  + private

    只能在当前类的内部被访问；

  + 无访问控制符

    可以被相同包下的其他类访问；

  + protected

    既可以被同一个包中的其他类访问，也可以被不同包中的子类访问；

  + public



## 5.8 继承

Java的继承为单继承，每个子类只能有一个直接父类；

```java
修饰符 class 子类 extends 父类
{ ... }
```

子类继承了父类，将获得父类的全部成员变量和方法；

### 5.8.1 重写父类方法

方法的重写要遵循“两同两小一大”规则：

+ “两同”即方法名相同*、*形参列表相同；
+ “两小”指子类方法**返回值类型**应比父类方法返回值类型更小或相等，子类方法声明抛出的异常类应比父类方法声明抛出的异常类更小或相等；
+ "一大"指子类方法的访问权限应比父类方法的访问权限更大或相等。

> 注：如果父类方法是 private 访问权限，则该方法对其子类是隐藏的，其子类无法访问该方法，也就是无法重写该方法。如果子类中定义了一个与父类private方法具有相同的方法名、相同的形参列表、相同的返回值类型的方法，依然不是重写，只是在子类中重新定义了一个新方法。

### 5.8.2 子类调用父类被重写方法

如果调用的是实例方法，使用super 关键字调用，如果是静态方法，使用父类类名调用；

```java
// 父类
class People
{
    void info()
    {
        System.out.println("父类被重写方法");
    }
}

// 子类
class Student extends People
{
    @Override
    void info()
    {
        super.info();
        System.out.println("子类重写方法");
    }
}
```

### 5.8.3 调用父类构造器

**如果子类的构造器没有显式调用父类的构造器，则系统将自动地调用父类默认构造器**（无参构造器）。此时如果父类没有无参构造器，则 Java 编译器将报告错误。

子类构造器可以使用 super 关键字显式调用父类构造器，使用super调用父类构造器的语句必须是子类构造器的第一条语句。

```java
class People
{
    String name;
    int age;
    char sex;
    
    //无参构造器
    public People() {}
    
    //有参构造器
    public People(String name, int age, char sex)
    {
        this.name = name;
        this.age = age;
        this.sex = sex;
        System.out.println("父类三个参数构造器");
    }
}

//子类
class Student extends People
{
    String name;
    int age;
    char sex;
    long id;
    
    public Student(String name, int age, char sex)
    {
        //使用super在子类构造器中调用父类构造器
        super(name, age, sex);
        System.out.println("子类构造器");
    }
}
```

子类构造器总会调用父类构造器一次，子类构造器调用父类构造器分如下几种情况：

+ 子类构造器第一行使用super显式调用父类构造器，系统将根据super调用传入的实参列表调用父类对应的构造器；
+ 子类构造器第一行代码使用this显式调用本类中的构造器，系统将根据this调用里传入的实参列表调用本类中的另一个构造器，执行本类中另一个构造器时即会调用父类构造器；
+ 子类构造器执行体中既没有super调用，也没有this调用，系统将会在执行子类构造器之前，隐式调用父类无参数的构造器。

**当调用子类构造器来初始化子类对象时，父类构造器总会在子类构造器之前执行**，不仅如此，执行父类构造器时，系统会再次上溯执行其父类构造器…… 依此类推，创建任何Java对象，最先执行的总是java.lang.Object类的构造器。

------



## 5.9 多态

一个引用变量可以引用多种类型对象的现象被称为多态；

Java引用变量有两个类型：一个是**编译时类型**，一个是**运行时类型**。

编译时类型由声明该变量时使用的类型决定，运行时类型由实际赋给该变量的对象决定。

**引用变量在编译阶段只能调用其编译时类型所具有的方法**（关键），但运行时执行它运行时类型所具有的方法；

```java
class People
{
    public void info()
    {
        System.out.println("父类方法");
    }
}

//子类
class Student extends People
{
    @Override
    public void info()
    {
        System.out.println("子类重写方法");
    }
    
    public void desc()
    {
        System.out.println("学生");
    }
}

public class Test
{
    public static void main(String[] args)
    {
        People student = new Student();
        student.info(); // 子类重写方法
        // student 变量不能调用 desc方法
    }
}
```



## 5.10 抽象类

使用 abstract 修饰的类；

抽象类不能被实例化，无法使用new关键字来调用抽象类的构造器创建抽象类的实例；

抽象类一般被用于继承；

### 5.10.1 抽象方法

```
访问控制符 abstract 返回类型 方法名(参数列表);
```

包含抽象方法的类必须被定义成抽象类，但抽象类里可以没有抽象方法；

抽象类可以包含构造函数，但抽象类的构造器不能用于创建实例，主要用于被其子类调用；

抽象类可以包含成员变量、方法(普通方法和抽象方法都可以)、构造器、初始化块、内部类(接口、枚举) 5种成分；

继承抽象类的子类必须实现父类的所有抽象方法，否则需要将其定义为抽象类；

abstract 不能修饰类方法；



## 5.11 接口

为什么要引入接口而不是把接口定义为抽象类：为了实现多继承。

### 5.11.1 定义接口

```
[访问控制符] interface 接口名 extends 父接口1, 父接口2, ...
{
	零到多个静态常量定义
	零到多个抽象方法定义
	零到多个私有方法（java9开始）、默认方法（java8开始）或类方法（java8开始）定义
}
```

+ 访问控制符只能是 public 或者省略；

+ 一个接口可以继承多个父接口；

  如果一个类继承的父类和接口包含同样的方法（方法名和参数类型相同），或者一个类实现的多个接口包含同样的默认方法，子类对象调用该方法时如何解决调用父类的方法还是接口的方法？

  类优先：如果父类提供了一个具体方法，接口的默认方法会被忽略；

  接口冲突：子类必须重写该方法；

+ 接口内成员

  + 接口没有构造器；

  + java8之前，接口中只能定义静态常量和抽象方法，java8接口增加了新特性：使用default关键字定义默认方法（可以被子类继承），使用static定义静态方法（不可以被子类继承，只能由接口名调用）；

  + **接口里的成员都是public访问权限**，定义接口成员时，可以省略访问控制符，系统将自动为其添加public修饰符；

  + 静态常量：对于接口里定义的静态常量而言，系统会自动为这些成员变量增加 static 和 final 两个修饰符。

  + 抽象方法

    如果接口内的一个方法不是定义为默认方法、类方法或私有方法，系统将该方法视为抽象方法，将自动为该方法增加 abstract 修饰符；

  + 默认方法（java8）

    使用 default 修饰的方法；

    默认方法自身提供了实现，这就为实现该接口的类提供了方便，实现该接口的类就不需要实现该方法了，除非该实现类有自己独特的行为方式；

    注：在Java API中，很多接口都有相应的伴随类，这个伴随类中实现了相应接口的部分或所有方法，如Collection/AbstractCollection。在JavaSE 8中，这个技术已经过时。

    默认方法的另一个重要用途是“*接口演化*”，比如对于接口Collection，它有许多实现类，如果要向该接口中添加一个新方法，JavaSE 8之前只能将方法定义为抽象方法，如果实现该接口的类没有实现该方法将会无法编译，所以就需要在每个实现类中实现该方法。JavaSE 8之后，可以将接口新定义的方法定义为默认方法，该接口以往的实现类不需要实现该方法也可以编译。

  + 私有方法

    主要作用就是作为工具方法，为接口中的默认方法或类方法提供支持；

    私有方法既可是类方法，也可是实例方；

### 5.11.2 接口实现

一个类可以实现一个或者多个接口；

类必须实现接口里定义的所有抽象方法，否则，该类必须定义成抽象类；

```java
[访问控制符] class 类名 extends 父类 implements 接口1, 接口2, ...
{
	...
}
```



## 5.12 内部类

内部类是外部类的一个成员，所以内部类可以访问外部类的方法或者变量（至于能访问什么方法或变量还要看是什么内部类）；

外部类的上一级程序单元是包，所以外部类有 2 个作用域：同一个包内和任何位置，因此只需 2 种访问权限：包访问权限和公开访问权限，对应省略访问控制符和public访问控制符。内部类的上一级程序单元是外部类，它就具有4 个作用域：同一个类、同一个包、子类和任何位置，因此需要4 种访问控制权限：private、省略访问控制符、protected、public；

### 5.12.1 非静态内部类

非静态内部类相当于外部类的一个实例成员，属于外部类的对象；

不能在非静态内部类里定义静态成员；

在非静态内部类的方法中访问某个变量，首先在该方法内查找该变量，如果存在就使用该变量；如果不存在，则在内部类中查找，如果存在就使用该成员变量；如果不存在，到外部类中查找，如果存在则使用该成员变量，如果依然不存在，系统将出现编译错误；

如果外部类成员变量、内部类成员变量与内部类定义方法的局部变量同名，则可通过使用`this`、`外部类类名.this` 访问；

非静态内部类对象必须寄生于外部类对象内，创建非静态内部类对象必须先创建外部类对象；

```java
// 外部类
class Outer
{
    private String string = "外部类成员变量";
    //非静态内部类
    class Inner
    {
        public void info()
        {
            System.out.println("内部类方法");
        }
    }
    // 非静态内部类对象必须寄生于外部类对象内
    public Inner createInnerObject()
    {
        Inner inner=new Inner();
        return inner;
    }
}
public class Test
{
    public static void main(String[] args)
    {
        //首先创建一个外部类对象
        Outer outer = new Outer();
        //利用外部类对象创建内部类对象
        Outer.Inner inner = outer.createInnerObject();
        inner.info();
    }
}
```

### 5.12.2 静态内部类

静态内部类相当于外部类的一个静态成员，属于外部类本身；

静态内部类只能访问外部类的静态成员；

外部类可以使用静态内部类的类名作为调用者来访问静态内部类的类成员，也可以使用静态内部类对象作为调用者来访问静态内部类的实例成员。

### 5.12.3 外部类以外使用内部类

+ **使用非静态内部类**

  + 在外部类以外的地方使用内部类时，内部类完整的类名是：

    ```
    外部类类名.内部类类名
    ```

  + 创建非静态内部类对象

    非静态内部类的对象必须寄生在外部类的对象里，因此创建非静态内部类对象之前，必须先创建其外部类对象。在外部类以外的地方创建非静态内部类实例的语法如下：

    ```
    外部类对象.new 内部类构造器()
    ```

    ```java
    // 外部类
    class Outer
    {
        //非静态内部类
        class Inner
        {
            public void info()
            {
                System.out.println("内部类方法");
            }
        }
    }
    
    public class Test
    {
        public static void main(String[] args)
        {
            //首先创建一个外部类对象
            Outer outer = new Outer();
            //利用外部类对象创建内部类对象
            Outer.Inner inner = outer.new Inner();
            inner.info();
        }
    }
    ```

  + 创建非静态内部类的子类

    创建子类对象时，子类构造器总会调用父类的构造器，因此在创建非静态内部类的子类时，必须保证子类构造器可以调用非静态内部类的构造器，调用非静态内部类的构造器时，必须存在一个外部类对象。

    非静态内部类对象和子类对象都必须持有指向外部类对象的引用，区别是创建两种对象时传入外部类对象的方式不同：当创建非静态内部类的对象时，必须通过外部类对象来调用new 关键字；当创建子类对象时，必须使用外部类对象作为调用者来调用内部类的构造器。

+ **使用静态内部类**

  在外部类以外的地方创建静态内部类对象语法如下：

  ```
  new 外部类类名.内部类构造器 ()
  ```

  ```java
  // 外部类
  class Outer
  {
      //静态内部类
      static class Inner
      {
          public void info()
          {
              System.out.println("内部类方法");
          }
      }
  }
  
  public class Test
  {
      public static void main(String[] args)
      {
          Outer.Inner inner = new Outer.Inner();
          inner.info();
      }
  }
  ```

### 5.12.4 局部内部类

把一个内部类放在方法里定义，局部内部类仅在该方法里有效。

局部内部类不使用访问控制符和static修饰符；

局部内部类是一个非常"鸡肋" 的语法，在实际开发中很少定义局部内部类。

### 5.12.5 匿名内部类

一般通过实现接口或者抽象类进而创建只需要使用一次的类；

创建匿名内部类时会创建一个该类的实例；

匿名内部类必须继承一个父类，或实现一个接口；

匿名内部类不能定义构造器，由于匿名内部类没有类名，所以无法定义构造器。

创建匿名内部类语法格式：

```
new 实现接口() | 父类构造器(实参列表)
{
    ...
}
```

```java
interface Student
{
    void info();
}

public class Test
{
    public static void main(String[] args)
    {
        Student student = new Student()
        {
            //匿名内部类必须实现抽象父类或者接口里包含的所有抽象方法
            @Override
            public void info()
            {
                System.out.println("我是一个学生");
            }
        };
        student.info();
    }
}
```

```java
abstract class People
{
    String name = "无";
    
    People(String name)
    {
        this.name = name;
    }
    
    abstract void info();
}

public class Test
{
    public static void main(String[] args)
    {
        People people = new People("zhu")
        {
            @Override
            void info()
            {
                System.out.println(name);
            }
        };
        people.info();
    }
}
```





## 5.15 final 修饰符

final 可用于修饰类*、*方法和变量（成员变量和局部变量）；

+ 修饰类

  当用 final 修饰一个类时，表明这个类**不能被继承**；

  final类中的所有方法都会被隐式地指定为final方法；

  > 注：除非某个类以后不会用来继承或者出于安全的考虑，尽量不要将类设计为final类；
  >
  > 注：String类就是一个final类；

+ 修饰方法

  final修饰的方法在子类中**不可被重写**；

  不希望子类重写父类的某个方法时才将方法使用final修饰；

+ 修饰变量

  final修饰的变量只能赋值一次；

  + 成员变量（类变量、实例变量）

    成员变量随类初始化或对象初始化而初始化。当类初始化时，系统会为该类的类变量分配内存，并分配默认值；当创建对象时，系统会为该对象的实例变量分配内存，并分配默认值；

    final修饰的类变量指定初始值的位置：只能在静态初始化块中或声明该类变量时指定初始值；

    final修饰的实例变量指定初始值的位置：只能在非静态初始化块、声明该实例变量或构造器中指定初始值；

    如果没有对final修饰的成员变量进行初始化，则不能访问该final成员变量；

  + 局部变量

  + 修饰基本类型变量和引用类型变量的区别

    对于引用类型变量而言，它保存的仅仅是一个引用，final只保证这个引用类型变量所引用的地址不会改变，即一直引用同一个对象，但这个对象的内容完全可以发生改变；

    

## 5.16 Native 方法（本地方法）

Native 方法：方法的实现由其他语言实现，比如C ；

Native 方法可以返回任何 java 类型，包括非基本类型，而且同样可以进行异常控制。

调用Native 方法与调用其他方法没有任何差别；

### 5.16.1 定义Native 方法

```java
第一步：在类中声明 Native方法
public class Test
{
    native public void test1(int x);
    native public int test2(Object obj);
}
第二步：编译java文件将产生一个class文件
第三步：使用javah编译上一步产生的class文件，会产生一个.h文件
第四步：写一个.cpp文件实现上一步.h文件中的方法
第五步：将上一步的.cpp文件编译成动态链接库文件.dll
第六步：使用 System 或者 Runtime 中的 loadLibrary() 方法加载上一步产生的动态链接库文件；
```

### 5.16.2 为什么使用 Native 方法

有些层次的任务用java实现起来不容易，或者对程序的效率很在意时；

+ 与java环境外交互
  有时 java 应用需要与 java 外面的环境交互。这是**本地方法存在的主要原因**，比如与一些底层系统（操作系统或某些硬件）交换信息时。本地方法为我们提供了一个非常简洁的接口，而且我们无需去了解java应用之外的繁琐的细节。
+ 与操作系统交互
  JVM由一个解释器（解释字节码）和一些连接到本地代码的库组成。经常依赖于一些底层系统的支持。这些底层系统通常是强大的操作系统。通过使用本地方法，我们可以 jre 的与底层系统的交互，如果我们要使用一些java语言本身没有提供封装的操作系统的特性时，我们也需要使用本地方法。

### 5.16.3 Native 方法运行原理

当一个类第一次被使用时，这个类的字节码会被加载到内存，并且只会加载一次。在被加载的字节码的入口维持着一个该类所有方法描述符的list，这些方法描述符包含这样一些信息：方法代码存于何处，它有哪些参数，方法的描述符（public之类）等等。
如果一个方法描述符内有native，这个描述符块将有一个指向该方法的实现的指针。这些实现在一些dll文件内，但是它们会被操作系统加载到java程序的地址空间。当一个带有本地方法的类被加载时，其相关的DLL并未被加载，因此指向方法实现的指针并不会被设置。当本地方法被调用之前，这些dll才会被加载，这是通过调用java.system.loadLibrary()实现的。

### 5.16.4 Native 方法缺点

本地方法意味着和平台有关，因此使用了native的程序可移植性都不太高；









## 接口

### 函数式接口

函数式接口（functional interface）：只包含一个抽象方法的接口。

> 函数式接口可以包含多个默认方法，但只能声明一个抽象方法
>
> JDK8 在`java.util.function`包中定义了许多函数式接口
>
> JDK8 为函数式接口提供了`@FunctionalInterface` 注解，该注解对程序功能没有任何影响， 只用于告诉编译器检查该接口是否为函数式接口，如果不是编译器将报错。





## Lambda 表达式

+ **lambda表达式作用**

  支持将代码块作为方法参数，允许使用更简洁的代码来创建函数式接口的实例。

  > lambda表达式的目标类型必须是明确的函数式接口，即必须将lambda表达式赋值给函数式接口类型的变量，不能将lambda表达式赋值给Object变量

+ **lambda表达式格式**

  ```java
  (形参列表)->{代码块}
  ```

  + 形参列表
    + 形参列表允许省略形参类型；
    + 如果形参列表中只有一个参数，形参列表的圆括号可以省略；
  + 代码块
    + 如果代码块只包含一条语句，可以省略代码块的花括号；
    + 代码块只有一条return语句，可以省略return关键字；

  ```java
  /**
   * 匿名内部类方式创建一个Comparator实例
   */
  String[] strs = {"stephen", "klay", "draymond", "andre"};
  Arrays.sort(strs, new Comparator<String>() {
      public int compare(String str1, String str2) {
          return str1.length() - str2.length();
      }
  });
  
  /**
   * 使用Lambda表达式创建一个Comparator实例
   */
  String[] strs = {"stephen", "klay", "draymond", "andre"};
  Arrays.sort(strs, (str1, str2) -> str1.length() - str2.length());
  ```

+ **方法引用**

  有时候，可能已经存在现成的方法完成了Lambda表达式代码块中的任务，此时可以使用方法引用代替Lambda表达式，从而使Lambda表达式更加简洁。

  ```java
  /**
   * 方法引用示例
   */
  public void methodReferenceTest() {
      String[] strs = {"stephen", "klay", "draymond", "andre"};
      // 按照字符串的字典序进行排序，String类中已经定义了比较方法，此时可以直接使用方法引用
      Arrays.sort(strs, String::compareToIgnoreCase);
      // 上述代码等价于如下代码
      // Arrays.sort(strs, (x,y)->x.compareToIgnoreCase(y));
      System.out.println(Arrays.toString(strs));
  }
  ```

  方法引用存在三种情况：

  + `object::instanceMethod`

    相当于Lambda表达式：`(a,b,...)->object.instanceMethod(a,b,...)`

  + `类名::staticMethod`

    相当于Lambda表达式：`(a,b,...)->类名.staticMethod(a,b,...)`

  + `类名::instanceMethod`

    相当于Lambda表达式：`(a,b,...)->a.instanceMethod(b,...)`

    **注意：此时函数式接口中被实现方法的第一个参数作为调用者**

  > 在方法引用中可以使用this参数或者super参数：
  >
  > `this::instanceMethod`：调用当前对象的其他实例方法
  >
  > `super::instanceMethod`：调用当前对象父类中的方法

+ **构造器引用**

  与方法引用类似，引用格式：`类名::new`，相当于Lambda表达式：`(a,b,...)->new 类名(a,b,...)`。

  ```java
  /**
   * 构造器引用示例
   */
  public void constructorReferenceTest() {
      String[] strs = {"stephen", "klay", "draymond", "andre"};
      Stream<Player> stream = Arrays.stream(strs).map(Player::new);
      Player[] players = stream.toArray(Player[]::new);
      System.out.println(Arrays.toString(players));
  }
  ```

+ **lambda表达式访问外部变量**

  在lambda表达式中可以访问外部方法中定义的局部变量或类的成员变量（包括实例变量和静态变量），但是**要求访问的变量是最终变量**（effectively final），即这个变量初始化之后值不会再改变。

  













# 常用接口

## Comparator/Comparable



# 常用类

## 包装类

Java为 8 种基本数据类型分别定义了相应的引用类型，称之为基本数据类型的包装类；

| 基本数据类型    | 包装类    |
| --------------- | --------- |
| byte            | Byte      |
| short           | Short     |
| int             | Integer   |
| long            | Long      |
| char（2字节）   | Character |
| float（4字节）  | Float     |
| double（8字节） | Double    |
| boolean         | Boolean   |

+ **自动装箱和自动拆箱**

  基本数据类型和包装类之间可以自动地相互转换；

  自动装箱：自动将基本数据类型转换为包装器类型；

  自动拆箱：自动将包装器类型转换为基本数据类型；

  ```java
  // 自动装箱：经编译器编译后会转换为：Integer.valueOf(int i)
  Integer temp = 10;
  
  // 自动拆箱:经编译器编译后会转换为：integer.intValue()
  int num = temp;
  ```

+ **常量池技术**

  Integer：当类被加载时，会自动创建值为 -128~127 的对象，然后保存在 cache 数组中；当通过自动装箱或者调用 valueOf(int) 方法创建Integer 对象时，当数值处于 -128~127 范围内，会从 cache 数组中直接获取 Integer 对象，不在  -128~127 范围内会使用 new 操作符创建对象；

  ```java
  Integer integer = new Integer(5);
  Integer integer2 = new Integer(5);
  System.out.println(integer==integer2);      // false，使用new操作符创建出来的对象永远不会相等
  
  Integer integer3 = 5;
  Integer integer4 = 5;
  System.out.println(integer3==integer4);     // true
  ```

  > Byte、Short、Long：创建数值在 [-128, 127] 范围内的对象；
  >
  > Character：创建数值在 [0,127] 范围内的缓存数据；
  >
  > Boolean：创建 true 和 false 两个对象；

+ 包装类对象是不可变的，一旦初始化，对象的值就不能更改；

  ```java
  // Integer对象底层使用变量 vaLue 存储对象的数值
  private final int value;
  ```









