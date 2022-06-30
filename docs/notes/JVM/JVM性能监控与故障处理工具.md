# JVM 性能监控与故障处理工具

## 1. JDK 命令行工具

> JDK 的 bin 目录下提供了多个命令行工具

### javac

将 JAVA 源代码编译成字节码文件

### java

解释执行字节码文件

### jps

> jps（JVM Process Status Tool，虚拟机进程状态工具）

显示正在运行的虚拟机进程的主类（main函数所在的类）以及该进程的本地虚拟机唯一ID（LVMID, Local Virtual Machine Identifier）

> 对于本地虚拟机进程来说，LVMID 与操作系统的进程ID（即PID）是一致的

```bash
C:\Users\shengwen>jps
19564 IdentificationApplication

# -l 选项会显示完整的主类名
C:\Users\shengwen>jps -l
19564 com.zhu.identification.IdentificationApplication

# -v 选项输出虚拟机进程启动时显示指定的 JVM 参数
```

### jstat

> jstat（虚拟机统计信息监视工具）

监控虚拟机各种运行状态信息；

```bash
# usage：
jstat -<option> [-t] [-h<lines>] <vmid> [<interval> [<count>]]

# 命令解释如下：
# 1. 使用如下命令可以查看jstat包含哪些选项
C:\Users\shengwen>jstat -options
-class		# 类加载统计，已加载的类数量、已加载的类所占的空间、加载类耗费的时间等
-compiler	# JIT即时编译器编译过的方法、耗时等信息
-gc			# 堆内存信息统计，包括Eden区、两个Survivor区、老年代等的容量、已用空间、GC耗时等信息
-gccapacity	# 监视内容与-gc基本相同，但输出主要关注Java堆各个区域使用到的最大、最小空间
-gcutil 	# 监视内容与-gc基本相同，但输出主要关注已使用空间占总空间的百分比
-gccause	# 与-gcutil功能相同，但会输出导致上一次GC产生的原因
-gcmetacapacity
-gcnew		# 监视新生代状态
-gcnewcapacity
-gcold		# 监视老年代状态
-gcoldcapacity
-printcompilation	# 输出已经被JIT即时编译器编译过的方法

# 2.其余参数
vmid		虚拟机进程ID，支持对远程服务器上的虚拟机进程查询
interval	查询间隔，表示每隔多长时间查询一次，配合参数count一起使用
count		查询次数

# 使用
C:\Users\shengwen>jstat -gcutil 19564
S0		0.00	# Survivor1已使用空间比例
S1		0.00	# Survivor2已使用空间比例
E		18.58	# 新生代Eden区已使用空间比例
0		13.78	# 老年代已使用的空间占比
M		94.88	# 元数据区已使用空间比例
CCS		93.50	# 压缩使用比例
YGC		5		# 新生代垃圾回收次数
YGCT	0.067	# 新生代垃圾回收总耗时
FGC		2		# 老年代垃圾回收次数
FGCT	0.122	# 老年代垃圾回收总耗时
GCT		0.188	# 垃圾回收总耗时
```

> 查看各种选项下输出结果的解释可以参考博客：
>
> + https://www.cnblogs.com/lizhonghua34/p/7307139.html
> + https://blog.csdn.net/zhaozheng7758/article/details/8623549

### jinfo

> jinfo（Java 配置信息工具）

实时查看和调整正在运行的 JVM 各项参数；

```bash
# 查看JVM进程的参数值
C:\Users\shengwen>jinfo -flag InitialHeapSize 19564
-XX:InitialHeapSize=264241152

# 通过如下命令可以在进程运行期间修改配置值
-flag [+|-]<name>    to enable or disable the named VM flag
-flag <name>=<value> to set the named VM flag to the given value
```

### jmap

> jmap（Java 内存映像工具）

用于生成堆转储快照（headdump 或 dump 文件）；



## 2. JDK 可视化工具

### JConsole

> Java 监视与管理控制台





+ **jvisualvm**

  JVM 可视化工具

  在Windows的CMD控制台或者Linux终端下执行“jvisualvm”命令进入Java VisualVM工作台；

  

  

+ **javap**

  class 文件分解器，可以字节码文件进行反编译，也可以查看java编译器生成的字节码；

  ```
  // 语法格式 
  javap [命令选项] class文件
      命令选项：
        -v					   输出附加信息
        -l                   	   输出行号和局部变量表
        -public                  仅显示公共类和成员
        -protected               显示受保护的/公共类和成员
        -package                 显示程序包/受保护的/公共类和成员 (默认)
        -p  -private             显示所有类和成员
        -c                       对代码进行反汇编
        -s                       输出内部类型签名
        -sysinfo                 显示正在处理的类的系统信息 (路径, 大小, 日期, MD5 散列)
        -constants               显示最终常量
        -classpath <path>        指定查找用户类文件的位置
        -cp <path>               指定查找用户类文件的位置
        -bootclasspath <path>    覆盖引导类文件的位置
  ```

  

  查看有权访问hotspot虚拟机的进程；

  

  













