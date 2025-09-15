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

    // 注册方法
    Result<String> register(String userName, String userEmail, String userPasswordHash);

    // 重置密码方法
    Result<String> resetPassword(String username, String email, String newPassword);

    // ================== 新增的管理方法声明 ==================

    /**
     * 分页获取所有用户
     * @param query 查询参数
     * @return 包含用户列表和总数的 Map
     */
    Map<String, Object> getAllUsers(UserQueryDTO query);

    /**
     * 更新用户信息
     * @param updateUserDTO 待更新的用户数据
     * @return 操作结果
     */
    Result<String> updateUser(UpdateUserDTO updateUserDTO);

    /**
     * 删除用户
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<String> deleteUser(Integer userId);
}