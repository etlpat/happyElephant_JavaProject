package com.etlpat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.SeckillVoucher;
import com.etlpat.service.SeckillVoucherService;
import com.etlpat.mapper.SeckillVoucherMapper;
import org.springframework.stereotype.Service;


/**
 * @author lenovo
 * @description 针对表【tb_seckill_voucher(秒杀优惠券表，与优惠券是一对一关系)】的数据库操作Service实现
 * @createDate 2025-08-04 11:29:25
 */
@Service
public class SeckillVoucherServiceImpl extends ServiceImpl<SeckillVoucherMapper, SeckillVoucher>
        implements SeckillVoucherService {

}




