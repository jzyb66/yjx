package com.yjx.module;

import lombok.Data;

/**
 * 用户查询数据传输对象 (DTO)。
 * 用于封装账号管理页面的筛选、分页和排序参数。
 */
@Data
public class UserQueryDTO {
    /**
     * 模糊搜索关键字。
     */
    private String searchKeyword;
    /**
     * 当前页码。
     */
    private int pageNum = 1;
    /**
     * 每页显示数量。
     */
    private int pageSize = 10;
    /**
     * 排序字段 (数据库字段名, 如: 'user_created_at')。
     */
    private String sortField = "user_created_at";
    /**
     * 排序顺序 ('asc' 或 'desc')。
     */
    private String sortPart = "desc";
}