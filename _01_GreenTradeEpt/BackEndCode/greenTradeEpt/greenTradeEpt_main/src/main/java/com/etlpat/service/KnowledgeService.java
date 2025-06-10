package com.etlpat.service;

import com.etlpat.pojo.Knowledge;
import com.baomidou.mybatisplus.extension.service.IService;
import com.etlpat.pojo.PageBean;

/**
 * @author lenovo
 * @description 针对表【tb_knowledge】的数据库操作Service
 * @createDate 2025-06-10 05:24:52
 */
public interface KnowledgeService extends IService<Knowledge> {
    PageBean<Knowledge> getPageKnowledgeByKeyword(Integer pageNum, Integer pageSize, String keyword);
}
