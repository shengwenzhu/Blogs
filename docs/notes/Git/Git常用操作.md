# 特别备注

+ 如果对某条命令不熟悉，可以在命令行窗口输入`git命令 -h` 查询命令的详细用法和各种选项的含义。

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

# git remote

用于对远程仓库的操作

```bash
# 显示所有远程仓库
git remote -v
# 添加远程仓库，即将本地仓库与远程仓库关联起来
git remote add [shortname] [url]
示例：git remote add origin git@github.com:shengwenzhu/Blogs.git
```

# git pull

同步远程仓库到本地仓库，相当于运行了git fetch、git merge两条命令

```bash
# 针对不同的使用场景，存在三种方式
# 使用场景一：将指定的远程分支同步到指定的本地分支，如当前分支是dev，但是想把远程master同步到本地master，但又不想使用checkout切换到master分支
git pull <远程仓库名> <远程分支名>:<本地分支名>
# 使用场景二：将指定的远程分支同步到当前本地分支
git pull <远程仓库名> <远程分支名>
# 使用场景三：将指定的远程分支同步到当前本地分支,并且当前本地分支已经与要拉取的远程分支建立了关联关系
git pull
```

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



# 使用过程中遇到的问题

+ 使用`git push`命令

  ```bash
  # 问题一：
  F:\Blogs>git push origin main
  error: src refspec main does not match any
  error: failed to push some refs to 'git@github.com:shengwenzhu/Blogs.git'
  问题的原因：在github创建了一个仓库，然后使用git init命令在本地也创建了一个仓库，github的默认分支为main，而本地的默认分支为master,之后第一次调用git push时出现上述问题
  解决办法：将本地仓库的master分支改名为main分支，即git branch -m master main
  ```

+ 使用`git pull`命令

  ```bash
  # 问题一：
  fatal: refusing to merge unrelated histories
  问题原因：同步的两个分支没有取得联系
  解决办法一：在命令后加参数--allow-unrelated-histories强行进行合并
  ```

  













