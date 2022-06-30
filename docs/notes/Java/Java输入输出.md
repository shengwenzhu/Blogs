JAVA 通过 **java.io** 包下的类与接口实现输入输出；

Java 输入输出流分为 **字节流** 和 **字符流** 两大类，其中字节流以字节为单位来处理输入输出操作，字符流以字符为单位来处理输入输出操作；











Java的I/O流使用了一种装饰器设计模式，它将I/O流分成*底层节点流*和*上层处理流*，其中节点流用于和底层的物理存储节点直接关联——不同的物理节点获取节点流的方式可能存在一定的差异，但程序可以把不同的物理节点流包装成统一的处理流，从而允许程序使用统一的输入、输出代码来读取不同的物理存储节点的资源。

Java7在java.nio及其子包下提供了一系列全新的API，这些API是对原有新I/O的升级，因此也被称为NIO2，通过这些NI02，程序可以更高效地进行输入、输出操作。

本章还会介绍Java对象的序列化机制，使用序列化机制可以把内存中的Java对象转换成二进制字节流，这样就可以把Java对象存储到磁盘里，或者在网络上传输Java对象。这也是Java提供分布式编程的重要基础。











## 输入输出流



按照流是否与一个I/O设备（磁盘等）直接关联，将流分为节点流和处理流：

+ 底层节点流
  节点流用于和底层的物理存储节点直接关联；
+ 上层处理流
  对一个己存在的节点流进行封装，通过封装后的流来实现数据读/写功能。处理流也被称为高级流。当使用处理流进行输入/输出时，程序并不会直接连接到实际的数据源，没有和实际的输入/输出节点连接。通过使用处理流来包装不同的节点流，可以消除不同节点流的实现差异，程序无须理会输入/输出节点是磁盘、网络还是其他的输入/输出设备，程序只要将这些节点流包装成处理流，就可以使用相同的输入/输出代码来读写不同的输入/输出设备的数据；

### 1. 流的基类

```java
//四个抽象基类
InputStream		//字节输入流
OutputStream	//字节输出流
Reader			//字符输入流
Writer			//字符输出流
```

#### 1）InputStream

```java
//InputStream的子类都必须实现 public abstract int read()方法


```













 

 



## 







### 3.    InputStream类

1)    从输入流中读取数据

l public abstract int read() throws IOException

从输入流中读取单个字节，字节将会自动转换为int类型后返回，如果由于到达了流的末端而没有可读取的字节，则返回值-1。

注：继承InputeStream的子类需要实现该方法；

l int read(byte[] b)

从输入流中读取最多读取b.length个字节的数据并将其存储到缓冲区数组b中，返回实际读取的字节数。

l int read(byte[] b, int off, int len)

从输入流中最多读取len个字节的数据，并将其存储在数组b中，放入数组b中时，并不是从数组起点开始，而是从off位置开始，返回实际读取的字节数。

2)    关闭流

不再使用输入流时需要使用close()方法关闭输入流以及释放与该输入流相关的系统资源；

注：java7改写了所有的I/O资源类，它们都实现了AutoCloseable接口，因此都可通过自动关闭资源的try语句来关闭这些I/O流。

### 4.    Reader类

在Reader里也包含如下三个从输入流中读取数据的方法：

l int read()

从输入流中读取单个字符，返回所读取的字符数据(字符数据可直接转换为int类型)。

l int read(char[] cbuf)

从输入流中最多读取cbuf.length个字符的数据，并将其存储在字符数组cbuf中，返回实际读取的字符数。

l int read(char[] cbuf, int off, int len)

从输入流中最多读取len个字符的数据，并将其存储在字符数组cbuf中，放入数组cbuf中时，并不是从数组起点开始，而是从off位置开始，返回实际读取的字符数。

### 5.    OutputStream/Writer类

OutputStream和Writer也非常相似，两个流都提供了如下三个方法向输出流写入数据：

l public abstract void write(int b) throws IOException

将指定的字节写入此输出流。写入的一般约定是将一个字节写入输出流。要写入的字节是参数b的8个低阶位。忽略参数b的24个高阶位。

l void write(byte[]/char[] buf)

将字节数组/字符数组中的数据输出到指定输出流中。

l void write(byte[]/char[] buf, int off, int len)

将字节数组/字符数组中从off位置开始，长度为len的字节/字符输出到输出流中。

因为字符流直接以字符作为操作单位，所以Writer可以用字符串来代替字符数组，即以String对象作为参数。Writer里还包含如下两个方法：

l void write(String str)

将由字符串里包含的字符输出到指定输出流中。

l void write(String str, int off, int len)

将由字符串里从off位置开始，长度为len的字符输出到指定输出流中。

