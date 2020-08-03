package com.ibiz.mq.redis.mq.test.util;

import java.util.UUID;

public class GenerateID {
    /**uuid生成id , 测试使用*/
    public static String createId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
