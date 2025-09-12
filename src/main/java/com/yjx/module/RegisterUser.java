package com.yjx.module;

import lombok.Data;

@Data
public class RegisterUser {
    private String userName;
    private String userEmail;
    private String userPhone;
    private String userPasswordHash; // 用于 register.js
    private String userPwd;          // 用于 user.js

    public String getPassword() {
        if (userPwd != null && !userPwd.isEmpty()) {
            return userPwd;
        }
        return userPasswordHash;
    }
}