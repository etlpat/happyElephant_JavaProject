package com.etlpat.service;

import com.etlpat.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author lenovo
 * @description 针对表【tb_user】的数据库操作Service
 * @createDate 2025-06-03 09:35:40
 */
public interface UserService extends IService<User> {
    User getUserByUsername(String username);

    void register(String username, String password);

    void updatePasswordByUserName(String username, String password);

    void updateDetail(User user);
}
