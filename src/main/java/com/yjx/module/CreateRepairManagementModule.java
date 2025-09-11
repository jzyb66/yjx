package com.yjx.module;

import lombok.Data;

/**
 * 接收创建维修管理记录请求参数的 Module
 */
@Data
public class CreateRepairManagementModule {

    private Integer repairRequestId;
    private String repairNotes;
    private Integer technicianId;

}