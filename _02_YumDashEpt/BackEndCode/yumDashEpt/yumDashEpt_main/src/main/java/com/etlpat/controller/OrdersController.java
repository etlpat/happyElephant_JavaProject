package com.etlpat.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etlpat.common.exception.MyException;
import com.etlpat.pojo.*;
import com.etlpat.pojo.dto.OrdersDto;
import com.etlpat.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


// 前台客户端 -- 订单表 + 订单细节表
@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    ShoppingCartService shoppingCartService;
    @Autowired
    AddressBookService addressBookService;
    @Autowired
    UserService userService;


    // 添加订单和订单细节
    @Transactional
    @PostMapping("/submit")
    public R submitOrder(@RequestBody Orders order, HttpServletRequest request) {
        // (1)提前获取需要使用的数据
        long orderId = IdWorker.getId();
        Long userId = (Long) request.getSession().getAttribute("user");
        User user = userService.getById(userId);
        AddressBook address = addressBookService.getById(order.getAddressBookId());
        List<ShoppingCart> cartItems = shoppingCartService.selectAllByUserId(userId);
        if (user == null || address == null || cartItems == null || cartItems.isEmpty()) {
            throw new MyException("订单数据异常，不能下单");
        }

        // (2)设置订单的所需信息，并保存
        order.setId(orderId);
        order.setNumber(String.valueOf(orderId));
        order.setUserId(userId);
        order.setStatus(2);
        order.setOrderTime(LocalDateTime.now());
        order.setCheckoutTime(LocalDateTime.now());
        order.setAmount(shoppingCartService.selectTotalAmountByUserId(userId));
        order.setPhone(user.getPhone());
        order.setUserName(user.getName());
        order.setConsignee(address.getConsignee());
        order.setAddress((address.getProvinceName() == null ? "" : address.getProvinceName())
                + (address.getCityName() == null ? "" : address.getCityName())
                + (address.getDistrictName() == null ? "" : address.getDistrictName())
                + (address.getDetail() == null ? "" : address.getDetail()));
        ordersService.save(order);

        // (3)设置订单细节的所需信息，并保存
        List<OrderDetail> orderDetails = cartItems.stream()
                .map(cartItem -> {
                    OrderDetail detail = new OrderDetail();
                    detail.setName(cartItem.getName());
                    detail.setImage(cartItem.getImage());
                    detail.setOrderId(order.getId());
                    detail.setDishId(cartItem.getDishId());
                    detail.setSetmealId(cartItem.getSetmealId());
                    detail.setDishFlavor(cartItem.getDishFlavor());
                    detail.setNumber(cartItem.getNumber());
                    detail.setAmount(cartItem.getAmount());
                    return detail;
                })
                .collect(Collectors.toList());
        orderDetailService.saveBatch(orderDetails);

        // (4)清空购物车
        shoppingCartService.deleteByUserId(userId);
        return R.success();
    }


    // 根据订单号和时间分页查询 -- 管理端
    @GetMapping("/page")
    public R<Page<OrdersDto>> getPageByNumberAndTime(Integer page, Integer pageSize, String number
            , @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beginTime
            , @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        Page<OrdersDto> ordersDtoPage = ordersService.getPageByNumberAndTimeAndUserId(
                page, pageSize, number, beginTime, endTime, null);
        return R.success(ordersDtoPage);
    }


    // 根据用户id分页查询 -- 客户端
    @GetMapping("/userPage")
    public R<Page<OrdersDto>> getPageByUserId(Integer page, Integer pageSize, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("user");
        Page<OrdersDto> ordersDtoPage = ordersService.getPageByNumberAndTimeAndUserId(
                page, pageSize, null, null, null, userId);
        return R.success(ordersDtoPage);
    }


    // 修改/删除订单
    @PutMapping
    public R updateAndDelete(@RequestBody Orders order) {
        // (1)若传来的状态>=5，则删除订单和订单细节
        if (order.getStatus() >= 5) {
            orderDetailService.deleteByOrderId(order.getId());
            ordersService.removeById(order.getId());
            return R.success();
        }

        // (2)若传来的状态<5，则更新订单（使状态+1）
        ordersService.updateById(order);
        return R.success();
    }


    // 再来一单
    @PostMapping("/again")
    public R submitOrderAgain(@RequestBody Orders order) {
        // (1)根据id获取订单/订单细节
        Orders orders = ordersService.getById(order.getId());
        List<OrderDetail> orderDetails = orderDetailService.getByOrderId(order.getId());
        long newOrdersId = IdWorker.getId();

        // (2)创建与原本订单相同的新订单，并保存
        orders.setId(newOrdersId);
        orders.setNumber(String.valueOf(newOrdersId));
        orders.setStatus(2);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        ordersService.save(orders);

        // (3)创建与原本订单细节相同的新订单细节，并保存
        for (OrderDetail orderDetail : orderDetails) {
            orderDetail.setId(null);
            orderDetail.setOrderId(newOrdersId);
        }
        orderDetailService.saveBatch(orderDetails);
        return R.success();
    }

}
