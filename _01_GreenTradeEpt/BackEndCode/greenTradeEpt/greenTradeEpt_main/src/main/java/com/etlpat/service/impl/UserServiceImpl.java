package com.etlpat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.User;
import com.etlpat.service.UserService;
import com.etlpat.mapper.UserMapper;
import com.etlpat.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author lenovo
 * @description 针对表【tb_user】的数据库操作Service实现
 * @createDate 2025-06-03 09:35:40
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public User getUserByUsername(String username) {
        List<User> users = userMapper.selectByUserName(username);
        if (users.size() == 0) {
            return null;
        }
        return users.get(0);
    }

    @Override
    public void register(String username, String password) {
        User user = new User();
        user.setUserName(username);
        user.setPassword(MD5Util.getMD5String(password));// 将原密码用MD5加密后再插入表中
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userMapper.insert(user);
    }

    @Override
    public void updatePasswordByUserName(String username, String password) {
        userMapper.updatePasswordByUserName(username, MD5Util.getMD5String(password));
    }
}




