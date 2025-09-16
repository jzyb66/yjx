package com.yjx.module;

import lombok.Data;

/**
 * 删除维修订单请求的数据传输对象 (DTO)。
 */
@Data
public class DeleteRepairDTO {

    /**
     * 待删除的维修订单ID (request_id)。
     */
    private Integer repairId;
    /**
     * 执行删除操作的用户ID。
     */
    private Integer userId;
    /**
     * 操作用户的登录密码，用于验证。
     */
    private String password;
}