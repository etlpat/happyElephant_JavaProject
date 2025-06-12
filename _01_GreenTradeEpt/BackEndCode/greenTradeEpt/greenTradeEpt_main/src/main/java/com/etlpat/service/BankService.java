package com.etlpat.service;

import com.etlpat.pojo.Bank;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author lenovo
 * @description 针对表【tb_bank】的数据库操作Service
 * @createDate 2025-06-12 10:15:36
 */
public interface BankService extends IService<Bank> {
    public List<Bank> getBankList();
}
