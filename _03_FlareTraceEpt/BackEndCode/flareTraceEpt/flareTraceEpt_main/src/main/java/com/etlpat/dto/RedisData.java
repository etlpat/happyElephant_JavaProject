package com.etlpat.dto;

import lombok.Data;

import java.time.LocalDateTime;


// 用于封装附带逻辑过期时间的数据
@Data
public class RedisData {
    private LocalDateTime expireTime;// 逻辑过期时间
    private Object data;// 其它数据
}
