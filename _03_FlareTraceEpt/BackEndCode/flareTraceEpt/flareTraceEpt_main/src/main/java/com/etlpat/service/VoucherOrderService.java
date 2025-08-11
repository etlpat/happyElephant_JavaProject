package com.etlpat.service;

import com.etlpat.dto.Result;
import com.etlpat.pojo.VoucherOrder;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * @author lenovo
 * @description 针对表【tb_voucher_order】的数据库操作Service
 * @createDate 2025-08-04 11:30:20
 */
public interface VoucherOrderService extends IService<VoucherOrder> {

    Result seckillVoucher(Long voucherId);

    Result createVoucherOrder(long userId, long voucherId, int stock);

    Result seckillVoucher_plus(Long voucherId);

    void createVoucherOrder_plus(VoucherOrder voucherOrder);

}
