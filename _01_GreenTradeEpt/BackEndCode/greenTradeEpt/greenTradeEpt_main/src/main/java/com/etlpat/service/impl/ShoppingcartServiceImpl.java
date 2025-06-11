package com.etlpat.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.PageBean;
import com.etlpat.pojo.Shoppingcart;
import com.etlpat.service.ShoppingcartService;
import com.etlpat.mapper.ShoppingcartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Date;


/**
 * @author lenovo
 * @description 针对表【tb_shoppingcart】的数据库操作Service实现
 * @createDate 2025-06-11 16:05:04
 */
@Service
public class ShoppingcartServiceImpl extends ServiceImpl<ShoppingcartMapper, Shoppingcart>
        implements ShoppingcartService {
    @Autowired
    private ShoppingcartMapper shoppingcartMapper;


    /**
     * 分页查询购物车及关联订单
     *
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @param ownName  用户名
     * @return 分页结果
     */
    @Override
    public PageBean<Shoppingcart> getCartPageWithOrder(Integer pageNum, Integer pageSize, String ownName) {
        // 创建分页对象
        Page<Shoppingcart> page = new Page<>(pageNum, pageSize);

        // 执行分页查询
        Page<Shoppingcart> resultPage = shoppingcartMapper.selectPageWithOrder(page, ownName);

        // 转换为自定义PageBean
        return new PageBean<>(
                (int) resultPage.getCurrent(),
                (int) resultPage.getSize(),
                resultPage.getTotal(),
                resultPage.getRecords()
        );
    }


    // 更新商品数量
    @Override
    public void updateCount(Integer count, Integer id) {
        LambdaUpdateWrapper<Shoppingcart> wrapper = new LambdaUpdateWrapper<Shoppingcart>()
                .set(Shoppingcart::getCount, count)
                .set(Shoppingcart::getUpdateTime, new Date())
                .eq(Shoppingcart::getShoppingId, id);
        shoppingcartMapper.update(null, wrapper);
    }
}




