package com.yjx.module;

import lombok.Data;

/**
 * 接收配件查询页面查询参数的 Module
 */
@Data
public class PartQueryDTO {

    private Integer userId;
    private Integer userRole;

    // 核心查询参数
    private String searchKeyword; // 对应前端的搜索框

    // 分页和排序参数
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String sortField; // 排序字段 (如: 'partId', 'partPrice')
    private String sortPart;  // 排序顺序 ('asc' 或 'desc')，与前端JS(access.js)保持一致
}