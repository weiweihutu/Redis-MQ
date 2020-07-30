package com.ibiz.mq.common.util;

/**
 * @auther yc
 * @date 2020/7/1618:55
 */
public class ClassUtil {
    public static Class<?> getClass(String classPath) throws ClassNotFoundException {
        return Class.forName(classPath);
    }

    /**
     * 创建对象
     * @param clazz clazz
     * @return  Object
     */
    public static Object getNewInstance(Class<?> clazz)  {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            RuntimeError.creator("new instance class error class:" + clazz.getName(), e);
        }
        return null;
    }

}
