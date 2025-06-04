package com.etlpat;

import com.etlpat.mapper.UserMapper;
import com.etlpat.pojo.Order;
import com.etlpat.pojo.PageBean;
import com.etlpat.service.OrderService;
import com.etlpat.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GreenTradeEptMainApplicationTests {
    @Autowired
    OrderService orderService;

    @Test
    void test01() {
        PageBean<Order> orders = orderService.getPageOrdersByKeyword(1, 10, "果");
        System.out.println(orders.getPageNum());
        System.out.println(orders.getPageSize());
        System.out.println(orders.getTotal());
        orders.getItems().forEach(System.out::println);
    }

}
