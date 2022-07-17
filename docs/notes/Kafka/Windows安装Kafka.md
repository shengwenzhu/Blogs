# Linux 安装 Kafka

## 第一步：安装 Zookeeper（必要组件）

> 安装版本：`apache-zookeeper-3.8.0-bin`

+ 添加系统变量：

  ```
  ZOOKEEPER_HOME		D:\zookeeper\apache-zookeeper-3.8.0-bin
  Path				%ZOOKEEPER_HOME%\bin
  ```

+ 修该配置文件

  进入安装目录，进入 conf 目录，将 `zoo_sample.cfg` 文件修改为 `zoo.cfg`，然后修改 `zoo.cfg` 配置文件。

  ```
  # zookeeper客户端与服务器之间的心跳时间，默认2000ms
  tickTime=2000
  
  # Follower连接到Leader并同步数据的最大时间
  initLimit=10
  
  # Follower同步Leader的最大时间
  syncLimit=5
  
  # 数据存放路径
  dataDir=D:/zookeeper/apache-zookeeper-3.8.0-bin/data
  
  # 日志路径
  dataLogDir=D:/zookeeper/apache-zookeeper-3.8.0-bin/log
  
  # 客户端连接zookeeper server的端口
  clientPort=2181
  ```

+ 设置 zookeeper 服务区编号

  在上述配置的数据存放路径下，即 datadir，创建 `myid` 文件（没有后缀，重要！！！），然后在该文件里写入服务器编号。

+ 启动  Zookeeper 服务

  打开 cmd 输入命令 `zkServer`。

  > 启动 zookeeper 服务时报错：` ZooKeeper audit is disabled.`
  >
  > 原因：新版本启动时，zookeeper 新增的审核日志是默认关闭的
  >
  > 解决办法：在配置文件 zoo.cfg 新增一行 `audit.enable=true` 即可
  
  

## 第二步：安装 Kafka

> 版本：kafka_2.13-3.1.0

+ 编辑配置文件

  `D:\kafka\kafka_2.13-3.1.0\config` 目录下的 `server.properties`

  ```
  # broker的编号，如果集群中有多个broker，每个broker的编号必须唯一
  broker.id=0
  
  # broker对外提供服务的入口地址
  listeners=PLAINTEXT://localhost:9092
  
  # 存放日志文件的路径
  log.dirs=D:/kafka/kafka_2.13-3.1.0/kafka-logs
  
  # zookeeper 集群地址
  zookeeper.connect=localhost:2181
  ```

+ 启动 kafka 服务

  在 `D:\kafka\kafka_2.13-3.1.0` 目录下执行以下命令：

  ```bash
  start bin\windows\kafka-server-start.bat config\server.properties
  ```

  > 判断 kafka 服务是否启动：使用 `jps -l` 命令

+ 创建主题

  在 `D:\kafka\kafka_2.13-3.1.0`  目录下输入以下命令创建一个 topic：

  ```bash
  bin\windows\kafka-topics.bat --create --bootstrap-server localhost:2181 --replication-factor 1 --partitions 1 --topic topic-demo
  
  # 输出 Created topic kafka-test. 表示创建成功
  
  # 命令解释：
  # --create	创建主题的动作指令
  # --replication-factor	副本因子
  # --partitions 分区个数
  # --topic	创建主题的名称
  ```

  





4. 查看已创建的topic

   ```bash
   bin\windows\kafka-topics.bat --list  --bootstrap-server localhost:9092
   ```

5. 新建生产者Producer：在D:\kafka\kafka_2.13-3.1.0主目录下重新打开一个cmd窗口，输入以下命令

   ```bash
   bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic kafka-test
   
   # --broker-list指定了连接的Kafka集群地址
   # --topic指定了发送消息的主题
   ```

6. 新建消费者Consumer：在D:\kafka\kafka_2.13-3.1.0主目录下重新打开一个cmd窗口，输入以下命令

   ```bash
   bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic kafka-test --from-beginning
   
   # --bootstrap-server指定了连接的Kafka集群地址
   # --topic指定了消费者订阅的主题
   ```

7. 生产者输入消息，消费者能很快获得。