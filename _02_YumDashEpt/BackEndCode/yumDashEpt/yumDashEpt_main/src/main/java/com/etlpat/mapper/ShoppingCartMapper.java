package com.etlpat.mapper;

import org.apache.ibatis.annotations.Param;

import com.etlpat.pojo.ShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author lenovo
 * @description 针对表【shopping_cart(购物车)】的数据库操作Mapper
 * @createDate 2025-06-20 19:25:30
 * @Entity com.etlpat.pojo.ShoppingCart
 */
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
    int deleteByUserId(@Param("userId") Long userId);
}




