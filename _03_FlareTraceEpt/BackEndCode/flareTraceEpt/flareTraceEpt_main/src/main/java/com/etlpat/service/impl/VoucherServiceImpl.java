package com.etlpat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.dto.Result;
import com.etlpat.pojo.SeckillVoucher;
import com.etlpat.pojo.Voucher;
import com.etlpat.service.SeckillVoucherService;
import com.etlpat.service.VoucherService;
import com.etlpat.mapper.VoucherMapper;
import com.etlpat.utils.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author lenovo
 * @description 针对表【tb_voucher】的数据库操作Service实现
 * @createDate 2025-08-04 11:30:15
 */
@Service
public class VoucherServiceImpl extends ServiceImpl<VoucherMapper, Voucher>
        implements VoucherService {

    @Autowired
    private SeckillVoucherService seckillVoucherService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public Result queryVoucherOfShop(Long shopId) {
        // 查询优惠券信息
        List<Voucher> vouchers = getBaseMapper().queryVoucherOfShop(shopId);
        // 返回结果
        return Result.ok(vouchers);
    }


    // 新增秒杀券
    @Override
    @Transactional
    public void addSeckillVoucher(Voucher voucher) {
        // 保存优惠券到数据库
        save(voucher);
        // 保存秒杀信息到数据库
        SeckillVoucher seckillVoucher = new SeckillVoucher();
        seckillVoucher.setVoucherId(voucher.getId());
        seckillVoucher.setStock(voucher.getStock());
        seckillVoucher.setBeginTime(voucher.getBeginTime());
        seckillVoucher.setEndTime(voucher.getEndTime());
        seckillVoucherService.save(seckillVoucher);

        // 保存秒杀券库存到Redis中
        stringRedisTemplate.opsForValue().set(RedisConstants.SECKILL_STOCK_KEY + voucher.getId(), voucher.getStock().toString());
    }

}




