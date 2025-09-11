package com.yjx.module;

import lombok.Data;

/**
 * 接收更新供应商管理记录请求参数的 Module
 */
@Data
public class UpdateSupplierManagementModule {
    private Integer supplierManagementId; // 关键ID
    private Integer supplierId;
    private Integer partId;
    private Integer supplyQuantity;
}