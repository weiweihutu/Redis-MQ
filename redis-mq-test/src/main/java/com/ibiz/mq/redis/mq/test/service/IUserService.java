package com.ibiz.mq.redis.mq.test.service;

import com.ibiz.mq.redis.mq.test.domain.UserInfo;

public interface IUserService {
    void insert(UserInfo userInfo);

    int count(String table);
}
