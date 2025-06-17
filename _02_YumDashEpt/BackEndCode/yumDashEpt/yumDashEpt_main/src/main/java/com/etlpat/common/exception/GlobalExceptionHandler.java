package com.etlpat.common.exception;

import com.etlpat.pojo.R;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


// 全局异常处理器（用于处理全局的异常）
@ControllerAdvice(annotations = {Controller.class, RestController.class})
@ResponseBody
public class GlobalExceptionHandler {

    // 处理异常的方法
    @ExceptionHandler(Exception.class)// 用于处理所有种类异常
    public R handleException(Exception e) {// 接收传来的异常e
        e.printStackTrace();
        String msg = e.getMessage();
        return R.error(msg.length() != 0 ? msg : "操作失败");
    }
}
