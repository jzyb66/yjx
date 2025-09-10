package com.yjx.pojo;

// 1. 确认这个 import 存在
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@TableName(value = "yjx_repair_request")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Repair{

    // 2. 确认注解是 @TableId(type = IdType.AUTO)
    @TableId(type = IdType.AUTO)
    private Integer requestId;

    private Integer userId;
    private Integer receptionistId;
    private String phoneModel;
    private String phoneIssueDescription;
    private Integer requestStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @TableField(exist = false)
    private String receptionistName;

}