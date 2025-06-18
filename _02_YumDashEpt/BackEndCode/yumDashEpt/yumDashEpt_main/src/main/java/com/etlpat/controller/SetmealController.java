package com.etlpat.controller;

import com.etlpat.service.SetmealDishService;
import com.etlpat.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


// 后台管理端 -- 套餐表 + 套餐菜品关系表
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;
}
