package com.ibiz.mq.common.util;

import java.nio.charset.StandardCharsets;

public class StringUtil {

    public static byte[] toByteUtf8(String str) {
        if (null == str || "".equals(str.trim())) {
            return new byte[0];
        }
        return str.getBytes(StandardCharsets.UTF_8);
    }

    public static boolean isBlank(String str) {
        return !isNotBlank(str);
    }

    public static boolean isNotBlank(String str) {
        return null != str && !"".equals(str.trim());
    }
}
