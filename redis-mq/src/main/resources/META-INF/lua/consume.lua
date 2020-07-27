--groupId 实际对应beanName
local groupId = KEYS[1]
--groupId_业务key
local topic = KEYS[2]
--每个消费者数量key
local every_topic_key = KEYS[3]
--总消费者数量key
local total_pop_quantity_key = KEYS[4]
--总共消费超过10000000000000000000 次数 key
local time_key = KEYS[5]

--从尾部出列
local rs = redis.call("RPOP", topic)
if rs then
    --出队成功
    --在当前groupId下的topic减1 score
    redis.call("zincrby", groupId, -1, topic)
    --每个消费者总数量
    redis.call("zincrby", every_topic_key, 1, topic)
    --总消费者数量
    redis.call("INCR", total_pop_quantity_key)
    --总消费者数量
    local totalMqProduceQuantity = redis.call("GET", total_pop_quantity_key)
    if totalMqProduceQuantity == 10000000000000000000 then
        --超过10000000000000000000,在sortset 添加REDIS_MQ_TOTAL_PRODUCE_POP_QUANTITY_TIME
        --最后总的数量=get(REDIS_MQ_TOTAL_PRODUCE_POP_QUEUE_QUANTITY) + (10000000000000000000 * get(REDIS_MQ_TOTAL_PRODUCE_POP_QUANTITY_TIME))
        redis.call("INCR", time_key)
        redis.call("SET", total_pop_quantity_key, 0)
    end
end
return rs



