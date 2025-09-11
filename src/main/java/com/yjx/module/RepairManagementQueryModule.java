package com.yjx.module;

import lombok.Data;

/**
 * 接收维修管理页面查询参数的 Module
 */
@Data
public class RepairManagementQueryModule {

    // 这两个字段用于权限校验，但在此次查询中可能不直接参与WHERE条件
    private Integer userId;
    private Integer userRole;

    // 核心查询参数
    private String searchKeyword; // 对应前端的搜索框

    // 分页和排序参数
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String sortField; // 排序字段 (如: 'createdAt', 'repairId')
    private String sortOrder; // 排序顺序 ('asc' 或 'desc')
}