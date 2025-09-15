package com.yjx.module;

import lombok.Data;

/**
 * 接收删除供应商管理记录请求参数的 Module
 */
@Data
public class DeleteSupplierManagementDTO {
    private Integer supplierManagementId;
    private Integer userId;
    private String userPasswd;
}