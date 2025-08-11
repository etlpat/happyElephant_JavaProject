package com.etlpat.utils;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.etlpat.dto.RedisData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;


// 缓存工具封装
//
// 基于StringRedisTemplate封装一个缓存工具类,满足下列需求:
//  --方法1:将任意Java对象序列化为json并存储在string类型的key中,并且可以设置TTL过期时间,用于处理缓存穿透问题
//  --方法2:将任意Java对象序列化为json并存储在string类型的key中,并且可以设置逻辑过期时间,用于处理缓存击穿问题
//  --方法3:根据指定的key查询缓存,并反序列化为指定类型,利用缓存空值的方式解决缓存穿透问题
//  --方法4:根据指定的key查询缓存,并反序列化为指定类型,需要利用逻辑过期解决缓存击穿问题
//


@Component
public class RedisCacheUtils {

    // “逻辑过期”解决缓存击穿的线程池
    private static final ExecutorService LOGIC_EXPIRED_EXECUTOR = Executors.newFixedThreadPool(10);

    private static StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        RedisCacheUtils.stringRedisTemplate = stringRedisTemplate; // 静态字段赋值
    }

    // 自定义互斥锁 -- 尝试加锁
    private static boolean tryLock(String key) {
        // 注意：redis的setnx指令，具有锁的性质（setnx：当且仅当key不存在，则创建键值对，返回1；否则返回0）
        //     多个线程并发调用setnx，只有第一个线程才能调用成功，其它线程均调用失败（类似于互斥锁）
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", RedisConstants.LOCK_SHOP_TTL, TimeUnit.SECONDS);// setnx指令，过期时间：10秒
        return BooleanUtil.isTrue(flag);
    }

    // 自定义互斥锁 -- 解锁
    private static void unlock(String key) {
        stringRedisTemplate.delete(key);
    }


    // TODO 方法1:将任意Java对象序列化为json并存储在string类型的key中,并且可以设置TTL过期时间,用于处理缓存穿透问题
    public static void setWithPassThrough(String key, Object value, Long time, TimeUnit unit) {
        String val = value == null ? "" : JSONUtil.toJsonStr(value);
        stringRedisTemplate.opsForValue().set(key, val, time, unit);
    }


    // TODO 方法2:将任意Java对象序列化为json并存储在string类型的key中,并且可以设置逻辑过期时间,用于处理缓存击穿问题
    public static void setWithLogicExpired(String key, Object value, Long time, TimeUnit unit) {
        RedisData redisData = new RedisData();// 创建逻辑过期数据
        redisData.setData(value);// 存入value
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));// 设置逻辑过期时间
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }


    // TODO 方法3:根据指定的key查询缓存,并反序列化为指定类型,利用缓存空值的方式解决缓存穿透问题
    public static <T, ID> T getWithPassThrough(String keyPrefix, ID id, Class<T> type // 返回类型
            , Function<ID, T> mysqlQueryById // 函数式编程，从mysql查询数据的方法，<ID, T>为参数和返回类型
            , Long time, TimeUnit unit) {
        // 1.从Redis中查询缓存数据
        String json = stringRedisTemplate.opsForValue().get(keyPrefix + id.toString());
        if (StrUtil.isNotBlank(json)) {// 若Redis中存在数据
            return JSONUtil.toBean(json, type);
        } else if ("".equals(json)) {// 缓存穿透情况
            return null;
        }
        // 2.缓存未命中，则从数据库中获取数据
        T data = mysqlQueryById.apply(id);// 从数据库获取数据
        setWithPassThrough(keyPrefix + id, data, time, unit);// 将数据存入Redis（按照“缓存空对象”的方式）
        return data;
    }


    // TODO 方法4:根据指定的key查询缓存,并反序列化为指定类型,需要利用逻辑过期解决缓存击穿问题
    public static <T, ID> T getWithLogicExpired(String keyPrefix, ID id, Class<T> type // 返回类型
            , String lockPrefix, Function<ID, T> mysqlQueryById // 函数式编程，从mysql查询数据的方法，<ID, T>为参数和返回类型
            , Long time, TimeUnit unit) {
        // 1.从Redis中查询“逻辑过期”缓存数据
        String json = stringRedisTemplate.opsForValue().get(keyPrefix + id.toString());
        if (StrUtil.isBlank(json)) {// 若热点数据不存在
            return null;
        }
        RedisData redisData = JSONUtil.toBean(json, RedisData.class);// 获取逻辑过期数据
        LocalDateTime expireTime = redisData.getExpireTime();// 获取逻辑过期时间
        T data = JSONUtil.toBean((JSONObject) redisData.getData(), type);// 获取缓存数据

        // 2.若“逻辑过期数据”已过期，且成功争抢到互斥锁：则开启一个新的线程，用于向Redis更新逻辑过期数据的新信息。
        if (LocalDateTime.now().isAfter(expireTime) && tryLock(lockPrefix + id)) {
            LOGIC_EXPIRED_EXECUTOR.submit(() -> {// 使用线程池开启线程
                try {
                    T newData = mysqlQueryById.apply(id);// 从数据库中获取新数据
                    setWithLogicExpired(keyPrefix + id, newData, time, unit);// 将数据存入Redis（按照“逻辑过期”的方式）
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    unlock(lockPrefix + id);// 释放锁
                }
            });
        }
        return data;// 返回旧数据（未过期 / 过期且未抢到锁 / 过期且抢到锁）
    }

}
