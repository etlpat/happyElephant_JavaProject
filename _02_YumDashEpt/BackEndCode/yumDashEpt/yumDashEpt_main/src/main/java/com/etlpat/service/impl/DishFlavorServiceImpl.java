package com.etlpat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.DishFlavor;
import com.etlpat.service.DishFlavorService;
import com.etlpat.mapper.DishFlavorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author lenovo
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service实现
* @createDate 2025-06-18 15:22:25
*/
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
    implements DishFlavorService{
    @Autowired
    private DishFlavorMapper dishFlavorMapper;


    @Override
    public List<DishFlavor> getAllByDishId(Long dishId) {
        return dishFlavorMapper.getAllByDishId(dishId);
    }

    @Override
    public int deleteByDishId(Long dishId) {
        return dishFlavorMapper.deleteByDishId(dishId);
    }
}




