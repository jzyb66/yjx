package com.yjx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjx.pojo.LoginUser;
import com.yjx.pojo.User;
import com.yjx.util.Result;

public interface UserService extends IService<User> {

    // 登录方法：接收账号/邮箱和密码，返回登录用户信息
    LoginUser login(String usernameOrEmail, String password);
    
    //注册
    Result<String> register(String userName, String userEmail, String userPasswordHash);

}