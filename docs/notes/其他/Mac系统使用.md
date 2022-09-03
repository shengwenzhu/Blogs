# 一、shell

查看系统支持的 shell：

```shell
➜  ~ cat /etc/shells
# List of acceptable shells for chpass(1).
# Ftpd will not allow users to connect who are not using
# one of these shells.

/bin/bash
/bin/csh
/bin/dash
/bin/ksh
/bin/sh
/bin/tcsh
/bin/zsh
```

查看系统当前使用的 shell：

```shell
➜  ~ echo $SHELL
/bin/zsh
```

切换 shell：

```shell
# 切换到zsh
chsh -s /bin/zsh
```

## 1. 常用 shell

### bash

bash 读取的配置文件：**~/.bash_profile**（每次打开终端都会执行该配置文件）

### zsh

zsh 基本完美兼容 bash 的命令，并且由于 [**oh-my-zsh**](https://github.com/ohmyzsh/ohmyzsh) 的存在，使用起来非常方便。

zsh 读取的配置文件：**~/.zshrc**

当从 bash 切换为 zsh 时，如果不想重新配置一遍 `.zshrc `文件，可以_在 `.zshrc` 文件中加上 `source ~/.bash_profile` ，从而直接从 `.bash_profile` 文件读取配置。

## 2. 为高频命令设置别名

```shell
# 编辑.bash_profile
➜  ~ vim ~/.bash_profile
# 配置命令别名
alias sshj="ssh zhushengwen@jumper.sankuai.com"
# 让修改立即生效
source ~/.bashrc

# 查看系统有哪些alias配置
➜  ~ alias
```





