package com.etlpat.mapper;

import org.apache.ibatis.annotations.Param;

import com.etlpat.pojo.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author lenovo
 * @description 针对表【dish(菜品管理)】的数据库操作Mapper
 * @createDate 2025-06-18 10:34:47
 * @Entity com.etlpat.pojo.Dish
 */
public interface DishMapper extends BaseMapper<Dish> {
    int countByCategoryId(@Param("categoryId") Long categoryId);
}




