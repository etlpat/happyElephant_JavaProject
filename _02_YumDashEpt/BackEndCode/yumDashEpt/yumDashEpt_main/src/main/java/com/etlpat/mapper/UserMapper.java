package com.etlpat.mapper;

import org.apache.ibatis.annotations.Param;

import com.etlpat.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author lenovo
 * @description 针对表【user(用户信息)】的数据库操作Mapper
 * @createDate 2025-06-19 21:15:44
 * @Entity com.etlpat.pojo.User
 */
public interface UserMapper extends BaseMapper<User> {
    User getOneByPhone(@Param("phone") String phone);
}




