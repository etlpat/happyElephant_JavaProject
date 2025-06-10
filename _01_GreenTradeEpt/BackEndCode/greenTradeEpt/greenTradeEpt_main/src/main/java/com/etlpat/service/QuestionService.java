package com.etlpat.service;

import com.etlpat.pojo.PageBean;
import com.etlpat.pojo.Question;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author lenovo
 * @description 针对表【tb_question】的数据库操作Service
 * @createDate 2025-06-10 12:20:11
 */
public interface QuestionService extends IService<Question> {
    PageBean<Question> getPageByKeyword(Integer pageNum, Integer pageSize, String keyword);
}
