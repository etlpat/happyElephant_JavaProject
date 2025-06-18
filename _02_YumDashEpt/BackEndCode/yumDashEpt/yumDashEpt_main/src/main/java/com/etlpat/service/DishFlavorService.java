package com.etlpat.service;

import com.etlpat.pojo.DishFlavor;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lenovo
 * @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service
 * @createDate 2025-06-18 15:22:25
 */
public interface DishFlavorService extends IService<DishFlavor> {
    List<DishFlavor> getAllByDishId(Long dishId);

    int deleteByDishId(Long dishId);
}
