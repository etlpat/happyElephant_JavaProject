package com.etlpat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etlpat.pojo.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.etlpat.pojo.dto.OrdersDto;

import java.time.LocalDateTime;

/**
 * @author lenovo
 * @description 针对表【orders(订单表)】的数据库操作Service
 * @createDate 2025-06-21 14:15:50
 */
public interface OrdersService extends IService<Orders> {
    Page<OrdersDto> getPageByNumberAndTimeAndUserId(Integer page, Integer pageSize
            , String number, LocalDateTime beginTime, LocalDateTime endTime, Long userId);
}
