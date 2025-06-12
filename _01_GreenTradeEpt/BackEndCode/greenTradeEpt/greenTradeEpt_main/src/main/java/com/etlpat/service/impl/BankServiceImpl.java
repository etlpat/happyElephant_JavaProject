package com.etlpat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.Bank;
import com.etlpat.service.BankService;
import com.etlpat.mapper.BankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lenovo
 * @description 针对表【tb_bank】的数据库操作Service实现
 * @createDate 2025-06-12 10:15:36
 */
@Service
public class BankServiceImpl extends ServiceImpl<BankMapper, Bank>
        implements BankService {
    @Autowired
    private BankMapper bankMapper;

    @Override
    public List<Bank> getBankList() {
        return bankMapper.selectList(null);
    }
}




