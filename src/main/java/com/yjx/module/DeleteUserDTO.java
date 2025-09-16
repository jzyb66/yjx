package com.yjx.module;

import lombok.Data;

/**
 * 删除用户请求的数据传输对象 (DTO)。
 * 封装了删除用户操作所需的操作员验证信息和目标用户信息。
 */
@Data
public class DeleteUserDTO {

    /**
     * 执行此删除操作的管理员ID。
     */
    private Integer adminId;

    /**
     * 执行此删除操作的管理员的登录密码，用于权限验证。
     */
    private String adminPassword;

    /**
     * 待删除的目标用户的ID。
     */
    private Integer userIdToDelete;
}