package com.etlpat.mapper;

import java.util.List;

import com.etlpat.pojo.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author lenovo
 * @description 针对表【tb_order】的数据库操作Mapper
 * @createDate 2025-06-03 20:32:41
 * @Entity com.etlpat.pojo.Order
 */
public interface OrderMapper extends BaseMapper<Order> {
    @Select("SELECT content FROM tb_order WHERE content IS NOT NULL")
    List<String> selectContent();
}




