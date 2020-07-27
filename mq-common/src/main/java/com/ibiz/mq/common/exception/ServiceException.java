package com.ibiz.mq.common.exception;

import com.ibiz.mq.common.constant.ErrorCode;

/**
 * @auther yc
 * @date 2020/7/2718:09
 */
public class ServiceException extends RuntimeException {

    private String code;

    public ServiceException(ErrorCode errorCode, Throwable e) {
        this(errorCode.getCode(), errorCode.getMessage(), e);
    }

    public ServiceException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(String code, String message, Throwable e) {
        this(message, e);
        this.code = code;
    }

    public ServiceException(String message, Throwable e) {
        super(message, e);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
