package com.etlpat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.SetmealDish;
import com.etlpat.service.SetmealDishService;
import com.etlpat.mapper.SetmealDishMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lenovo
 * @description 针对表【setmeal_dish(套餐菜品关系)】的数据库操作Service实现
 * @createDate 2025-06-18 21:31:40
 */
@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish>
        implements SetmealDishService {
    @Autowired
    SetmealDishMapper setmealDishMapper;

    @Override
    public List<SetmealDish> getAllBySetmealId(Long setmealId) {
        return setmealDishMapper.getAllBySetmealId(setmealId);
    }

    @Override
    public int deleteBySetmealId(Long setmealId) {
        return setmealDishMapper.deleteBySetmealId(setmealId);
    }
}




