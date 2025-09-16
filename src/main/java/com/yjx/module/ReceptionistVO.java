package com.yjx.module;

import lombok.Data;

/**
 * 接待人员视图对象 (VO)。
 * 用于向前台返回简化的接待员信息列表。
 */
@Data
public class ReceptionistVO {
    /**
     * 接待人员的用户ID。
     */
    private Integer userId;
    /**
     * 接待人员的用户名。
     */
    private String userName;
}