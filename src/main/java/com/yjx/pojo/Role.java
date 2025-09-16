package com.yjx.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色实体类 (POJO)。
 * 映射数据库中的 `yjx_role` 表。
 */
@Data
@TableName("yjx_role")
public class Role {

    /**
     * 角色ID (主键, 数据库自增)。
     */
    @TableId(type = IdType.AUTO)
    private Integer roleId;

    /**
     * 角色名称 (例如 "admin", "user")。
     */
    private String roleName;

    /**
     * 角色的详细描述。
     */
    private String roleDescription;
}