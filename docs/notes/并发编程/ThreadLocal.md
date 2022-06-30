# ThreadLocal

ThreadLocal，表示线程局部变量，**每一个访问该变量的线程都会创建一个该变量的副本**，从而使每一个线程都可以独立地访问和修改自己的副本，不同的线程之间互不干扰，从而避免并发访问的线程安全问题。

## 1. ThreadLocal使用

+ **创建一个线程局部变量**

  ```java
  // 通常将ThreadLocal变量定义为私有静态字段
  // 方式一：第一次调用get方法访问该线程局部变量返回null(在这之前没有调用set方法设置当前线程中的副本值)
  private static ThreadLocal<Integer> id = new ThreadLocal<>();
  // 方式二：重写initialValue()方法设置线程局部变量的初始值
  private static ThreadLocal<String> message = new ThreadLocal<String>() {
      @Override
      /**
       * 返回当前线程该线程局部变量的初始值
       * 这个方法将会在线程第一次调用get方法访问该线程局部变量时调用（此前该线程没有调用过set方法，否则该方法不调用）
       * 通常每个线程至多调用一次该方法，当在调用remove方法后再次调用get方法时该方法将再一次被调用
       * 如果没有重写该方法，每个线程中该线程局部变量的初始值为null
       */
      protected String initialValue() {
          return "线程描述";
      }
  };
  ```

+ get()：获取线程局部变量在当前线程中的副本值；

+ set(T value)：设置线程局部变量在当前线程中的副本值；

+ remove()：删除线程局部变量在当前线程中的副本值

```java
public class Test {
    // 通常将ThreadLocal变量定义为私有静态字段
    private static ThreadLocal<String> message = new ThreadLocal<String>() {
        @Override
        /**
         * 返回当前线程该线程局部变量的初始值
         * 这个方法将会在线程第一次调用get方法访问该线程局部变量时调用（此前该线程没有调用过set方法，否则该方法不调用）
         * 通常每个线程至多调用一次该方法，当在调用remove方法后再次调用get方法时该方法将再一次被调用
         * 如果没有重写该方法，每个线程中该线程局部变量的初始值为null
         */
        protected String initialValue() {
            return "线程描述";
        }
    };

    public static void test() {
        System.out.println(Thread.currentThread().getName()
                + "ThreadLocal默认初始值："
                + message.get());
        message.set(Thread.currentThread().getName());
        System.out.println(Thread.currentThread().getName()
                + "ThreadLocal初始值："
                + message.get());
        message.remove();
        System.out.println(Thread.currentThread().getName()
                + "调用remove方法后ThreadLocal值："
                + message.get());
    }

    public static void main(String[] args) {
        Thread thread1 = new Thread(Test::test, "线程一");
        Thread thread2 = new Thread(Test::test, "线程二");
        thread1.start();
        thread2.start();
    }
}
```

## 2. 实现原理

`Thread` 类中定义了一个 `ThreadLocal.ThreadLocalMap` 类型的变量 `threadLocals`；

```java
class Thread implements Runnable {
    ...
    ThreadLocal.ThreadLocalMap threadLocals = null;
    ...
}
```

对于每个线程实例，该变量存储了该线程访问的所有线程局部变量的引用及其在当前线程中的副本值；

`ThreadLocalMap` 类是一个用于存储线程局部变量的map实现，底层使用一个数组（`Entry[] table`）进行存储，每个 `Entry` 使用 ThreadLocal 对象做为key，该对象在当前线程中的值做为value。

**`ThreadLocalMap` 类中的键值对 `Entry` 通过继承弱引用（`WeakReference`）实现。**

```java
// 表示一个键值对
static class Entry extends WeakReference<ThreadLocal<?>> {
    /** The value associated with this ThreadLocal. */
    Object value;

    Entry(ThreadLocal<?> k, Object v) {
        super(k);
        value = v;
    }
}
```

<span style="color:red;font-weight:bold;">为什么使用弱引用？</span>

官方解释：To help deal with very large and long-lived usages, the hash table entries use WeakReferences for keys.

意思就是：

为了解决占用空间大的ThreaLocal实例以及生命周期长的线程，哈希表中键key使用弱引用。

> 假如 key 使用强引用，只要线程一直存活，并且没有通过调用 `threadLocal.remove()` 删除该key对应的键值对，该ThreadLocal对象就不会被回收，一直占用空间；
>
> 假如 key 使用弱引用，当ThreadLocal对象除了Entry对象外没有其他引用的时候，在下一次垃圾回收时，该ThreadLocal对象将被回收；

## 3. 内存泄露

ThreadLocalMap 类的 Entry 继承了 WeakReference ，其中 Key 是弱引用类型的，Value 是强引用类型；

