package com.etlpat.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName tb_expert
 */
@TableName(value ="tb_expert")
@Data
public class Expert {
    private String userName;

    private String realName;

    private String phone;

    private String profession;

    private String position;

    private String belong;
}