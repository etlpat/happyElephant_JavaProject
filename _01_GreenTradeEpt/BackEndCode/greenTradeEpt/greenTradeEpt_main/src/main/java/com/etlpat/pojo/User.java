package com.etlpat.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName tb_user
 */
@TableName(value ="tb_user")
@Data
public class User {
    private String userName;

    private String password;

    private String nickName;

    private String phone;

    private String identityNum;

    private String address;

    private String role;

    private Date createTime;

    private Date updateTime;

    private Integer integral;

    private Integer credit;

    private String avatar;

    private String realName;
}