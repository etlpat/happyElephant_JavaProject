package com.etlpat.controller;

import com.etlpat.pojo.R;
import com.etlpat.pojo.User;
import com.etlpat.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


// 前台客户端 -- 用户表
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;


    // 移动端用户登录
    @PostMapping("/login")
    public R<User> login(@RequestBody User user, HttpServletRequest request) {
        User byPhone = userService.getOneByPhone(user.getPhone());

        // 若手机号对应的用户不存在，则自动注册新用户
        if (byPhone == null) {
            byPhone = new User();
            byPhone.setPhone(user.getPhone());
            byPhone.setName("用户" + user.getPhone());
            byPhone.setStatus(1);
            userService.save(byPhone);
        } else {
            // 判断状态是否已禁用
            if (byPhone.getStatus() == 0) {
                return R.error("用户已被禁用");
            }
        }

        request.getSession().setAttribute("user", byPhone.getId());// 将当前登录用户的id存入session中
        return R.success(byPhone);
    }


    // 移动端用户退登
    @PostMapping("/loginout")
    public R logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");// 删除session中当前登录用户的id
        return R.success();
    }

}
