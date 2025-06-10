package com.etlpat.service;

import com.etlpat.pojo.Discuss;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author lenovo
 * @description 针对表【tb_discuss】的数据库操作Service
 * @createDate 2025-06-10 08:52:01
 */
public interface DiscussService extends IService<Discuss> {
    List<Discuss> getAllByKnowledgeId(Integer knowledgeId);
}
