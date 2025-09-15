package com.yjx.module;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private Integer userId;
    private String userName;
    private String userEmail;
    private Integer roleId;
    private String userPhone;
    private String userBio;
}