package com.yjx.module;

import lombok.Data;

@Data
public class RegisterUser {
    private String userName;
    private String userEmail;
    private String userPhone;
    private String userPasswordHash; // 用于 register.js
    private String userPwd;          // 用于 user.js
    private Integer roleId;          // 新增：用于接收添加用户时选择的角色ID

    public String getPassword() {
        if (userPwd != null && !userPwd.isEmpty()) {
            return userPwd;
        }
        return userPasswordHash;
    }
}