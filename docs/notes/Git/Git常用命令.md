# 备注

+ 查看命令帮助信息（查询命令的详细用法和各种选项的含义）

  ```bash
  $ git push -h
  usage: git push [<options>] [<repository> [<refspec>...]]
  
      -v, --verbose         be more verbose
      -q, --quiet           be more quiet
      --repo <repository>   repository
      ...
  ```


# git pull

同步远程仓库到本地仓库，相当于运行了 `git fetch`、`git merge` 两条命令。

命令格式如下：

```bash
git pull <远程主机名> <远程分支名>:<本地分支名>
```

命令示例如下：

```bash
# 将origin主机的master分支拉取过来，与本地的main分支合并
$ git pull origin master:main

# 将指定的远程分支同步到当前本地分支,可以省略本地分支名
$ git pull origin master

# 将指定的远程分支同步到当前本地分支,并且当前本地分支已经与要拉取的远程分支建立了“追踪关系”,可以省略远程分支名
git pull origin

# 如果当前分支只有一个追踪分支,可以省略远程主机名
git pull
```

> 本地分支与远程分支建立“追踪关系”：
>
> + 使用 `git clone` 命令克隆远程仓库时，所有本地分支默认与远程主机的同名分支建立追踪关系。
>
> + 手动建立追踪关系：
>
>   ```bash
>   # 指定master分支追踪origin/next分支
>   $ git branch --set-upstream master origin/next
>   ```

# git push

用于将本地分支的更新推送到远程主机并合并。

```bash
# 命令格式
git push <远程主机名> <本地分支名>:<远程分支名>
# 如果本地分支名与远程分支名相同，则可以省略冒号和远程分支名
git push <远程主机名> <本地分支名>
```

命令示例如下：

```bash
# 将本地的master分支推送到origin主机的master分支（注：如果origin主机不存在master分支，会新建该分支）
$ git push origin master

# 省略本地分支名时表示删除origin主机的master分支
$ git push origin :master
# 注：该命令等同于 “git push origin --delete master”

# 将“当前分支”推送到远程主机时，如果当前分支与远程分支之间存在“追踪关系”，则本地分支和远程分支都可以省略
$ git push origin
# 如果当前分支只有一个追踪分支，那么主机名都可以省略
$ git push
# 如果当前分支与多个主机存在追踪关系，则可以使用“-u选项”指定一个默认主机，这样之后就可以不加任何参数使用git push
$ git push -u origin master
```

常见报错：

+ **问题1：本地仓库版本落后于远程仓库**

  ```bash
  ! [rejected] main -> main (non-fast-forward)
  error: failed to push some refs to 'xxx'
  hint: Updates were rejected because the tip of your current branch is behind its remote counterpart. Integrate the remote changes (e.g. 'git pull ...') before pushing again.
  ```

  解决方案：

  + 方案一：使用 ”git pull“ 命令同步远程仓库到本地仓库

    ```
    git pull origin master
    ```

  + 方案二：使用 ”--force“ 选项强制推送

    ```bash
    git push origin master --force
    ```


# git remote

用于管理远程仓库。

命令示例如下：

```bash
# 查看当前所有的远程仓库
$ git remote -v
origin  git@github.com:shengwenzhu/Blogs.git (fetch)
origin  git@github.com:shengwenzhu/Blogs.git (push)
# 注：origin表示远程仓库的别名，并且后面显示了该仓库的url

# 列出已经存在的远程仓库，git 默认使用 origin 标识远程仓库
$ git remote

# 列出远程仓库的详细信息，在别名后面列出URL地址
$ git remote -v
$ git remote --verbo

# 添加远程仓库
$ git remote add <远程仓库的别名> <远程仓库的URL地址>

# 修改远程仓库的别名
$ git remote rename <原远程仓库的别名> <新的别名>

# 删除指定名称的远程仓库
$ git remote remove <远程仓库的别名>

# 修改远程仓库的 URL 地址
$ git remote set-url <远程仓库的别名> <新的远程仓库URL地址>
```

# git init

在当前目录中创建一个新的 Git 仓库

```bash
F:\Blogs>git init
Initialized empty Git repository in F:/Blogs/.git/
```

# git add

将文件添加到暂存区

```bash
# 添加当前目录下的所有文件到暂存区
git add .
# 添加一个或多个文件到暂存区
git add [file1] [file2] ...
```

# git status

用于查看工作目录和暂存区的状态：

+ 哪些修改已经被添加到暂存区
+ 哪些修改还没有被添加到暂存区
+ 哪些文件还没有被Git tracked到

```bash
# 以精简的方式输出状态信息
git status -s
```







# git config

**配置 git 的相关参数；**

Git 一共有3个配置文件：配置文件的权重为 **仓库>全局>系统**，Git 首先会查找系统级的配置文件，该文件含有对系统上所有用户及他们所拥有的仓库都生效的配置值；接着 Git 会查找每个用户的全局配置文件，最后查找各个仓库中 Git 目录下的配置文件；

