package com.etlpat.mapper;

import org.apache.ibatis.annotations.Param;

import com.etlpat.pojo.Setmeal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author lenovo
 * @description 针对表【setmeal(套餐)】的数据库操作Mapper
 * @createDate 2025-06-18 10:35:01
 * @Entity com.etlpat.pojo.Setmeal
 */
public interface SetmealMapper extends BaseMapper<Setmeal> {
    int countByCategoryId(@Param("categoryId") Long categoryId);
}




