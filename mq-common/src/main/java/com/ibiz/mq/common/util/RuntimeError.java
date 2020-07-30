package com.ibiz.mq.common.util;

import com.ibiz.mq.common.constant.ErrorCode;
import com.ibiz.mq.common.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @auther yc
 * @date 2020/7/3018:05
 */
public class RuntimeError {
    private static Logger logger = LoggerFactory.getLogger(RuntimeError.class);
    /**抛出异常*/
    public static void creator(String error, Throwable e) {
        if (StringUtil.isNotBlank(error)) {
            logger.error(error, e);
        }
        throw new ServiceException(error, e);
    }
}
