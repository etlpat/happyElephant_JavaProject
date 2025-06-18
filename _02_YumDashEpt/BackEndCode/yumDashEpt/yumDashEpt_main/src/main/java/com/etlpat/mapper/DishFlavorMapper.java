package com.etlpat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.etlpat.pojo.DishFlavor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author lenovo
 * @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Mapper
 * @createDate 2025-06-18 15:22:25
 * @Entity com.etlpat.pojo.DishFlavor
 */
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {
    List<DishFlavor> getAllByDishId(@Param("dishId") Long dishId);

    int deleteByDishId(@Param("dishId") Long dishId);
}




