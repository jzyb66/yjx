package com.yjx.controller;

import com.yjx.module.LoginUser;
import com.yjx.module.RegisterUser;
import com.yjx.module.ResetPasswordModule; // 1. 导入新模块
import com.yjx.service.UserService;
import com.yjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登录接口 (保持不变)
     */
    @PostMapping("/login")
    public Result<LoginUser> login(
            @RequestParam String usernameOrEmail,
            @RequestParam String password
    ) {
        LoginUser loginUser = userService.login(usernameOrEmail, password);
        if (loginUser == null) {
            return Result.fail("用户名或密码错误", 400);
        }
        return Result.success(loginUser);
    }

    /**
     * 注册接口 (保持不变)
     */
    @PostMapping("/createUser")
    public Result<String> register(@RequestBody RegisterUser registerUser) {
        return userService.register(
                registerUser.getUserName(),
                registerUser.getUserEmail(),
                registerUser.getUserPasswordHash()
        );
    }

    /**
     * 通过验证信息重置密码接口 (已修改为使用DTO)
     */
    @PostMapping("/resetPasswordByVerification")
    // 2. 方法参数修改为接收 ResetPasswordModule 对象
    public Result<String> resetPassword(@RequestBody ResetPasswordModule resetPasswordModule) {
        // 3. 从 module 对象中获取数据传递给 service
        return userService.resetPassword(
                resetPasswordModule.getUsername(),
                resetPasswordModule.getEmail(),
                resetPasswordModule.getNewPassword()
        );
    }
}