package com.yjx.module;

import lombok.Data;

/**
 * 接收删除维修订单请求参数的 Module
 */
@Data
public class DeleteRepairModule {

    private Integer repairId;
    private Integer userId;
    private String password;

}