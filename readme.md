

## Redis-MQ介绍

与第三方中间件消息通讯会占用部分系统资源，并且第三方会启动很多额外的功能，例如启动守护线程监听MQ的消费情况等，这些都会占用系统资源。因此写一套能够支持MQ机制的组件

支持配置多个redis实例 ， 配置多个消费者。实例与消费者多对多关系

![redis-MQ](image\redis-MQ-2.png)

## SPI

功能均是通过实现已定义的接口进行完成。

com.ibiz.mq.common.consumer.IConsumer	文件名(接口)

​	redis=com.ibiz.redis.mq.consumer.RedisConsumer （bean名称 - 实现类）

com.ibiz.mq.common.lifecycle.Lifecycle

​	redis=com.ibiz.redis.mq.lifecycle.RedisLifecycle

```java
ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(ISerializerHandler.class);
        ISerializerHandler serializerHandler = (ISerializerHandler)extensionLoader.getInstance(serialize,"protobuf");
        Message message = serializerHandler.deserializer(buf, messageBodyClass);
```

## 配置项

```properties
#redis 标识使用redisMQ
#mq-config-consumer 消费者必须配置
#userInfoConsumerHandler 消费者id
#instanceId 消费者对应生产者实例id , 这里instanceId=m2 对应redis-mq-config-instance.m2
#corePoolSize 消费者Work线程池核心线程数
#maximumPoolSize 消费者Work线程池最大线程数
#queueSize 消费者Work线程池最大容量
#serializer 序列化方式 支持 gson jdk protobuf jackson
#strategy 消费策略 (最多任务先消费,最少任务先消费)
redis-mq-config-consumer.userInfoConsumerHandler={instanceId:"m2",bean:"userInfoConsumerHandler",corePoolSize:5,maximumPoolSize:10,queueSize:100,strategy:"MORE_FIRST",serializer:"gson"}
#redis 标识使用redisMQ
#mq-config-instance  实例必须配置
#m2 实例id, 用户任意配置,作用(消费者通过instanceId与实例绑定)
#hostname...等  redis相关配置
redis-mq-config-instance.m2={hostname:"192.168.131.101",port:6379,timeout:3000,usePool:true,dbIndex:0,maxTotal:500,maxIdle:20,timeBetweenEvictionRunsMillis:30000,minEvictableIdleTimeMillis:30000,maxWaitMillis:5000,testOnCreate:true,testOnBorrow:false,testOnReturn:true,testWhileIdle:true,sentinel:false,sentinelMasterName:"sentinelMaster",sentinels:["127.0.0.1:6378","127.0.0.1:6379"]}
```

### 如何提高吞吐量？

Redis-MQ采用线程池管理机制，根据不同业务点的特殊性，例如简易业务，频繁IO,调用第三方服务，配置不同的线程池。已达到最大消化率。

### 模型

![redis-MQ](image\redis-MQ.png)

一个消费者对应一个BOSS线程，一个BOSS线程管理一个WORK线程池，WORK线程池中处理每一个具体的消费任务

```java
public class BossThreadManager {	//BOSS线程管理
    //key : consumerId   value : BossThread (Runnable)
    private final Map<String, BossThread> BOSS_RUNNABLE_MANAGER = new ConcurrentHashMap<>(64);
    //key : consumerId  value : Thread
    private final Map<String, Thread> BOSS_THREAD_MANAGER =  new ConcurrentHashMap<>(64);
    //key : instanceId  value : List<consumerId>
    private final Map<String, Set<String>> INSTANCE_CONSUMER_MANAGER = new ConcurrentHashMap<>();
}
```

```java
public class WorkThreadPoolManager {	//WORK线程池管理
    //key : consumerId value : DefineThreadPoolExecutor
    private final Map<String, DefineThreadPoolExecutor> WORK_THREAD_POOL_MANAGER = new ConcurrentHashMap<>();
}
```

自定义线程池

```java
public class DefineThreadPoolExecutor extends ThreadPoolExecutor {
    /**当前线程池总提交数量
     * BOSS线程需要通过 count来控制到redis取待消费任务
     * WORK线程需要通过 count 和 restrict 判断任务是否能入队列*/
    private final AtomicInteger count = new AtomicInteger(0);
    /**当线程池和队列都满了,线程休眠时间*/
    private long sleep;
    /**当前线程池最大容量 最大线程数 + 队列容量*/
    private final int restrict;
    /**当前线程池总提交数量
     * BOSS线程需要通过 count来控制到redis取待消费任务
     * WORK线程需要通过 count 和 restrict 判断任务是否能入队列*/
    public boolean nextSubmit() {
        return count.get() < restrict;
    }
    public void increment() {
        count.getAndIncrement();
    }
    public void decrement() {
        count.decrementAndGet();
    }
    /**
     * 根据消费者id创建线程池,如果已经创建则直接返回,反之新创建一个
     * @instanceId redis实例id
     * @param consumerId    消费者id
     * @param corePoolSize  核心线程数
     * @param maximumPoolSize   最大线程数
     * @param size 队列最大容量
     * @return  ThreadPoolExecutor
     */
    public DefineThreadPoolExecutor(int corePoolSize, int maximumPoolSize, int size, String consumerId, long sleep) {
        super(corePoolSize, maximumPoolSize, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(size), new BossThreadFactory(consumerId), new DefineRejectHandler());
        restrict = maximumPoolSize + size;
        this.sleep = sleep;
    }
    public long getSleep() {
        return sleep;
    }
    /**消费一个任务后提交数量减1*/
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        decrement();
    }
}
```

