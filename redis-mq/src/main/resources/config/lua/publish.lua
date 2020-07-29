--groupId 实际对应beanName
local groupId = KEYS[1]
--beanName_业务key
local topic = KEYS[2]
--每个生产者数量key
local every_topic_key = KEYS[3]
--总生产者总数量key
local total_pull_quantity_key = KEYS[4]
--总共生产者超过10000000000000000000 次数 key
local time_key = KEYS[5]
-- msg类路径
local clazzNameKey = KEYS[6]
local clazzName = KEYS[7]
local msg = ARGV[1]

--从头部入队
local rs = redis.call("LPUSH", topic, msg)
--设置bean消息实体的class路径,用于反序列化
redis.call("SET", clazzNameKey, clazzName)
if rs > 0 then
    --入队成功
    --在当前groupId下的topic增加1 score
    redis.call("zincrby", groupId, 1, topic)
    --每个生产者总数量
    redis.call("zincrby", every_topic_key, 1, topic)
    --总生产者数量
    redis.call("INCR", total_pull_quantity_key)
    --总生产者数量
    local totalMqProduceQuantity = redis.call("GET", total_pull_quantity_key)
    if totalMqProduceQuantity == 10000000000000000000 then
        --超过10000000000000000000,在sortset 添加TOTAL_MQ_PRODUCE_PULL_QUANTITY_TIME
        --最后总的数量=get(REDIS_MQ_TOTAL_PRODUCE_PULL_QUEUE_QUANTITY) + (10000000000000000000 * get(REDIS_MQ_TOTAL_PRODUCE_PULL_QUANTITY_TIME))
        redis.call("INCR", time_key)
        redis.call("SET", total_pull_quantity_key, 0)
    end
end
return rs



