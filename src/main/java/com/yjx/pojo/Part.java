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
 * 配件实体类
 * 直接映射到数据库的 yjx_parts 表
 */
@Data
@TableName(value = "yjx_parts")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Part {

    @TableId(value = "part_id", type = IdType.AUTO)
    private Integer partId;

    private String partName;
    private String partDescription;
    private BigDecimal partPrice;
    private Integer stockQuantity;
    private Integer supplierId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
