# 特别备注

+ 学习方式
  
+ 《Linux命令行大全》
  
    > https://weread.qq.com/web/reader/f5c32ac072287278f5cc0e6kc81322c012c81e728d9d180
  
+ **shell与终端**

  + Shell 是一个程序，它接收由键盘输入的命令并将其传递给操作系统执行，几乎所有的 Linux 发行版都提供了来自 GNU 项目的 Shell 程序 Bash。
  + 当使用图形用户界面时，使用终端仿真器（简称终端）与Shell进行交互。

+ <span style = "color:red;font-weight:bold">Linux 中的命令是区分大小写的。</span>

+ 命令选项

  + 短选项：单个字符前加上单个连字符，如`ls -l`
  + 长选项：在单词前加两个连字符

+ 命令行操作技巧

  + 使用鼠标，双击可以复制，单击中键可以将其粘贴到命令中。



# 一、获取命令帮助信息

## type：显示命令类型

```bash
# 命令格式
type command [command ...]
```

> Linux 中的命令可以分为以下几种：
>
> + 可执行程序：如 /usr/bin 中的文件
> + Shell 中的内建命令
> + Shell 函数
> + 别名（在其他命令的基础上自定义的命令）

```bash
# 命令示例
zhu@ubuntu:~$ type type
type is a shell builtin		# type是一个shell内建命令
zhu@ubuntu:~$ type ls
ls is aliased to `ls --color=auto`	# ls是其他命令的别名 	
zhu@ubuntu:~$ type cp
cp is /bin/cp		# cp命令是一个可执行程序，其位置在/bin/cp
```

## which：显示可执行文件的位置

> 只能用于可执行文件的位置查找

```bash
# 命令格式
which command
```

```bash
# 命令示例
zhu@ubuntu:~$ which ls
/bin/ls
```

## help：获取Shell内建命令帮助信息

```bash
# 命令格式
help command
```

```bash
# 命令示例
zhu@ubuntu:/$ help cd
cd: cd [-L|[-P [-e]] [-@]] [dir]
    Change the shell working directory.
    
    Change the current directory to DIR.  The default DIR is the value of the
    HOME shell variable.
    
    ......
```

## --help选项：显示命令帮助信息

```bash
# 命令格式
command --help
```

## man：显示命令手册页

Linux 中大多数命令会提供一份命令的正式文档

```bash
# 命令格式
man command
```

## whatis：显示命令的简述

```bash
# 命令格式
whatis command 
```

```bash
# 命令示例
zhu@ubuntu:/$ whatis mv
mv (1)               - move (rename) files
zhu@ubuntu:/$ whatis ls
ls (1)               - list directory contents
```



# 二、文件系统导航

## 1. pwd——查看当前工作目录

pwd

```bash
zhu@ubuntu:~$ pwd
/home/zhu
```

> 注：当用户启动终端时，当前工作目录就是用户的主目录。每个用户都有自己的主目录，这是普通用户唯一有权限写入文件的地方。

## 2. ls——列出目录内容

```bash
# 简单的列出当前目录内容
ls

# 显示当前目录内容的更多细节
ls -l
# 输出示例：drwxr-xr-x 3 zhu zhu 4.0K Jun 26  2020 Desktop
# 输出解释：
	# “drwxr-xr-x”：表示文件的访问权限。第一个字符指明了文件类型，为d表示目录，为-表示普通文件；接下来的三个字符“rwx”表示文件属主的访问权限，r表示可读取，w表示可修改，x表示可执行；后续的三个字符“r-x”表示文件属组的访问权限；最后三个字符“r-x”表示其他用户的访问权限。
	# “3”：文件的硬链接数量
	# “zhu”：文件属主
	# “zhu”：文件属组
	# “4.0K”：文件大小（单位字节）
	# “Jun 26  2020”：文件最后修改时间
	# “Desktop”：目录名

# 列出所有文件，包括隐藏文件
ls -a
```

## 3. cd——更改当前工作目录

```bash
# 方式一：绝对路径名
# 绝对路径名从根目录开始，直到目标目录或文件，根目录在路径中用/来表示
cd /usr/bin

# 方式二：相对路径名
# 相对路径名从当前工作目录开始，存在两个特殊符号：.表示当前工作目录，..表示当前工作目录的父目录
cd .. # 更改当前工作目录为其父目录

# 几个超便捷用法
cd		# 将当前工作目录更改为用户主目录
cd -	# 将当前工作目录切换回上一个工作目录
cd ~user_name		# 将当前工作目录切为用户user_name的主目录
```



# 三、文件

## 1. file——查看文件类型

```bash
zhu@ubuntu:~$ file Desktop
Desktop: directory
```

## 2. less——查看文本文件

命令格式：`less 文本文件`

less是一个分页程序，查看文本文件时可以配合以下输入进行操作

| 命令            | 操作                 |
| --------------- | -------------------- |
| Page Up或b      | 后翻一页             |
| Page Down或空格 | 前翻一页             |
| 上方向键        | 向后一行             |
| 下方向键        | 向前一行             |
| G               | 移动到文本文件末尾   |
| g               | 移动到文本文件开头   |
| /characters     | 向前搜索指定的字符串 |
| n               | 重复上一次搜索       |
| q               | 退出less命令         |
| h               | 显示帮助信息         |

## 3. mkdir——创建目录

```bash
mkdir DIRECTORY...
# ...表示可以同时创建多个目录
```

## 4. cp——复制文件和目录

```bash
# 将单个文件或目录SOURCE复制到文件或目录DEST
cp SOURCE DEST

# 将多个文件或目录SOURCE复制到目录DEST
cp SOURCE...ST
```

cp命令的一些常用选项如下所示：

