package com.etlpat.mapper;

import com.etlpat.pojo.Voucher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author lenovo
 * @description 针对表【tb_voucher】的数据库操作Mapper
 * @createDate 2025-08-04 11:30:15
 * @Entity com.etlpat.pojo.Voucher
 */
public interface VoucherMapper extends BaseMapper<Voucher> {
    List<Voucher> queryVoucherOfShop(@Param("shopId") Long shopId);
}




