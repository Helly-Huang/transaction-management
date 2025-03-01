package com.demo.transaction.management.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

/**
 * 统一响应
 * @param <T>
 */
public class Response<T> extends ResponseEntity<ResponseBody<T>> {

    public Response() {
        super(new ResponseBody<>(ResponseCode.SUCCESS, null), HttpStatus.OK);
    }

    public Response(HttpStatus status) {
        super(status);
    }

    public Response(ResponseBody<T> body, HttpStatus status) {
        super(body, status);
    }

    public Response(MultiValueMap<String, String> headers, HttpStatus status) {
        super(headers, status);
    }

    public Response(ResponseBody<T> body, MultiValueMap<String, String> headers, HttpStatus status) {
        super(body, headers, status);
    }

    public void setCode(int code) {
        ResponseBody<T> body = getResponseBody();
        body.setCode(code);
    }

    public void setMsg(String msg) {
        ResponseBody<T> body = getResponseBody();
        body.setMsg(msg);
    }

    public void setData(T data) {
        ResponseBody<T> body = getResponseBody();
        body.setData(data);
    }

    public int getCode() {
        ResponseBody<T> body = getResponseBody();
        return body.getCode();
    }

    public String getMsg() {
        ResponseBody<T> body = getResponseBody();
        return body.getMsg();
    }

    public T getData() {
        ResponseBody<T> body = getResponseBody();
        return body.getData();
    }

    private ResponseBody<T> getResponseBody() {
        ResponseBody<T> body = this.getBody();
        if (body == null) {
            throw new IllegalStateException("ResponseBody is null!");
        }
        return body;
    }

}