package com.etlpat.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;


// 基于Redis的全局ID生成器（功能类似于雪花算法）
@Component
public class RedisGlobalIdGenerator {

    // 起始时间戳
    private static final long BEGIN_TIMESTAMP = 1735689600L;

    private static StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        RedisGlobalIdGenerator.stringRedisTemplate = stringRedisTemplate; // 静态字段赋值
    }


    // ID的组成部分（Long共64位）:
    //  ①符号位:1bit,永远为0
    //  ②时间戳:31bit,以秒为单位,可以使用69年
    //  ③序列号:32bit,秒内的计数器,支持每秒产生2^32个不同ID（每天一重置）
    public static long nextId(String keyPrefix) {
        // 1.生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timeStamp = nowSecond - BEGIN_TIMESTAMP;

        // 2.生成序列号
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));// 生成当前日期
        String idKey = RedisConstants.GLOBAL_ID_KEY + keyPrefix + ":" + date;// 一天一个key
        long count = stringRedisTemplate.opsForValue().increment(idKey);// 每天从0开始统计自增的序列号

        // 3.拼接并返回
        return count | timeStamp << 32;
    }


//    public static void main(String[] args) {
//        long second = LocalDateTime.of(2025, 1, 1, 0, 0, 0)
//                .toEpochSecond(ZoneOffset.UTC);
//        System.out.println(second);
//    }

}
