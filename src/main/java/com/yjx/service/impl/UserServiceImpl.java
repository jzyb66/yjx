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
    
}