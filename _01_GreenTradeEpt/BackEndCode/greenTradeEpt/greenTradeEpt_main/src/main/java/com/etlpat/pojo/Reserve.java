package com.etlpat.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName tb_reserve
 */
@TableName(value ="tb_reserve")
@Data
public class Reserve {
    private Integer id;

    private String expertName;

    private String questioner;

    private String area;

    private String address;

    private String plantName;

    private String soilCondition;

    private String plantCondition;

    private String plantDetail;

    private String phone;

    private String message;

    private String answer;

    private Integer status;
}