package com.etlpat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.User;
import com.etlpat.service.UserService;
import com.etlpat.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lenovo
 * @description 针对表【user(用户信息)】的数据库操作Service实现
 * @createDate 2025-06-19 21:15:44
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getOneByPhone(String phone) {
        return userMapper.getOneByPhone(phone);
    }
}




