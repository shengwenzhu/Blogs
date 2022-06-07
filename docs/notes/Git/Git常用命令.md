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

# git commit

将暂存区中的内容添加到本地仓库中

> 注：每次使用 git commit 命令都会在本地仓库中生成一个 40 位的哈希值，这个哈希值叫作 commit-id，commit-id 在版本回退的时候是非常有用的，它相当于一个快照，通过与`git reset`组合使用。

# git branch

对分支进行管理

```bash
# 展示当前所在的本地分支
git branch
# 创建一个本地分支
git branch newbranchname
# 重新命名分支
git branch -m newname
# 查看当前分支关联的远程分支
git branch -vv
# 查看远程分支
git branch -r
# 删除远程分支
git push origin --delete 远程分支名
```

















