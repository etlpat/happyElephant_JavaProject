package com.etlpat.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etlpat.pojo.User;
import com.etlpat.service.UserService;
import com.etlpat.mapper.UserMapper;
import com.etlpat.utils.MD5Util;
import com.mysql.cj.util.StringUtils;
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

    @Override
    public void updateDetail(User user) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<User>()
                .eq(User::getUserName, user.getUserName())
                .set(!StringUtils.isNullOrEmpty(user.getNickName()), User::getNickName, user.getNickName())
                .set(!StringUtils.isNullOrEmpty(user.getRealName()), User::getRealName, user.getRealName())
                .set(!StringUtils.isNullOrEmpty(user.getPhone()), User::getPhone, user.getPhone())
                .set(!StringUtils.isNullOrEmpty(user.getAddress()), User::getAddress, user.getAddress())
                .set(!StringUtils.isNullOrEmpty(user.getIdentityNum()), User::getIdentityNum, user.getIdentityNum());
        userMapper.update(null, wrapper);
    }
}




