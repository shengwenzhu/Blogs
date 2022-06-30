# synchronized（重量级锁）

## 1 synchronized 使用

synchronized 有三种使用方式：

+ 修饰实例方法，锁是调用该方法的实例对象；
+ 修饰静态方法，锁是当前类的Class对象；
+ 修饰代码块，锁是Synchonized括号里指定的对象；

## 2 synchronized底层实现原理

每个对象都有一个monitor（监视器锁）与之关联，JVM基于进入和退出 `Monitor` 对象实现方法同步和代码块同步；

> 在 JVM 中，monitor由 ObjectMonitor实现；
>
> ObjectMonitor中有两个队列：_WaitSet (等待队列)和 _EntryList (阻塞队列)；
>
> _WaitSet 存储处于 wait状态的线程；
>
> _EntryList 存储因等待获取锁处于阻塞状态的线程；

对于同步代码块，编译后会在同步代码块的开始位置插入 monitorenter 指令，在结束位置插入 monitorexit 指令；

线程执行 monitorenter 指令时，将会尝试获取 monitor 的所有权，过程如下：

+ monitor 使用一个计数器 count记录线程获取 monitor的次数，如果count为0，线程获得 monitor 的所有权，然后将count+1；
+ 如果当前线程已经占有该monitor，只是重新进入，则将 count+1；
+ 如果其他线程已经占用了monitor，则当前线程进入阻塞队列；

线程执行 monitorexit 指令时，monitor的计数器 count-1，如果减 1后计数器 count为0，当前线程退出monitor，不再是这个monitor的所有者，其他被这个monitor 阻塞的线程可以尝试去获取这个 monitor 的所有权；

对于同步方法，不是通过指令 monitorenter 和 monitorexit 实现，而是通过常量池中该方法的 ACC_SYNCHRONIZED 标志来隐式实现，当调用同步方法时，调用指令将会检查方法的 ACC_SYNCHRONIZED 标志是否被设置，如果设置了，执行线程将先获取monitor的所有权，获取成功之后才能执行方法体，方法执行完后再释放monitor；

### 对象头

在 JVM 中，对象在内存中分为三部分：对象头、实例数据、对齐填充；

对象头是实现synchronized锁对象的基础，**synchronized使用的锁对象是存储在Java对象头中**；

对于数组类型对象，JVM 使用 3个字宽（ Word）存储对象头；对于非数组类型对象，使用 2字宽存储对象头；在 32位虚拟机中， 1字宽等于 4字节，即 32bit；

| 长度     | 内容                   | 描述                          |
| -------- | ---------------------- | ----------------------------- |
| 32/64bit | Mark Word              | 存储对象的hashcode 或锁信息等 |
| 32/64bit | Class Metadata Address | 存储到对象类型数据的指针      |
| 32/64bit | Array Length           | 数组的长度                    |

32位 JVM 中对象头的 Mark Word 的默认存储结构如下所示：

| 25bit        | 4bit         | 1bit（是否是偏向锁） | 2bit（锁标志位） | 锁状态   |
| ------------ | ------------ | -------------------- | ---------------- | -------- |
| 对象的哈希值 | 对象分代年龄 | 0                    | 01               | 无锁状态 |

在运行期间，**Mark Word里存储的数据会随着锁标志位的变化而变化**；Mark Word 可能变化为以下4种状态：

![对象头](../../../../旧笔记/program_notes/docs/notes/并发编程/image/对象头.PNG)

## 3 synchronized的优化

### 锁升级

**Java SE 1.6 为了减少获得锁和释放锁带来的性能消耗，引入了“偏向锁”和“轻量级锁”；**

在 Java SE 1.6 中，锁一共有 4 种状态，级别从低到高依次是：

+ 无锁状态
+ 偏向锁状态
+ 轻量级锁状态
+ 重量级锁状态

这四种状态会随着竞争情况逐渐升级，**锁可以升级但不能降级**；

### 偏向锁

大多数情况下，**锁不仅不存在多线程竞争，而且总是由同一线程多次获得**，为了减少同一线程获取锁的代价引入了偏向锁；

