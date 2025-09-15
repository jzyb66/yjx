package com.yjx.module;

import lombok.Data;

/**
 * 用于接收重置密码请求数据的模块 (DTO)
 */
@Data
public class ResetPasswordTO {
    private String username;
    private String email;
    private String newPassword;
}