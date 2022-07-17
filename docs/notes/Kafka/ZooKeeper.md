ZooKeeper 是一个开源的分布式协调服务，分布式应用程序可以基于 ZooKeeper 实现诸如数据发布/订阅、负载均衡、命名服务、分布式协调/通知、集群管理、Master选举、配置维护等功能。

在 ZooKeeper 中共有3个角色：leader、follower 和 observer，同一时刻 ZooKeeper 集群中只会有一个 leader，其他的都是follower 和 observer。observer 不参与投票，默认情况下 ZooKeeper 中只有 leader 和 follower 两个角色。