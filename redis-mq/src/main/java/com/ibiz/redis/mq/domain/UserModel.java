package com.ibiz.redis.mq.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @auther 喻场
 * @date 2020/7/2511:59
 */
public class UserModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer age;
    private Double price;
    private Float amount;
    private Map<String, String> map;
    private List<SuperUserModel> superUsers;
    private SuperUserModel superUser;
    private String name;
    private Date now = new Date();

    public List<SuperUserModel> getSuperUsers() {
        return superUsers;
    }

    public void setSuperUsers(List<SuperUserModel> superUsers) {
        this.superUsers = superUsers;
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public SuperUserModel getSuperUser() {
        return superUser;
    }

    public void setSuperUser(SuperUserModel superUser) {
        this.superUser = superUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
