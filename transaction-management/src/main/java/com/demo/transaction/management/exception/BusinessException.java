package com.demo.transaction.management.exception;

import com.demo.transaction.management.response.ResponseCode;

public class BusinessException extends RuntimeException {

    /**
     * 异常编码
     */
    private final int code;
    /**
     * 异常码描述
     */
    private final String description;

    /**
     * @param code
     * @param message
     * @param description
     */
    public BusinessException(int code, String message, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    /**
     * @param message
     */
    public BusinessException(String message) {
        super(message);
        this.code = ResponseCode.BUSINESS_ERROR;
        this.description = "";
    }

}
