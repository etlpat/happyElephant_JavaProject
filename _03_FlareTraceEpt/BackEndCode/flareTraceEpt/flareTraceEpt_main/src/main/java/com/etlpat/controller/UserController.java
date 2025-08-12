package com.etlpat.controller;

import cn.hutool.core.bean.BeanUtil;
import com.etlpat.dto.LoginFormDTO;
import com.etlpat.dto.Result;
import com.etlpat.dto.UserDTO;
import com.etlpat.pojo.User;
import com.etlpat.pojo.UserInfo;
import com.etlpat.service.UserInfoService;
import com.etlpat.service.UserService;
import com.etlpat.utils.RedisConstants;
import com.etlpat.utils.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    // 发送手机验证码
    @PostMapping("code")
    public Result sendCode(@RequestParam("phone") String phone) {
        return userService.sendCode(phone);
    }


    // 登录功能
    // @param loginForm 登录参数，包含手机号、验证码；或者手机号、密码
    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDTO loginForm) {
        return userService.login(loginForm);
    }


    // 登出功能
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        String tokenKey = RedisConstants.LOGIN_USER_KEY + token;
        stringRedisTemplate.delete(tokenKey);
        return Result.ok();
    }


    // 获取当前登录的user信息
    @GetMapping("/me")
    public Result me() {
        return Result.ok(UserHolder.getUser());
    }


    // 查询用户详细信息
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long userId) {
        UserInfo info = userInfoService.getById(userId);
        if (info == null) {
            return Result.ok();
        }
        info.setCreateTime(null);
        info.setUpdateTime(null);
        return Result.ok(info);
    }


    // 根据id查询用户
    @GetMapping("/{id}")
    public Result queryUserById(@PathVariable("id") Long userId) {
        User user = userService.getById(userId);
        if (user == null) {
            return Result.ok();
        }
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        return Result.ok(userDTO);
    }


    // 签到（基于Redis的BitMap位图）
    @PostMapping("/sign")
    public Result sign() {
        return userService.sign();
    }


    // 本月的连续签到统计（基于Redis的BitMap位图）
    @GetMapping("/sign/count")
    public Result signCount() {
        return userService.signCount();
    }


}
