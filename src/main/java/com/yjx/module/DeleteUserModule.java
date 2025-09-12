package com.yjx.module;

import lombok.Data;

@Data
public class DeleteUserModule {
    private Integer adminId;
    private String adminPassword;
    private Integer userIdToDelete;
}