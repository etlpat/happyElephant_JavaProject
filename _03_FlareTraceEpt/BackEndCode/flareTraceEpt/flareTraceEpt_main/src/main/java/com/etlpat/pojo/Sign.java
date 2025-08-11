package com.etlpat.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import lombok.Data;


/**
 * @TableName tb_sign
 */
@TableName(value = "tb_sign")
@Data
public class Sign {
    private Long id;

    private Long userId;

    private Object year;

    private Integer month;

    private Date date;

    private Integer isBackup;
}