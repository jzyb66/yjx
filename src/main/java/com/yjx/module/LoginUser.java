package com.yjx.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 登录用户信息类（仅包含核心登录信息）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {
    private Integer userId;
    private String userName;
    private String userEmail;
    private String userPassword;
    private Integer roleId;
    private String userStatus;
    private String userPhone;
    private String userGender;
    private LocalDateTime userLastActive;


}