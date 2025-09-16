package com.yjx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjx.module.LoginUser;
import com.yjx.module.UpdateUserDTO;
import com.yjx.module.UserQueryDTO;
import com.yjx.pojo.User;
import com.yjx.util.Result;

import java.util.Map;

public interface UserService extends IService<User> {

    // 登录方法
    LoginUser login(String usernameOrEmail, String password);

    // 注册方法 (已修改)
    Result<String> register(String userName, String userEmail, String userPasswordHash, Integer roleId);

    // 重置密码方法
    Result<String> resetPassword(String username, String email, String newPassword);

    // 分页获取所有用户
    Map<String, Object> getAllUsers(UserQueryDTO query);

    // 更新用户信息
    Result<String> updateUser(UpdateUserDTO updateUserDTO);

    /**
     * 删除用户 (已修改)
     * @param targetUserId 要删除的用户ID
     * @param currentUserId 当前登录的用户ID
     * @return 操作结果
     */
    Result<String> deleteUser(Integer targetUserId, Integer currentUserId);
}