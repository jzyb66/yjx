package com.yjx.module;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 接收创建配件记录请求参数的 Module
 */
@Data
public class CreatePartModule {

    private String partName;
    private String partDescription;
    private BigDecimal partPrice;
    private Integer stockQuantity;
    private Integer supplierId;
}