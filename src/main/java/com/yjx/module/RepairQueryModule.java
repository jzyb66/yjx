package com.yjx.module;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class RepairQueryModule {

    private Integer userId;
    private Integer userRole;
    private String phoneModel;
    private String phoneIssueDescription;
    private Integer requestId;
    private Integer requestStatus;
    private Integer receptionistId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String sortField;
    private String sortOrder;
}