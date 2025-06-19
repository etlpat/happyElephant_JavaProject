package com.etlpat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etlpat.pojo.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;
import com.etlpat.pojo.dto.SetmealDto;

/**
 * @author lenovo
 * @description 针对表【setmeal(套餐)】的数据库操作Service
 * @createDate 2025-06-18 10:35:01
 */
public interface SetmealService extends IService<Setmeal> {
    Page<SetmealDto> getPageByName(Integer page, Integer pageSize, String name);

    int deleteById(Long id);

    int updateStatusById(Integer status, Long id);
}
