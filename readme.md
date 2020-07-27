BOSS线程从redis读取待消费topic数远大于WORK线程消费能力时，如何处理大批任务读进内存问题？

WORK如何保证消费完每一个任务而不丢失？

1. BOSS线程管理一个自定义的WORK线程池，线程池定义了核心线程和最大线程以及固定队列（ArrayBlockingQueue），拒绝策略.
2. 线程池持有已sumit待消费(包括未出列)的AtomicInteger计数器，当提交线程时，计数器+1 ，自定义的线程池实现afterExecute方法，当任务执行完毕计数器-1, 当然也考虑到线程池submit时出现异常的情况，这种情况计数器也-1.
3. 在BOSS线程中，通过计数器实时与线程池最大容量数（最大线程数+队列容量）相比，计数器更大，也BOSS线程休眠（休眠时间用户设定，默认500）,当小于容量时说明可以继续去redis拿topic
4. 自定义拒绝策略，定义一个自旋操作，当计数器小于线程池最大容量（最大线程数+队列容量）并且能成功入队才退出。虽然第三步中拿任务使用计数器和容量做了条件控制，但是不能避免一些临界点的情况，因此在拒绝策略中再次保证任务能够顺利入列

如何优雅关闭服务

```java
bootstrap.shutdown();
```

通过BOSS线程管理WORK线程池，每个BOSS线程持有一个status标志来管理线程运行状态。

status使用AtomicBoolean是为了解决同步问题。

关闭服务时，先将BOSS线程运行状态置为false,BOSS线程即时停止。

然后关闭WORK线程池

Protobuf
安装protobuf程序
https://github.com/protocolbuffers/protobuf/releases
1.设置环境变量
2.编写实体proto文件
```java
syntax = "proto2";  //使用proto2
import "DefineMap.proto"; //导入其他proto
import "SuperUser.proto";
package com.ibiz.redis.mq.domain; 

option java_package = "com.ibiz.redis.mq.domain"; //生成目标类包名
option java_outer_classname = "UserProto";  ////生成目标类类名
//生成的主要实体类,继承了com.google.protobuf.GeneratedMessage
message User {
  //optional 代表属性可选
  optional int32 age = 1;
  optional double price = 2;
  optional float amount = 3;
  //repeated 代表集合
  repeated DefineMap map = 4;
  optional SuperUser superUser = 5;
  optional string name = 6;
}
```
3.编译proto
protoc --proto_path=. --java_out=E:\workspace\spring-execrise\redis-mq-x\src\main\java User.proto

4.将生成的java类放入对应包下

jdk, jackson, gson, protobuf 序列化

1、JDK 序列化实体必须实现Serializable。jdk的序列化时会把实体的所有信息，包括超类，属性已经属性的超类均写入字节流中。在反序列化中根据这些数据进行反序列化

2、protobuf序列化采用二进制,时间和空间上比GSON占优势。但是当对象中中文字符比较多，空间比GSON更多
protobuf序列化完成后，在反序列化需要知道具体类型才能进行反序列化。
这里有2种情况：
    1 在序列化时记录下原始类型，反序列化时通过反射调用反序列化方法进行（本MQ使用这种）
    2 结构相同的proto类也能反序列化生成对应(优点：消费者无需依赖生产者的实体。只要结构相同即可)

3、GSON序列化对象，简单对象时没问题。复杂对象例如集合，反序列化时无法为嵌套对象的属性设置值

4、Jackson序列化对象，序列化后占空间比gson大，但是序列化耗时最低，反序列化耗时比gson略高

下面是三种序列化已经反序列化占比和耗时。可以看出protobuf性能最优，空间小而且耗时短。缺点是反序列化时需要知道类型

其次是gson,耗时短，序列化大小比jdk小。缺点是复杂的集合类型反序列化时无法得知泛型中具体类型。反系列化后的对象需要手动处理

最次是jdk。优点时无需记录反序列化类型。缺点是占空间大，耗时长

```java
protobuf Object size 1216 * (1000 * 1000) 序列化大小 355000000 , 序列化cost :2021 反序列化cost:1003
jdk Object size 960 * (1000 * 1000) 序列化大小 1115000000 , 序列化cost :7222 反序列化cost:31133
gson Object size 960 * (1000 * 1000) 序列化大小 268000000 , 序列化cost :3372 反序列化cost:1559
jackson Object size 960 * (1000 * 1000) 序列化大小 456000000 , 序列化cost :1707 反序列化cost:2818
```



