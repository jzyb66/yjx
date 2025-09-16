package com.yjx.module;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/**
 * 维修请求查询数据传输对象 (DTO)。
 * 封装前端传递的用于查询维修订单的各种筛选条件。
 */
@Data
public class RepairQueryDTO {

    // 用户信息，用于权限校验
    private Integer userId;
    private Integer userRole;

    // 筛选条件
    private String phoneModel;
    private String phoneIssueDescription;
    private Integer requestId;
    private Integer requestStatus;
    private Integer receptionistId;

    // 日期范围筛选
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    // 分页参数
    private Integer pageNum = 1;
    private Integer pageSize = 10;

    // 排序参数
    private String sortField;
    private String sortOrder;
}