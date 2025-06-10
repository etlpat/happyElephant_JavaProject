package com.etlpat.controller;

import com.etlpat.pojo.PageBean;
import com.etlpat.pojo.Question;
import com.etlpat.pojo.Result;
import com.etlpat.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


// 问题表
@RestController
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;


    // 根据关键词，获取分页数据
    @GetMapping("/getPage/{pageNum}")
    public Result<PageBean<Question>> getPageByKeyword(@PathVariable Integer pageNum, Integer pageSize, String keyword) {
        if (pageNum == null || pageSize == null || pageNum < 1 || pageSize < 1) {
            pageNum = 1;
            pageSize = 10;
        }
        PageBean<Question> pageBean = questionService.getPageByKeyword(pageNum, pageSize, keyword);
        return Result.success(pageBean);
    }
}
