package com.etlpat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.ShoppingCart;
import com.etlpat.service.ShoppingCartService;
import com.etlpat.mapper.ShoppingCartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author lenovo
 * @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
 * @createDate 2025-06-20 19:25:30
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
        implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;


    // 根据条件查询购物车列表
    @Override
    public List<ShoppingCart> getList(ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<ShoppingCart>()
                .eq(shoppingCart.getUserId() != null, ShoppingCart::getUserId, shoppingCart.getUserId())
                .orderByDesc(ShoppingCart::getCreateTime);
        return shoppingCartMapper.selectList(wrapper);
    }


    @Override
    public int countByUserIdAndDishIdAndSetmealId(ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, shoppingCart.getUserId())
                .eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId())
                .eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        return Math.toIntExact(shoppingCartMapper.selectCount(wrapper));
    }


    @Override
    public int setNumberByUserIdAndDishIdAndSetmealId(ShoppingCart shoppingCart) {
        LambdaUpdateWrapper<ShoppingCart> wrapper = new LambdaUpdateWrapper<ShoppingCart>()
                .set(ShoppingCart::getNumber, shoppingCart.getNumber())
                .eq(ShoppingCart::getUserId, shoppingCart.getUserId())
                .eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId())
                .eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        return shoppingCartMapper.update(null, wrapper);
    }


    @Override
    public int deleteByUserIdAndDishIdAndSetmealId(ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, shoppingCart.getUserId())
                .eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId())
                .eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        return shoppingCartMapper.delete(wrapper);
    }


    @Override
    public int getNumberByUserIdAndDishIdAndSetmealId(ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<ShoppingCart>()
                .select(ShoppingCart::getNumber)
                .eq(ShoppingCart::getUserId, shoppingCart.getUserId())
                .eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId())
                .eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        return shoppingCartMapper.selectOne(wrapper).getNumber();
    }


    @Override
    public int deleteByUserId(Long userId) {
        return shoppingCartMapper.deleteByUserId(userId);
    }


    @Override
    public BigDecimal selectTotalAmountByUserId(Long userId) {
        return shoppingCartMapper.selectTotalAmountByUserId(userId);
    }


    @Override
    public List<ShoppingCart> selectAllByUserId(Long userId) {
        return shoppingCartMapper.selectAllByUserId(userId);
    }


}




