package com.etlpat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.mapper.CategoryMapper;
import com.etlpat.pojo.Category;
import com.etlpat.pojo.Setmeal;
import com.etlpat.pojo.dto.SetmealDto;
import com.etlpat.service.SetmealService;
import com.etlpat.mapper.SetmealMapper;
import com.etlpat.utils.PageQueryUtil;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @author lenovo
 * @description 针对表【setmeal(套餐)】的数据库操作Service实现
 * @createDate 2025-06-18 10:35:01
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
        implements SetmealService {
    @Autowired
    SetmealMapper setmealMapper;

    @Autowired
    CategoryMapper categoryMapper;


    // 根据name分页查询
    @Override
    public Page<SetmealDto> getPageByName(Integer page, Integer pageSize, String name) {
        // (1)获取Setmeal分页数据
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<Setmeal>()
                .like(!StringUtils.isNullOrEmpty(name), Setmeal::getName, name)
                .eq(Setmeal::getIsDeleted, 0)
                .orderByDesc(Setmeal::getUpdateTime);
        Page<Setmeal> setmealPage = PageQueryUtil.getPage(page, pageSize, wrapper, setmealMapper);

        // (2)将Setmeal分页数据转化为SetmealDto分页数据
        Page<SetmealDto> setmealDtoPage = new Page<>();
        ArrayList<SetmealDto> dtoList = new ArrayList<>();
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");
        setmealDtoPage.setRecords(dtoList);

        // 遍历Setmeal列表，将Setmeal转化为SetmealDto，添加到SetmealDto列表中
        for (Setmeal setmeal : setmealPage.getRecords()) {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);
            // 获取分类名称，存入SetmealDto中
            Category category = categoryMapper.selectById(setmealDto.getCategoryId());
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            dtoList.add(setmealDto);
        }
        return setmealDtoPage;
    }


    // 根据id删除（不能删除起售中的数据）
    @Override
    public int deleteById(Long id) {
        LambdaUpdateWrapper<Setmeal> wrapper = new LambdaUpdateWrapper<Setmeal>()
                .set(Setmeal::getIsDeleted, 1)
                .eq(Setmeal::getId, id)
                .eq(Setmeal::getStatus, 0);
        return setmealMapper.update(null, wrapper);
    }


    @Override
    public int updateStatusById(Integer status, Long id) {
        return setmealMapper.updateStatusById(status, id);
    }

}




