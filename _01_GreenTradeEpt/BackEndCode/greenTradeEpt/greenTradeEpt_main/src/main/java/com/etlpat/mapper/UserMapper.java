package com.etlpat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.etlpat.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author lenovo
 * @description 针对表【tb_user】的数据库操作Mapper
 * @createDate 2025-06-03 09:35:40
 * @Entity com.etlpat.pojo.User
 */
public interface UserMapper extends BaseMapper<User> {
    List<User> selectByUserName(@Param("userName") String userName);

    int updatePasswordByUserName(@Param("userName") String userName, @Param("password") String password);
}




