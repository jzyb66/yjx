package com.yjx.module;

import com.yjx.pojo.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户视图对象 (VO)。
 * 继承自 User 实体类，并额外添加了角色名称等用于前端展示的字段。
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserVO extends User {
    /**
     * 角色名称 (通过连表查询获得)。
     */
    private String roleName;
}