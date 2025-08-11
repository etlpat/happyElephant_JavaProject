package com.etlpat.controller;

import com.etlpat.dto.Result;
import com.etlpat.pojo.ShopType;
import com.etlpat.service.ShopTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/shop-type")
public class ShopTypeController {
    @Autowired
    private ShopTypeService typeService;


    @GetMapping("list")
    public Result queryTypeList() {
        List<ShopType> typeList = typeService
                .query().orderByAsc("sort").list();
        return Result.ok(typeList);
    }

}