第10000次消费 cost :12440

```
corePoolSize:5,maximumPoolSize:10,queueSize:100
```

MQ 10000次耗时 131779



支持动态修改消费者配置，例如修改序列化方式等

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

## 序列化反序列化

支持protobuf , gson, jackon, jdk 四种机制

 Protobuf

> 安装protobuf程序
> https://github.com/protocolbuffers/protobuf/releases
> 1.设置环境变量
> 2.编写实体proto文件

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
> 3.编译proto
> protoc --proto_path=. --java_out=E:\workspace\spring-execrise\redis-mq-x\src\main\java User.proto

> 4.将生成的java类放入对应包下

#### JDK

JDK 序列化实体必须实现Serializable。jdk的序列化时会把实体的所有信息，包括超类，属性已经属性的超类均写入字节流中。在反序列化中根据这些数据进行反序列化

2、protobuf序列化采用二进制,时间和空间上比GSON占优势。但是当对象中中文字符比较多，空间比GSON更多
protobuf序列化完成后，在反序列化需要知道具体类型才能进行反序列化。
这里有2种情况：
    1 在序列化时记录下原始类型，反序列化时通过反射调用反序列化方法进行（本MQ使用这种）
    2 结构相同的proto类也能反序列化生成对应(优点：消费者无需依赖生产者的实体。只要结构相同即可)

#### GSON

GSON序列化对象，简单对象时没问题。复杂对象例如集合，反序列化时无法为嵌套对象的属性设置值

#### Jackson

Jackson序列化对象，序列化后占空间比gson大，但是序列化耗时最低，反序列化耗时比gson略高

下面是三种序列化已经反序列化占比和耗时。可以看出protobuf性能最优，空间小而且耗时短。缺点是反序列化时需要知道类型

其次是gson,耗时短，序列化大小比jdk小。缺点是复杂的集合类型反序列化时无法得知泛型中具体类型。反系列化后的对象需要手动处理

最次是jdk。优点时无需记录反序列化类型。缺点是占空间大，耗时长

```java
protobuf Object size 1216 * (1000 * 1000) 序列化大小 355000000 , 序列化cost :2021 反序列化cost:1003
jdk Object size 960 * (1000 * 1000) 序列化大小 1115000000 , 序列化cost :7222 反序列化cost:31133
gson Object size 960 * (1000 * 1000) 序列化大小 268000000 , 序列化cost :3372 反序列化cost:1559
jackson Object size 960 * (1000 * 1000) 序列化大小 456000000 , 序列化cost :1707 反序列化cost:2818
```



## MQ测试

测试思路

分布式服务中，不同服务请求能够确保生产者产生的每个任务都被消费者消费，

这里使用2台机器部署6个服务，每个服务均可以作为生产者又可作为消费者。

使用3张表，然后分别对三张表进行新增数据操作。

### 准备

1、设计三张表，三张表表名不同，结构相同

```sql
CREATE TABLE user_info
(`user_id` VARCHAR(64) PRIMARY KEY,
`user_name` VARCHAR(30),
`age` INT(3),
`country` VARCHAR(30),
`city` VARCHAR(30),
`created_time` DATETIME,
`updated_time` DATETIME
)
```

部署2台机 192.168.131.101 ， 192.168.131.100

分别在2台上部署3个服务相同，端口不同的服务，port : [22221, 22222, 22223 ]

在192.168.131.101 部署

>  redis-mq-test-22221.jar

>  redis-mq-test-22222.jar

>  redis-mq-test-22223.jar

在192.168.131.100部署相同服务

>  redis-mq-test-22221.jar

>  redis-mq-test-22222.jar

>  redis-mq-test-22223.jar

看下产生任务代码

