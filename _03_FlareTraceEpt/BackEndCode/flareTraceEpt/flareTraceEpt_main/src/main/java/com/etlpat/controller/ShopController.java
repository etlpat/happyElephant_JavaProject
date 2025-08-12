package com.etlpat.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etlpat.dto.Result;
import com.etlpat.pojo.Shop;
import com.etlpat.service.ShopService;
import com.etlpat.utils.SystemConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    public ShopService shopService;


    // 根据id查询商铺信息
    @GetMapping("/{id}")
    public Result queryShopById(@PathVariable("id") Long id) {
        return shopService.queryShopById(id);
    }


    // 新增商铺信息
    @PostMapping
    public Result saveShop(@RequestBody Shop shop) {
        shopService.save(shop);
        return Result.ok(shop.getId());
    }


    // 更新商铺信息
    @PutMapping
    public Result updateShop(@RequestBody Shop shop) {
        return shopService.updateShop(shop);
    }


    // 根据商铺类型和地理坐标分页查询商铺信息
    // todo 基于Redis的GEO地理坐标，完成“附近商铺搜索”功能
    @GetMapping("/of/type")
    public Result queryShopByType(@RequestParam("typeId") Integer typeId,
                                  @RequestParam(value = "current", defaultValue = "1") Integer current,
                                  @RequestParam(value = "x", required = false) Double x,
                                  @RequestParam(value = "y", required = false) Double y) {
        return shopService.queryShopByType(typeId, current, x, y);
    }


    // 根据商铺名称关键字分页查询商铺信息
    @GetMapping("/of/name")
    public Result queryShopByName(@RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "current", defaultValue = "1") Integer current) {
        // 根据名称分页查询
        Page<Shop> page = shopService.query()
                .like(StrUtil.isNotBlank(name), "name", name)
                .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        return Result.ok(page.getRecords());
    }


}
