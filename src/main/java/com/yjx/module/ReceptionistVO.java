package com.yjx.module;

import lombok.Data;

/**
 * 接待人员VO: 包含用户信息和关联的角色名称
 */
@Data
public class ReceptionistVO {
    private Integer userId; // 接待人员ID (关联用户表)
    private String userName; // 接待人员姓名 (用户表)
}