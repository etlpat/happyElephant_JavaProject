package com.etlpat.service;

import com.etlpat.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.etlpat.pojo.PageBean;

import java.util.List;

/**
 * @author lenovo
 * @description 针对表【tb_order】的数据库操作Service
 * @createDate 2025-06-03 20:32:41
 */
public interface OrderService extends IService<Order> {
    PageBean<Order> getAllPageOrders(Integer pageNum, Integer pageSize);

    PageBean<Order> getPageOrdersByKeyword(Integer pageNum, Integer pageSize, String keyword);

    PageBean<Order> getPageOrdersByKeywordAndType(Integer pageNum, Integer pageSize, String keyword, String type);

    List<String> getAllContent();
}
