package com.etlpat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.dto.Result;
import com.etlpat.mapper.SeckillVoucherMapper;
import com.etlpat.pojo.SeckillVoucher;
import com.etlpat.pojo.VoucherOrder;
import com.etlpat.service.SeckillVoucherService;
import com.etlpat.service.VoucherOrderService;
import com.etlpat.mapper.VoucherOrderMapper;
import com.etlpat.utils.RedisConstants;
import com.etlpat.utils.RedisGlobalIdGenerator;
import com.etlpat.utils.UserHolder;
import jakarta.annotation.PostConstruct;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author lenovo
 * @description 针对表【tb_voucher_order】的数据库操作Service实现
 * @createDate 2025-08-04 11:30:20
 */
@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder>
        implements VoucherOrderService {

    @Autowired
    private VoucherOrderMapper voucherOrderMapper;

    @Autowired
    private SeckillVoucherService seckillVoucherService;

    @Autowired
    private SeckillVoucherMapper seckillVoucherMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    // 创建Lua脚本对象
    private static final DefaultRedisScript<Long> SECKILL_LUA_SCRIPT;

    // 初始化Lua脚本
    static {
        SECKILL_LUA_SCRIPT = new DefaultRedisScript<>();
        SECKILL_LUA_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));// 配置Lua文件路径
        SECKILL_LUA_SCRIPT.setResultType(Long.class);// 设置返回类型
    }

    // TODO 创建阻塞队列
    //  （当线程尝试从阻塞队列中获取元素时，若队列中没有元素，则线程会被阻塞；直到队列中有元素时，线程才会被唤醒，并获取元素）
    private BlockingQueue<VoucherOrder> orderBlockingQueue = new ArrayBlockingQueue<>(1024 * 1024);// 数组初始长度为1024*1024

    // 创建秒杀订单的线程池
    private static final ExecutorService SECKILL_ORDER_EXECUTOR = Executors.newSingleThreadExecutor();

    // 接收当前类的spring代理对象
    private VoucherOrderService proxy;

    // 定义初始化方法
    @PostConstruct
    private void init() {
        // 使用“秒杀订单”线程池，提交“保存阻塞订单”线程任务。使得该任务在类初始化完毕后就开始执行。
        SECKILL_ORDER_EXECUTOR.submit(new saveBlockOrder());
    }


    // ##############################秒杀下单[优化前]##############################
    // 实现【秒杀下单】
    // 功能：使当前登录的用户购买指定id的秒杀优惠券，并生成订单
    @Override
    public Result seckillVoucher(Long voucherId) {
        Long userId = UserHolder.getUser().getId();
        SeckillVoucher seckillVoucher = seckillVoucherService.getById(voucherId);

        // 1.判断是否在秒杀开始、结束时间内
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(seckillVoucher.getBeginTime())
                || now.isAfter(seckillVoucher.getEndTime())) {
            return Result.fail("当前不是秒杀时间段！");
        }

        // 2.判断库存是否充足
        Integer stock = seckillVoucher.getStock();// TODO 获取库存数量（供后续乐观锁判断是否变动）
        if (stock <= 0) {
            return Result.fail("秒杀券库存不足！");
        }


        // 3.调用创建订单的方法（加锁）
        // TODO 悲观锁，解决一人一单问题（单机锁--如：synchronized or 分布式锁）
        // TODO 对用户id加锁，保证同一个用户在同一时间，只能有一条线程执行一人一单的[先select-后insert]操作
        // (1)创建锁对象【分布式锁】
        String lockKey = "voucherOrderLock:" + userId;// TODO 使用userId作为锁的key，保证了同一个用户只有一个锁，不同用户可以有不同的锁。
//        _1_SimpleRedisLock redisLock = new _1_SimpleRedisLock(lockKey, stringRedisTemplate);// 1.简单的Redis分布式锁
//        _2_LuaRedisLock redisLock = new _2_LuaRedisLock(lockKey, stringRedisTemplate);// 2.使用Lua脚本优化的Redis分布式锁
        RLock redissonLock = redissonClient.getLock(RedisConstants.DISTRIBUTED_LOCK_KEY + lockKey);// 3.Redisson提供的Redis分布式锁（可重入、锁重试、锁续期）

        // (2)尝试争抢锁
