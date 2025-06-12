package com.etlpat.controller;

import com.etlpat.pojo.Purchase;
import com.etlpat.pojo.Result;
import com.etlpat.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


// 购物清单表
@RestController
@RequestMapping("/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;


    //添加数据
    @PostMapping("/save")
    public Result save(Purchase purchase) {
        purchase.setCreateTime(new Date());
        purchase.setUpdateTime(new Date());
        purchaseService.save(purchase);
        return Result.success();
    }
}