```java
@RestController
public class RedisMqController  {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IUserService userService;
    /**
     * 生产任务
     * @param size  任务数量
     * @param table 操作的表名
     */
    @GetMapping("/push")
    public void pushQueue(@RequestParam(name = "size") Integer size, @RequestParam(name = "table") String table) {
        producer(size, table);
    }

    @GetMapping("/count")
    public int count(@RequestParam(name = "table") String table) {
        return userService.count(table);
    }

    private void producer(int size, String table) {
        //userInfoConsumerHandler 消费者id ,在配置文件中配置
        ConsumerConfig consumerConfig = MQConfig.CONSUMER_CONFIG.get("userInfoConsumerHandler");
        ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(IProducer.class);
        IProducer producer = (IProducer) extensionLoader.getInstance(consumerConfig.getProtocol());
        IntStream.range(0, size).forEach(tpc -> {
            Message message = new Message();
            UserInfo userInfo = new UserInfo();
            userInfo.setAge(tpc % 10 + 20);
            userInfo.setCity("深圳" + tpc);
            userInfo.setCountry("CN");
            userInfo.setTable(table);
            userInfo.setUserName("测试" + tpc);
            message.setBody(userInfo);
            producer.publisher(consumerConfig.getInstanceId(), consumerConfig.getBean(), "TEST", consumerConfig.getSerializer(), message);
        });
    }
}
```

### 启动服务

#### 1、相同实例，不同消费者

启动6个服务，请求服务：

这里只分别请求192.168.131.101机器的服务，192.168.131.100 单纯用作消费者消费任务

> http://192.168.131.101:22222/push?size=10000&table=user_info

> http://192.168.131.101:22222/push?size=10000&table=user_info_2

> http://192.168.131.101:22222/push?size=10000&table=user_info_3

> http://192.168.131.101:22221/push?size=10000&table=user_info

> http://192.168.131.101:22221/push?size=10000&table=user_info_2

> http://192.168.131.101:22221/push?size=10000&table=user_info_3

> http://192.168.131.101:22223/push?size=10000&table=user_info_3

![deploy](image\deploy.png)

![deploy](image\deploy_2.png)给user_info表产生20000个任务 ， user_info_2产生20000个任务，user_info_3产生30000个任务.结果如下

```sql
tb            total  
-----------  --------
user_info       20000
user_info_2     20000
user_info_3     30000
```

2、不同实例，相同消费者

产生1000个任务，在不同实例中分别创建了1000个任务，最终产生2000个消费任务

```sql
tb            total  
-----------  --------
user_info        2000
```



### 测试结果

2台机6个服务，在分布式服务下，同一时刻产生70000个任务。每台机的服务均能够正常消费。

CPU使用情况

![pid](image\pid.png)

![pid](image\cpu.png)

redis 任务消费情况

```shell
127.0.0.1:6379> keys *       ## 所有key
1) "REDIS_MQ_EVERY_PRODUCE_POP_QUEUE_QUANTITY"	## 每个任务消费者消费数量 sortedSet
2) "REDIS_MQ_EVERY_PRODUCE_PULL_QUEUE_QUANTITY" ## 每个任务生产数量 sortedSet
3) "REDIS_MQ_TOTAL_PRODUCE_POP_QUEUE_QUANTITY"  ## redis总任务数量 
4) "REDIS_MQ_TOTAL_PRODUCE_PULL_QUEUE_QUANTITY" ## redis总消费数量 
5) "REDIS_MQ_userInfoConsumerHandler"	## 任务待消费topic  sortedSet
6) "REDIS_MQ_userInfoConsumerHandler_TEST_CLAZZ"  ## 任务实体数据类型
#####实际上在运行时，还有下面这个key,用来保存任务实体数据,用的redis List列表存储。当生产任务时push,消费时pop
7) "REDIS_MQ_userInfoConsumerHandler_TEST"
127.0.0.1:6379> get REDIS_MQ_TOTAL_PRODUCE_POP_QUEUE_QUANTITY
"519389"
127.0.0.1:6379> get REDIS_MQ_TOTAL_PRODUCE_POP_QUEUE_QUANTITY
"519389"
127.0.0.1:6379> zrange REDIS_MQ_userInfoConsumerHandler 0 -1 withscores
1) "REDIS_MQ_userInfoConsumerHandler_TEST"
2) "0"
127.0.0.1:6379> zrange REDIS_MQ_EVERY_PRODUCE_PULL_QUEUE_QUANTITY 0 -1 withscore
s
1) "REDIS_MQ_producer.demo_TEST"
2) "245374"
3) "REDIS_MQ_userInfoConsumerHandler_TEST"
4) "274015"
127.0.0.1:6379> zrange REDIS_MQ_EVERY_PRODUCE_POP_QUEUE_QUANTITY 0 -1 withscores

1) "REDIS_MQ_producer.demo_TEST"
2) "245374"
3) "REDIS_MQ_userInfoConsumerHandler_TEST"
4) "274015"
```

TODO 是否可以支持动态服务治理功能？

redis实例动态治理，例如修改redis hostname,port或其他配置