package com.yjx.module;

import lombok.Data;

@Data
public class UserQueryModule {
    private String searchKeyword;
    private int pageNum = 1;
    private int pageSize = 10;
    private String sortField = "user_created_at"; // 注意：数据库字段名
    private String sortPart = "desc"; // 前端传来的是 'desc' 或 'asc'
}