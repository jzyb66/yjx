package com.yjx.module;

import lombok.Data;

/**
 * 更新用户信息数据传输对象 (DTO)。
 * 用于封装从前端发送的待更新的用户数据。
 */
@Data
public class UpdateUserDTO {
    /**
     * 待更新的用户ID。
     */
    private Integer userId;
    /**
     * 新的用户名。
     */
    private String userName;
    /**
     * 新的电子邮箱。
     */
    private String userEmail;
    /**
     * 新的角色ID。
     */
    private Integer roleId;
    /**
     * 新的手机号码。
     */
    private String userPhone;
    /**
     * 新的个人简介。
     */
    private String userBio;
}