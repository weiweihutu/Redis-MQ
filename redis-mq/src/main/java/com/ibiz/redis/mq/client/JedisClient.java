package com.ibiz.redis.mq.client;

import com.ibiz.mq.common.message.Message;
import com.ibiz.mq.common.util.StringUtil;
import com.ibiz.redis.mq.config.RedisConfig;
import com.ibiz.redis.mq.constant.Constant;
import com.ibiz.redis.mq.script.ScriptManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.util.Pool;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @auther yc
 * @date 2020/7/2212:34
 */
public class JedisClient {
    private static Logger logger = LoggerFactory.getLogger(JedisClient.class);

    /**
     * sortSet 查询groupID下所有member
     * @param jedis jedis
     * @param groupId   groupId
     * @return 所有元素&分数
     */
    public static Set<Tuple> zrangeByScoreWithScores(Jedis jedis, String groupId) {
        Set<Tuple> tuples = jedis.zrangeByScoreWithScores(groupId, 1, Double.MAX_VALUE);
        jedis.close();
        return tuples;
    }

    public static byte[] pop(Jedis jedis, String topic) {
        byte[] buf = jedis.rpop(StringUtil.toByteUtf8(topic));
        jedis.close();
        return buf;
    }

    public static void evalSha(String instanceId, Message message, Jedis jedis, String topic, List<byte[]> keys, byte[] messageByte) {
        Object result = jedis.evalsha(ScriptManager.getInstance().loadSha(jedis, Constant.LUA_PUBLISH), keys, Arrays.asList(messageByte));
        logger.debug("instanceId :{} lpush produce topic :{} message:{}, result:{}", instanceId, topic, message, result);
        close(jedis);
    }

    public static byte[] evalsha(String bean, Jedis jedis, byte[]...params) {
        Object result = jedis.evalsha(ScriptManager.getInstance().loadSha(jedis, Constant.LUA_CONSUME), 5, params);
        close(jedis);
        return (byte[])result;
    }

    private static void close(Jedis jedis) {
        try {
            jedis.close();
        } catch (Exception e) {
            logger.error("redis close error", e);
        }
    }

    public static String get(Jedis jedis, String key) {
        String str = jedis.get(key);
        close(jedis);
        return str;
    }

    public final static class JedisPoolCreator {
        public static Pool<Jedis> getInstance(RedisConfig instanceConfig) {
            Pool<Jedis> pool = create(instanceConfig);
            try {
                ping(pool);
            } catch (Exception e) {
                throw new RuntimeException("redis ping failure hostname :" + instanceConfig.getHostname() + ":" + instanceConfig.getPort() +
                        " , username:" + instanceConfig.getUserName() + "/" + instanceConfig.getPassword() + ", dbIndex:" + instanceConfig.getDbIndex());
            }
            return pool;
        }

        private static Pool<Jedis> create(RedisConfig instanceConfig) {
            if (instanceConfig.isSentinel()) {
                return new JedisSentinelPool(instanceConfig.getSentinelMasterName(), instanceConfig.getSentinels());
            }
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setBlockWhenExhausted(instanceConfig.isBlockWhenExhausted());
            poolConfig.setJmxEnabled(instanceConfig.isJmxEnabled());
            poolConfig.setLifo(instanceConfig.isLifo());
            poolConfig.setMaxIdle(instanceConfig.getMaxIdle());
            poolConfig.setMaxTotal(instanceConfig.getMaxTotal());
            poolConfig.setMaxWaitMillis(instanceConfig.getMaxWaitMillis());
            poolConfig.setMinEvictableIdleTimeMillis(instanceConfig.getMinEvictableIdleTimeMillis());
            poolConfig.setMinIdle(instanceConfig.getMinIdle());
            poolConfig.setTestOnBorrow(instanceConfig.isTestOnBorrow());
            poolConfig.setTestOnCreate(instanceConfig.isTestOnCreate());
            poolConfig.setTestOnReturn(instanceConfig.isTestOnReturn());
            poolConfig.setTestWhileIdle(instanceConfig.isTestWhileIdle());
            poolConfig.setTimeBetweenEvictionRunsMillis(instanceConfig.getTimeBetweenEvictionRunsMillis());
            return new JedisPool(poolConfig, instanceConfig.getHostname(), instanceConfig.getPort(), instanceConfig.getTimeout(), instanceConfig.getPassword());
        }

        static void ping(Pool<Jedis> pool) {
            Jedis jedis = pool.getResource();
            String ping = jedis.ping();
            if (!"PONG".equalsIgnoreCase(ping)) {
                throw new RuntimeException("redis ping failure");
            }
            jedis.close();
        }
    }
}
