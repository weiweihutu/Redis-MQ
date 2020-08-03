package com.ibiz.mq.redis.mq.test.service.impl;

import com.ibiz.mq.redis.mq.test.domain.UserInfo;
import com.ibiz.mq.redis.mq.test.service.IUserService;
import com.ibiz.mq.redis.mq.test.util.GenerateID;
import com.ibiz.redis.mq.context.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class UserService implements IUserService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final GenerateID idUtil = new GenerateID();
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public void insert(UserInfo userInfo) {
        String uid = GenerateID.createId();
        userInfo.setUserId(uid);
        String sql = "insert into " + userInfo.getTable() +" (user_id, user_name, age, country, city, created_time, updated_time) " +
                "value ('" + uid + "' , '" + userInfo.getUserName() + "' , " + userInfo.getAge() +", '" + userInfo.getCountry() +
                "','" + userInfo.getCity() + "',now(), now())";
        jdbcTemplate.execute(sql);
        logger.info("insert userInfo:" + userInfo);
    }

    @Override
    public int count(String table) {
        String sql = "select count(1) from " + table;
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }


}
