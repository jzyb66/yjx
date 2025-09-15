package com.yjx.module;

import lombok.Data;

/**
 * 接收删除维修管理记录请求参数的 Module
 */
@Data
public class DeleteRepairManagementDTO {

    private Integer repairId;
    private Integer userId;
    private String userPasswd;

}