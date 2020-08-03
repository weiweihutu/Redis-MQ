package com.ibiz.mq.common.constant;

public enum ErrorCode {
    COMMON_CODE("9999", "公共错误"),

    PRODUCE_ERROR("1000","发布任务异常");
    private String code;
    private String message;
    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
