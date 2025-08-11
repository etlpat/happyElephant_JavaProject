package com.etlpat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.BlogComments;
import com.etlpat.service.BlogCommentsService;
import com.etlpat.mapper.BlogCommentsMapper;
import org.springframework.stereotype.Service;


/**
 * @author lenovo
 * @description 针对表【tb_blog_comments】的数据库操作Service实现
 * @createDate 2025-08-04 11:29:00
 */
@Service
public class BlogCommentsServiceImpl extends ServiceImpl<BlogCommentsMapper, BlogComments>
        implements BlogCommentsService {

}




