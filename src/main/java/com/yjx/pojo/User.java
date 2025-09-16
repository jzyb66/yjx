package com.yjx.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体类 (POJO)。
 * 该类映射数据库中的 `yjx_user` 表。
 */
@Data
@TableName(value = "yjx_user")
public class User implements Serializable {

    /**
     * 用户ID (主键, 数据库自增)。
     */
    @TableId(type = IdType.AUTO)
    private Integer userId;

    /**
     * 用户名。
     */
    private String userName;

    /**
     * 用户电子邮箱。
     */
    private String userEmail;

    /**
     * 用户密码的MD5哈希值。
     */
    private String userPasswordHash;

    /**
     * 角色ID (外键, 关联 yjx_role 表)。
     */
    private Integer roleId;

    /**
     * 用户个人简介。
     */
    private String userBio;

    /**
     * 用户手机号码。
     */
    private String userPhone;

    /**
     * 用户性别 ('male', 'female', 'secret')。
     */
    private String userGender;

    /**
     * 用户上次活跃时间。
     */
    private LocalDateTime userLastActive;

    /**
     * 用户账户创建时间。
     */
    private LocalDateTime userCreatedAt;

    /**
     * 用户账户状态 ('active', 'inactive')。
     */
    private String userStatus;
}