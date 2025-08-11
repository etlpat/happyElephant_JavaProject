package com.etlpat.service;

import com.etlpat.dto.Result;
import com.etlpat.pojo.Voucher;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * @author lenovo
 * @description 针对表【tb_voucher】的数据库操作Service
 * @createDate 2025-08-04 11:30:15
 */
public interface VoucherService extends IService<Voucher> {

    Result queryVoucherOfShop(Long shopId);

    void addSeckillVoucher(Voucher voucher);
}
