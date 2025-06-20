package com.etlpat.service;

import com.etlpat.pojo.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author lenovo
 * @description 针对表【shopping_cart(购物车)】的数据库操作Service
 * @createDate 2025-06-20 19:25:30
 */
public interface ShoppingCartService extends IService<ShoppingCart> {
    List<ShoppingCart> getList(ShoppingCart shoppingCart);

    int countByUserIdAndDishIdAndSetmealId(ShoppingCart shoppingCart);

    int setNumberByUserIdAndDishIdAndSetmealId(ShoppingCart shoppingCart);

    int deleteByUserIdAndDishIdAndSetmealId(ShoppingCart shoppingCart);

    int getNumberByUserIdAndDishIdAndSetmealId(ShoppingCart shoppingCart);

    int deleteByUserId(Long userId);
}
