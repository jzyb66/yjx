package com.yjx.controller;

import com.yjx.module.*;
import com.yjx.service.UserService;
import com.yjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户账户及认证API控制器。
 * 负责处理用户登录、注册、重置密码以及后台用户管理等功能。
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 自动注入用户服务层的Bean。
     */
    @Autowired
    private UserService userService;

    /**
     * 用户登录接口。
     *
     * @param usernameOrEmail 用户名或电子邮箱。
     * @param password 密码。
     * @return 如果登录成功，返回包含用户基本信息的结果对象；否则返回失败信息。
     */
    @PostMapping("/login")
    public Result<LoginUser> login(@RequestParam String usernameOrEmail, @RequestParam String password) {
        LoginUser loginUser = userService.login(usernameOrEmail, password);
        if (loginUser == null) {
            return Result.fail("用户名或密码错误", 400);
        }
        return Result.success(loginUser);
    }

    /**
     * 用户注册或后台创建新用户接口。
     *
     * @param registerUser 包含新用户注册信息的DTO对象。
     * @return 表示操作成功或失败的结果对象。
     */
    @PostMapping("/createUser")
    public Result<String> register(@RequestBody RegisterUser registerUser) {
        return userService.register(
                registerUser.getUserName(),
                registerUser.getUserEmail(),
                registerUser.getPassword(),
                registerUser.getRoleId()
        );
    }

    /**
     * 通过验证信息（用户名和邮箱）重置密码接口。
     *
     * @param resetPasswordTO 包含用户名、邮箱和新密码的DTO对象。
     * @return 表示操作成功或失败的结果对象。
     */
    @PostMapping("/resetPasswordByVerification")
    public Result<String> resetPassword(@RequestBody ResetPasswordTO resetPasswordTO) {
        return userService.resetPassword(
                resetPasswordTO.getUsername(),
                resetPasswordTO.getEmail(),
                resetPasswordTO.getNewPassword()
        );
    }

    /**
     * 获取所有用户列表，支持分页、排序和关键字搜索。
     *
     * @param query 包含查询条件的DTO对象。
     * @return 包含用户列表和总记录数的结果Map。
     */
    @GetMapping("/getAllUsers")
    public Result<Map<String, Object>> getAllUsers(@ModelAttribute UserQueryDTO query) {
        Map<String, Object> data = userService.getAllUsers(query);
        return Result.success(data);
    }

    /**
     * 更新指定用户的信息。
     *
     * @param updateUserDTO 包含待更新用户ID和新信息的DTO对象。
     * @return 表示操作成功或失败的结果对象。
     */
    @PostMapping("/updateUser")
    public Result<String> updateUser(@RequestBody UpdateUserDTO updateUserDTO) {
        return userService.updateUser(updateUserDTO);
    }

    /**
     * 删除指定ID的用户。
     *
     * @param targetUserId 要删除的用户ID。
     * @param currentUserId 执行此操作的当前登录用户的ID，用于权限校验。
     * @return 表示操作成功或失败的结果对象。
     */
    @DeleteMapping("/delete")
    public Result<String> deleteUser(@RequestParam Integer targetUserId, @RequestParam Integer currentUserId) {
        return userService.deleteUser(targetUserId, currentUserId);
    }
}