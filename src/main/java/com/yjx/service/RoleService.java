package com.yjx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjx.pojo.Role;
import java.util.List;

public interface RoleService extends IService<Role> {
    List<Role> getAllRoles();
}