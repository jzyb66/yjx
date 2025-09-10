package com.yjx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjx.module.LoginUser;
import com.yjx.pojo.User;
import com.yjx.util.Result;

public interface UserService extends IService<User> {

    // 登录方法
    LoginUser login(String usernameOrEmail, String password);

    // 注册方法
    Result<String> register(String userName, String userEmail, String userPasswordHash);

    // 新增：重置密码方法
    Result<String> resetPassword(String username, String email, String newPassword);

}