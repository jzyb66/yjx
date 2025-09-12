package com.yjx.controller;

import com.yjx.module.*;
import com.yjx.service.UserService;
import com.yjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
     * 注册/创建用户接口
     */
    @PostMapping("/createUser")
    public Result<String> register(@RequestBody RegisterUser registerUser) {
        // 修改：调用 getPassword() 方法而不是直接获取字段
        return userService.register(
                registerUser.getUserName(),
                registerUser.getUserEmail(),
                registerUser.getPassword() // <--- 修改在这里
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

    /**
     * 获取所有用户（分页、排序、搜索）
     * @param query 查询参数
     * @return 用户数据
     */
    @GetMapping("/getAllUsers")
    public Result<Map<String, Object>> getAllUsers(@ModelAttribute UserQueryModule query) {
        Map<String, Object> data = userService.getAllUsers(query);
        return Result.success(data);
    }

    /**
     * 更新用户信息
     * @param updateUserModule 用户更新数据
     * @return 操作结果
     */
    @PostMapping("/updateUser")
    public Result<String> updateUser(@RequestBody UpdateUserModule updateUserModule) {
        return userService.updateUser(updateUserModule);
    }

    /**
     * 删除用户
     * @param userId 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/delete/{userId}")
    public Result<String> deleteUser(@PathVariable Integer userId) {
        return userService.deleteUser(userId);
    }


}