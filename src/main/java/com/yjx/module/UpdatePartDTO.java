package com.yjx.module;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 接收更新配件记录请求参数的 Module
 */
@Data
public class UpdatePartDTO {

    private Integer partId; // 必须字段，用于定位要更新的记录
    private String partName;
    private String partDescription;
    private BigDecimal partPrice;
    private Integer stockQuantity;
    private Integer supplierId;
}