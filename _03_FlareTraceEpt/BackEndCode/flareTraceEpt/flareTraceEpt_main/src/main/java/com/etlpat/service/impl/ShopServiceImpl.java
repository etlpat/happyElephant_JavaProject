package com.etlpat.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.dto.Result;
import com.etlpat.pojo.Shop;
import com.etlpat.service.ShopService;
import com.etlpat.mapper.ShopMapper;
import com.etlpat.utils.RedisCacheUtils;
import com.etlpat.utils.RedisConstants;
import com.etlpat.dto.RedisData;
import com.etlpat.utils.SystemConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * @author lenovo
 * @description 针对表【tb_shop】的数据库操作Service实现
 * @createDate 2025-08-04 11:29:32
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop>
        implements ShopService {

    // “逻辑过期”解决缓存击穿的线程池
    private static final ExecutorService LOGIC_EXPIRED_EXECUTOR = Executors.newFixedThreadPool(10);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    // TODO 自定义互斥锁 -- 尝试加锁
    private boolean tryLock(String key) {
        // 注意：redis的setnx指令，具有锁的性质（setnx：当且仅当key不存在，则创建键值对，返回1；否则返回0）
        //     多个线程并发调用setnx，只有第一个线程才能调用成功，其它线程均调用失败（类似于互斥锁）
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1",
                RedisConstants.LOCK_SHOP_TTL, TimeUnit.SECONDS);// setnx指令，过期时间：10秒
        return BooleanUtil.isTrue(flag);
    }


    // TODO 自定义互斥锁 -- 解锁
    private void unlock(String key) {
        stringRedisTemplate.delete(key);
    }


    // 根据id查询商铺信息（利用缓存优化该方法）
    // TODO (1)基于[缓存空对象]来解决[缓存穿透]
    // p.s.这里redis中统一使用json字符串来存放shop对象
    public Result queryWithPassThrough(Long id) {
        // 1.从Redis中查询商铺缓存
        String shopKey = RedisConstants.CACHE_SHOP_KEY + id;
        String shopJson = stringRedisTemplate.opsForValue().get(shopKey);
        // 若shop对象在缓存中，直接返回该对象
        if (StrUtil.isNotBlank(shopJson)) {
            Shop shop = JSONUtil.toBean(shopJson, Shop.class);
            return Result.ok(shop);
        }
        // TODO 若shopJson为空串""，则表示该数据在数据库中不存在（缓存穿透），返回错误信息
        if ("".equals(shopJson)) {
            return Result.fail("店铺不存在！");
        }

        // 2.若缓存未命中，则从数据库中获取商铺对象
        Shop shop = this.getById(id);
        if (shop == null) {// 若数据库中不存在该对象（缓存穿透）
            // TODO 使用“缓存空对象”来解决缓存穿透！
            stringRedisTemplate.opsForValue().set(shopKey, ""
                    , RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);// 过期时间：2分钟
            return Result.fail("店铺不存在！");
        }

        // 3.将商铺对象存入Redis缓存中，并返回
        stringRedisTemplate.opsForValue().set(shopKey, JSONUtil.toJsonStr(shop)
                , RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);// 过期时间：30分钟
        return Result.ok(shop);
    }


    // 根据id查询商铺信息（利用缓存优化该方法）
    // TODO (2)基于[互斥锁]来解决[缓存击穿]
    public Result queryWithMutex(Long id) {
        // 1.从Redis中查询商铺缓存
        String shopKey = RedisConstants.CACHE_SHOP_KEY + id;
        String shopJson = stringRedisTemplate.opsForValue().get(shopKey);
        if (StrUtil.isNotBlank(shopJson)) {// 若shop对象在Redis中，直接返回该对象
            return Result.ok(JSONUtil.toBean(shopJson, Shop.class));
        }

        String lockKey = RedisConstants.LOCK_SHOP_KEY + id;
        Shop shop = null;
        try {
            // TODO 2.若缓存未命中，则尝试获取互斥锁
            if (!tryLock(lockKey)) {// TODO 若未抢占到锁
                Thread.sleep(50);// 休眠一段时间
                return queryWithMutex(id);// 重新递归执行该方法
            }

            // TODO 3.若抢占到锁，则从数据库中获取商铺对象，并存入redis中
            shop = this.getById(id);
            if (shop == null) {
                return Result.fail("店铺不存在！");
            }
            stringRedisTemplate.opsForValue().set(shopKey, JSONUtil.toJsonStr(shop)
                    , RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            unlock(lockKey);// TODO 释放锁
        }
        return Result.ok(shop);
    }


    // 根据id查询商铺信息（利用缓存优化该方法）
    // TODO (3)基于[逻辑过期]来解决[缓存击穿]
    public Result queryWithLogicExpired(Long id) {
        // 注意：逻辑过期数据，一般为热点数据。其TTL的值为-1，物理上不会过期，只会在逻辑上过期。
        //      逻辑过期数据，默认是始终存在的，一般在管理端手动注入，这里在Test测试包为其注入。

        // 1.从Redis中查询“逻辑过期商铺”信息
        String shopKey = RedisConstants.CACHE_SHOP_KEY + id;
        String shopJson = stringRedisTemplate.opsForValue().get(shopKey);
        if (StrUtil.isBlank(shopJson)) {
            return Result.fail("店铺不存在！");
        }
        RedisData redisData = JSONUtil.toBean(shopJson, RedisData.class);// 获取逻辑过期数据
        LocalDateTime expireTime1 = redisData.getExpireTime();// 获取逻辑过期时间
        Shop shop1 = JSONUtil.toBean((JSONObject) redisData.getData(), Shop.class);// 获取店铺信息

        // 2.查看“逻辑过期商铺”是否过期，若未过期，则返回商铺信息
        if (LocalDateTime.now().isBefore(expireTime1)) {// 未过期
            return Result.ok(shop1);
        }

        // 3.若商铺已过期，则尝试获取互斥锁。若未抢到互斥锁，则直接返回旧的商铺信息
        String lockKey = RedisConstants.LOCK_SHOP_KEY + id;
        if (!tryLock(lockKey)) {
            return Result.ok(shop1);
        }

        // 4.若争抢到互斥锁，则①开启独立线程，用于更新“逻辑过期商铺”信息，②并返回旧的商铺信息
        LOGIC_EXPIRED_EXECUTOR.submit(() -> {// 使用线程池开启线程
            try {
                saveLogicExpiredShopToRedis(id, RedisConstants.CACHE_SHOP_EXPIRE);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                unlock(lockKey);// 释放锁
            }
        });
        return Result.ok(shop1);
    }


    // 添加逻辑过期数据
    public void saveLogicExpiredShopToRedis(Long id, Long expireSeconds) {
        // 创建附带逻辑过期时间的数据
        RedisData redisData = new RedisData();
        redisData.setData(getById(id));
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(expireSeconds));// 设置逻辑过期时间
        // 写入Redis
        stringRedisTemplate.opsForValue().set(
                RedisConstants.CACHE_SHOP_KEY + id, JSONUtil.toJsonStr(redisData));// 不设置TTL（仅在逻辑上设置过期时间）
    }


    // 根据id查询商铺信息
    @Override
    public Result queryShopById(Long id) {
//        return queryWithPassThrough(id);// (1)基于[缓存空对象]来解决[缓存穿透]
//        return queryWithMutex(id);// (2)基于[互斥锁]来解决[缓存击穿]
//        return queryWithLogicExpired(id);// (3)基于[逻辑过期]来解决[缓存击穿]

        // (4)使用工具类，完成基于[缓存空对象]来解决[缓存穿透]，并获取数据
        Shop shop = RedisCacheUtils.getWithPassThrough(RedisConstants.CACHE_SHOP_KEY, id
                , Shop.class, this::getById, RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);
        if (shop == null) {
            return Result.fail("店铺不存在！");
        }
        return Result.ok(shop);

//        // (5)使用工具类，完成基于[逻辑过期]来解决[缓存击穿]，并获取数据
//        Shop shop = RedisCacheUtils.getWithLogicExpired(RedisConstants.CACHE_SHOP_KEY, id, Shop.class
//                , RedisConstants.LOCK_SHOP_KEY, this::getById, RedisConstants.CACHE_SHOP_EXPIRE, TimeUnit.SECONDS);
//        if (shop == null) {
//            return Result.fail("店铺不存在！");
//        }
//        return Result.ok(shop);
    }


    // 更新商铺信息，并删除缓存
    @Override
    @Transactional// 添加事务，保证mysql更新与redis删除操作的原子性
    public Result updateShop(Shop shop) {
        Long id = shop.getId();
        if (id == null) {
            return Result.fail("商铺id不能为空!");
        }
        // 1.更新数据库
        updateById(shop);
        // 2.删除缓存
        stringRedisTemplate.delete(RedisConstants.CACHE_SHOP_KEY + id);
        return Result.ok();
    }


    // 根据商铺类型和地理坐标分页查询商铺信息
    // todo 基于Redis的GEO地理坐标，完成“附近商铺搜索”功能
    @Override
    public Result queryShopByType(Integer typeId, Integer current, Double x, Double y) {
        // 1.若坐标不存在，则按数据库进行分页查询
        if (x == null || y == null) {
            Page<Shop> page = query().eq("type_id", typeId)
                    .page(new Page<>(current, SystemConstants.DEFAULT_PAGE_SIZE));
            return Result.ok(page.getRecords());
        }

        // 2.使用Redis的GEO集合，按照地理坐标进行分页查询
        String geoKey = RedisConstants.SHOP_GEO_KEY + typeId;// 使用typeId作为key（同类型的商铺在同一个GEO集合中）
        int start = (current - 1) * SystemConstants.DEFAULT_PAGE_SIZE;// 元素的起始下标
        int end = current * SystemConstants.DEFAULT_PAGE_SIZE;// 元素的终止下标
        // 对GEO进行查询
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = stringRedisTemplate.opsForGeo()
                .search(geoKey,// GEO集合的key
                        GeoReference.fromCoordinate(x, y),// 圆心
                        new Distance(5000),// 半径
                        RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs().includeDistance().limit(end)// 搜索参数（元素个数）
                );
        if (results == null) {
            return Result.ok(Collections.emptyList());
        }

        // 3.对获取的结果进行处理
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = results.getContent();
        if (content.size() <= start) {// 若已经没有下一页
            return Result.ok(Collections.emptyList());
        }
        List<Long> shopIds = new ArrayList<>(content.size());// 用于保存商铺的id列表
        Map<String, Distance> distanceMap = new HashMap<>(content.size());// 用于保存<商铺id，离圆心距离>的map
        content.stream().skip(start).forEach(result -> {// 跳过start个元素，之后开始遍历列表
            String shopIdStr = result.getContent().getName();// 获取店铺id
            shopIds.add(Long.valueOf(shopIdStr));
            Distance distance = result.getDistance();// 获取与圆心的距离
            distanceMap.put(shopIdStr, distance);
        });
        // 从数据库中获取id列表对应的shop列表
        String shopIdStr = StrUtil.join(",", shopIds);
        List<Shop> shops = query().in("id", shopIds).last("ORDER BY FIELD(id," + shopIdStr + ")").list();
        // 为店铺设置距离
        for (Shop shop : shops) {
            shop.setDistance(distanceMap.get(shop.getId().toString()).getValue());
        }
        return Result.ok(shops);
    }


}




