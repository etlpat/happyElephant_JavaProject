package com.etlpat.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName tb_discuss
 */
@TableName(value ="tb_discuss")
@Data
public class Discuss {
    private Integer discussId;

    private Integer knowledgeId;

    private String ownName;

    private String content;

    private Date createTime;
}