package com.etlpat.pojo.dto;

import com.etlpat.pojo.OrderDetail;
import com.etlpat.pojo.Orders;
import lombok.Data;

import java.util.List;


@Data
public class OrdersDto extends Orders {

    // 订单详情列表
    private List<OrderDetail> orderDetails;
}

