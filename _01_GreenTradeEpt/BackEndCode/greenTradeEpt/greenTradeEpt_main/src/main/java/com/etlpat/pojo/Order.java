package com.etlpat.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @TableName tb_order
 */
@TableName(value = "tb_order")
@Data
public class Order {
    private Integer orderId;

    private String title;

    private BigDecimal price;

    private String content;

    private Integer orderStatus;

    private String type;

    private String picture;

    private String ownName;

    private String cooperationName;

    private Date createTime;

    private Date updateTime;

    private String address;
}