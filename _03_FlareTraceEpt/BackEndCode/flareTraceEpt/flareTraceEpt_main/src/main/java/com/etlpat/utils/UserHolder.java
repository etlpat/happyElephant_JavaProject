package com.etlpat.utils;

import com.etlpat.dto.UserDTO;


// ThreadLocal的工具类
public class UserHolder {// 用于保存当前登录的用户
    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();

    // 保存用户到ThreadLocal
    public static void saveUser(UserDTO user) {
        tl.set(user);
    }

    // 从ThreadLocal中获取用户
    public static UserDTO getUser() {
        return tl.get();
    }

    // 删除ThreadLocal中的用户
    public static void removeUser() {
        tl.remove();
    }
}
