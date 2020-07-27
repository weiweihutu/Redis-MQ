package com.ibiz.mq.common.util;

import com.google.gson.Gson;

/**
 * @auther yc
 * @date 2020/7/2718:06
 */
public class GsonUtil {

    static class GsonHolder {
        static Gson instance = new Gson();
    }

    public static Gson getGsonInstance() {
        return GsonHolder.instance;
    }

    public static <T> T fromJson(String str, Class<T> type) {
        return GsonHolder.instance.fromJson(str, type);
    }

}
