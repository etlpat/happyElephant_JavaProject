package com.etlpat.service;

import com.etlpat.dto.Result;
import com.etlpat.pojo.Shop;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * @author lenovo
 * @description 针对表【tb_shop】的数据库操作Service
 * @createDate 2025-08-04 11:29:32
 */
public interface ShopService extends IService<Shop> {

    Result queryShopById(Long id);

    Result updateShop(Shop shop);

    Result queryShopByType(Integer typeId, Integer current, Double x, Double y);
}
