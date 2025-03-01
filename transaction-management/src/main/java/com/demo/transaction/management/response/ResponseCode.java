package com.demo.transaction.management.response;

/**
 * 返回码
 */
public class ResponseCode {
    /**
     * 成功
     */
    public static final int SUCCESS = 200;

    /**
     * 失败
     */
    public static final int FAILURE = 300;

    /**
     * 系统错误
     */
    public static final int SYSTEM_ERROR = 500;

    /**
     * 参数校验错误
     */
    public static final int PARAMETERS_VALIDATION_ERROR = 501;

    /**
     * 业务错误
     */
    public static final int BUSINESS_ERROR = 502;

}