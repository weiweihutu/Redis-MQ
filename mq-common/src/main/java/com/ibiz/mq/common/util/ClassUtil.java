package com.ibiz.mq.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @auther yc
 * @date 2020/7/1618:55
 */
public class ClassUtil {
    private static final Logger logger = LoggerFactory.getLogger(ClassUtil.class);

    public static Class<?> getClass(String classPath) throws ClassNotFoundException {
        return Class.forName(classPath);
    }

    public static Object getNewInstance(Class<?> clazz)  {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            logger.error("new instance class error class:{}", clazz.getName());
            throw new RuntimeException(e);
        }
    }

    /*public <T> T getNewInstance(String classPath) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(classPath);
        return getNewInstance(clazz);
    }*/
}
