package com.ibiz.mq.common.util;

import java.nio.charset.StandardCharsets;

public class StringUtil {

    public static byte[] toByteUtf8(String str) {
        if (null == str || "".equals(str.trim())) {
            return new byte[0];
        }
        return str.getBytes(StandardCharsets.UTF_8);
    }

    public static boolean equals(String s1, String s2) {
        return s1 == null && s2 == null ? true :
                s2 == null && s1 == null ? true :
                        s1.trim().equals(s2.trim());
    }

    public static boolean isBlank(String str) {
        return !isNotBlank(str);
    }

    public static boolean isNotBlank(String str) {
        return null != str && !"".equals(str.trim());
    }
}
