package com.yjx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjx.mapper.RoleMapper;
import com.yjx.pojo.Role;
import com.yjx.service.RoleService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<Role> getAllRoles() {
        // 使用 Mybatis-Plus 的 list() 方法查询所有角色
        return list();
    }
}