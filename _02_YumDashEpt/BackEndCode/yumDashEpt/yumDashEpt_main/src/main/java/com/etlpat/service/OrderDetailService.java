package com.etlpat.service;

import com.etlpat.pojo.OrderDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author lenovo
 * @description 针对表【order_detail(订单明细表)】的数据库操作Service
 * @createDate 2025-06-21 14:16:09
 */
public interface OrderDetailService extends IService<OrderDetail> {
    List<OrderDetail> getByOrderId(Long orderId);

    int deleteByOrderId(Long orderId);
}
