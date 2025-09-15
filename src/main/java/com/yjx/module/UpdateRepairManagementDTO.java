package com.yjx.module;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 接收更新维修管理记录请求参数的 Module
 */
@Data
public class UpdateRepairManagementDTO {

    private Integer repairId; // 必须字段，用于定位要更新的记录
    private BigDecimal repairPrice;
    private String paymentStatus;
    private String repairNotes;
    private Integer technicianId;

}