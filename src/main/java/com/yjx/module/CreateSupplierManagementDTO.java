package com.yjx.module;

import lombok.Data;

/**
 * 接收创建供应商管理记录请求参数的 Module
 */
@Data
public class CreateSupplierManagementDTO {
    private Integer supplierId;
    private Integer partId;
    private Integer supplyQuantity;
}