如果一个线程获得了锁，那么锁就进入偏向模式，此时Mark Word 的结构也变为偏向锁结构，当这个线程再次请求锁时，无需再获取锁，这样就省去了大量有关锁申请的操作，从而提高程序的性能；

当遇到其他线程竞争锁时，偏向锁就失效了，偏向锁膨胀为轻量级锁；

### 轻量级锁

轻量级锁适用的场景是不同的线程交替执行同步块，不存在锁的竞争；

如果存在同一时间访问同一锁的场合，就会导致轻量级锁膨胀为重量级锁；

### 自旋锁

在大多数情况下，线程持有锁的时间都不会太长，如果直接挂起线程可能会得不偿失，因为线程之间切换时需要从用户态转换到核心态，这个状态之间的转换需要相对比较长的时间；

当轻量级锁失败后，虚拟机为了避免线程被挂起，会使用自旋锁；

自旋锁假设在不久的将来该线程可以获得锁，所以让当前线程做几个空循环，在经过若干循环之后如果还不能获得锁，该线程被挂起；

## 4 等待通知机制

调用 wait()、notify()、notifyAll() 可以控制线程处于等待状态或者唤醒处于等待状态下的线程；

+ wait()

  当前线程进入该对象的等待队列，并**释放锁**；

  ```java
  // 有三种重载形式：
  wait()    //一直等待，直到其他线程通知
  wait(long timeout)    //其他线程唤醒或者等待指定时间后自动唤醒
  wait(long timeout, int nanos)
  ```

  当线程被重新唤醒后，与其他线程竞争锁，一旦获得了该对象的内部锁，该线程将从wait方法返回，所以**调用wait方法总是在循环中**；

+ notify()

  唤醒在此对象上等待的**单个线程**。如果多个线程都在等待此对象内部锁，则会选择唤醒其中一个线程。选择是任意性的。

+ notifyAll()

  唤醒在此对象上等待的所有线程；

### 两个线程交替输出

```java
// 两个线程交替输出1、2、3、...
public class Test
{
    private int count = 1;
    private boolean flag = true;
    
    public synchronized void print1()
    {
        while (true)
        {
            while (flag && count <= 30)
            {
                try
                {
                    wait();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            if (count > 30) break;
            System.out.println(Thread.currentThread().getName() + ":" + count);
            count++;
            flag = true;
            notify();
        }
    }
    
    public synchronized void print2()
    {
        while (true)
        {
            while (!flag && count <= 30)
            {
                try
                {
                    wait();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            if (count > 30) break;
            System.out.println(Thread.currentThread().getName() + ":" + count);
            count++;
            flag = false;
            notify();
        }
    }
    
    public static void main(String[] args)
    {
        Test test = new Test();
        Thread thread = new Thread(test::print1);
        Thread thread2 = new Thread(test::print2);
        thread.start();
        thread2.start();
    }
}
```

### 三个线程交替输出

```java
// 三个线程交替输出A、B、C
public class Test
{
    private int count = 0;
    
    public synchronized void printA()
    {
        while (true)
        {
            while (count % 3 != 0 && count < 30)
            {
                try
                {
                    wait();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            if (count >= 30) break;
            System.out.println(Thread.currentThread().getName() + ":" + "A");
            count++;
            notifyAll();
        }
    }
    
    public synchronized void printB()
    {
        while (true)
        {
            while (count % 3 != 1 && count < 30)
            {
                try
                {
                    wait();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            if (count >= 30) break;
            System.out.println(Thread.currentThread().getName() + ":" + "B");
            count++;
            notifyAll();
        }
    }
    
    public synchronized void printC()
    {
        while (true)
        {
            while (count % 3 != 2 && count < 30)
            {
                try
                {
                    wait();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            if (count >= 30) break;
            System.out.println(Thread.currentThread().getName() + ":" + "C");
            count++;
            notifyAll();
        }
    }
    
    public static void main(String[] args)
    {
        Test test = new Test();
        Thread thread = new Thread(test::printA);
        Thread thread2 = new Thread(test::printB);
        Thread thread3 = new Thread(test::printC);
        thread.start();
        thread2.start();
        thread3.start();
    }
}
```





