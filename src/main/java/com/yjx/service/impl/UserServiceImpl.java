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
import org.springframework.util.StringUtils;

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
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", usernameOrEmail).or().eq("user_email", usernameOrEmail);
        User user = this.getOne(queryWrapper);

        if (user == null) {
            return null;
        }

        String encryptedPassword = Md5Password.generateMD5(password);
        if (!encryptedPassword.equals(user.getUserPasswordHash())) {
            return null;
        }

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
        if (this.getOne(new QueryWrapper<User>().eq("user_name", userName)) != null) {
            return Result.fail("用户名已存在", 409);
        }
        if (this.getOne(new QueryWrapper<User>().eq("user_email", userEmail)) != null) {
            return Result.fail("邮箱已存在", 409);
        }

        User newUser = new User();
        newUser.setUserName(userName);
        newUser.setUserEmail(userEmail);
        newUser.setUserPasswordHash(Md5Password.generateMD5(userPasswordHash));
        newUser.setRoleId(roleId != null ? roleId : 2);

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
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", username);
        queryWrapper.eq("user_email", email);
        User user = this.getOne(queryWrapper);

        if (user == null) {
            return Result.fail("用户名与邮箱不匹配，请检查后重试。", 400);
        }

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

        String sortField = query.getSortField();
        if (!"user_created_at".equals(sortField) && !"user_id".equals(sortField)) {
            sortField = "user_created_at";
        }
        query.setSortField(sortField);

        IPage<UserVO> userPage = baseMapper.selectUserPage(page, query);
        Map<String, Object> result = new HashMap<>();
        result.put("userList", userPage.getRecords());
        result.put("count", userPage.getTotal());
        return result;
    }

    /**
     * 更新用户信息，并增加对用户名和邮箱的唯一性校验。
     *
     * @param updateUserDTO 包含待更新用户信息的 DTO。
     * @return 包含操作结果的 Result 对象。
     */
    @Override
    public Result<String> updateUser(UpdateUserDTO updateUserDTO) {
        // 校验1：检查待更新的用户是否存在
        User user = this.getById(updateUserDTO.getUserId());
        if (user == null) {
            return Result.fail("用户不存在", 404);
        }

        // 业务校验：检查更新后的用户名或邮箱是否与系统内其他用户冲突。
        // 校验2：检查用户名是否被修改，且新用户名是否已存在
        if (StringUtils.hasText(updateUserDTO.getUserName()) && !user.getUserName().equals(updateUserDTO.getUserName())) {
            QueryWrapper<User> usernameWrapper = new QueryWrapper<>();
            usernameWrapper.eq("user_name", updateUserDTO.getUserName());
            usernameWrapper.ne("user_id", updateUserDTO.getUserId()); // 排除当前用户自身
            if (this.baseMapper.exists(usernameWrapper)) {
                return Result.fail("更新失败，该用户名已存在", 409);
            }
        }

        // 校验3：检查邮箱是否被修改，且新邮箱是否已存在
        if (StringUtils.hasText(updateUserDTO.getUserEmail()) && !user.getUserEmail().equals(updateUserDTO.getUserEmail())) {
            QueryWrapper<User> emailWrapper = new QueryWrapper<>();
            emailWrapper.eq("user_email", updateUserDTO.getUserEmail());
            emailWrapper.ne("user_id", updateUserDTO.getUserId()); // 排除当前用户自身
            if (this.baseMapper.exists(emailWrapper)) {
                return Result.fail("更新失败，该邮箱已被注册", 409);
            }
        }

        // 复制属性并执行更新
        BeanUtils.copyProperties(updateUserDTO, user);
        boolean success = this.updateById(user);
        return success ? Result.success("更新成功") : Result.fail("更新失败", 500);
    }

    /**
     * 删除用户，并增加密码验证逻辑。
     *
     * @param userIdToDelete 待删除用户的ID。
     * @param adminId        当前操作员的ID。
     * @param adminPassword  当前操作员的密码。
     * @return 包含操作结果的 Result 对象。
     */
    @Override
    public Result<String> deleteUser(Integer userIdToDelete, Integer adminId, String adminPassword) {
        // 校验1：用户不能删除自己的账号
        if (Objects.equals(userIdToDelete, adminId)) {
            return Result.fail("操作失败：不能删除当前登录的账号！", 400);
        }

        // 校验 2：验证执行删除操作的管理员是否存在
        User admin = this.getById(adminId);
        if (admin == null) {
            return Result.fail("操作员用户不存在，无法验证权限。", 404);
        }

        // 校验 3：验证操作员的密码是否正确
        String encryptedPassword = Md5Password.generateMD5(adminPassword);
        if (!encryptedPassword.equals(admin.getUserPasswordHash())) {
            return Result.fail("密码错误，验证失败。", 401);
        }

        // 校验通过后，执行删除操作
        boolean success = this.removeById(userIdToDelete);
        return success ? Result.success("删除成功") : Result.fail("删除失败，可能目标用户不存在。", 500);
    }
}