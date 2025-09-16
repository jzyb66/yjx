package com.yjx.module;

import lombok.Data;

/**
 * 用户注册信息传输对象 (DTO)。
 * 用于接收前端发送的注册或创建新用户的表单数据。
 */
@Data
public class RegisterUser {

    /**
     * 用户名。
     */
    private String userName;

    /**
     * 电子邮箱。
     */
    private String userEmail;

    /**
     * 用户手机号 (可选)。
     */
    private String userPhone;

    /**
     * 密码 (兼容前端字段 userPasswordHash)。
     */
    private String userPasswordHash;

    /**
     * 密码 (兼容前端字段 userPwd)。
     */
    private String userPwd;

    /**
     * 角色ID (用于后台创建用户时指定角色)。
     */
    private Integer roleId;

    /**
     * 获取密码的统一方法。
     * 优先返回 userPwd，如果为 null 或空，则返回 userPasswordHash。
     * 这是为了兼容前端可能使用不同字段名传递密码的情况。
     *
     * @return 用户的明文密码。
     */
    public String getPassword() {
        if (userPwd != null && !userPwd.isEmpty()) {
            return userPwd;
        }
        return userPasswordHash;
    }
}