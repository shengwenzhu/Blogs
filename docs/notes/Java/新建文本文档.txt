# 类与接口



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