| 选项                | 含义                                                         |
| ------------------- | ------------------------------------------------------------ |
| -i, --interactive   | 在覆盖已有文件之前，提示用户进行确认，如果未指定该选项，命令会自动覆盖文件 |
| -R, -r, --recursive | 递归地复制目录，在复制目录时使用该选项（或者-a选项）         |
| -u, --update        | 只有目标目录中不存在或当前文件比目标目录中的文件新时，才复制 |
| -v, --verbose       | 复制时显示相关信息                                           |

## 5. mv——移动和重命名文件

该命令的用法与cp命令大同小异，唯一的区别是源文件被删除了。

```bash
Usage: cp [OPTION]... SOURCE DEST
  or:  cp [OPTION]... SOURCE... DIRECTORY
```

## 6. rm——删除文件和目录

```bash
Usage: rm [OPTION]... [FILE]...

常用选项：
-f, --force		忽略不存在的文件
-i				在每次删除之前，提示用户确认
-r, -R, --recursive		递归地删除目录,即如果被删除的目录内还有子目录，也会一并删除，想要删除目录，必须指定该选项
```

## 7. ln——创建硬链接和符号链接

在Linux的文件系统中，保存在磁盘分区中的文件不管什么类型都会分配一个编号，称为索引节点号(Inode Index)。

Linux系统存在两种链接：硬链接、符号链接。

硬链接的作用是让多个文件名指向同一索引节点，这样只删除一个链接并不影响索引节点本身和其它的链接，只有当最后一个链接被删除后，文件才会被删除。

```bash
# 创建硬链接命令格式，LINK_NAME可以使用绝对路径名或相对路径名表示
ln file LINK_NAME

# 创建硬链接示例，可以看出创建的硬链接的索引节点号相同，指向同一个文件
zhu@ubuntu:~/test$ ls
passwd
zhu@ubuntu:~/test$ ln passwd fun
zhu@ubuntu:~/test$ ls -il
total 8
679024 -rw-r--r-- 2 zhu zhu 2398 Mar  9 22:14 fun
679024 -rw-r--r-- 2 zhu zhu 2398 Mar  9 22:14 passwd
```

硬链接存在两个问题：硬链接不能引用其所在文件系统之外的文件，即**不在同一个磁盘分区**内的文件；硬链接不能引用目录，只能引用文件。

符号链接旨在弥补硬链接的这两个不足，符号链接类似于Windows的快捷方式，是一个保存了被引用文件位置信息的特殊文件。

```bash
# 创建符号链接命令格式，其中，item可以是文件或目录
ln -s item LINK_NAME
```



# 四、I/O重定向

I/O：即输入输出

标准输入：程序在运行过程中一般从标准输入获取输入，默认情况下标准输入与键盘相关联。

标准输出：程序在运行过程中将运行结果发送到标准输出，默认情况下标准输出与显示器屏幕相关联，不会保存为磁盘文件。

标准错误：程序运行过程中产生的错误信息发送到标准错误，默认情况下标准错误与显示器屏幕相关联。

I/O重定向：改变输入的来源（不从键盘输入）和输出的位置（不输出到屏幕）。

## 1. 标准输出重定向 

重定向操作符：`>`

```bash
# 命令示例
zhu@ubuntu:~$ ls -l > ls-output.txt
# 将命令`ls -l`的输出结果存储到ls-output.txt文件中
# 如果ls-output.txt文件不存在，会新建该文件
# 如果ls-output.txt文件已存在，会覆盖已有文件内容
```

将输出追加到文件末尾（不覆盖已有内容），使用重定向操作符：`>>`

## 2. 标准错误重定向

不存在专门的重定向操作符。

标准错误重定向需要使用文件描述符，shell 使用文件描述符0、1、2 分别表示标准输入、标准输出、标准错误。

```bash
# 命令示例
zhu@ubuntu:~$ ls -l /test 2> ls-error.txt
# 注：文件描述符2紧靠在重定向操作符之前，将标准错误重定向到ls-error.txt文件
```

将标准输出和标准错误重定向到一个文件中：

```bash
# 旧版本
ls -l /test > ls-output.txt 2>&1
# 注：标准错误的重定向操作必须在标准输出重定向之后

# 新版本
ls -l /test &> ls-output.txt
```

丢弃不需要的输出结果：将输出结果重定向到 `/dev/null`  的特殊文件

```bash
ls -l /test 2> /dev/null
```

## 3. 标准输入重定向

标准输入重定向：从磁盘文件中获取输入。

### cat：在标准输出上显示文件内容

读取一个或多个文件，拼接后显示在标准输出（常用于显示短文本文件）。

```bash
# 命令示例
zhu@ubuntu:~$ cat shige.txt 
他日若遂凌云志
敢笑黄巢不丈夫
```







# 通配符

利用通配符，可以构建出复杂的文件名匹配条件。

常用的通配符及其含义如下所示：

| 通配符        | 含义                                         |
| ------------- | -------------------------------------------- |
| *             | 匹配任意多个字符                             |
| ？            | 匹配任意单个字符                             |
| [characters]  | 匹配字符集合characters中的任意单个字符       |
| [!characters] | 匹配不属于字符集合characters中的任意单个字符 |
| [[:class:]]   | 匹配字符类class中的任意单个字符              |

> 不能匹配以点号开头的文件（即隐藏文件），如果想匹配此类文件，可以使用模式`.[!.]`

常用的字符类及其含义如下所示：

| 字符类    | 含义                     |
| --------- | ------------------------ |
| [:alnum:] | 匹配任意单个字母数字字符 |
| [:alpha:] | 匹配任意单个字母         |
| [:digit:] | 匹配任意单个数字         |
| [:lower:] | 匹配任意单个小写字母     |
| [:upper:] | 匹配任意单个大写字母     |

























