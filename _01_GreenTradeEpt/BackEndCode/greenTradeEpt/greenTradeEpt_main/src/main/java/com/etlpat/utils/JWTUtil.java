package com.etlpat.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;


// JWT令牌
//  详细讲解，见：src/test/java/com/etlpat/JWTTest.java
//
//  备注说明
//      用户登录成功后,系统会自动下发JWT令牌,然后在后续的每次请求中,浏览器都需要在请求头header中携带到服务端,
//      请求头的名称为Authorization,值为登录时下发的JWT令牌。如果检测到用户未登录,则http响应状态码为401。


// JWT令牌相关的工具类
public class JWTUtil {
    public static final String KEY = "etlpatKey";// 密钥


    // 创建JWT令牌
    public static String createToken(Map<String, Object> claims) {// 接收有效载荷
        return JWT.create()// 创建令牌
                .withClaim("claims", claims)// 设置有效载荷
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))// 设置过期时间
                .sign(Algorithm.HMAC256(KEY));// 设置加密方式
    }


    // 验证JWT令牌
    public static Map<String, Object> verifyToken(String token) {// 接收jwt令牌
        return JWT.require(Algorithm.HMAC256(KEY))// 设置解密方式
                .build()// 获取令牌验证器
                .verify(token)// 验证token，生成一个解析后的JWT对象
                .getClaim("claims")// 获取有效载荷
                .asMap();
    }

}
