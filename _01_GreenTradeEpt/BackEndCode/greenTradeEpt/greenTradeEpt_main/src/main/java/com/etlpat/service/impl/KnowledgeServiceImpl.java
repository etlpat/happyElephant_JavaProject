package com.etlpat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.Knowledge;
import com.etlpat.pojo.PageBean;
import com.etlpat.service.KnowledgeService;
import com.etlpat.mapper.KnowledgeMapper;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lenovo
 * @description 针对表【tb_knowledge】的数据库操作Service实现
 * @createDate 2025-06-10 05:24:52
 */
@Service
public class KnowledgeServiceImpl extends ServiceImpl<KnowledgeMapper, Knowledge>
        implements KnowledgeService {
    @Autowired
    private KnowledgeMapper knowledgeMapper;


    // 分页查询--骨架
    private PageBean<Knowledge> getPageKnowledgeByWrapper(Integer pageNum,// 第几页
                                                          Integer pageSize,// 每页大小
                                                          LambdaQueryWrapper<Knowledge> wrapper) {// 查询条件
        // 设置分页参数
        Page<Knowledge> page = new Page<>(pageNum, pageSize);
        // 进行分页查询（查询结果封装在page对象中）
        knowledgeMapper.selectPage(page, wrapper);
        // 获取分页数据
        PageBean<Knowledge> pageBean = new PageBean<>();
        pageBean.setPageNum(pageNum);// 设置第几页
        pageBean.setPageSize(pageSize);// 设置每页大小
        pageBean.setTotal(page.getTotal());// 设置总记录数
        pageBean.setItems(page.getRecords());// 设置本页数据列表
        return pageBean;
    }


    // 根据关键词分页查询（关键词在标题/内容/作者名中）
    @Override
    public PageBean<Knowledge> getPageKnowledgeByKeyword(Integer pageNum, Integer pageSize, String keyword) {
        // 若keyword不为null或""，进行模糊查询
        LambdaQueryWrapper<Knowledge> wrapper = new LambdaQueryWrapper<Knowledge>()
                .like(!StringUtils.isNullOrEmpty(keyword), Knowledge::getTitle, keyword)
                .or().like(!StringUtils.isNullOrEmpty(keyword), Knowledge::getContent, keyword)
                .or().like(!StringUtils.isNullOrEmpty(keyword), Knowledge::getOwnName, keyword);
        return getPageKnowledgeByWrapper(pageNum, pageSize, wrapper);
    }

}




