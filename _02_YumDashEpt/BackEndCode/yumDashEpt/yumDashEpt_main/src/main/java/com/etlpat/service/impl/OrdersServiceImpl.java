package com.etlpat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.mapper.OrderDetailMapper;
import com.etlpat.pojo.OrderDetail;
import com.etlpat.pojo.Orders;
import com.etlpat.pojo.dto.OrdersDto;
import com.etlpat.service.OrdersService;
import com.etlpat.mapper.OrdersMapper;
import com.etlpat.utils.PageQueryUtil;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * @author lenovo
 * @description 针对表【orders(订单表)】的数据库操作Service实现
 * @createDate 2025-06-21 14:15:50
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
        implements OrdersService {
    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    OrderDetailMapper orderDetailMapper;


    // 根据订单号和时间和用户id分页查询
    @Override
    public Page<OrdersDto> getPageByNumberAndTimeAndUserId(Integer page, Integer pageSize
            , String number, LocalDateTime beginTime, LocalDateTime endTime, Long userId) {
        // (1)获取Orders分页数据
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<Orders>()
                .eq(userId != null, Orders::getUserId, userId)
                .like(!StringUtils.isNullOrEmpty(number), Orders::getNumber, number)
                .ge(beginTime != null, Orders::getOrderTime, beginTime)// orderTime >= beginTime
                .le(endTime != null, Orders::getOrderTime, endTime)// orderTime <= endTime
                .orderByDesc(Orders::getOrderTime);
        Page<Orders> ordersPage = PageQueryUtil.getPage(page, pageSize, wrapper, ordersMapper);

        // (2)将Orders分页数据转化为OrdersDto分页数据
        Page<OrdersDto> ordersDtoPage = new Page<>();
        List<OrdersDto> ordersDtos = new ArrayList<>();
        BeanUtils.copyProperties(ordersPage, ordersDtoPage, "records");
        ordersDtoPage.setRecords(ordersDtos);

        // 遍历Orders分页列表，将Orders转换为OrdersDto，添加到OrdersDto分页列表中
        for (Orders orders : ordersPage.getRecords()) {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(orders, ordersDto);
            List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(ordersDto.getId());
            ordersDto.setOrderDetails(orderDetails);
            ordersDtos.add(ordersDto);
        }
        return ordersDtoPage;
    }

}




