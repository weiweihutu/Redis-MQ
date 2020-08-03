package com.ibiz.mq.redis.mq.test.handler;

import com.ibiz.mq.common.Invocation;
import com.ibiz.mq.common.message.Message;
import com.ibiz.mq.redis.mq.test.domain.UserInfo;
import com.ibiz.mq.redis.mq.test.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserInfoConsumerHandler implements Invocation {
    @Autowired
    private IUserService userService;
    @Override
    public void invoke(Message message) {
        UserInfo user = (UserInfo) message.getBody();
        userService.insert(user);
    }
}
