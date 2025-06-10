package com.etlpat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.Expert;
import com.etlpat.pojo.PageBean;
import com.etlpat.pojo.Question;
import com.etlpat.service.ExpertService;
import com.etlpat.mapper.ExpertMapper;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lenovo
 * @description 针对表【tb_expert】的数据库操作Service实现
 * @createDate 2025-06-10 12:19:58
 */
@Service
public class ExpertServiceImpl extends ServiceImpl<ExpertMapper, Expert>
        implements ExpertService {
    @Autowired
    ExpertMapper expertMapper;


    // 分页查询--骨架
    private PageBean<Expert> getPageByWrapper(Integer pageNum,// 第几页
                                              Integer pageSize,// 每页大小
                                              LambdaQueryWrapper<Expert> wrapper) {// 查询条件
        // 设置分页参数
        Page<Expert> page = new Page<>(pageNum, pageSize);
        // 进行分页查询（查询结果封装在page对象中）
        expertMapper.selectPage(page, wrapper);
        // 获取分页数据
        PageBean<Expert> pageBean = new PageBean<>();
        pageBean.setPageNum(pageNum);// 设置第几页
        pageBean.setPageSize(pageSize);// 设置每页大小
        pageBean.setTotal(page.getTotal());// 设置总记录数
        pageBean.setItems(page.getRecords());// 设置本页数据列表
        return pageBean;
    }


    // 分页查询全部专家
    @Override
    public PageBean<Expert> getPageAll(Integer pageNum, Integer pageSize) {
        return getPageByWrapper(pageNum, pageSize, null);
    }
}




