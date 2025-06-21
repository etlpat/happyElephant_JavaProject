package com.etlpat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.etlpat.pojo.ShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * @author lenovo
 * @description 针对表【shopping_cart(购物车)】的数据库操作Mapper
 * @createDate 2025-06-20 19:25:30
 * @Entity com.etlpat.pojo.ShoppingCart
 */
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
    int deleteByUserId(@Param("userId") Long userId);

    @Select("SELECT COALESCE(SUM(number * amount), 0) FROM shopping_cart WHERE user_id = #{userId}")
    BigDecimal selectTotalAmountByUserId(@Param("userId") Long userId);

    List<ShoppingCart> selectAllByUserId(@Param("userId") Long userId);
}




