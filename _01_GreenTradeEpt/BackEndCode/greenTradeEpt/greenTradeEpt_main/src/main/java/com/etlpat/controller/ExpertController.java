package com.etlpat.controller;

import com.etlpat.pojo.Expert;
import com.etlpat.pojo.PageBean;
import com.etlpat.pojo.Result;
import com.etlpat.service.ExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


// 专家表
@RestController
@RequestMapping("/expert")
public class ExpertController {
    @Autowired
    private ExpertService expertService;


    // 分页获取全部专家
    @GetMapping("/getPageAll/{pageNum}")
    public Result<PageBean<Expert>> getPageAll(@PathVariable Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageSize == null || pageNum < 1 || pageSize < 1) {
            pageNum = 1;
            pageSize = 10;
        }
        return Result.success(expertService.getPageAll(pageNum, pageSize));
    }
}
