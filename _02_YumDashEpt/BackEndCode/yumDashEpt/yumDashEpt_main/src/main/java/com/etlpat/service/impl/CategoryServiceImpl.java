package com.etlpat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.common.exception.MyException;
import com.etlpat.mapper.DishMapper;
import com.etlpat.mapper.SetmealMapper;
import com.etlpat.pojo.Category;
import com.etlpat.service.CategoryService;
import com.etlpat.mapper.CategoryMapper;
import com.etlpat.utils.PageQueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;


    // 分页查询分类信息
    @Override
    public Page<Category> getPage(Integer page, Integer pageSize) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<Category>()
                .orderByAsc(Category::getSort);// 根据sort字段升序排序

        return PageQueryUtil.getPage(page, pageSize, wrapper, categoryMapper);
    }


    // 根据id进行删除
    @Override
    public void remove(Long id) {
        // 查询当前分类是否关联了菜品,如果已经关联,抛出一个业务异常
        int dishCount = dishMapper.countByCategoryId(id);
        if (dishCount > 0) {
            throw new MyException("当前分类关联了菜品，不能删除！");
        }

        // 查询当前分类是否关联了套餐,如果已经关联,抛出一个业务异常
        int setmealCount = setmealMapper.countByCategoryId(id);
        if (setmealCount > 0) {
            throw new MyException("当前分类关联了套餐，不能删除！");
        }

        // 正常删除分类
        categoryMapper.deleteById(id);
    }


    @Override
    public List<Category> getByType(Integer type) {
        return categoryMapper.selectAllByType(type);
    }

}




