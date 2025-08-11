package com.etlpat.mapper;

import org.apache.ibatis.annotations.Param;

import com.etlpat.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


/**
 * @author lenovo
 * @description 针对表【tb_user】的数据库操作Mapper
 * @createDate 2025-08-04 11:30:03
 * @Entity com.etlpat.pojo.User
 */
public interface UserMapper extends BaseMapper<User> {
    User getOneByPhone(@Param("phone") String phone);
}




