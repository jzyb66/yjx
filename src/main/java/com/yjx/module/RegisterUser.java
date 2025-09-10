package com.yjx.module;

import lombok.Data;

@Data
public class RegisterUser {
    private String userName;       // 和前端传的 key 完全一致
    private String userEmail;      // 和前端传的 key 完全一致
    private String userPasswordHash; // 和前端传的 key 完全一致
}
