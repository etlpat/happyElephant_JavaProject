package com.etlpat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.Setmeal;
import com.etlpat.service.SetmealService;
import com.etlpat.mapper.SetmealMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lenovo
 * @description 针对表【setmeal(套餐)】的数据库操作Service实现
 * @createDate 2025-06-18 10:35:01
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
        implements SetmealService {
    @Autowired
    SetmealMapper setmealMapper;

}




