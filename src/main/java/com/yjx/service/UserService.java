package com.yjx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjx.module.LoginUser;
import com.yjx.module.UpdateUserDTO;
import com.yjx.module.UserQueryDTO;
import com.yjx.pojo.User;
import com.yjx.util.Result;

import java.util.Map;

/**
 * 用户服务接口。
 * 定义了用户相关的业务逻辑，包括认证、信息管理等。
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录。
     *
     * @param usernameOrEmail 用户名或电子邮箱。
     * @param password 用户的明文密码。
     * @return 登录成功则返回 LoginUser 对象，封装了传递给前端的安全用户信息；否则返回 null。
     */
    LoginUser login(String usernameOrEmail, String password);

    /**
     * 用户注册。
     *
     * @param userName 用户名。
     * @param userEmail 电子邮箱。
     * @param userPasswordHash 明文密码。
     * @param roleId 角色ID。
     * @return 包含操作结果的 Result 对象。
     */
    Result<String> register(String userName, String userEmail, String userPasswordHash, Integer roleId);

    /**
     * 重置密码。
     *
     * @param username 用户名。
     * @param email 电子邮箱。
     * @param newPassword 新的明文密码。
     * @return 包含操作结果的 Result 对象。
     */
    Result<String> resetPassword(String username, String email, String newPassword);

    /**
     * 分页获取所有用户信息。
     *
     * @param query 查询参数 DTO。
     * @return 封装了用户列表和总数的 Map 对象。
     */
    Map<String, Object> getAllUsers(UserQueryDTO query);

    /**
     * 更新用户信息。
     *
     * @param updateUserDTO 包含待更新用户信息的 DTO。
     * @return 包含操作结果的 Result 对象。
     */
    Result<String> updateUser(UpdateUserDTO updateUserDTO);

    /**
     * 删除用户（需要密码验证）。
     *
     * @param userIdToDelete 待删除用户的ID。
     * @param adminId        当前操作员的ID。
     * @param adminPassword  当前操作员的密码。
     * @return 包含操作结果的 Result 对象。
     */
    Result<String> deleteUser(Integer userIdToDelete, Integer adminId, String adminPassword);
}