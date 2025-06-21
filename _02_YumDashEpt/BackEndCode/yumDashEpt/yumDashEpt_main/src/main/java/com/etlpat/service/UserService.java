package com.etlpat.service;

import com.etlpat.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author lenovo
 * @description 针对表【user(用户信息)】的数据库操作Service
 * @createDate 2025-06-19 21:15:44
 */
public interface UserService extends IService<User> {
    User getOneByPhone(String phone);
}
