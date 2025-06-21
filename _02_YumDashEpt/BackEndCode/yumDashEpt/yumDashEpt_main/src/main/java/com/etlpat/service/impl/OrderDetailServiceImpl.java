package com.etlpat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.OrderDetail;
import com.etlpat.service.OrderDetailService;
import com.etlpat.mapper.OrderDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lenovo
 * @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
 * @createDate 2025-06-21 14:16:09
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
        implements OrderDetailService {
    @Autowired
    private OrderDetailMapper orderDetailMapper;


    @Override
    public List<OrderDetail> getByOrderId(Long orderId) {
        return orderDetailMapper.getByOrderId(orderId);
    }

    @Override
    public int deleteByOrderId(Long orderId) {
        return orderDetailMapper.deleteByOrderId(orderId);
    }
}




