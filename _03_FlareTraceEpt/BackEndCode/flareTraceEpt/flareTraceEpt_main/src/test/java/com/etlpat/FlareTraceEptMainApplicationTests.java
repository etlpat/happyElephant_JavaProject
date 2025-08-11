package com.etlpat;

import com.etlpat.service.impl.ShopServiceImpl;
import com.etlpat.utils.RedisConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class FlareTraceEptMainApplicationTests {

    @Autowired
    private ShopServiceImpl shopService;


    // 测试向Redis添加附带逻辑过期时间的数据
    @Test
    void testSaveLogicExpiredShopToRedis() {
        shopService.saveLogicExpiredShopToRedis(1L, RedisConstants.CACHE_SHOP_EXPIRE);
    }

}
