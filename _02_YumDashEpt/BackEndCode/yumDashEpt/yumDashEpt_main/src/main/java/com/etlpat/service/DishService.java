package com.etlpat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etlpat.pojo.Dish;
import com.baomidou.mybatisplus.extension.service.IService;
import com.etlpat.pojo.dto.DishDto;

import java.util.List;

/**
 * @author lenovo
 * @description 针对表【dish(菜品管理)】的数据库操作Service
 * @createDate 2025-06-18 10:34:47
 */
public interface DishService extends IService<Dish> {
    Page<DishDto> getPageByName(Integer page, Integer pageSize, String name);

    List<Dish> getList(Dish dish);

    int deleteById(Long id);

    int updateStatusById(Integer status, Long id);
}
