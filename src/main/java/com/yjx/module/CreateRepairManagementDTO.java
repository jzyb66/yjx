package com.yjx.module;

import lombok.Data;

/**
 * 创建维修管理记录请求的数据传输对象 (DTO)。
 */
@Data
public class CreateRepairManagementDTO {
    /**
     * 关联的维修请求ID。
     */
    private Integer repairRequestId;
    /**
     * 维修备注。
     */
    private String repairNotes;
    /**
     * 分配的技术人员ID。
     */
    private Integer technicianId;
}