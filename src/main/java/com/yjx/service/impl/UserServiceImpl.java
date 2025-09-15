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

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

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
        newUser.setRoleId(2); // 默认角色为普通用户
        boolean save = this.save(newUser);
        return save ? Result.success("注册成功") : Result.fail("注册失败", 500);
    }

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

    // --- 新增的管理方法实现 ---

    @Override
    public Map<String, Object> getAllUsers(UserQueryDTO query) {
        Page<UserVO> page = new Page<>(query.getPageNum(), query.getPageSize());

        String sortField = query.getSortField();
        // 排序字段白名单校验，防止SQL注入
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

    @Override
    public Result<String> updateUser(UpdateUserDTO updateUserDTO) {
        User user = this.getById(updateUserDTO.getUserId());
        if (user == null) {
            return Result.fail("用户不存在", 404);
        }

        BeanUtils.copyProperties(updateUserDTO, user);

        boolean success = this.updateById(user);
        return success ? Result.success("更新成功") : Result.fail("更新失败", 500);
    }

    @Override
    public Result<String> deleteUser(Integer userId) {
        boolean success = this.removeById(userId);
        return success ? Result.success("删除成功") : Result.fail("删除失败", 500);
    }
}