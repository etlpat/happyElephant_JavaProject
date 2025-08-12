package com.etlpat;

import com.etlpat.pojo.Shop;
import com.etlpat.service.impl.ShopServiceImpl;
import com.etlpat.utils.RedisConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@SpringBootTest
class FlareTraceEptMainApplicationTests {

    @Autowired
    private ShopServiceImpl shopService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    // *向Redis添加附带逻辑过期时间的数据
    @Test
    void testSaveLogicExpiredShopToRedis() {
        shopService.saveLogicExpiredShopToRedis(1L, RedisConstants.CACHE_SHOP_EXPIRE);
    }


    // *向Redis添加商铺的地理坐标数据
    @Test
    void testLoadShopDataToGEO() {
        // 1.查询全部店铺
        List<Shop> shops = shopService.list();
        // 2.将店铺按照type_id分组
        Map<Long, List<Shop>> shopMap = shops.stream().collect(Collectors.groupingBy(Shop::getTypeId));
        // 3.以type_id作为key，将不同类型的商铺，存入不同的Redis地理坐标集合中
        for (Map.Entry<Long, List<Shop>> entry : shopMap.entrySet()) {
            // 获取type_id
            Long typeId = entry.getKey();
            // 获取类型相同的商铺列表
            List<Shop> shopList = entry.getValue();

            // 将地理坐标存入Redis中
            String geoKey = RedisConstants.SHOP_GEO_KEY + typeId;
            List<RedisGeoCommands.GeoLocation<String>> shopLocations = new ArrayList<>(shopList.size());
            for (Shop shop : shopList) {
                shopLocations.add(new RedisGeoCommands.GeoLocation<>(shop.getId().toString(), new Point(shop.getX(), shop.getY())));
            }
            stringRedisTemplate.opsForGeo().add(geoKey, shopLocations);
        }
    }


}