```java
static class Entry extends WeakReference<ThreadLocal<?>> 
{
    Object value;
    Entry(ThreadLocal<?> k, Object v)
    {
        super(k);
        value = v;
    }
}
```

如果 ThreadLocal对象没有强引用时，下一次垃圾回收时ThreadLocal就会被回收；这就会导致 Entry 中 Key 已经被回收，出现一个null Key的情况，外部读取 ThreadLocalMap 中的键值对是无法通过null Key来找到Value的；因此如果当前线程的生命周期很长，那么其内部的ThreadLocalMap对象也一直存活，这些null key就存在一条强引用链：Thread --> ThreadLocalMap-->Entry-->Value，这条强引用链导致Entry不会回收，Value也不会回收，但Entry中的Key却已经被回收的情况，造成内存泄漏。

从表面上看，发生内存泄漏，是因为Key使用了弱引用类型，**但其实是因为整个Entry的key为null后，没有主动清除value导致**；

**如何保证 ThreadLocal 尽量不会内存泄漏**：在ThreadLocal的get()、set()、remove()方法调用的时候会清除掉线程 ThreadLocalMap 中所有 Entry 中 Key 为 null 的 Value，并将整个 Entry 设置为null，利于下次内存回收。但这样也并不能保证ThreadLocal不会发生内存泄漏，例如：

+ 使用 static 修饰的 ThreadLocal ，延长了ThreadLocal的生命周期，可能导致内存泄漏；

+ 分配使用了 ThreadLocal 又不再调用get()、set()、remove()方法，那么就会导致内存泄漏；

### 如何避免内存泄漏

既然Key是弱引用，那么我们要做的事，就是在调用ThreadLocal的get()、set()方法后再调用remove方法，将Entry节点和Map的引用关系移除，这样整个Entry对象在GC Roots分析后就变成不可达了，下次GC的时候就可以被回收。如果使用ThreadLocal的set方法之后，没有显示的调用remove方法，就有可能发生内存泄露，所以养成良好的编程习惯十分重要，使用完ThreadLocal之后，记得调用remove方法；

```java
ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
Try
{
    threadLocal.set(new Session(1, "Misout的博客"));
    // 其它业务逻辑
}
Finally
{
    threadLocal.remove();
}
```

## 4. ThreadLocal 使用

在调用get方法获取线程中某个线程局部变量的副本值之前，必须调用set方法设置该线程中该线程局部变量的副本值，否则会报空指针异常；如果想在调用get方法之前不需要调用set方法就能正常访问的话，必须重写initialValue()方法。

如果没有先set的话，在map中将查找不到线程局部变量对应的存储，则会通过调用setInitialValue方法返回，而在setInitialValue方法中，有一个语句是T value = initialValue()，而默认情况下，initialValue方法返回的是null。

```java
public class Test
{
    // 定义两个线程局部变量
    ThreadLocal<Long> longLocal = new ThreadLocal<>();
    ThreadLocal<String> stringLocal = new ThreadLocal<>();
    
    // 设置线程中线程局部变量的副本值
    public void set()
    {
        longLocal.set(Thread.currentThread().getId());
        stringLocal.set(Thread.currentThread().getName());
    }
    
    // 返回线程中线程局部变量的副本值
    public long getLong()
    {
        return longLocal.get();
    }
    
    public String getString()
    {
        return stringLocal.get();
    }
    
    public static void main(String[] args) throws InterruptedException
    {
        final Test test = new Test();
        
        // 主线程中
        test.set();
        System.out.println(test.getLong());     //1
        System.out.println(test.getString());   //main
        
        Thread t = new Thread(() ->
        {
            test.set();
            System.out.println(test.getLong());     //11
            System.out.println(test.getString());   //Thread-0
        });
        t.start();
    }
}
```

## 5. ThreadLocal 使用场景

最常见的ThreadLocal使用场景为用来解决数据库连接、Session管理等。

ThreadLocal将需要并发访问的资源复制多份，每个线程都拥有自己的资源副本，从而也就没有必要对该变量进行同步了。

```java
// 创建一个线程局部变量connectionHolder 
private static ThreadLocal<Connection> connectionHolder
= new ThreadLocal<>() 
{
    // 重写initialValue()方法
    public Connection initialValue() 
    {
        return DriverManager.getConnection(DB_URL);
    }
}; 
public static Connection getConnection() 
{
    return connectionHolder.get();
}
```

## 6. ThreadLocal与同步区别

ThreadLocal并不能替代同步机制，两者面向的问题领域不同。同步机制是为了同步多个线程对相同资源的并发访问，是多个线程之间进行通信的有效方式，而ThreadLocal是为了隔离多个线程的数据共享，从根本上避免多个线程之间对共享资源(变量)的竞争，也就不需要对多个线程进行同步了。


