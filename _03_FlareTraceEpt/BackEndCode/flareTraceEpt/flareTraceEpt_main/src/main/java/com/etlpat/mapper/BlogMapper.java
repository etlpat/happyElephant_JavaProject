package com.etlpat.mapper;

import com.etlpat.pojo.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;


/**
 * @author lenovo
 * @description 针对表【tb_blog】的数据库操作Mapper
 * @createDate 2025-08-04 11:26:03
 * @Entity com.etlpat.pojo.Blog
 */
public interface BlogMapper extends BaseMapper<Blog> {
    // 点赞数+1
    void incrementLiked(@Param("id") Long id);

    // 点赞数-1
    void decrementLiked(@Param("id") Long id);
}




