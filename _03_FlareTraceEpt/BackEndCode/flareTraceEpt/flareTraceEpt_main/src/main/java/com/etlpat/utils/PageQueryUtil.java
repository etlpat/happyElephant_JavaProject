package com.etlpat.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


// 分页查询的工具类
public class PageQueryUtil {

    // 分页查询--骨架
    public static <T> Page<T> getPage(Integer pageNum,// 第几页
                                      Integer pageSize,// 每页大小
                                      LambdaQueryWrapper<T> wrapper,// 查询条件
                                      BaseMapper<T> mapper) {// mapper对象
        // 设置分页参数
        Page<T> page = new Page<>(pageNum, pageSize);

        // 进行分页查询（查询结果封装在page对象中）
        mapper.selectPage(page, wrapper);
        return page;
    }
}
