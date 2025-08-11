package com.etlpat.distributedLock;

import cn.hutool.core.lang.UUID;
import com.etlpat.utils.RedisConstants;
import lombok.Data;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;


@Data
public class _1_SimpleRedisLock implements ILock {

    // 创建静态UUID作为【服务器标识】
    // TODO 在分布式系统下，由于不同tomcat服务器，对应不同JVM。因此该静态UUID在本JVM内部共享，可以作为本台服务器的唯一标识。
    private static final String UUID_PREFIX = UUID.randomUUID().toString(true);

    private String lockKey;

    private StringRedisTemplate stringRedisTemplate;

    public _1_SimpleRedisLock(String lockKey, StringRedisTemplate stringRedisTemplate) {
        this.lockKey = RedisConstants.DISTRIBUTED_LOCK_KEY + lockKey;
        this.stringRedisTemplate = stringRedisTemplate;
    }


    // 获取锁
    @Override
    public boolean tryLock(long timeoutSec) {
        // 创建线程标识，作为锁的value
        // TODO 在分布式系统下，由于不同服务器中的线程id可能重复，因此使用【服务器标识+线程id】来确定一个唯一的线程标识。
        String threadId = UUID_PREFIX + "-" + Thread.currentThread().getId();

        // SETNX指令，等价于setIfAbsent()方法
        Boolean flag = stringRedisTemplate.opsForValue()
                .setIfAbsent(lockKey, threadId, timeoutSec, TimeUnit.SECONDS);

        // 当flag为真，返回true；flag为假或null，返回false
        return Boolean.TRUE.equals(flag);
    }


    // 释放锁
    @Override
    public void unlock() {
        // 获取线程标识
        String threadId = UUID_PREFIX + "-" + Thread.currentThread().getId();
        // 判断当前线程标识是否与Redis（锁）中的值一致，若一致，则释放锁
        if (threadId.equals(stringRedisTemplate.opsForValue().get(lockKey))) {
            stringRedisTemplate.delete(lockKey);
        }
    }

}
