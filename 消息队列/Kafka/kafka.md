1、kafka简介
    kafka提供了类似JMS的特性，但并不是JMS规范的实现。
    是一个消息队列，不仅仅是一个消息队列。
    流处理平台：数据流的传输和处理。

2、基本概念
    Producer: 消息和数据的生产者，向Kafka的一个topic发布消息的进程/代码/服务。
    Consumer: 消息和数据的消费者，订阅数据（topic）并且处理其发布的消息的进程/代码/服务。
    Consumer Group: 逻辑概念，对于同一个topic，会广播给不同的group，一个group中，只有一个
                    consumer可以消费该消息。
    Broker: 物理概念，Kafka集群中的每个Kafka节点。
    Topic：逻辑概念，Kafka消息的类别，对数据进行分区、隔离。
    Partition: 物理概念，Kafka下数据存储的基本单元。一个topic数据会被分散存储到多个Partition，每个Partition是有序的。
    Replication: 同一个Partition可能会有多个Replica，多个Replica之间数据是一样的。
    Replication Leader: 一个Partition的多个Replica上，需要一个Leader负责该Partition上与Producer和Consumer交互。
    ReplicatManager: 负责管理当前broker所有分区和副本的信息，处理KafkaController发起的一些请求，副本状态的切换、
    添加/读取信息等。

3、概念延申
    Partition:
        * 每一个topic被切分为多个partitions。
        * 消费者数目小于或等于partitonde的数目。（每一个消费者会消费一个partition）
        * Broker Group中的每一个Broker保存topic的一个或多个partitions。（多个broker共同保存一份partition）
        * Consumer Group中的仅有一个Consumer读取topic的一个或多个partitions，并且是唯一的Consumer。
    Replication:
        * 当集群中有Broker挂掉的情况，系统可以主动地使Replicas提供服务。
        * 系统默认设置每一个topic的replication系数为1，可以在创建topic时单独设置。
        * Replication的基本单位是topic的Partition。
        * 所有的读和写都从Leader进，Followers只是作为备份。
        * Follower必须能够及时复制Leader的数据。
        * 增加了容错性和可拓展性。
4、核心api
    * The Producer API 允许一个应用程序发布一串流式的数据到一个或者多个Kafka topic。
    * The Consumer API 允许一个应用程序订阅一个或多个 topic ，并且对发布给他们的流式数据进行处理。
    * The Streams API 允许一个应用程序作为一个流处理器，消费一个或者多个topic产生的输入流，然后生产一个输出流到一个或多个topic中去，在输入输出流中进行有效的转换。
    * The Connector API 允许构建并运行可重用的生产者或者消费者，将Kafka topics连接到已存在的应用程序或者数据系统。比如，连接到一个关系型数据库，捕捉表（table）的所有变更内容。

4、Kafka基本结构

5、应用场景
    * 消息
        数据生成器和数据处理解耦
        缓冲未处理的消息
    * 行为追踪
    * 元信息监控
    * 日志收集
    * 流处理
    * 事件源
    * 持久化日志
6、高级特性
    * 事务
        为什么提供事务机制？
        是为了支持：1、Exactly Once；2、操作的原子性；3、有状态操作的可恢复性。
    * 零拷贝
7、生产者事务和幂等
    * 生产者幂等性
        引入目的：生产者重复生产消息，生产者进行retry会产生重试，会重复产生消息。有了幂等性之后，在进行retry重试时，只会生成一个消息。
        实现：PID（Producer ID）和Sequence Number。
            PID: 每个新的Producer在初始化的时候会被分配一个唯一的PID，这个PID对用户是不可见的。
            Sequence Number: 对于每个PID，该Producer发送数据的每个<Topic, Partition>都对应一个从0开始单调递增的Sequence Number。
            ![20170607091428631](/assets/2.png)


            http://kafka.apachecn.org/documentation.html#introduction
            https://www.cnblogs.com/sujing/p/10960832.html