package com.etlpat.service;

import com.etlpat.pojo.PageBean;
import com.etlpat.pojo.Shoppingcart;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * @author lenovo
 * @description 针对表【tb_shoppingcart】的数据库操作Service
 * @createDate 2025-06-11 16:05:04
 */
public interface ShoppingcartService extends IService<Shoppingcart> {
    /**
     * 分页查询购物车及关联订单
     *
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @param ownName  用户名
     * @return 分页结果
     */
    PageBean<Shoppingcart> getCartPageWithOrder(Integer pageNum, Integer pageSize, String ownName);


    public void updateCount(Integer count, Integer id);

    Shoppingcart selectOneByOrderIdAndOwnName(@Param("orderId") Integer orderId, @Param("ownName") String ownName);
}
