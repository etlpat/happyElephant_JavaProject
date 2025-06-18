package com.etlpat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etlpat.pojo.Dish;
import com.etlpat.pojo.DishFlavor;
import com.etlpat.pojo.R;
import com.etlpat.pojo.dto.DishDto;
import com.etlpat.service.DishFlavorService;
import com.etlpat.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// 后台管理端 -- 菜品表 + 菜品口味表
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;


    // 添加菜品/菜品口味
    @Transactional// 开启事务（为了保证多表操作的原子性/一致性）
    @PostMapping
    public R save(@RequestBody DishDto dishDto) {
        // 添加菜品
        dishService.save(dishDto);
        Long dishId = dishDto.getId();// 获取主键回显

        // 添加菜品口味
        for (DishFlavor flavor : dishDto.getFlavors()) {
            flavor.setDishId(dishId);
            dishFlavorService.save(flavor);
        }
        return R.success();
    }


    // 分页查询菜品信息
    @GetMapping("/page")
    public R<Page<DishDto>> getPageByName(Integer page, Integer pageSize, String name) {
        Page<DishDto> pageByName = dishService.getPageByName(page, pageSize, name);
        return R.success(pageByName);
    }


    // 根据id获取菜品/菜品口味
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id) {
        DishDto dishDto = new DishDto();
        Dish dish = dishService.getById(id);
        // 对象拷贝（将dish中数据拷贝到dishDto中）
        BeanUtils.copyProperties(dish, dishDto);

        // 获取菜品口味列表
        List<DishFlavor> flavors = dishFlavorService.getAllByDishId(dishDto.getId());
        dishDto.setFlavors(flavors);
        return R.success(dishDto);
    }


    // 修改菜品/菜品口味
    @Transactional// 开启事务（为了保证多表操作的原子性/一致性）
    @PutMapping
    public R update(@RequestBody DishDto dishDto) {
        // (1)删除全部旧的菜品口味
        Long dishId = dishDto.getId();
        dishFlavorService.deleteByDishId(dishId);
        // (2)添加全部新的菜品口味
        for (DishFlavor flavor : dishDto.getFlavors()) {
            flavor.setDishId(dishId);
            dishFlavorService.save(flavor);
        }
        // (3)根据id更新菜品
        dishService.updateById(dishDto);
        return R.success();
    }


    // 根据条件查询菜品列表
    @GetMapping("/list")
    public R<List<Dish>> getList(Dish dish) {
        List<Dish> list = dishService.getList(dish);
        return R.success(list);
    }
}
