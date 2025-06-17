package com.etlpat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.Category;
import com.etlpat.service.CategoryService;
import com.etlpat.mapper.CategoryMapper;
import com.etlpat.utils.PageQueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lenovo
 * @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
 * @createDate 2025-06-17 20:37:26
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;


    // 分页查询分类信息
    @Override
    public Page<Category> getPage(Integer page, Integer pageSize) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<Category>()
                .orderByAsc(Category::getSort);// 根据sort字段升序排序

        return PageQueryUtil.getPage(page, pageSize, wrapper, categoryMapper);
    }
}




