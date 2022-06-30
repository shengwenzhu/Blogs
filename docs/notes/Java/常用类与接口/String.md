# String(StringBuffer, StringBuilder)

Java 提供了 String 、StringBuffer和 StringBuilder 三个类表示字符串；

## 1. 三个类的区别

+ String
  不可变字符串，一个String对象被创建以后，该对象代表的字符串中的字符不能修改。
  区别String变量和String对象，变量可以引用不同的String对象，String对象所代表的字符串不能修改；

+ StringBuffer
  可变字符串，当一个StringBuffer对象被创建以后，通过StringBuffer类定义的append()、insert()、reverse()、setCharAt()、setLength()等方法可以改变这个字符串对象的字符序列；
  可以通过 `StringBuffer.toString()` 方法将一个StringBuffer对象转换为一个String对象；
+ StringBuilder
  可变字符串类，StringBuilder和StringBuffer基本相似，两个类的构造器和方法也基本相同。不同的是，StringBuffer是线程安全的，而StringBuilder是线程不安全的，所以性能略高。因此在通常情况下，如果需要创建一个内容可变的字符串对象， 应该优先考虑使用StringBuilder类。

## 2. 字符串底层存储方式

字符串对象底层使用char数组存储；

```java
// 底层代码实现
private final char value[];
```

## 3. 字符串创建方式

两种方式：

+ 使用new运算符
  String str=new String(“HelloWorld”);

+ 使用字符串常量或者常量表达式

  String str=”Hello”;

  String str=”Hel” + “lo”;

## 4. 字符串常量池

大量的创建字符串，极大程度地影响程序的性能；

JVM为了提高性能和减少内存开销，在**使用字符串常量或者常量表达式创建字符串对象的时候进行了一些优化**，为字符串开辟了一个字符串常量池；

在使用字符串常量或者常量表达式创建字符串对象时，JVM首先检查该字符串是否存在于字符串常量池中，如果存在，返回池中的实例引用，如果不存在，则在常量池中创建该字符串对象并返回其引用；

```java
String str = "hello";
String str2 = "hello";
System.out.println(str == str2);  //true，这两个String变量引用字符串常量池中的同一个对象
```

使用`new String(String original) `创建字符串对象时，JVM首先在字符串常量池中查找有没有这个字符串对象，如果有，则只在堆中创建该字符串对象，返回堆中字符串的引用；如果没有，则首先在字符串常量池中创建该字符串对象，然后再在堆中创建该字符串对象，然后返回堆中字符串的引用；（因为该构造函数参数也是字符串常量，所以会在字符串常量池中创建一个对象，使用其他String构造函数不会在字符串常量池中创建对象）（一般不推荐使用该方法创建字符串，String str = new String("hello")不如直接使用String str = "hello"）

```java
// 在字符串常量池创建"hello"对象
String str = "hello";
// 在堆中创建“hello”对象，因为常量池已经有该字符串对象，不需要在常量池中创建
String str2 = new String("hello");
System.out.println(str == str2);  //false
```

`String.intern()`方法返回字符串对象的规范化表示形式，也就是**引用指向从堆改到常量池**；调用该方法，如果常量池中已经包含了与该字符串对象相同的字符串，则返回池中的字符串引用；如果在常量池中没有，在JDK 6及其之前的版本中，JVM会在字符串常量池中创建该字符串对象，然后返会池中对象的引用；JDK 7以后常量池中不需要再创建该字符串对象，只需要存储堆中字符串的引用，返回堆中字符串的引用；

```java
// 在字符串常量池中创建"hello"对象
String str = "hello";
// 在堆中创建“hello”对象
String str2 = new String("hello");
// 调用intern()方法首先查找字符串常量池，字符串常量池中含有该字符串，返回池中对象的引用
String str3 = str2.intern();
System.out.println(str == str3);  //true

// 在堆中创建“helloworld”对象，引用堆中的对象
String str = new String("helloworld");
// 调用intern()方法首先查找字符串常量池，字符串常量池中没有该字符串
// JDK8 在字符串常量池中存储堆中字符串的引用，返回堆中字符串的引用
String str2 = str.intern();
String str3 = "helloworld";
System.out.println(str == str2);  // true
System.out.println(str == str3);  // true
```

## 5. 常用方法

### 1）类型转换

字符串类型转换为基本类型有两种方式：

+ 利用包装类提供的 parseXxx(String s) 静态方法(除Char之外的所有包装类都提供了该方法)：

  ```
  String str1="19970318";
  int date=Integer.parseInt(str1);
  ```

