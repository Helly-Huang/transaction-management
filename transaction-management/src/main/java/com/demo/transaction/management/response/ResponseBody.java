package com.demo.transaction.management.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 统一返回格式定义类
 */
@Getter
@Setter
@ToString
public class ResponseBody<T> {
    private int code;

    private String msg;

    private T data;

    public ResponseBody(int code) {
        this.code = code;
    }

    public ResponseBody(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseBody(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


}