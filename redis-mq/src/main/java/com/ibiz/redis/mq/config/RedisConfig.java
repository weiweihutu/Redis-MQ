package com.ibiz.redis.mq.config;

import com.ibiz.mq.common.config.InstanceConfig;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @auther yc
 * @date 2020/7/1519:11
 */
public class RedisConfig extends InstanceConfig {
    /**
     * 主机
     */
    private String hostname;
    /**
     * 端口号
     */
    private int port = 6379;
    /**
     * 超时
     */
    private int timeout = 2000;
    /**
     * 是否使用池
     */
    private boolean usePool = true;
    /**
     * DB索引
     */
    private int dbIndex = 0;
    //最大连接数, 默认8个
    private int maxTotal = 200;
    //最大空闲连接数, 默认8个
    private int maxIdle = 20;
    //最小空闲连接数, 默认0个
    private int minIdle = 0;
    private boolean lifo = Boolean.TRUE;
    private boolean blockWhenExhausted = Boolean.TRUE;
    //当资源池用尽后，调用者是否要等待。只有当值为true时，下面的maxWaitMillis才会生效
    //是否启用后进先出, 默认true
    //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零 -1（表示永不超时）,  默认-1
    private long maxWaitMillis = 5000;
    /**
     * 创建时是否检查
     */
    private boolean testOnCreate = Boolean.FALSE;
    //向资源池借用连接时是否做连接有效性检测（ping）。检测到的无效连接将会被移除。
    //业务量很大时候建议设置为false，减少一次ping的开销
    private boolean testOnBorrow = Boolean.TRUE;
    //向资源池归还连接时是否做连接有效性检测（ping）。检测到无效连接将会被移除。
    //业务量很大时候建议设置为false，减少一次ping的开销
    private boolean testOnReturn = Boolean.TRUE;
    //是否开启JMX监控
    private boolean jmxEnabled = Boolean.TRUE;
    //是否开启空闲资源检测（单位为毫秒）
    //建议设置，周期自行选择，也可以默认也可以使用下方JedisPoolConfig 中的配置
    private long timeBetweenEvictionRunsMillis = 30000L;
    //空闲资源的检测周期（单位为毫秒）
    private boolean testWhileIdle = Boolean.TRUE;
    //逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认180000（即30分钟）
    //资源池中资源的最小空闲时间（单位为毫秒），达到此值后空闲资源将被移除。
    private long minEvictableIdleTimeMillis = 30000L;

    /**
     * 是否为:sentinel模式
     */
    private boolean sentinel = Boolean.FALSE;

    /**
     * sentinel master name
     */
    private String sentinelMasterName = "";

    /**
     * 192.168.131.101:6379 ,192.168.131.102:6379, 192.168.131.103:6379
     * sentinels
     */
    private Set<String> sentinels = new HashSet<>(0);

    public boolean isSentinel() {
        return sentinel;
    }

    public void setSentinel(boolean sentinel) {
        this.sentinel = sentinel;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public boolean isJmxEnabled() {
        return jmxEnabled;
    }

    public void setJmxEnabled(boolean jmxEnabled) {
        this.jmxEnabled = jmxEnabled;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isUsePool() {
        return usePool;
    }

    public void setUsePool(boolean usePool) {
        this.usePool = usePool;
    }

    public int getDbIndex() {
        return dbIndex;
    }

    public void setDbIndex(int dbIndex) {
        this.dbIndex = dbIndex;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public long getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public boolean isLifo() {
        return lifo;
    }

    public void setLifo(boolean lifo) {
        this.lifo = lifo;
    }

    public boolean isBlockWhenExhausted() {
        return blockWhenExhausted;
    }

    public void setBlockWhenExhausted(boolean blockWhenExhausted) {
        this.blockWhenExhausted = blockWhenExhausted;
    }

    public boolean isTestOnCreate() {
        return testOnCreate;
    }

    public void setTestOnCreate(boolean testOnCreate) {
        this.testOnCreate = testOnCreate;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public String getSentinelMasterName() {
        return sentinelMasterName;
    }

    public void setSentinelMasterName(String sentinelMasterName) {
        this.sentinelMasterName = sentinelMasterName;
    }

    public Set<String> getSentinels() {
        return sentinels;
    }

    public void setSentinels(Set<String> sentinels) {
        this.sentinels = sentinels;
    }
}
