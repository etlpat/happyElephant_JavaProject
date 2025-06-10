package com.etlpat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.Order;
import com.etlpat.pojo.PageBean;
import com.etlpat.service.OrderService;
import com.etlpat.mapper.OrderMapper;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.List;

/**
 * @author lenovo
 * @description 针对表【tb_order】的数据库操作Service实现
 * @createDate 2025-06-03 20:32:41
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
        implements OrderService {
    @Autowired
    OrderMapper orderMapper;


    // 分页查询--骨架
    private PageBean<Order> getPageOrdersByWrapper(Integer pageNum,// 第几页
                                                   Integer pageSize,// 每页大小
                                                   LambdaQueryWrapper<Order> wrapper) {// 查询条件
        // 设置分页参数
        Page<Order> page = new Page<>(pageNum, pageSize);
        // 进行分页查询（查询结果封装在page对象中）
        orderMapper.selectPage(page, wrapper);
        // 获取分页数据
        PageBean<Order> pageBean = new PageBean<>();
        pageBean.setPageNum(pageNum);// 设置第几页
        pageBean.setPageSize(pageSize);// 设置每页大小
        pageBean.setTotal(page.getTotal());// 设置总记录数
        pageBean.setItems(page.getRecords());// 设置本页数据列表
        return pageBean;
    }


    // 分页查询全部数据
    @Override
    public PageBean<Order> getAllPageOrders(Integer pageNum, Integer pageSize) {
        return getPageOrdersByWrapper(pageNum, pageSize, null);
    }


    // 根据关键词分页查询（关键词在标题/内容/作者名中）
    @Override
    public PageBean<Order> getPageOrdersByKeyword(Integer pageNum, Integer pageSize, String keyword) {
        // 若keyword不为null或""，进行模糊查询
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .like(!StringUtils.isNullOrEmpty(keyword), Order::getTitle, keyword)
                .or().like(!StringUtils.isNullOrEmpty(keyword), Order::getContent, keyword)
                .or().like(!StringUtils.isNullOrEmpty(keyword), Order::getOwnName, keyword);
        return getPageOrdersByWrapper(pageNum, pageSize, wrapper);
    }


    // 根据关键词和类型分页查询
    @Override
    public PageBean<Order> getPageOrdersByKeywordAndType(Integer pageNum, Integer pageSize, String keyword, String type) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getType, type);

        // 只有 keyword 非空时，才添加 AND 和模糊查询
        if (!StringUtils.isNullOrEmpty(keyword)) {
            wrapper.and(qw -> qw
                    .like(Order::getTitle, keyword)
                    .or().like(Order::getContent, keyword)
                    .or().like(Order::getOwnName, keyword)
            );
        }
        return getPageOrdersByWrapper(pageNum, pageSize, wrapper);
    }


    // 获取全部评论
    @Override
    public List<String> getAllContent() {
        return orderMapper.selectContent();
    }
}
