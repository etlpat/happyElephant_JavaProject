package com.etlpat.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName tb_question
 */
@TableName(value ="tb_question")
@Data
public class Question {
    private Integer id;

    private String expertName;

    private String questioner;

    private String phone;

    private String plantName;

    private String title;

    private String question;

    private String answer;

    private Integer status;
}