package com.etlpat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data// 添加get、set、toString方法
@NoArgsConstructor// 添加无参构造器
@AllArgsConstructor// 添加全参构造器

// 用于存放响应信息
public class Result<T> {
    private Integer code;// 响应状态码 0-成功 1-失败
    private String message;// 提示信息
    private T data;// 响应数据


    // 返回操作成功的结果
    public static <T> Result<T> success(T data) {
        return new Result<T>(0, "操作成功", data);
    }

    // 返回操作成功的结果
    public static Result success() {
        return new Result(0, "操作成功", null);
    }

    // 返回操作失败的结果
    public static Result error(String message) {
        return new Result(1, message, null);
    }

    // 返回操作失败的结果
    public static Result error() {
        return new Result(1, "操作失败", null);
    }
}
