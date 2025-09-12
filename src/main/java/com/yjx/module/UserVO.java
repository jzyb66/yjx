package com.yjx.module;

import com.yjx.pojo.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserVO extends User {
    private String roleName;
}