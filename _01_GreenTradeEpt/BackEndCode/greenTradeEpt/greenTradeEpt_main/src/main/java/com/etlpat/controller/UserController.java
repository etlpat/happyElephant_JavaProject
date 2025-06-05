package com.etlpat.controller;

import com.etlpat.pojo.Result;
import com.etlpat.pojo.User;
import com.etlpat.service.UserService;
import com.etlpat.service.impl.UserServiceImpl;
import com.etlpat.utils.JWTUtil;
import com.etlpat.utils.MD5Util;
import com.etlpat.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


// 用户表
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    // 用户注册
    @PostMapping("/register")// 请求方式为post
    public Result register(String username, String password1, String password2) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password1) || StringUtils.isEmpty(password2)) {
            return Result.error("注册信息不能为空");
        }

        // 查询该用户是否已经注册
        User user = userService.getUserByUsername(username);
        if (user != null) {// 若用户已经被注册
            return Result.error("用户名已被占用");
        }

        // 判断两次密码是否不一致
        if (!password1.equals(password2)) {
            return Result.error("两次密码不一致");
        }

        // 注册该用户
        userService.register(username, password1);
        return Result.success();
    }


    // 用户登录
    @PostMapping("/login")
    public Result login(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return Result.error("登录信息不能为空");
        }

        // ①根据用户名获取用户
        User user = userService.getUserByUsername(username);
        if (user == null) {// 若用户不存在
            return Result.error("用户名错误");
        }

        // ②判断密码是否正确
        boolean isTrue = MD5Util.match(password, user.getPassword());
        if (!isTrue) {// 若密码错误
            return Result.error("密码错误");
        }

        // ③登录成功，将该用户的JWT令牌发送给前端以及Redis
        HashMap<String, Object> userMap = new HashMap<>();// JWT令牌的有效载荷
        userMap.put("username", user.getUserName());
        String token = JWTUtil.createToken(userMap);// 获取该用户的JWT令牌
        stringRedisTemplate.opsForValue().set("etlpat:greenTradeEpt:token:" + user.getUserName()
                , token, 7, TimeUnit.DAYS);// 将JWT令牌存入Redis中
        return Result.success(token);// 将令牌作为响应数据，发送给前端
    }


    // 获取当前登录用户的全部信息（通过令牌获取当前正在登录的用户）
    @GetMapping("/userInfo")
    public Result userInfo() {
        Map<String, Object> userMap = ThreadLocalUtil.get();// 从ThreadLocal中获取本次登录的用户的map
        User user = userService.getUserByUsername(userMap.get("username").toString());
        return Result.success(user);
    }


//    // 更新用户信息
//    @PutMapping("/update")
//    public Result update(@RequestBody User user) {// @RequestBody注解用于接收json数据，并将其转换为实体类
//        userService.update(user);
//        return Result.success();
//    }


    // 更新用户密码
    // 注意：使用map集合接收密码，其中(原密码：old_pwd，新密码：new_pwd，确认新密码：re_pwd)
    @PatchMapping("/updatePassword")
    public Result updatePassword(@RequestBody Map<String, String> pwdMap) {// 接收请求体中的json数据
        Map<String, Object> userMap = ThreadLocalUtil.get();// 获取本次登录用户信息
        String username = (String) userMap.get("username");
        String oldPwd = pwdMap.get("old_pwd");
        String newPwd = pwdMap.get("new_pwd");
        String rePwd = pwdMap.get("re_pwd");

        // 检验密码是否为空
        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
            return Result.error("缺少必要参数");
        }

        // 验证旧密码是否正确
        String password = userService.getUserByUsername(username).getPassword();
        if (!MD5Util.match(oldPwd, password)) {
            return Result.error("原密码书写不正确");
        }

        // 验证两次新密码是否一致
        if (!newPwd.equals(rePwd)) {
            return Result.error("两次新密码填写不一致");
        }

        // 验证全部正确，更新密码，并删除该用户原本在Redis中的JWT令牌
        userService.updatePasswordByUserName(username, newPwd);
        stringRedisTemplate.delete("etlpat:greenTradeEpt:token:" + username);// 删除Redis中的令牌
        return Result.success();
    }
}
