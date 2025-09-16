package com.yjx.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 登录用户信息传输对象 (DTO)。
 * 用于在用户成功登录后，向前端返回部分安全的用户信息。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {

    /**
     * 用户ID。
     */
    private Integer userId;

    /**
     * 用户名。
     */
    private String userName;

    /**
     * 用户电子邮箱。
     */
    private String userEmail;

    /**
     * 用户密码 (此字段在此DTO中未使用，为安全起见不应返回给前端)。
     */
    private String userPassword;

    /**
     * 角色ID。
     */
    private Integer roleId;

    /**
     * 账户状态。
     */
    private String userStatus;

    /**
     * 用户手机号码。
     */
    private String userPhone;

    /**
     * 用户性别。
     */
    private String userGender;

    /**
     * 用户上次活跃时间。
     */
    private LocalDateTime userLastActive;
}