package com.etlpat.controller;

import com.etlpat.dto.Result;
import com.etlpat.service.VoucherOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/voucher-order")
public class VoucherOrderController {

    @Autowired
    VoucherOrderService voucherOrderService;


    // 实现秒杀下单（购买秒杀优惠券，生成订单）
    @PostMapping("seckill/{id}")
    public Result seckillVoucher(@PathVariable("id") Long voucherId) {
//        return voucherOrderService.seckillVoucher(voucherId);// 秒杀下单
        return voucherOrderService.seckillVoucher_plus(voucherId);// 秒杀下单（异步优化）
    }
}
