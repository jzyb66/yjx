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
     * 登录接口：接收账号/邮箱和密码
     */
    @PostMapping("/login") // 处理POST请求：/user/login
    public Result<LoginUser> login(
            @RequestParam String usernameOrEmail, // 接收前端传入的账号/邮箱
            @RequestParam String password         // 接收前端传入的密码
    ) {
        // 调用Service层的登录逻辑
        LoginUser loginUser = userService.login(usernameOrEmail, password);
        // 根据结果返回响应
        if (loginUser == null) {
            return Result.fail("用户名或密码错误", 400); // 失败：状态码400
        }
        return Result.success(loginUser); // 成功：返回用户信息
    }


    @PostMapping("/createUser")
    public Result<String> register(@RequestBody RegisterUser registerUser) {
        // 调用 Service 时，从 DTO 中获取参数
        return userService.register(
                registerUser.getUserName(),
                registerUser.getUserEmail(),
                registerUser.getUserPasswordHash()
        );
    }


}