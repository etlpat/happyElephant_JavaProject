package com.etlpat.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @TableName tb_bank
 */
@TableName(value ="tb_bank")
@Data
public class Bank {
    @TableId
    private Integer bankId;

    private String bankName;

    private String introduce;

    private String bankPhone;

    private BigDecimal money;

    private BigDecimal rate;

    private Integer repayment;
}