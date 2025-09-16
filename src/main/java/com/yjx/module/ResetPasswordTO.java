package com.yjx.module;

import lombok.Data;

/**
 * 重置密码数据传输对象 (DTO)。
 * 用于接收前端发送的、通过验证信息重置密码时所需的数据。
 * TO (Transfer Object) 在此场景下与 DTO 作用相同。
 */
@Data
public class ResetPasswordTO {

    /**
     * 用户的账户名。
     */
    private String username;

    /**
     * 用户绑定的电子邮箱。
     */
    private String email;

    /**
     * 用户设置的新密码（明文）。
     */
    private String newPassword;
}