- 系统级配置文件：`/usr/local/etc/gitconfig` 文件
- 全局配置文件：`~/.gitconfig`
- 仓库级配置文件：仓库的 `.git/config` 文件；

```
# 查看配置信息: 命令参数 -list, 简写 -l
# --local：仓库级，--global：全局级，--system：系统级
$ git config <--local | --global | --system> -l

# 编辑配置文件：命令参数 -edit，简写 -e，注：git config -e 默认编辑仓库级的配置文件；
$ git config <--local | --global | --system> -e

# 添加配置项
$ git config <--local | --global | --system> --add <section.key> <value>
# 注：section、key、value 三者缺一不可；

# 获取配置项
$ git config <--local | --global | --system> --get <section.key>

# 删除配置项
$ git config <--local | --global | --system> --unset <section.key>

# 配置提交记录中的用户信息
$ git config --global user.name <用户名>
$ git config --global user.email <邮箱地址>

# 查看当前生效的配置信息
$ git config -l

# 更改Git缓存区的大小
# 如果提交的内容较大，默认缓存较小，提交会失败
# 缓存大小单位：B，例如：524288000（500MB）
$ git config --global http.postBuffer <缓存大小>

# 调用 git status/git diff 命令时以高亮或彩色方式显示改动状态
$ git config --global color.ui true

# 配置可以缓存密码，默认缓存时间15分钟
$ git config --global credential.helper cache

# 配置密码的缓存时间
# 缓存时间单位：秒
$ git config --global credential.helper 'cache --timeout=<缓存时间>'

# 配置长期存储密码
$ git config --global credential.helper store
```



# git clone

从远程仓库克隆一个版本库到本地

```
# 终端输入 git clone 查询 
用法：git clone [<选项>] [--] <仓库> [<路径>]

经常使用：
# 默认在当前目录下创建和版本库名相同的文件夹并下载版本到该文件夹下
$ git clone <远程仓库的网址>

# 指定本地仓库的目录
$ git clone <远程仓库的网址> <本地目录>

# -b 指定要克隆的分支，默认是master分支
$ git clone <远程仓库的网址> -b <分支名称> <本地目录>
```



# git reset

用于回退版本，可以指定退回某一次提交的版本；

```bash
git reset [--soft | --mixed | --hard] [HEAD]

usage: git reset [--mixed | --soft | --hard | --merge | --keep] [-q] [<commit>]
   or: git reset [-q] [<tree-ish>] [--] <pathspec>...
   or: git reset [-q] [--pathspec-from-file [--pathspec-file-nul]] [<tree-ish>]
   or: git reset --patch [<tree-ish>] [--] [<pathspec>...]

    -q, --quiet           be quiet, only report errors
    --mixed               reset HEAD and index
    --soft                reset only HEAD
    --hard                reset HEAD, index and working tree
    --merge               reset HEAD, index and working tree
    --keep                reset HEAD but keep local changes
    --recurse-submodules[=<reset>]
                          control recursive updating of submodules
    -p, --patch           select hunks interactively
    -N, --intent-to-add   record only the fact that removed paths will be added later
    --pathspec-from-file <file>
                          read pathspec from file
    --pathspec-file-nul   with --pathspec-from-file, pathspec elements are separated with NUL character
```

# git stash

适用场景：

- 场景一：当正在 dev 分支上开发某个项目，这时项目中出现一个bug，需要紧急修复，但是正在开发的内容只是完成一半，还不想提交，这时可以用 git stash 命令将修改的内容保存至堆栈区，然后顺利切换到其他分支进行bug修复，修复完成后，再次切回到dev分支，从堆栈中恢复刚刚保存的内容。
- 场景二：由于疏忽，本应该在 dev 分支开发的内容，却在 master 上进行了开发，需要重新切回到 dev 分支上进行开发，可以用 git stash 将内容保存至堆栈中，切回到 dev 分支后，再次恢复内容即可；

总的来说，git stash 命令的作用就是将目前还不想提交的但是已经修改的内容进行保存至堆栈中，后续可以在某个分支上恢复出堆栈中的内容。这也就是说，**stash 中的内容不仅仅可以恢复到原先开发的分支，也可以恢复到其他任意指定的分支上**。git stash作用的范围包括工作区和暂存区中的内容，也就是说没有提交的内容都会保存至堆栈中。

```bash
# 将所有未提交的修改（工作区和暂存区）保存至堆栈中，用于后续恢复
git stash

# 查看当前 stash 中的内容
git stash list

# 将当前 stash 中的内容弹出，并应用到当前分支对应的工作目录上，该命令将堆栈中最近保存的内容删除
git stash pop

# 将堆栈中的内容应用到当前目录，不同于git stash pop，该命令不会将内容从堆栈中删除，也就说该命令能够将堆栈的内容多次应用到工作目录中，适应于多个分支的情况
git stash apply
```











