package com.yjx.module;

import com.yjx.pojo.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

// @EqualsAndHashCode(callSuper = true) 允许子类使用父类的 equals 和 hashCode 方法
@EqualsAndHashCode(callSuper = true)
@Data
public class UserVO extends User {
    // 额外添加 roleName 字段用于前端展示
    private String roleName;
}