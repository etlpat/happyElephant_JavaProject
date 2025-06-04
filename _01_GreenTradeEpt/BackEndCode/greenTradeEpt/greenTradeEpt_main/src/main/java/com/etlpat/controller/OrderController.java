package com.etlpat.controller;

import com.etlpat.pojo.Order;
import com.etlpat.pojo.PageBean;
import com.etlpat.pojo.Result;
import com.etlpat.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 订单表
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;


    // 根据关键词，获取分页数据
    @GetMapping("/getPage/{pageNum}")
    public Result<PageBean<Order>> getPageOrdersByKeyword(@PathVariable Integer pageNum, Integer pageSize, String keyword) {
        if (pageNum == null || pageSize == null || pageNum < 1 || pageSize < 1) {
            pageNum = 1;
            pageSize = 10;
        }
        PageBean<Order> orders = orderService.getPageOrdersByKeyword(pageNum, pageSize, keyword);
        return Result.success(orders);
    }


    // 获取全部评论
    @GetMapping("/getContent")
    public Result<List<String>> getContent() {
        return Result.success(orderService.getAllContent());
    }
}
