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
 * 维修管理实体类 (POJO)。
 * 映射数据库中的 `yjx_repair_management` 表。
 */
@Data
@TableName(value = "yjx_repair_management")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RepairManagement {

    /**
     * 维修管理ID (主键, 自增)。
     */
    @TableId(type = IdType.AUTO)
    private Integer repairId;

    /**
     * 关联的维修请求ID (外键)。
     */
    private Integer repairRequestId;

    /**
     * 负责维修的技术人员ID。
     */
    private Integer technicianId;

    /**
     * 维修备注或日志。
     */
    private String repairNotes;

    /**
     * 维修总价格。
     */
    private BigDecimal repairPrice;

    /**
     * 支付状态 (枚举: '待支付', '支付中', '支付完成', '支付异常')。
     */
    private String paymentStatus;

    /**
     * 记录创建时间。
     */
    private LocalDateTime createdAt;

    /**
     * 记录更新时间。
     */
    private LocalDateTime updatedAt;

    // --- 以下为非数据库字段，用于连表查询或逻辑处理 ---

    /**
     * 手机型号 (来自 yjx_repair_request 表)。
     */
    @TableField(exist = false)
    private String phoneModel;

    /**
     * 维修状态的中文名称 (由后端代码根据 requestStatus 转换而来)。
     */
    @TableField(exist = false)
    private String statusName;

    /**
     * 客户的用户名 (来自 yjx_user 表)。
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 原始维修请求的状态码 (来自 yjx_repair_request 表)，用于在Service层进行逻辑转换。
     */
    @TableField(exist = false)
    private Integer requestStatus;
}