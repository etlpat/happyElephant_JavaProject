package com.etlpat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etlpat.pojo.Category;
import com.etlpat.pojo.R;
import com.etlpat.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// 后台管理端 -- 分类表
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    // 添加分类信息
    @PostMapping
    public R save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success();
    }


    // 分页查询分类信息
    @GetMapping("/page")
    public R<Page<Category>> getPage(Integer page, Integer pageSize) {
        Page<Category> categoryPage = categoryService.getPage(page, pageSize);
        return R.success(categoryPage);
    }


    // 更新分类信息
    @PutMapping
    public R updateById(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success();
    }


    // 删除分类信息
    @DeleteMapping
    public R removeById(Long ids) {
        categoryService.remove(ids);
        return R.success();
    }


    // 根据type获取分类信息
    @GetMapping("/list")
    public R<List<Category>> getByType(Integer type) {
        List<Category> byType = categoryService.getByType(type);
        return R.success(byType);
    }

}
