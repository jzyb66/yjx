package com.yjx.pojo;

// Add this import
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@TableName(value = "yjx_user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    // Add this annotation to identify the primary key
    @TableId
    private Integer userId;

    private String userName;
    private String userEmail;
    private String userPasswordHash;
    private String roleId;
    private String userBio;
    private String userPhone;
    private String userGender;
    private LocalDateTime userLastActive;
    private LocalDateTime userCreatedAt;
    private String userStatus;
}