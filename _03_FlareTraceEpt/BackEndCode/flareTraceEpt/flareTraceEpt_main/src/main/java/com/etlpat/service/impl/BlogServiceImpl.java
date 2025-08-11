package com.etlpat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.Blog;
import com.etlpat.service.BlogService;
import com.etlpat.mapper.BlogMapper;
import org.springframework.stereotype.Service;


/**
 * @author lenovo
 * @description 针对表【tb_blog】的数据库操作Service实现
 * @createDate 2025-08-04 11:26:03
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
        implements BlogService {

}




