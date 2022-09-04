[Learn Git Branching：在游戏中学会Git](https://learngitbranching.js.org/?locale=zh_CN)

# 1. git commit

`git commit` 用于将**暂存区**中的内容添加到**本地仓库**中

```shell
git commit -m "备注信息"
```

每次提交都会生成一个  **commit-id** (版本号)，可以通过命令 `git log` 或 `git reflog` 查看。

# 2. git branch

Git 的分支非常轻量，只是简单地指向了某次提交记录。

`git branch` 命令用于对分支进行管理：列出、创建或删除分支

- 查看本地所有分支：

```shell
git branch
```

- 创建本地新分支：

```shell
git branch new_branch_name
```

- 删除指定的本地分支：

```
git branch -d branch_name
```

- 查看远程分支：

```shell
git branch -r
```

- 查看当前分支关联的远程分支：

```shell
git branch -vv
```



# 3. git checkout

`git checkout` 命令用于切换分支（之后会被 `git switch` 命令替换）

- 切换到已存在的指定分支

```shell
git checkout <分支名称>
```

- 创建新分支并切换到该分支

```shell
git checkout -b <分支名称>
```



# 4. git merge

`git merge` 命令用于合并两个分支。

在 Git 中合并两个分支时会产生一个特殊的提交记录，它有两个父节点。

- 将指定的分支合并到当前分支

```shell
git merge <分支名称>
```











# git log 和 git reflog

`git log` 命令可以查看 **当前分支** 提交过的版本信息，不包括已经被删除的 commit 记录和 reset 的操作。（注：只能查看当前分支！！！）

`git reflog` 命令可以查看所有分支的所有操作记录信息（包括已经被删除的 commit 记录和 reset 的操作）

```shell
git log
# 加上参数 --pretty=oneline 只会显示版本号和提交时的备注信息
git log --pretty=oneline

git reflog
```

