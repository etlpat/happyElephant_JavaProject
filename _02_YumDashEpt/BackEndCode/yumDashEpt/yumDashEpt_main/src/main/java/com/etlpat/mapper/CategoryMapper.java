package com.etlpat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.etlpat.pojo.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author lenovo
 * @description 针对表【category(菜品及套餐分类)】的数据库操作Mapper
 * @createDate 2025-06-17 20:37:26
 * @Entity com.etlpat.pojo.Category
 */
public interface CategoryMapper extends BaseMapper<Category> {

}




