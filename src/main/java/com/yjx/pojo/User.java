package com.yjx.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName(value = "yjx_user")
public class User implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer userId;

    private String userName;
    private String userEmail;
    private String userPasswordHash;
    private Integer roleId;
    private String userBio;
    private String userPhone;
    private String userGender;
    private LocalDateTime userLastActive;
    private LocalDateTime userCreatedAt;
    private String userStatus;
}