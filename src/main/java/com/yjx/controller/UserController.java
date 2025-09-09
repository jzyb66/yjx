package com.yjx.controller;

import com.yjx.pojo.LoginUser;
import com.yjx.pojo.RegisterUser;
import com.yjx.service.UserService;
import com.yjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController // 标记为控制器，返回JSON数据
@RequestMapping("/user") // 接口前缀：/user
public class UserController {

    @Autowired // 自动注入UserService实例
    private UserService userService;

    /**
     * 登录接口
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
     * 注册接口
     */
    @PostMapping("/createUser")
    public Result<String> register(@RequestBody RegisterUser registerUser) {
        return userService.register(
                registerUser.getUserName(),
                registerUser.getUserEmail(),
                registerUser.getUserPasswordHash()
        );
    }

}