+ 利用包装类提供的 valueOf(String s)静态方法：

  ```java
  String str1="19970318";
  int date=Integer.valueOf(str1);
  ```

将基本类型转换为字符串：

+ 使用String类的valueOf()方法；

  ```java
  String s = String.valueOf(123);
  ```

+ 使用 Integer.toString() 方法；

  ```java
  String s = Integer.toString(123);
  ```


### 2）码点和代码单元

Java字符串由`char`类型数组存储，char数据类型是一个采用UTF-16编码表示Unicode码点的代码单元。大多数的常用Unicode字符使用一个代码单元就可以表示，而辅助字符需要一对代码单元表示。

如果文本中包含Unicode辅助字符时需要考虑如下几个方法的区别：

|                                                  | 描述                                                         |
| ------------------------------------------------ | ------------------------------------------------------------ |
| int length()                                     | 返回当前字符串长度，该长度为字符串中Unicode代码单位的数量（注意辅助字符需要两个代码单元） |
| int codePointCount(int beginIndex, int endIndex) | 返回此字符串的指定范围内的Unicode码点的数量。文本范围的长度(以char为单位)是endIndex-beginIndex。 |
| char charAt(int index)                           | 获取指定索引处的char值（即代码单元），索引的范围是从0到length()-1。  注：除非对底层的代码单元感兴趣， 否则不需要调用这个方法； |
| int codePointAt(int index)                       | 返回从给定索引开始的码点。索引范围从0到length()-1。          |
| 如果需要获取第i个码点                            | int index=str.offsetByCodePoints(0, i);  int cp=str.codePointAt(index); |

为什么会对代码单元如此大惊小怪？请考虑下列字符串：

String str="𝕆 is the set of octonions";

使用UTF-16编码表示字符𝕆(U+1D546)需要两个代码单元。调用

char ch = str.charAt(l);

返回的不是一个空格，而是𝕆的第二个代码单元。为了避免这个问题，不要使用char 类型。这太底层了。如果想要遍历一个字符串，并且依次査看每一个码点，可以使用下列语句：

int cp = str.codePointAt(i);

if (Character.isSupplementaryCodePoint(cp)) i += 2;

else i++;

```java
String str="𝕆 is the set of octonions";
System.out.println(str.length());	// 输出26
System.out.println(str.codePointCount(0, str.length()));	// 输出25
//遍历字符串
//遇到有辅助字符出现的字符串，不能使用charAt()获取每个字符，而应该采用codePointAt()方法
for(int i=0; i<str.length(); i++)
{
    int cp = str.codePointAt(i);
    if(cp<0xFFFF)
        System.out.print((char)cp+" ");
    else
        System.out.print(cp+" ");
    if (Character.isSupplementaryCodePoint(cp))
        i++;
}
//输出：120134   i s   t h e   s e t   o f   o c t o n i o n s
//120134是𝕆的Unicode编号

//一种更简洁的遍历字符串，使用codePoints()方法返回码点值的IntStream流
//再使用IntStream流的toArray()将流转换为字符串
int[] codePoints=str.codePoints().toArray();
for(int i=0; i<codePoints.length; i++)
{
    if(codePoints[i]<0xFFFF)
        System.out.print((char)codePoints[i]+" ");
    else
        System.out.print(codePoints[i]+" ");
}
```

## 6. StringBuilder类方法

StringBuilder提供了一系列插入、追加、改变该字符串里包含的字符序列的方法。而StringBuffer与其用法完全相同；

StringBuilder 、StringBuffer 有两个属性：length 和capacity ，其中length 属性表示其包含的字符序列的长度。与String对象的length 不同的是， StringBuilder 、StringBuffer 的length 是可以改变的，可以通过length()、setLength(int len)方法来访问和修改其字符序列的长度。capacity属性表示StringBuilder的容量， capacity 通常比length 大，程序通常无须关心capacity 属性。

|                                                   | 描述                                |
| ------------------------------------------------- | ----------------------------------- |
| append(String str)                                | 在StringBuilder对象的最后添加字符串 |
| insert(int index, String str)                     | 在索引index处插入字符串             |
| replace(int beginIndex, int endIndex, String str) | 替换开始到结尾处的字符串            |
| delete(int beginIndex, int endIndex)              | 删除开始到结尾处的字符串            |
| reverse()                                         | 反转字符串                          |





