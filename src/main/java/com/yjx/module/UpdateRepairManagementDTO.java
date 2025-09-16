package com.yjx.module;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 更新维修管理记录请求的数据传输对象 (DTO)。
 */
@Data
public class UpdateRepairManagementDTO {
    /**
     * 待更新的维修管理ID。
     */
    private Integer repairId;
    /**
     * 更新后的维修价格。
     */
    private BigDecimal repairPrice;
    /**
     * 更新后的支付状态。
     */
    private String paymentStatus;
    /**
     * 更新后的维修备注。
     */
    private String repairNotes;
    /**
     * 更新后的技术人员ID。
     */
    private Integer technicianId;
}