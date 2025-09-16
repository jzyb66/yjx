package com.yjx.module;

import lombok.Data;

/**
 * 删除维修管理记录请求的数据传输对象 (DTO)。
 */
@Data
public class DeleteRepairManagementDTO {
    /**
     * 待删除的维修管理ID。
     */
    private Integer repairId;
    /**
     * 执行操作的用户ID。
     */
    private Integer userId;
    /**
     * 操作用户的登录密码，用于验证。
     */
    private String userPasswd;
}