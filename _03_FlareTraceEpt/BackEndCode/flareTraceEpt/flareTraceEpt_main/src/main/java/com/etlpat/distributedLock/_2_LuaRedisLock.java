package com.etlpat.distributedLock;

import cn.hutool.core.lang.UUID;
import com.etlpat.utils.RedisConstants;
import lombok.Data;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;


@Data
public class _2_LuaRedisLock implements ILock {

    // TODO 创建Lua脚本对象
    private static final DefaultRedisScript<Long> UNLOCK_LUA_SCRIPT;

    // TODO 初始化Lua脚本
    static {
        UNLOCK_LUA_SCRIPT = new DefaultRedisScript<>();
        UNLOCK_LUA_SCRIPT.setLocation(new ClassPathResource("unlock.lua"));// 配置Lua文件路径
        UNLOCK_LUA_SCRIPT.setResultType(Long.class);// 设置返回类型
    }


    private static final String UUID_PREFIX = UUID.randomUUID().toString(true);
    private String lockKey;
    private StringRedisTemplate stringRedisTemplate;

    public _2_LuaRedisLock(String lockKey, StringRedisTemplate stringRedisTemplate) {
        this.lockKey = RedisConstants.DISTRIBUTED_LOCK_KEY + lockKey;
        this.stringRedisTemplate = stringRedisTemplate;
    }


    // 获取锁
    @Override
    public boolean tryLock(long timeoutSec) {
        String threadId = UUID_PREFIX + "-" + Thread.currentThread().getId();
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, threadId, timeoutSec, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(flag);
    }


    // 释放锁
    @Override
    public void unlock() {
        // 获取线程标识
        String threadId = UUID_PREFIX + "-" + Thread.currentThread().getId();

        // 使用Lua脚本，解决分布式锁的原子性问题
        // TODO 调用Lua脚本（Lua脚本中的多条Redis语句，执行时具有原子性）
        stringRedisTemplate.execute(
                UNLOCK_LUA_SCRIPT,// Lua脚本对象
                Collections.singletonList(lockKey),// KEYS集合
                threadId);// args参数列表
    }

}