//        if (!redisLock.tryLock(RedisConstants.LOCK_VOUCHERORDER_TTL)) {// 若未抢到锁
        if (!redissonLock.tryLock()) {// 若未抢到锁
            return Result.fail("不能重复下单！");
        }

        // (3)若抢到锁，则执行业务逻辑，之后释放锁
        try {
            VoucherOrderService proxy = (VoucherOrderService) AopContext.currentProxy();// 获取当前类的spring代理对象
            return proxy.createVoucherOrder(userId, voucherId, stock);// 使用本类的代理对象调用方法，保证事务注解的正常执行！
        } finally {
//            redisLock.unlock();// 释放锁
            redissonLock.unlock();// 释放锁
        }
    }


    // 创建订单（一人一单）
    @Override
    @Transactional
    public Result createVoucherOrder(long userId, long voucherId, int stock) {
        // 4.确保一人一单
        int count = voucherOrderMapper.countByUserIdAndVoucherId(userId, voucherId);
        if (count > 0) {// 用户已经买过该秒杀券
            return Result.fail("用户已经购买过一次！");
        }

        // 5.扣减库存
        // TODO 乐观锁，解决超卖问题！
        // TODO 乐观锁-CAS法：在where条件中对库存数量stock进行判断，当stock未变动，表示该数据未被其它并发的线程改动，可进行更新！
        int i = seckillVoucherMapper.updateStockByVoucherIdAndStock(stock - 1, voucherId, stock);
        if (i == 0) {
            return Result.fail("未抢到，请重试！");
        }

        // 6.创建订单
        long orderId = RedisGlobalIdGenerator.nextId(RedisConstants.VOUCHERORDER_ID_FIELD);// todo 利用Redis生成全局唯一ID
        VoucherOrder voucherOrder = new VoucherOrder(orderId, userId, voucherId);
        save(voucherOrder);
        return Result.ok(orderId);
    }


    // ##############################秒杀下单[优化后]##############################
    // 实现【秒杀下单-(优化版)】
    // 功能：使当前登录的用户购买指定id的秒杀优惠券，并生成订单
    // TODO 使用Lua脚本判断秒杀资格，将具有资格的订单放入阻塞队列
    @Override
    public Result seckillVoucher_plus(Long voucherId) {
        Long userId = UserHolder.getUser().getId();
        // 1.执行Lua脚本，判断是否具有秒杀资格
        // todo 注意：由于Lua脚本具有原子性，因此不会发生“超卖问题”和“一人一单问题”！！！
        Long flag = stringRedisTemplate.execute(
                SECKILL_LUA_SCRIPT,
                Collections.emptyList(),
                voucherId.toString(), userId.toString());

        // 2.若返回结果不为0，则无秒杀资格，返回错误信息
        int flag1 = flag.intValue();
        if (flag1 != 0) {
            return Result.fail(flag1 == 1 ? "库存不足" : "不能重复下单");
        }

        // todo 3.若具有秒杀资格，则将下单信息保存到阻塞队列中
        long orderId = RedisGlobalIdGenerator.nextId(RedisConstants.VOUCHERORDER_ID_FIELD);// 生成全局唯一ID
        VoucherOrder voucherOrder = new VoucherOrder(orderId, userId, voucherId);
        orderBlockingQueue.add(voucherOrder);// todo 将订单放入阻塞队列中

        proxy = (VoucherOrderService) AopContext.currentProxy();// 获取代理对象
        return Result.ok(orderId);
    }


    // TODO 线程任务，用于持续获取阻塞队列中的订单，将其存入数据库中
    private class saveBlockOrder implements Runnable {
        @Override
        public void run() {
            while (true) {// 不会无限死循环：若阻塞队列中不存在对象，则会阻塞！
                try {
                    // todo 1.获取阻塞队列中的订单对象（若无对象，则阻塞等待）
                    VoucherOrder voucherOrder = orderBlockingQueue.take();

                    // 2.将订单存入数据库中
                    proxy.createVoucherOrder_plus(voucherOrder);// 使用本类的代理对象调用方法，保证事务注解的正常执行！

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // 创建订单
    @Override
    @Transactional
    public void createVoucherOrder_plus(VoucherOrder voucherOrder) {
        // 扣减库存
        seckillVoucherMapper.deductStock(voucherOrder.getVoucherId());
        // 保存订单
        save(voucherOrder);
    }

    // ######################################################################


}




