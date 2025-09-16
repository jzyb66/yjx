package com.yjx.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 维修请求实体类 (POJO)。
 * 映射数据库中的 `yjx_repair_request` 表。
 */
@Data
@TableName(value = "yjx_repair_request")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Repair{

    /**
     * 请求ID, 主键, 数据库自增。
     */
    @TableId(type = IdType.AUTO)
    private Integer requestId;

    /**
     * 提交订单的用户ID。
     */
    private Integer userId;

    /**
     * 接待人员ID。
     */
    private Integer receptionistId;

    /**
     * 手机型号。
     */
    private String phoneModel;

    /**
     * 手机问题描述。
     */
    private String phoneIssueDescription;

    /**
     * 维修状态 (关联 yjx_repair_status 表)。
     */
    private Integer requestStatus;

    /**
     * 订单创建时间。
     */
    private LocalDateTime createdAt;

    /**
     * 订单更新时间。
     */
    private LocalDateTime updatedAt;

    /**
     * 接待人员姓名 (非数据库字段, 通过连表查询获得)。
     */
    @TableField(exist = false)
    private String receptionistName;
}