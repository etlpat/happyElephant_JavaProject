package com.etlpat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.etlpat.pojo.SetmealDish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author lenovo
 * @description 针对表【setmeal_dish(套餐菜品关系)】的数据库操作Mapper
 * @createDate 2025-06-18 21:31:40
 * @Entity com.etlpat.pojo.SetmealDish
 */
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {
    List<SetmealDish> getAllBySetmealId(@Param("setmealId") Long setmealId);

    int deleteBySetmealId(@Param("setmealId") Long setmealId);
}




