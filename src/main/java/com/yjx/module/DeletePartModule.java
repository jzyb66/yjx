package com.yjx.module;

import lombok.Data;

/**
 * 接收含密码验证的删除配件请求参数的 Module
 */
@Data
public class DeletePartModule {
    private Integer partId;
    private Integer userId;
    private String password;
}