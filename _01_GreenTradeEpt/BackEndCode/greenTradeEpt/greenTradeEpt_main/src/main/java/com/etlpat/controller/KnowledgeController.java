package com.etlpat.controller;

import com.etlpat.pojo.Knowledge;
import com.etlpat.pojo.PageBean;
import com.etlpat.pojo.Result;
import com.etlpat.service.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


// 农业知识表
@RestController
@RequestMapping("/knowledge")
public class KnowledgeController {
    @Autowired
    private KnowledgeService knowledgeService;


    // 根据关键词，获取分页数据
    @GetMapping("/getPage/{pageNum}")
    public Result<PageBean<Knowledge>> getPageKnowledgeByKeyword(@PathVariable Integer pageNum, Integer pageSize, String keyword) {
        if (pageNum == null || pageSize == null || pageNum < 1 || pageSize < 1) {
            pageNum = 1;
            pageSize = 4;
        }
        PageBean<Knowledge> knowledgePageBean = knowledgeService.getPageKnowledgeByKeyword(pageNum, pageSize, keyword);
        return Result.success(knowledgePageBean);
    }

}
