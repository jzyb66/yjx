package com.yjx.module;

import lombok.Data;

/**
 * 接收创建维修订单请求参数的 Module
 */
@Data
public class CreateRepairDTO {

    private Integer userId;
    private Integer receptionistId;
    private String phoneModel;
    private String phoneIssueDescription;

}