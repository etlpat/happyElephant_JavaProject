package com.etlpat.service;

import com.etlpat.dto.Result;
import com.etlpat.pojo.Blog;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * @author lenovo
 * @description 针对表【tb_blog】的数据库操作Service
 * @createDate 2025-08-04 11:26:03
 */
public interface BlogService extends IService<Blog> {

    Result queryBlogById(Long id);

    Result queryHotBlog(Integer current);

    Result likeBlog(Long id);

    Result queryBlogLikes(Long id);

    Result saveBlog(Blog blog);

    Result queryBlogOfFollow(Long max, Integer offset);
}
