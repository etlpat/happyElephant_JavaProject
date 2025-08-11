package com.etlpat.mapper;

import org.apache.ibatis.annotations.Param;

import com.etlpat.pojo.SeckillVoucher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


/**
 * @author lenovo
 * @description 针对表【tb_seckill_voucher(秒杀优惠券表，与优惠券是一对一关系)】的数据库操作Mapper
 * @createDate 2025-08-04 11:29:25
 * @Entity com.etlpat.pojo.SeckillVoucher
 */
public interface SeckillVoucherMapper extends BaseMapper<SeckillVoucher> {
    int updateStockByVoucherIdAndStock(@Param("stock") Integer stock, @Param("voucherId") Long voucherId, @Param("oldStock") Integer oldStock);

    // 根据voucherId，使库存stock减一
    int deductStock(@Param("voucherId") Long voucherId);
}




