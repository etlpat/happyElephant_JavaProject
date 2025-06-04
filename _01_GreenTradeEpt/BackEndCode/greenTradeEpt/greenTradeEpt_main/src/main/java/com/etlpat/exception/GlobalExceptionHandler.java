package com.etlpat.exception;

import com.etlpat.pojo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


// 全局异常处理器（用于处理全部异常）
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 处理异常的方法
    @ExceptionHandler(value = Exception.class)// 用于处理所有种类异常
    public Result handleException(Exception e) {// 接收传来的异常e
        e.printStackTrace();
        String msg = e.getMessage();
        return Result.error(msg.length() != 0 ? msg : "操作失败");
    }
}
