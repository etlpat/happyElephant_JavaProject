package com.etlpat.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import lombok.Data;

/**
 * @TableName tb_shoppingcart
 */
@TableName(value = "tb_shoppingcart")
@Data
public class Shoppingcart {
    @TableId
    private Integer shoppingId;

    private Integer orderId;

    private Integer count;

    private String ownName;

    private Date createTime;

    private Date updateTime;

    // 关联的订单对象（非数据库字段）
    @TableField(exist = false)
    private Order order;
}