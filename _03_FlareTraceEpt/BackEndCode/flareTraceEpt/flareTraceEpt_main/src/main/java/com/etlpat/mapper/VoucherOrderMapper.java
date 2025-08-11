package com.etlpat.mapper;

import org.apache.ibatis.annotations.Param;

import com.etlpat.pojo.VoucherOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


/**
 * @author lenovo
 * @description 针对表【tb_voucher_order】的数据库操作Mapper
 * @createDate 2025-08-04 11:30:20
 * @Entity com.etlpat.pojo.VoucherOrder
 */
public interface VoucherOrderMapper extends BaseMapper<VoucherOrder> {
    int countByUserIdAndVoucherId(@Param("userId") Long userId, @Param("voucherId") Long voucherId);
}




