package com.etlpat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.mapper.CategoryMapper;
import com.etlpat.mapper.DishFlavorMapper;
import com.etlpat.pojo.Category;
import com.etlpat.pojo.Dish;
import com.etlpat.pojo.DishFlavor;
import com.etlpat.pojo.dto.DishDto;
import com.etlpat.service.DishService;
import com.etlpat.mapper.DishMapper;
import com.etlpat.utils.PageQueryUtil;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author lenovo
 * @description 针对表【dish(菜品管理)】的数据库操作Service实现
 * @createDate 2025-06-18 10:34:47
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
        implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;


    // 根据name分页查询
    @Override
    public Page<DishDto> getPageByName(Integer page, Integer pageSize, String name) {
        // (1)获取dish分页数据
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<Dish>()
                .like(!StringUtils.isNullOrEmpty(name), Dish::getName, name)
                .eq(Dish::getIsDeleted, 0)
                .orderByAsc(Dish::getSort)
                .orderByDesc(Dish::getUpdateTime);
        Page<Dish> dishPage = PageQueryUtil.getPage(page, pageSize, wrapper, dishMapper);

        // (2)将dish分页数据转化为dishDto分页数据
        Page<DishDto> dishDtoPage = new Page<>();
        // 对象拷贝（将dishPage的属性拷贝到dishDtoPage中，除了records属性）
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");

        // 构造dishDto列表
        ArrayList<DishDto> dtoList = new ArrayList<>();
        dishDtoPage.setRecords(dtoList);
        DishDto tmp;

        // 遍历dish分页列表，将dish转换为dishDto，添加到dishDto分页列表中
        for (Dish dish : dishPage.getRecords()) {
            tmp = new DishDto();
            BeanUtils.copyProperties(dish, tmp);// 对象拷贝

            // 获取菜品对应的分类对象，若分类不为空，则将分类名添加到dishDto对象中
            Category category = categoryMapper.selectById(tmp.getCategoryId());
            if (category != null) {
                tmp.setCategoryName(category.getName());
            }
            dtoList.add(tmp);
        }
        return dishDtoPage;
    }


    // 根据条件查询菜品列表
    @Override
    public List<DishDto> getList(Dish dish) {
        // (1)获取Dish列表
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<Dish>()
                .eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId())
                .like(!StringUtils.isNullOrEmpty(dish.getName()), Dish::getName, dish.getName())
                .eq(Dish::getStatus, 1)// 只查询状态为1的菜品（起售状态）
                .eq(Dish::getIsDeleted, 0)
                .orderByAsc(Dish::getSort)
                .orderByDesc(Dish::getUpdateTime);
        List<Dish> dishList = dishMapper.selectList(wrapper);

        // (2)将Dish列表转化为DishDto列表
        List<DishDto> dishDtoList = new ArrayList<>();
        for (Dish d : dishList) {
            DishDto dto = new DishDto();
            BeanUtils.copyProperties(d, dto);
            List<DishFlavor> flavors = dishFlavorMapper.getAllByDishId(dto.getId());
            dto.setFlavors(flavors);
            dishDtoList.add(dto);
        }
        return dishDtoList;
    }


    // 根据id删除（不能删除起售中的数据）
    @Override
    public int deleteById(Long id) {
        LambdaUpdateWrapper<Dish> wrapper = new LambdaUpdateWrapper<Dish>()
                .set(Dish::getIsDeleted, 1)
                .eq(Dish::getId, id)
                .eq(Dish::getStatus, 0);
        return dishMapper.update(null, wrapper);
    }


    @Override
    public int updateStatusById(Integer status, Long id) {
        return dishMapper.updateStatusById(status, id);
    }

}




