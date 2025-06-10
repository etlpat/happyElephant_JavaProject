package com.etlpat.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName tb_knowledge
 */
@TableName(value ="tb_knowledge")
@Data
public class Knowledge {
    private Integer knowledgeId;

    private String title;

    private String content;

    private String picPath;

    private String ownName;

    private Date createTime;

    private Date updateTime;
}