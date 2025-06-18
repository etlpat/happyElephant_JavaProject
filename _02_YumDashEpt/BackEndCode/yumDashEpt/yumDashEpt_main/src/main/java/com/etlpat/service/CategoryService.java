package com.etlpat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etlpat.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lenovo
 * @description 针对表【category(菜品及套餐分类)】的数据库操作Service
 * @createDate 2025-06-17 20:37:26
 */
public interface CategoryService extends IService<Category> {
    Page<Category> getPage(Integer page, Integer pageSize);

    void remove(Long id);

    List<Category> getByType(Integer type);
}
