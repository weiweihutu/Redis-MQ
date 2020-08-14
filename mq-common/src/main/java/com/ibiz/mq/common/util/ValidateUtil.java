package com.ibiz.mq.common.util;
import com.ibiz.mq.common.constant.ErrorCode;
import com.ibiz.mq.common.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

/**
 * @auther 喻场
 * @date 2020/7/2414:37
 */
public class ValidateUtil {
    private static Logger logger = LoggerFactory.getLogger(ValidateUtil.class);
    public static <T> void validate(T t, Predicate<T> predicate, ErrorCode errorCode, String message) {
        if(predicate.test(t)) {
            logger.error(message + " : code:{} , message:{}", errorCode.getCode(), errorCode.getMessage());
            throw new ServiceException(errorCode.getCode(), StringUtil.isNotBlank(message) ? message : errorCode.getMessage());
        }
    }
}
