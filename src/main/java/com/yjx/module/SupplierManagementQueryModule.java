package com.yjx.module;

import lombok.Data;

/**
 * 接收供应商管理记录查询参数的 Module
 */
@Data
public class SupplierManagementQueryModule {
    private String searchKeyword;
    private Integer pageNum;
    private Integer pageSize;
    private String sortField;
    private String sortOrder;
}