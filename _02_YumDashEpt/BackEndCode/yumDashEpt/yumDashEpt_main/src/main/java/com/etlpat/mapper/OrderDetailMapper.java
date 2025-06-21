package com.etlpat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.etlpat.pojo.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author lenovo
 * @description 针对表【order_detail(订单明细表)】的数据库操作Mapper
 * @createDate 2025-06-21 14:16:09
 * @Entity com.etlpat.pojo.OrderDetail
 */
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
    List<OrderDetail> getByOrderId(@Param("orderId") Long orderId);

    int deleteByOrderId(@Param("orderId") Long orderId);
}




