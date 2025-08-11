package com.etlpat.service;

import com.etlpat.dto.LoginFormDTO;
import com.etlpat.dto.Result;
import com.etlpat.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpSession;


/**
 * @author lenovo
 * @description 针对表【tb_user】的数据库操作Service
 * @createDate 2025-08-04 11:30:04
 */
public interface UserService extends IService<User> {

    Result sendCode(String phone);

    Result login(LoginFormDTO loginForm);

    User saveUser(String phone);
}
