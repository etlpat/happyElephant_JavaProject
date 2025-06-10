package com.etlpat.service;

import com.etlpat.pojo.Expert;
import com.baomidou.mybatisplus.extension.service.IService;
import com.etlpat.pojo.PageBean;

/**
 * @author lenovo
 * @description 针对表【tb_expert】的数据库操作Service
 * @createDate 2025-06-10 12:19:58
 */
public interface ExpertService extends IService<Expert> {
    public PageBean<Expert> getPageAll(Integer pageNum, Integer pageSize);
}
