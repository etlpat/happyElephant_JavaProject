package com.etlpat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.mapper.CategoryMapper;
import com.etlpat.mapper.DishMapper;
import com.etlpat.mapper.SetmealDishMapper;
import com.etlpat.pojo.Category;
import com.etlpat.pojo.Dish;
import com.etlpat.pojo.Setmeal;
import com.etlpat.pojo.SetmealDish;
import com.etlpat.pojo.dto.DishDto;
import com.etlpat.pojo.dto.SetmealDto;
import com.etlpat.service.SetmealService;
import com.etlpat.mapper.SetmealMapper;
import com.etlpat.utils.PageQueryUtil;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lenovo
 * @description 针对表【setmeal(套餐)】的数据库操作Service实现
 * @createDate 2025-06-18 10:35:01
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
        implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;


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


    // 根据条件查询套餐列表
    @Override
    public List<Setmeal> getList(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<Setmeal>()
                .eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId())
                .like(!StringUtils.isNullOrEmpty(setmeal.getName()), Setmeal::getName, setmeal.getName())
                .eq(Setmeal::getStatus, 1)// 只查询状态为1的套餐（起售状态）
                .eq(Setmeal::getIsDeleted, 0)
                .orderByDesc(Setmeal::getUpdateTime);
        return setmealMapper.selectList(wrapper);
    }


    // 获取套餐中的全部菜品
    @Override
    public List<DishDto> getDishListBySetmealId(Long id) {
        List<SetmealDish> setmealDishList = setmealDishMapper.getAllBySetmealId(id);
        List<DishDto> dishDtos = new ArrayList<>();

        // 遍历setmealDish列表，获取每个关系对应的dish菜品，将其转换为dishDto添加到DishDto列表中
        for (SetmealDish setmealDish : setmealDishList) {
            DishDto dishDto = new DishDto();
            Dish dish = dishMapper.selectById(setmealDish.getDishId());
            BeanUtils.copyProperties(dish, dishDto);// 对象拷贝 dish->dishDto
            dishDto.setCopies(setmealDish.getCopies());// 设置份数
            dishDtos.add(dishDto);
        }
        return dishDtos;
    }

}




