package com.yjx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjx.mapper.UserMapper;
import com.yjx.module.LoginUser;
import com.yjx.module.UpdateUserDTO;
import com.yjx.module.UserQueryDTO;
import com.yjx.module.UserVO;
import com.yjx.pojo.User;
import com.yjx.service.UserService;
import com.yjx.util.Md5Password;
import com.yjx.util.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 用户服务接口的实现类。
 * 提供了用户登录、注册、信息管理等功能的具体业务逻辑。
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 处理用户登录逻辑。
     *
     * @param usernameOrEmail 登录时使用的用户名或邮箱。
     * @param password 登录密码（明文）。
     * @return 验证成功返回封装了用户信息的 LoginUser 对象，失败则返回 null。
     */
    @Override
    public LoginUser login(String usernameOrEmail, String password) {
        // 1. 构造查询条件，支持用户名或邮箱登录
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", usernameOrEmail).or().eq("user_email", usernameOrEmail);
        User user = this.getOne(queryWrapper);

        // 2. 如果数据库中不存在该用户，直接返回null
        if (user == null) {
            return null;
        }

        // 3. 将用户输入的明文密码通过MD5加密，与数据库中存储的哈希值进行比对
        String encryptedPassword = Md5Password.generateMD5(password);
        if (!encryptedPassword.equals(user.getUserPasswordHash())) {
            return null; // 密码不匹配
        }

        // 4. 验证通过，封装需要返回给前端的用户信息到LoginUser对象
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getUserId());
        loginUser.setUserName(user.getUserName());
        loginUser.setUserEmail(user.getUserEmail());
        loginUser.setRoleId(user.getRoleId());
        loginUser.setUserPhone(user.getUserPhone());
        return loginUser;
    }

    /**
     * 处理用户注册或创建新用户的逻辑。
     *
     * @param userName 用户名。
     * @param userEmail 电子邮箱。
     * @param userPasswordHash 明文密码。
     * @param roleId 角色ID，若为null则默认为普通用户(2)。
     * @return 包含操作结果的 Result 对象。
     */
    @Override
    public Result<String> register(String userName, String userEmail, String userPasswordHash, Integer roleId) {
        // 检查用户名是否已存在
        if (this.getOne(new QueryWrapper<User>().eq("user_name", userName)) != null) {
            return Result.fail("用户名已存在", 400);
        }
        // 检查邮箱是否已存在
        if (this.getOne(new QueryWrapper<User>().eq("user_email", userEmail)) != null) {
            return Result.fail("邮箱已存在", 400);
        }

        // 创建新的用户实体
        User newUser = new User();
        newUser.setUserName(userName);
        newUser.setUserEmail(userEmail);
        newUser.setUserPasswordHash(Md5Password.generateMD5(userPasswordHash));
        newUser.setRoleId(roleId != null ? roleId : 2); // 如果roleId未提供，默认为普通用户

        // 保存到数据库
        boolean save = this.save(newUser);
        return save ? Result.success("注册成功") : Result.fail("注册失败", 500);
    }

    /**
     * 处理重置密码的逻辑。
     *
     * @param username 用户名。
     * @param email 电子邮箱。
     * @param newPassword 新的明文密码。
     * @return 包含操作结果的 Result 对象。
     */
    @Override
    public Result<String> resetPassword(String username, String email, String newPassword) {
        // 查询用户名和邮箱是否匹配的唯一用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", username);
        queryWrapper.eq("user_email", email);
        User user = this.getOne(queryWrapper);

        if (user == null) {
            return Result.fail("用户名与邮箱不匹配，请检查后重试。", 400);
        }

        // 更新密码
        user.setUserPasswordHash(Md5Password.generateMD5(newPassword));
        boolean updated = this.updateById(user);

        return updated ? Result.success("密码重置成功！") : Result.fail("密码重置失败，请稍后再试。", 500);
    }

    /**
     * 分页查询所有用户。
     *
     * @param query 查询参数 DTO。
     * @return 封装了用户列表和总数的 Map 对象。
     */
    @Override
    public Map<String, Object> getAllUsers(UserQueryDTO query) {
        Page<UserVO> page = new Page<>(query.getPageNum(), query.getPageSize());

        // 安全校验：防止SQL注入，只允许特定的排序字段
        String sortField = query.getSortField();
        if (!"user_created_at".equals(sortField) && !"user_id".equals(sortField)) {
            sortField = "user_created_at"; // 默认排序字段
        }
        query.setSortField(sortField);

        IPage<UserVO> userPage = baseMapper.selectUserPage(page, query);
        Map<String, Object> result = new HashMap<>();
        result.put("userList", userPage.getRecords());
        result.put("count", userPage.getTotal());
        return result;
    }

    /**
     * 更新用户信息。
     *
     * @param updateUserDTO 包含待更新用户信息的 DTO。
     * @return 包含操作结果的 Result 对象。
     */
    @Override
    public Result<String> updateUser(UpdateUserDTO updateUserDTO) {
        User user = this.getById(updateUserDTO.getUserId());
        if (user == null) {
            return Result.fail("用户不存在", 404);
        }
        // 使用Spring的工具类，将DTO中的非空属性值复制到POJO对象中
        BeanUtils.copyProperties(updateUserDTO, user);
        boolean success = this.updateById(user);
        return success ? Result.success("更新成功") : Result.fail("更新失败", 500);
    }

    /**
     * 删除用户。
     *
     * @param targetUserId 待删除用户的ID。
     * @param currentUserId 当前操作员的ID。
     * @return 包含操作结果的 Result 对象。
     */
    @Override
    public Result<String> deleteUser(Integer targetUserId, Integer currentUserId) {
        // 业务校验：用户不能删除自己
        if (Objects.equals(targetUserId, currentUserId)) {
            return Result.fail("操作失败：不能删除当前登录的账号！", 400);
        }

        boolean success = this.removeById(targetUserId);
        return success ? Result.success("删除成功") : Result.fail("删除失败，可能用户不存在或数据库异常。", 500);
    }
}