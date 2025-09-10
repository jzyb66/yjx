package com.yjx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjx.mapper.UserMapper;
import com.yjx.module.LoginUser;
import com.yjx.pojo.User;
import com.yjx.service.UserService;
import com.yjx.util.Md5Password;
import com.yjx.util.Result;
import org.springframework.stereotype.Service;

@Service // 标记为服务层组件
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    // login 和 register 方法保持不变...
    @Override
    public LoginUser login(String usernameOrEmail, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", usernameOrEmail).or().eq("user_email", usernameOrEmail);
        User user = this.getOne(queryWrapper);
        if (user == null) return null;
        String encryptedPassword = Md5Password.generateMD5(password);
        if (!encryptedPassword.equals(user.getUserPasswordHash())) return null;
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getUserId());
        loginUser.setUserName(user.getUserName());
        loginUser.setUserEmail(user.getUserEmail());
        loginUser.setRoleId(user.getRoleId());
        loginUser.setUserPhone(user.getUserPhone());
        return loginUser;
    }

    @Override
    public Result<String> register(String userName, String userEmail, String userPasswordHash) {
        if (this.getOne(new QueryWrapper<User>().eq("user_name", userName)) != null) {
            return Result.fail("用户名已存在", 400);
        }
        if (this.getOne(new QueryWrapper<User>().eq("user_email", userEmail)) != null) {
            return Result.fail("邮箱已存在", 400);
        }
        User newUser = new User();
        newUser.setUserName(userName);
        newUser.setUserEmail(userEmail);
        newUser.setUserPasswordHash(Md5Password.generateMD5(userPasswordHash));
        newUser.setRoleId(2);
        boolean save = this.save(newUser);
        return save ? Result.success("注册成功") : Result.fail("注册失败", 500);
    }

    /**
     * 新增：重置密码的实现
     * @param username 用户名
     * @param email 邮箱
     * @param newPassword 新密码
     * @return Result<String>
     */
    @Override
    public Result<String> resetPassword(String username, String email, String newPassword) {
        // 1. 根据用户名和邮箱查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", username);
        queryWrapper.eq("user_email", email);
        User user = this.getOne(queryWrapper);

        // 2. 如果用户不存在，返回错误信息
        if (user == null) {
            return Result.fail("用户名与邮箱不匹配，请检查后重试。", 400);
        }

        // 3. 更新密码（MD5加密）
        user.setUserPasswordHash(Md5Password.generateMD5(newPassword));
        boolean updated = this.updateById(user);

        // 4. 返回结果
        return updated ? Result.success("密码重置成功！") : Result.fail("密码重置失败，请稍后再试。", 500);
    }
}