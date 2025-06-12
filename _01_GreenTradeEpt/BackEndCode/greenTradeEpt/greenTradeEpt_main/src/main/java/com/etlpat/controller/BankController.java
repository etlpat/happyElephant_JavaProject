package com.etlpat.controller;

import com.etlpat.pojo.Bank;
import com.etlpat.pojo.Result;
import com.etlpat.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


// 银行表
@RestController
@RequestMapping("/bank")
public class BankController {
    @Autowired
    private BankService bankService;

    @GetMapping("/getBankList")
    public Result<List<Bank>> getBankList() {
        return Result.success(bankService.getBankList());
    }

    @GetMapping("/getBank")
    public Result<Bank> getBank(Integer id) {
        return Result.success(bankService.getById(id));
    }
}
