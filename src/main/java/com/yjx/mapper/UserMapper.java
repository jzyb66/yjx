package com.yjx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjx.module.UserQueryModule;
import com.yjx.module.UserVO;
import com.yjx.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("""
            <script>
            SELECT
                u.*,
                r.role_name
            FROM
                yjx_user u
            LEFT JOIN
                yjx_role r ON u.role_id = r.role_id
            <where>
                <if test="query.searchKeyword != null and query.searchKeyword != ''">
                    (u.user_name LIKE CONCAT('%', #{query.searchKeyword}, '%')
                    OR u.user_email LIKE CONCAT('%', #{query.searchKeyword}, '%')
                    OR u.user_phone LIKE CONCAT('%', #{query.searchKeyword}, '%'))
                </if>
            </where>
            ORDER BY ${query.sortField} ${query.sortPart}
            </script>
            """)
    IPage<UserVO> selectUserPage(Page<UserVO> page, @Param("query") UserQueryModule query);
}