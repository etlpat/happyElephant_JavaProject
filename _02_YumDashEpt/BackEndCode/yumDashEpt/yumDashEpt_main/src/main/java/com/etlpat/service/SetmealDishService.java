package com.etlpat.service;

import com.etlpat.pojo.SetmealDish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author lenovo
 * @description 针对表【setmeal_dish(套餐菜品关系)】的数据库操作Service
 * @createDate 2025-06-18 21:31:40
 */
public interface SetmealDishService extends IService<SetmealDish> {
    List<SetmealDish> getAllBySetmealId(Long setmealId);

    int deleteBySetmealId(Long setmealId);
}
