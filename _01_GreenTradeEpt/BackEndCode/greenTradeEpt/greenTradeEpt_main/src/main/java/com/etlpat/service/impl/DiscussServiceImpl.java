package com.etlpat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.Discuss;
import com.etlpat.service.DiscussService;
import com.etlpat.mapper.DiscussMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lenovo
 * @description 针对表【tb_discuss】的数据库操作Service实现
 * @createDate 2025-06-10 08:52:01
 */
@Service
public class DiscussServiceImpl extends ServiceImpl<DiscussMapper, Discuss>
        implements DiscussService {
    @Autowired
    private DiscussMapper discussMapper;


    @Override
    public List<Discuss> getAllByKnowledgeId(Integer knowledgeId) {
        return discussMapper.getAllByKnowledgeId(knowledgeId);
    }

}




