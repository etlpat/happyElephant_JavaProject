package com.etlpat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etlpat.common.exception.MyException;
import com.etlpat.pojo.*;
import com.etlpat.pojo.dto.SetmealDto;
import com.etlpat.service.SetmealDishService;
import com.etlpat.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// 后台管理端 -- 套餐表 + 套餐菜品关系表
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;


    // 添加套餐/套餐菜品关系
    @Transactional
    @PostMapping
    public R save(@RequestBody SetmealDto setmealDto) {
        // 添加套餐
        setmealService.save(setmealDto);
        Long setmealId = setmealDto.getId();// 获取主键回显

        // 添加套餐菜品关系
        for (SetmealDish setmealDish : setmealDto.getSetmealDishes()) {
            setmealDish.setSetmealId(setmealId);
            setmealDishService.save(setmealDish);
        }
        return R.success();
    }


    // 分页查询套餐信息
    @GetMapping("/page")
    public R<Page<SetmealDto>> getPageByName(Integer page, Integer pageSize, String name) {
        Page<SetmealDto> pageByName = setmealService.getPageByName(page, pageSize, name);
        return R.success(pageByName);
    }


    // 根据id获取套餐/套餐菜品关系
    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id) {
        SetmealDto setmealDto = new SetmealDto();
        Setmeal setmeal = setmealService.getById(id);
        // 对象拷贝
        BeanUtils.copyProperties(setmeal, setmealDto);

        // 获取套餐菜品关系列表
        List<SetmealDish> setmealDishList = setmealDishService.getAllBySetmealId(setmealDto.getId());
        setmealDto.setSetmealDishes(setmealDishList);
        return R.success(setmealDto);
    }


    // 修改套餐/套餐菜品关系
    @Transactional
    @PutMapping
    public R update(@RequestBody SetmealDto setmealDto) {
        // (1)删除全部旧的套餐菜品关系
        Long setmealId = setmealDto.getId();
        setmealDishService.deleteBySetmealId(setmealId);
        // (2)添加全部新的套餐菜品关系
        for (SetmealDish setmealDish : setmealDto.getSetmealDishes()) {
            setmealDish.setSetmealId(setmealId);
            setmealDishService.save(setmealDish);
        }
        // (3)根据id更新套餐
        setmealService.updateById(setmealDto);
        return R.success();
    }


    // 删除套餐
    @Transactional
    @DeleteMapping
    public R remove(@RequestParam List<Long> ids) {
        for (Long id : ids) {
            if (setmealService.deleteById(id) == 0) {// 删除失败
                throw new MyException("只能删除停售的套餐");// 抛出异常，全部回滚（事务）
            }
        }
        return R.success();
    }


    // 更新套餐状态
    @Transactional
    @PostMapping("/status/{status}")
    public R updateStatus(@PathVariable Integer status, @RequestParam List<Long> ids) {
        for (Long id : ids) {
            setmealService.updateStatusById(status, id);
        }
        return R.success();
    }


    // 根据条件查询套餐列表
    @GetMapping("/list")
    public R<List<Setmeal>> getList(Setmeal setmeal) {
        List<Setmeal> serviceList = setmealService.getList(setmeal);
        return R.success(serviceList);
    }

}
