package com.demo.transaction.management.response;

import org.springframework.http.HttpStatus;

public class ResponseFactory {
    private ResponseFactory() {
    }

    /**
     * 构造Http响应，Http Status设为200
     *
     * @param code body中code字段
     * @param msg  body中msg字段
     */
    public static <T> Response<T> build(int code, String msg) {
        return new Response<>(new ResponseBody<>(code, msg), HttpStatus.OK);
    }

    /**
     * 构造Http响应
     *
     * @param httpStatus Http响应状态码
     * @param code       body中code字段
     * @param msg        body中msg字段
     */
    public static <T> Response<T> build(HttpStatus httpStatus, int code, String msg) {
        return new Response<>(new ResponseBody<>(code, msg), httpStatus);
    }

    /**
     * 构造Http响应，Http Status设为200
     *
     * @param code body中code字段
     * @param msg  body中msg字段
     * @param data body中data字段
     */
    public static <T> Response<T> build(int code, String msg, T data) {
        return new Response<>(new ResponseBody<>(code, msg, data), HttpStatus.OK);
    }

    /**
     * 构造Http响应
     *
     * @param httpStatus Http响应状态码
     * @param code       body中code字段
     * @param msg        body中msg字段
     * @param data       body中data字段
     */
    public static <T> Response<T> build(HttpStatus httpStatus, int code, String msg, T data) {
        return new Response<>(new ResponseBody<>(code, msg, data), httpStatus);
    }

    /**
     * 构造成功响应，Http Status设为200
     */
    public static <T> Response<T> success() {
        return new Response<>(new ResponseBody<>(ResponseCode.SUCCESS, ResponseMsg.SUCCESS), HttpStatus.OK);
    }

    /**
     * 构造成功响应，Http Status设为200
     *
     * @param data body中data字段
     */
    public static <T> Response<T> success(T data) {
        return new Response<>(new ResponseBody<>(ResponseCode.SUCCESS, ResponseMsg.SUCCESS, data), HttpStatus.OK);
    }

    /**
     * 构造成功响应，Http Status设为200
     *
     * @param msg  body中msg字段
     * @param data body中data字段
     */
    public static <T> Response<T> success(String msg, T data) {
        return new Response<>(new ResponseBody<>(ResponseCode.SUCCESS, msg, data), HttpStatus.OK);
    }


    /**
     * 构造失败响应，Http Status设为200
     */
    public static <T> Response<T> failure() {
        return new Response<>(new ResponseBody<>(ResponseCode.FAILURE, ResponseMsg.FAILURE), HttpStatus.OK);
    }

    /**
     * 构造失败响应，Http Status设为200
     *
     * @param msg body中msg字段
     */
    public static <T> Response<T> failure(String msg) {
        return new Response<>(new ResponseBody<>(ResponseCode.FAILURE, msg), HttpStatus.OK);
    }

    /**
     * 构造失败响应，Http Status设为200
     *
     * @param msg  body中msg字段
     * @param data body中data字段
     */
    public static <T> Response<T> failure(String msg, T data) {
        return new Response<>(new ResponseBody<>(ResponseCode.FAILURE, msg, data), HttpStatus.OK);
    }

    /**
     * 构造失败响应，Http Status设为200
     *
     * @param code body中code字段
     * @param msg  body中msg字段
     * @param data body中data字段
     */
    public static <T> Response<T> failure(int code, String msg, T data) {
        return new Response<>(new ResponseBody<>(code, msg, data), HttpStatus.OK);
    }

    /**
     * 构造失败响应，Http Status设为200
     *
     * @param code body中code字段
     * @param msg  body中msg字段
     * @param data body中data字段
     */
    public static <T> Response<T> failure(HttpStatus status, int code, String msg, T data) {
        return new Response<>(new ResponseBody<>(code, msg, data), status);
    }

}
