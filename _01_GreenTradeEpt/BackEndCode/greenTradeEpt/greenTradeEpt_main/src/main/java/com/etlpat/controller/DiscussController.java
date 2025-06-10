package com.etlpat.controller;

import com.etlpat.pojo.Discuss;
import com.etlpat.pojo.Result;
import com.etlpat.service.DiscussService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

// 农业知识评论表
@RestController
@RequestMapping("/discuss")
public class DiscussController {
    @Autowired
    private DiscussService discussService;


    // 根据knowledgeId获取评论列表
    @GetMapping("/getAllByKnowledgeId")
    public Result<List<Discuss>> getAllByKnowledgeId(Integer knowledgeId) {
        if (knowledgeId == null) {
            return Result.error("knowledgeId不能为空");
        }
        return Result.success(discussService.getAllByKnowledgeId(knowledgeId));
    }

    // 插入评论对象
    @PostMapping("/save")
    public Result save(Discuss discuss) {
        discuss.setCreateTime(new Date());
        discussService.save(discuss);
        return Result.success();
    }
}
