package com.etlpat.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// Redisson的配置类
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() {
        // 创建配置对象，对Redisson进行配置
        Config config = new Config();
        config.useSingleServer()// 单节点模式
                .setAddress("redis://192.168.238.130:6379")// 配置redis地址
                .setPassword("JMGMKSZ1919810");// 配置密码
        // 返回Redisson客户端对象
        return Redisson.create(config);
    }

}
