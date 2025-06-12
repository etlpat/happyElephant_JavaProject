package com.etlpat.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etlpat.pojo.Shoppingcart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lenovo
 * @description 针对表【tb_shoppingcart】的数据库操作Mapper
 * @createDate 2025-06-11 16:05:04
 * @Entity com.etlpat.pojo.Shoppingcart
 */
public interface ShoppingcartMapper extends BaseMapper<Shoppingcart> {
    /**
     * 分页查询购物车及关联订单
     *
     * @param page    分页参数
     * @param ownName 用户名
     * @return 分页结果
     */
    Page<Shoppingcart> selectPageWithOrder(Page<Shoppingcart> page, @Param("ownName") String ownName);

    Shoppingcart selectOneByOrderIdAndOwnName(@Param("orderId") Integer orderId, @Param("ownName") String ownName);
}