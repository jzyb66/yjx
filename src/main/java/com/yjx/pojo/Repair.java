package com.yjx.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 维修请求实体类
 */
@Data
@TableName(value = "yjx_repair_request")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Repair{
    private Integer requestId;
    private Integer userId;
    private Integer receptionistId;
    private String phoneModel;
    private String phoneIssueDescription;
    private Integer requestStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 以下字段用于关联查询结果展示，不映射到数据库
    @TableField(exist = false)
    private String receptionistName; // 接待员名称

}
