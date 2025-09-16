package com.yjx.module;

import lombok.Data;

/**
 * 维修管理页面查询参数的数据传输对象 (DTO)。
 */
@Data
public class RepairManagementQueryDTO {

    /**
     * 当前登录用户ID (用于权限校验等)。
     */
    private Integer userId;
    /**
     * 当前登录用户角色 (用于权限校验等)。
     */
    private Integer userRole;
    /**
     * 模糊搜索的关键字 (对应手机型号, 维修备注, 用户名)。
     */
    private String searchKeyword;
    /**
     * 当前页码。
     */
    private Integer pageNum = 1;
    /**
     * 每页显示数量。
     */
    private Integer pageSize = 10;
    /**
     * 排序字段 (如: 'createdAt', 'repairId')。
     */
    private String sortField;
    /**
     * 排序顺序 ('asc' 或 'desc')。
     */
    private String sortOrder;
}