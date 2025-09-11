package com.yjx.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 维修管理实体类
 * (已修改以匹配您的数据库)
 */
@Data
@TableName(value = "yjx_repair_management")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RepairManagement {

    @TableId(type = IdType.AUTO)
    private Integer repairId;
    private Integer repairRequestId;
    private Integer technicianId;
    private String repairNotes;
    private BigDecimal repairPrice;
    private String paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // --- 修改区域开始 ---

    // 移除 statusId 字段

    /**
     * 手机型号 (来自 yjx_repair_request 表)
     */
    @TableField(exist = false)
    private String phoneModel;

    /**
     * 状态名称 (由后端代码根据 requestStatus 转换而来)
     */
    @TableField(exist = false)
    private String statusName;

    /**
     * 客户用户名 (来自 yjx_user 表)
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 维修请求状态码 (来自 yjx_repair_request 表)
     * 临时用于在Service层进行逻辑转换
     */
    @TableField(exist = false)
    private Integer requestStatus;

    // --- 修改区域结束 ---
}