package com.etlpat.controller;

import com.etlpat.pojo.Reserve;
import com.etlpat.pojo.Result;
import com.etlpat.service.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


// 预约表
@RestController
@RequestMapping("/reserve")
public class ReserveController {
    @Autowired
    private ReserveService reserveService;


    @PostMapping("/save")
    public Result save(Reserve reserve) {
        reserveService.save(reserve);
        return Result.success();
    }
}