注意：使用Java的I/O流执行输出时，不要忘记关闭输出流，关闭输出流除可以保证流的物理资源被回收之外，可能*还可以将输出流缓冲区中的数据flush**到物理节点里*。

## 11.3      输入输出流体系



| 分类       | 字节输入流           | 字节输出流            | 字符输入流        | 字符输出流         |
| ---------- | -------------------- | --------------------- | ----------------- | ------------------ |
| 抽象基类   | InputStream          | OutputStream          | Reader            | Writer             |
| 访问文件   | FileInputStream      | FileOutputSteam       | FileReader        | FileWriter         |
| 访问数组   | ByteArrayInputStream | ByteArrayOutputStream | CharArrayReader   | CharArrayWriter    |
| 访问管道   | PipedInputStream     | PipedOutputStream     | PipedReader       | PipedWriter        |
| 访问字符串 |                      |                       | StringReader      | StringWriter       |
| 缓冲流     | BufferedInputStream  | BufferedOutputStream  | BufferedReader    | BufferedWriter     |
| 转换流     |                      |                       | InputStreamReader | OutputStreamWriter |
| 对象流     | ObjectInputStream    | ObjectOutputStream    |                   |                    |
| 抽象基类   | FilterInputStream    | FilterOutputStream    | FilterReader      | FilterWriter       |
| 打印流     |                      | *PrintStream*         |                   | *PrintWriter*      |
| 推回输入流 | PushbackInputStream  |                       | PushbackReader    |                    |
| 特殊流     | DataInputStream      | DataOutputStream      |                   |                    |

l 如果进行输入/输出的内容是文本内容，则应该考虑使用字符流，如果进行输入/输出的内容是二进制内容，则应该考虑使用字节；（计算机的文件常被分为文本文件和二进制文件两大类：能用记事本打开并看到其中字符内容的文件称为文本文件，反之则称为二进制文件）

l PrintStream的功能十分强大，如果*需要输出文本内容，都应该将输出流包装成PrintStream**后输出*；

l 4个访问管道的流：PipedInputStream、PipedOutputStream、PipedReader、PipedWriter，它们都是用于*实现进程之间通信功能*的；

l 缓冲流则增加了缓冲功能，增加缓冲功能可以提高输入、输出的效率，增加缓冲功能后需要使用flush()才可以将缓冲区的内容写入实际的物理节点；*由于BufferedReader* *具有一个readLine()**方法，可以非常方便地一次读入一行内容，所以经常把读取文本内容的输入流包装成BufferedReader**，用来方便地读取输入流的文本*；

l InputStreamReader/OutputStreamWriter为转换流，用于实现将字节流转换成字符流（只有字节流到字符流的转换，没有字符流到字节流的转换，原因是字符流比字节流处理方便）， 其中InputStreamReader将字节输入流转换成宇符输入流，OutputStreamWriter将字节输出流转换成字符输出流。

### 1.    推回输入流PushbackInputStream/PushbackReader

推回输入流是在普通输入流的基础上增减了一个功能：将输入流中的字节/字符回推到内部缓冲区。这在以下情况下非常有用：可以方便地读取由特定字节值分隔的不定数量的输入流字节，当读取到结束字节后，代码片段可以“unread”它，将其推回到内部缓冲区，以便输入流上的下一个读取操作将重新读取回推的字节。

PushbackInputStream和PushbackReader它们都提供了如下三个方法：

l void unread(byte[]/char[] buf)：将一个字节/字符数组内容推回到推回缓冲区里，从而允许重复读取刚刚读取的内容。

l void unread(byte[]/char[] b, int off, int len)：将一个字节/字符数组里从off开始，长度为len字节/字符的内容推回到推回缓冲区里，从而允许重复读取刚刚读取的内容。

l void unread(int b)：将一个字节/字符推回到推回缓冲区里，从而允许重复读取刚刚读取的内容。

推回输入流每次调用read方法时总是先从推回缓冲区读取，只有完全读取了推回缓冲区的内容后，但还没有装满read所需的数组时才会从原输入流中读取。

