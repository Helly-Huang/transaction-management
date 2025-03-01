package com.demo.transaction.management.exception;

import com.demo.transaction.management.response.Response;
import com.demo.transaction.management.response.ResponseCode;
import com.demo.transaction.management.response.ResponseFactory;
import com.demo.transaction.management.response.ResponseMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 自定义异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public Response<String> handleBusinessException(BusinessException ex) {
        log.error("businessException:{}", ex.getMessage(), ex);
        return ResponseFactory.failure(ResponseCode.BUSINESS_ERROR, ResponseMsg.BUSINESS_ERROR, ex.getMessage());
    }

    /**
     * 参数校验异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<Map<String, String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException:{}", ex.getMessage(), ex);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseFactory.failure(ResponseCode.PARAMETERS_VALIDATION_ERROR, ResponseMsg.PARAMETERS_VALIDATION_ERROR, errors);
    }

    /**
     * 其他异常，兜底
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Response<String> handleException(Exception ex) {
        log.error("businessException:{}", ex.getMessage(), ex);
        return ResponseFactory.failure(ResponseCode.SYSTEM_ERROR, ResponseMsg.DEFAULT_ERROR, ex.getMessage());
    }

}
