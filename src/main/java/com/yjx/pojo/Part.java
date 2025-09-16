package com.yjx.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 配件实体类 (POJO)。
 * 映射数据库中的 `yjx_parts` 表。
 */
@Data
@TableName(value = "yjx_parts")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Part {

    /**
     * 配件ID (主键, 自增)。
     */
    @TableId(value = "part_id", type = IdType.AUTO)
    private Integer partId;

    /**
     * 配件名称。
     */
    private String partName;

    /**
     * 配件描述。
     */
    private String partDescription;

    /**
     * 配件价格。
     */
    private BigDecimal partPrice;

    /**
     * 库存数量。
     */
    private Integer stockQuantity;

    /**
     * 供应商ID (外键)。
     */
    private Integer supplierId;

    /**
     * 记录创建时间。
     */
    private LocalDateTime createdAt;

    /**
     * 记录更新时间。
     */
    private LocalDateTime updatedAt;
}