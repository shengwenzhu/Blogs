# volatile 关键字

用于修饰变量，在并发编程中可以保证可见性、有序性，但不保证原子性；

**1）volatile 特性**

+ 可见性

  当一个线程修改了共享变量，其他线程能够立即看到修改后的值；

+ 有序性

  有序性即程序执行的顺序按照代码的先后顺序执行；

  volatile 禁止指令重排序；

+ 原子性

  volatile 不保证原子性；


**2）实现原理**

+ 如何保证可见性

  对

  对 volatile 变量进行写操作时，经过汇编后会在写指令前加 lock前缀；

  Lock前缀的指令在多核处理器下会引发两件事情：

  + 将当前处理器缓存行中的数据写回到系统内存（不仅仅只将 volatile 变量写回到内存中，还会将线程中volatile 变量之前改变的所有的变量也刷新到内存中）；

  + 根据缓存一致性协议其他CPU里缓存了该内存地址的数据无效；

    > 缓存一致性协议：当前最主流的缓存一致性协议是 MESI；
    >
    > 在MESI协议中，每个缓存行有4个状态：
    >
    > ![img](https://images2015.cnblogs.com/blog/801753/201706/801753-20170620131125273-1538564632.png)
    >
    > 嗅探协议：所有CPU与内存数据的传输都发生在一条共享的总线上，CPU缓存不仅仅在做内存传输的时候才与总线打交道，而是不停在嗅探总线上发生的数据交换，跟踪其他缓存在做什么。所以当一个缓存代表它所属的处理器去读写内存时，其它处理器都会得到通知，它们以此来使自己的缓存保持同步。只要某个处理器一写内存，其它处理器马上知道这块内存在它们的缓存段中已失效。

+ 禁止重排序

  编译器在生成字节码时，会在 volatile 读写指令前后插入内存屏障来禁止指令重排序；

3 volatile 使用

使用 volatile 实现同步必须具备以下2个条件：

+ 对变量的写操作不依赖于当前值；
+ 该变量没有包含在具有其他变量的不变式中；

适用情况一：状态标记量

```java
/**
 * 两个线程交替输出1, 2, 3, ...
 */
public class Test
{
    volatile boolean flag = false;
    private int count = 1;
    
    public void print1()
    {
        while (true && count<30)
        {
            // 当条件不满足时，线程会空转，所以一般不使用该方法实现交替输出
            while (flag)
            {
                System.out.println(count);
                count++;
                flag = false;
            }
        }
    }
    
    public void print2()
    {
        while (true && count<30)
        {
            while (!flag)
            {
                System.out.println(count);
                count++;
                flag = true;
            }
        }
    }
    
    
    public static void main(String[] args)
    {
        Test t = new Test();
        Thread thread = new Thread(t::print1);
        Thread thread1 = new Thread(t::print2);
        thread.start();
        thread1.start();
    }
}
```

适用情况二：单例模式双重检查

```java
public class Singleton
{
    private volatile static Singleton instance = null;

    private Singleton() { }

    public static Singleton getInstance()
    {
        if(instance==null)
        {
            synchronized (Singleton.class)
            {
                if(instance==null)
                    instance = new Singleton();
            }
        }
        return instance;
    }
}
```

