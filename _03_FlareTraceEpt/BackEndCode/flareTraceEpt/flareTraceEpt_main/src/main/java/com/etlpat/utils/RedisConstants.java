package com.etlpat.utils;


// 用于存放redis相关的常量
public class RedisConstants {
    public static final String KEY_PREFIX = "etlpat:flareTraceEpt:";// Redis的key的前缀

    public static final String LOGIN_CODE_KEY = KEY_PREFIX + "login:code:";// 手机验证码
    public static final Long LOGIN_CODE_TTL = 2L;// 验证码有效期：2分钟
    public static final String LOGIN_USER_KEY = KEY_PREFIX + "login:token:";// 登录的token
    public static final Long LOGIN_USER_TTL = 10080L;// token有效期：7天

    public static final Long CACHE_NULL_TTL = 2L;

    public static final Long CACHE_SHOP_TTL = 30L;
    public static final String CACHE_SHOP_KEY = KEY_PREFIX + "cache:shop:";

    public static final String LOCK_SHOP_KEY = KEY_PREFIX + "lock:shop:";
    public static final Long LOCK_SHOP_TTL = 10L;
    public static final Long CACHE_SHOP_EXPIRE = 10L;

    public static final String GLOBAL_ID_KEY = KEY_PREFIX + "globalId:";
    public static final String VOUCHERORDER_ID_FIELD = "voucherOrderId";

    public static final String DISTRIBUTED_LOCK_KEY = KEY_PREFIX + "distributedLock:";// 分布式锁的key前缀
    public static final Long LOCK_VOUCHERORDER_TTL = 10L;

    public static final String SECKILL_STOCK_KEY = KEY_PREFIX + "seckill:stock:";
    public static final String BLOG_LIKED_KEY = KEY_PREFIX + "blog:liked:";
    public static final String FOLLOW_KEY = KEY_PREFIX + "follows:";
    public static final String FEED_KEY = KEY_PREFIX + "feed:";
    public static final String SHOP_GEO_KEY = KEY_PREFIX + "shop:geo:";
    public static final String USER_SIGN_KEY = KEY_PREFIX + "sign:";
}
