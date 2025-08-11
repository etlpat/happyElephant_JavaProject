技术点及所在文件：

    1.基于Redis实现登录验证
        见：service层 -- UserServiceImpl.java
           拦截器 -- RefreshTokenInterceptor.java

    2.添加商户缓存、“缓存空对象”解决缓存穿透、“互斥锁”解决缓存击穿、“逻辑过期”解决缓存击穿；创建工具类，封装解决缓存问题的相关方法。
        见：service层 -- ShopServiceImpl.java
           工具包 -- RedisCacheUtils.java

    3.优惠券秒杀：
        ①CAS乐观锁解决超卖问题、悲观锁（分布式锁）解决一人一单问题；
        ②Redis分布式锁的基本实现、基于Lua脚本的Redis分布式锁的实现、Redisson分布式锁的使用；
        ③基于异步和阻塞队列优化优惠券秒杀。
            见：service层 -- VoucherOrderServiceImpl.java（秒杀下单）
               distributedLock包 -- _1_SimpleRedisLock.java（Redis分布式锁）
               distributedLock包 -- _2_LuaRedisLock.java（基于Lua的Redis分布式锁）