当程序创建一个PushbackInputStream和PushbackReader时需要指定推回缓冲区的大小， 默认的推回缓冲区的长度为l。如果程序中推回到推回缓冲区的内容超出了推回缓冲区的大小，将会引发Pushback buffer overflow的IOException异常。

  import java.io.PushbackReader;  import java.io.FileReader;  import java.io.IOException;  public class Test   {      public  static void main(String[] args) throws IOException      {               try(             //创建一个PushbackReader对象，指定推回缓冲区的长度为64             PushbackReader  f=new PushbackReader(new FileReader("Poem.txt"), 64))                {             char[]  array=new char[64];             //用以保存上次读取的字符串内容             String  lastContent="";             //用以保存本次读取字符的数量             int  count=0;             while((count=f.read(array))>0)             {                String  content=new String(array, 0, count);                lastContent=lastContent+content;                //用于保存想要推回内部缓冲区的目标字符的开始索引和开始索引                int  targetStartIndex=0;                int  targetEndIndex=0;                if(lastContent.indexOf("春晓")>0  && lastContent.indexOf("杂诗")>0)                {                    targetStartIndex=lastContent.indexOf("春晓");                    targetEndIndex=lastContent.indexOf("杂诗");                    f.unread(lastContent.substring(targetStartIndex,  targetEndIndex).toCharArray());                    f.read(array,0,  targetEndIndex-targetStartIndex);                    System.out.println(new  String(array, 0, targetEndIndex-targetStartIndex));                    break;                }                              }                   }            }  }  

 

 



## 11.4      RandomAccessFile

Java输入/输出流体系中功能最丰富的文件内容访问类;

既可以读取文件内容，也可以向文件写入数据。

支持"随机访问"的方式，程序可以直接跳转到文件的任意地方来读写数据。

有一个最大的局限，就是*只能读写文件*，不能读写其他I/O节点。

RandomAccessFile对象包含了一个记录指针，用以标识当前读写处的位置，当程序新创建一个RandomAccessFile对象时，该对象的文件记录指针位于文件头(也就是0处)，当读/写了n个字节后，文件记录指针将会向后移动n个字节。除此之外，RandomAccessFile可以自由移动该记录指针。RandomAccessFile包含了如下两个方法来操作文件记录指针：

l long getFilePointer()：返回文件记录指针的当前位置（从文件开头开始的偏移量，以字节为单位，在此位置发生下一次读或写。）。

l void seek(long pos)：将文件记录指针定位到pos位置。

### 1.    构造器

有两个构造器，只是指定文件的形式不同而已，一个使用String参数来指定文件名，一个使用File参数来指定文件本身：

l RandomAccessFile(String name, String mode) throws FileNotFoundException

l RandomAccessFile(File file, String mode) throws FileNotFoundException

mode参数指定RandomAccessFile的访问模式，该参数有如下4个值：

“r”：以只读方式打开指定文件。如果试图对该RandomAccessFile执行写入方法，都将抛出IOException异常；

“rw”：以读、写方式打开指定文件。如果该文件尚不存在，则尝试创建该文件。

“rws”：以读、写方式打开指定文件。相对于"rw"模式，还要求对文件的内容或元数据的每个更新都同步写入到底层存储设备。

“rwd”：以读、写方式打开指定文件。相对于"rw"模式，还要求对文件内容的每个更新都同步写入到底层存储设备。

### 2.    使用该类注意事项

该类与普通的InputStream和OutputStream只有一个不同就是该类可以任意修改记录指针，然后在记录指针处进行读写操作；

向指定文件后追加内容，程序应该先将记录指针移动到文件最后，然后开始向文件中写入内容：

  //对于一个RandomAcessFile对象f，首先将记录指针移到文本最后  f.seek(f.length());  //然后使用write()方法向文本写入内容  

RandomAccessFile向文件的指定位置插入内容，如果直接将文件记录指针移动到中间某位置后开始写入，则新写入的内容会覆盖文件中原有的内容。如果需要向指定位置插入内容，程序需要先把插入点后面的内容读入缓冲区，等把需要插入的数据写入文件后，再将缓冲区的内容追加到文件后面。

  import  java.io.File;  import  java.io.RandomAccessFile;  import  java.io.FileOutputStream;  import  java.io.FileInputStream;  import java.io.IOException;  import  java.io.FileNotFoundException;  public class  Test  {   public static void main(String[] args) throws  IOException, FileNotFoundException   {       //使用File类的createTempFile方法创建一个临时文件用于保存被操作文件指定位置后面的文本内容       File  tmp=File.createTempFile("tmp", null);       try(            RandomAccessFile f=new  RandomAccessFile("Poem.txt", "rw");            FileOutputStream f2=new  FileOutputStream(tmp);            FileInputStream f3=new  FileInputStream(tmp))       {            //用于指定被操作文件的当前位置            int position=50;            f.seek(position);            //将当前位置以后的文本内容都存入临时文件tmp中            byte[] array=new byte[50];            int count=0;            while((count=f.read(array))>0)                f2.write(array, 0,  count);            //把文件位置重新定位到positon处进行插入操作            f.seek(position);            f.write("注意：50个字节".getBytes());            //插入临时文件中的内容            while((count=f3.read(array))>0)                f.write(array, 0,  count);                   }   }  }  

 