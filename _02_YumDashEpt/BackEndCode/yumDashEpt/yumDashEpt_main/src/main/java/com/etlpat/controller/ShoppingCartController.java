package com.etlpat.controller;

import com.etlpat.pojo.R;
import com.etlpat.pojo.ShoppingCart;
import com.etlpat.service.ShoppingCartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


// 前台客户端 -- 购物车表
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;


    // 根据条件查询购物车列表
    @GetMapping("/list")
    public R<List<ShoppingCart>> getList(ShoppingCart shoppingCart, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("user");// 获取当前登录用户id
        shoppingCart.setUserId(userId);
        List<ShoppingCart> list = shoppingCartService.getList(shoppingCart);
        return R.success(list);
    }


    // 添加购物车项
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("user");// 获取当前登录用户id
        shoppingCart.setUserId(userId);

        // (1)若该购物车项已经存在，则让其菜品/套餐的数量+1
        int count = shoppingCartService.countByUserIdAndDishIdAndSetmealId(shoppingCart);
        if (count > 0) {
            int number = shoppingCartService.getNumberByUserIdAndDishIdAndSetmealId(shoppingCart);
            shoppingCart.setNumber(number + 1);
            shoppingCartService.setNumberByUserIdAndDishIdAndSetmealId(shoppingCart);
            return R.success(shoppingCart);
        }

        // (2)若该购物车项不存在，则将该项添加到购物车表中
        shoppingCart.setNumber(1);
        shoppingCart.setCreateTime(LocalDateTime.now());
        shoppingCartService.save(shoppingCart);
        return R.success(shoppingCart);
    }


    // 减少购物车项
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("user");// 获取当前登录用户id
        shoppingCart.setUserId(userId);

        // (1)若当前菜品/套餐的数量>1，则使其-1
        int number = shoppingCartService.getNumberByUserIdAndDishIdAndSetmealId(shoppingCart);
        if (number > 1) {
            shoppingCart.setNumber(number - 1);
            shoppingCartService.setNumberByUserIdAndDishIdAndSetmealId(shoppingCart);
            return R.success(shoppingCart);
        }

        // (2)若当前菜品/套餐的数量<=1，则移除该购物车项
        shoppingCartService.deleteByUserIdAndDishIdAndSetmealId(shoppingCart);
        shoppingCart.setNumber(0);
        return R.success(shoppingCart);// 配合前端逻辑，返回num为0的原数据
    }


    // 清空全部购物车项
    @DeleteMapping("/clean")
    public R clear(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("user");// 获取当前登录用户id
        shoppingCartService.deleteByUserId(userId);
        return R.success();
    }

}
