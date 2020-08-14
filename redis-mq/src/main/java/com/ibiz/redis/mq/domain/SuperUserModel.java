package com.ibiz.redis.mq.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * @auther 喻场
 * @date 2020/7/2511:59
 */
public class SuperUserModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean admin;
    private int level;
    private List<String> list;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
