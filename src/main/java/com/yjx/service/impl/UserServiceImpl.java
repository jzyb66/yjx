package com.yjx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjx.mapper.UserMapper;
import com.yjx.pojo.LoginUser;
import com.yjx.pojo.User;
import com.yjx.service.UserService;
import com.yjx.util.Md5Password;
import com.yjx.util.Result;
import org.springframework.stereotype.Service;

@Service // 标记为服务层组件
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public LoginUser login(String usernameOrEmail, String password) {
        // 1. 根据账号或邮箱查询用户（MyBatis-Plus的条件构造器）
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", usernameOrEmail) // 匹配用户名
                .or() // 或
                .eq("user_email", usernameOrEmail); // 匹配邮箱
        User user = this.getOne(queryWrapper); // 执行查询

        // 2. 校验用户是否存在
        if (user == null) {
            return null; // 用户名/邮箱不存在
        }

        // 3. 校验密码（MD5加密后比对，避免明文存储密码）
        String encryptedPassword = Md5Password.generateMD5(password);
        if (!encryptedPassword.equals(user.getUserPasswordHash())) {
            return null; // 密码错误
        }

        // 4. 封装登录用户信息（返回给前端，隐藏敏感字段如密码）
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getUserId());
        loginUser.setUserName(user.getUserName());
        loginUser.setUserEmail(user.getUserEmail());
        loginUser.setRoleId(user.getRoleId()); // 角色ID（用于后续权限控制）
        loginUser.setUserPhone(user.getUserPhone());

        return loginUser; // 登录成功，返回用户信息
    }

    @Override
    public Result<String> register(String userName, String userEmail, String userPasswordHash) {
        //判断用户名是否已存在
        User user = this.getOne(new QueryWrapper<User>().eq("user_name", userName));
        if (user != null) {
            return Result.fail("用户名已存在", 400);
        }
        //判断邮箱是否已存在
        user = this.getOne(new QueryWrapper<User>().eq("user_email", userEmail));
        if (user != null) {
            return Result.fail("邮箱已存在", 400);
        }
        User newUser = new User();
        newUser.setUserName(userName);
        newUser.setUserEmail(userEmail);
        newUser.setUserPasswordHash(Md5Password.generateMD5(userPasswordHash));
        newUser.setRoleId("2");
        boolean save = this.save(newUser);

        return save ? Result.success("注册成功") : Result.fail("注册失败", 500);
    }


}