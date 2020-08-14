package com.ibiz.mq.test;

import com.ibiz.redis.mq.domain.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @auther 喻场
 * @date 2020/7/2416:06
 */
public class UserFactory {

    public static UserProto.User getUser() {
        StringBuilder sb = new StringBuilder();
        IntStream.range(0,100).forEach(i -> sb.append("中"));
        UserProto.User.Builder userBuilder =  UserProto.User.newBuilder();
        userBuilder.setName(sb.toString()).setAge(12).setAmount(24.2f).setPrice(100d)
                .addMap(DefineMapProto.DefineMap.newBuilder().setKey("a").setValue("1").build())
                .addMap(DefineMapProto.DefineMap.newBuilder().setKey("b").setValue("2").build())
                .addMap(DefineMapProto.DefineMap.newBuilder().setKey("c").setValue("3").build())
                .setSuperUser(
                        SuperUserProto.SuperUser.newBuilder().setLevel(23).addList("a").addList("b").setAdmin(true).build());
        UserProto.User user = userBuilder.build();
        return user;
    }

    public static UserModel getJdkUser() {
        StringBuilder sb = new StringBuilder();
        IntStream.range(0,2).forEach(i -> sb.append("中"));
        UserModel user = new UserModel();
        SuperUserModel superUser = new SuperUserModel();
        superUser.setAdmin(true);
        superUser.setLevel(23);
        superUser.setList(Arrays.asList("a","b"));
        user.setSuperUser(superUser);
        user.setSuperUsers(Arrays.asList(superUser, superUser, superUser, superUser, superUser));
        user.setAge(12);
        user.setAmount(24.2f);
        user.setPrice(1000d);
        user.setName(sb.toString());
        Map<String, String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");
        user.setMap(map);
        return user;
    }
}
