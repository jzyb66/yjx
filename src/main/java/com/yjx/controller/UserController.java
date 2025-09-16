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
        // 修改：调用 getPassword() 方法和新增的 getRoleId()
        return userService.register(
                registerUser.getUserName(),
                registerUser.getUserEmail(),
                registerUser.getPassword(), // <--- 此处逻辑不变
                registerUser.getRoleId()    // <--- 新增传递 roleId
        );
    }

    /**
     * 通过验证信息重置密码接口 (已修改为使用DTO)
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
     * 获取所有用户（分页、排序、搜索）
     * @param query 查询参数
     * @return 用户数据
     */
    @GetMapping("/getAllUsers")
    public Result<Map<String, Object>> getAllUsers(@ModelAttribute UserQueryDTO query) {
        Map<String, Object> data = userService.getAllUsers(query);
        return Result.success(data);
    }

    /**
     * 更新用户信息
     * @param updateUserDTO 用户更新数据
     * @return 操作结果
     */
    @PostMapping("/updateUser")
    public Result<String> updateUser(@RequestBody UpdateUserDTO updateUserDTO) {
        return userService.updateUser(updateUserDTO);
    }

    /**
     * 删除用户 (已修改)
     * @param targetUserId 要删除的用户ID
     * @param currentUserId 当前登录的用户ID
     * @return 操作结果
     */
    @DeleteMapping("/delete") // URL修改为 /delete
    public Result<String> deleteUser(@RequestParam Integer targetUserId, @RequestParam Integer currentUserId) {
        return userService.deleteUser(targetUserId, currentUserId);
    }

}