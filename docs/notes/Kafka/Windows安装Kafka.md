# Windows 安装 Kafka

第一步：安装 Zookeeper

第二步：安装 Kafka

第三步：测试

1. 运行 Zookeeper：打开 cmd 输入命令 `zkServer`；（测试过程中不能关闭该 cmd 窗口）

2. 运行 Kafka：在 `D:\kafka\kafka_2.13-3.1.0` 目录下打开一个 cmd 窗口，输入命令：（测试过程中不能关闭该 cmd 窗口）

   ```bash
   start bin\windows\kafka-server-start.bat config\server.properties
   ```

3. 创建 topic：在 `D:\kafka\kafka_2.13-3.1.0`  目录下重新打开一个 cmd 窗口，输入以下命令创建一个topic；

   ```bash
   bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic kafka-test
   # 输出Created topic kafka-test.表示创建成功
   
   # 命令解释：
   # --topic指定了所要创建主题的名称
   # --replication-factor 指定了副本因子
   # --partitions 指定了分区个数
   # --create是创建主题的动作指令
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