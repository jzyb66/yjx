package com.yjx.module;

import lombok.Data;

/**
 * 创建维修订单请求的数据传输对象 (DTO)。
 */
@Data
public class CreateRepairDTO {

    /**
     * 客户的用户ID。
     */
    private Integer userId;
    /**
     * 接待人员的用户ID。
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
}