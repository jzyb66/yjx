package com.yjx.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 供应商管理实体类
 * 映射到数据库的 yjx_supplier_management 表
 */
@Data
@TableName(value = "yjx_supplier_management")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierManagement {

    @TableId(value = "supplier_management_id", type = IdType.AUTO)
    private Integer supplierManagementId;

    private Integer supplierId;
    private Integer partId;
    private Integer supplyQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}