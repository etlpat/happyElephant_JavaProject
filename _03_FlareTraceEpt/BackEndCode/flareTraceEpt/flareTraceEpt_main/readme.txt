技术点及所在文件：

    1.基于Redis实现登录验证
        见：service层 -- UserServiceImpl.java
           拦截器 -- RefreshTokenInterceptor.java

    2.实现商户查询的缓存：
        ①基于Redis添加商户缓存、实现缓存更新；
        ②解决缓存问题：“缓存空对象”解决缓存穿透、“互斥锁”解决缓存击穿、“逻辑过期”解决缓存击穿；
        ③创建工具类，封装解决缓存问题的相关方法。
            见：service层 -- ShopServiceImpl.java
               工具包 -- RedisCacheUtils.java

    3.优惠券秒杀：
        ①CAS乐观锁解决超卖问题、悲观锁（分布式锁）解决一人一单问题；
        ②Redis分布式锁的基本实现、基于Lua脚本的Redis分布式锁的实现、Redisson分布式锁的使用；
        ③基于异步和阻塞队列优化优惠券秒杀。
            见：service层 -- VoucherOrderServiceImpl.java（秒杀下单）
               distributedLock包 -- _1_SimpleRedisLock.java（Redis分布式锁）
               distributedLock包 -- _2_LuaRedisLock.java（基于Lua的Redis分布式锁）

    4.探店博客模块：博客点赞、点赞排行榜
        见：service层 -- BlogServiceImpl.java

    5.好友关注模块：关注、取关、查询共同关注
        见：service层 -- FollowServiceImpl.java

    6.关注推送（Timeline模式的Feed流），通过“推模式（写扩散）”方式实现
        见：service层 -- BlogServiceImpl.java

    7.附近商铺模块：通过GEO地理坐标，搜索附近商铺
        见：service层 -- ShopServiceImpl.java

    8.用户签到模块：通过BitMap位图，进行签到、连续签到统计
        见：service层 -- UserServiceImpl.java
