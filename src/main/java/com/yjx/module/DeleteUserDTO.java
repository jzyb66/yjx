package com.yjx.module;

import lombok.Data;

@Data
public class DeleteUserDTO {
    private Integer adminId;
    private String adminPassword;
    private Integer userIdToDelete;
}