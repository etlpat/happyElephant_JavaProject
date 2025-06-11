package com.etlpat.controller;

import com.etlpat.pojo.PageBean;
import com.etlpat.pojo.Result;
import com.etlpat.pojo.Shoppingcart;
import com.etlpat.service.ShoppingcartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


// 购物车表
@RestController
@RequestMapping("/shoppingcart")
public class ShoppingcartController {
    @Autowired
    private ShoppingcartService shoppingcartService;


    // 分页查询购物车项（关联对应的订单对象）
    // 参数ownName：当前登录用户的用户名 -- 最终查询当前登录用户的购物车信息
    @GetMapping("/getPage/{pageNum}")
    public Result<PageBean<Shoppingcart>> getCartPageWithOrder(@PathVariable Integer pageNum, Integer pageSize, String ownName) {
        if (pageNum == null || pageSize == null || pageNum < 1 || pageSize < 1) {
            pageNum = 1;
            pageSize = 10;
        }
        PageBean<Shoppingcart> pageBean = shoppingcartService.getCartPageWithOrder(pageNum, pageSize, ownName);
        return Result.success(pageBean);
    }


    // 添加购物车项
    @PostMapping("/save")
    public Result save(Shoppingcart shoppingcart) {
        shoppingcart.setCreateTime(new Date());
        shoppingcart.setUpdateTime(new Date());
        shoppingcartService.save(shoppingcart);
        return Result.success();
    }

    // 删除购物车项
    @GetMapping("/remove")
    public Result remove(Integer id) {
        shoppingcartService.removeById(id);
        return Result.success();
    }


    // 更新商品数量（使得原本的count值加上changeCount）
    @GetMapping("/updateCount")
    public Result updateCount(Integer changeCount, Integer id) {
        Integer count = shoppingcartService.getById(id).getCount();
        count += changeCount;// 获取修改后的count

        // 假如数量归零，删除该项
        if (count <= 0) {
            remove(id);
        }
        shoppingcartService.updateCount(count, id);
        return Result.success();
    }
}
