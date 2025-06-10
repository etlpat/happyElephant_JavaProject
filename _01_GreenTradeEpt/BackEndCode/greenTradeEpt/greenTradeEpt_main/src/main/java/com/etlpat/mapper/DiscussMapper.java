package com.etlpat.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.etlpat.pojo.Discuss;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author lenovo
* @description 针对表【tb_discuss】的数据库操作Mapper
* @createDate 2025-06-10 08:52:01
* @Entity com.etlpat.pojo.Discuss
*/
public interface DiscussMapper extends BaseMapper<Discuss> {
    List<Discuss> getAllByKnowledgeId(@Param("knowledgeId") Integer knowledgeId);
}




