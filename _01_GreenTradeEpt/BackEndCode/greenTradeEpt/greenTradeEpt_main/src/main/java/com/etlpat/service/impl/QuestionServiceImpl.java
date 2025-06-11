package com.etlpat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.PageBean;
import com.etlpat.pojo.Question;
import com.etlpat.service.QuestionService;
import com.etlpat.mapper.QuestionMapper;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lenovo
 * @description 针对表【tb_question】的数据库操作Service实现
 * @createDate 2025-06-10 12:20:11
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
        implements QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    // 分页查询--骨架
    private PageBean<Question> getPageByWrapper(Integer pageNum,// 第几页
                                                Integer pageSize,// 每页大小
                                                LambdaQueryWrapper<Question> wrapper) {// 查询条件
        // 设置分页参数
        Page<Question> page = new Page<>(pageNum, pageSize);
        // 进行分页查询（查询结果封装在page对象中）
        questionMapper.selectPage(page, wrapper);
        // 获取分页数据
        PageBean<Question> pageBean = new PageBean<>();
        pageBean.setPageNum(pageNum);// 设置第几页
        pageBean.setPageSize(pageSize);// 设置每页大小
        pageBean.setTotal(page.getTotal());// 设置总记录数
        pageBean.setItems(page.getRecords());// 设置本页数据列表
        return pageBean;
    }


    // 根据关键词分页查询（关键词在专家名/植物名/标题中）
    @Override
    public PageBean<Question> getPageByKeyword(Integer pageNum, Integer pageSize, String keyword) {
        // 若keyword不为null或""，进行模糊查询
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<Question>()
                .like(!StringUtils.isNullOrEmpty(keyword), Question::getExpertName, keyword)
                .or().like(!StringUtils.isNullOrEmpty(keyword), Question::getPlantName, keyword)
                .or().like(!StringUtils.isNullOrEmpty(keyword), Question::getTitle, keyword);
        return getPageByWrapper(pageNum, pageSize, wrapper);
    }

}




