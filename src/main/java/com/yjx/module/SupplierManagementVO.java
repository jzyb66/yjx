package com.yjx.module;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 供应商管理视图对象
 * 用于向前端返回包含关联表信息的列表数据
 */
@Data
public class SupplierManagementVO {

    private Integer supplierManagementId;
    private Integer supplierId;
    private String supplierName; // 关联 yjx_user 表
    private Integer partId;
    private String partName;     // 关联 yjx_parts 表
    private Integer supplyQuantity;
    private LocalDateTime createdAt;
}