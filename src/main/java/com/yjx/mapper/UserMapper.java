package com.yjx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yjx.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 如需自定义SQL，可在此添加（例：根据ID查询用户）
    @Select("SELECT * FROM yjx_user WHERE user_id = #{userId}")
    User selectById(Integer userId);
}