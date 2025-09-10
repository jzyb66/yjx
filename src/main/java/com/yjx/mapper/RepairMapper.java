package com.yjx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjx.module.ReceptionistVO;
import com.yjx.pojo.Repair;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 维修请求Mapper接口
 */
@SuppressWarnings("ALL")
@Mapper
public interface RepairMapper extends BaseMapper<Repair> {
    // MyBatis-Plus已提供基础的CRUD方法，无需额外定义

    @Select("""
           SELECT
               rr.*,
               u.user_name AS receptionistName
           FROM yjx_repair_request rr
           LEFT JOIN yjx_user u ON rr.receptionist_id = u.user_id
           LEFT JOIN yjx_user cu ON cu.user_id = #{userId}
           WHERE 1=1
           AND (cu.role_id IN (1, 3) OR rr.user_id = #{userId})
           AND (rr.phone_model LIKE CONCAT('%', #{searchKeyword}, '%')
               OR rr.phone_issue_description LIKE CONCAT('%', #{searchKeyword}, '%'))
           ORDER BY
               CASE
                   WHEN #{sortField} = 'createdAt' THEN rr.created_at
                   WHEN #{sortField} = 'requestId' THEN rr.request_id
                   ELSE rr.created_at
               END
               ${sortOrder == 'desc' ? 'DESC' : 'ASC'}
    """)
    IPage<Repair> selectRepairByCondition(
            Page<Repair> page,               //MP分页对象

            @Param("userId") Integer userId,

            @Param("searchKeyword") String searchKeyword,
            @Param("sortField") String sortField,
            @Param("sortOrder") String sortOrder
    );
    @Select("""
            SELECT 
                user_id AS userId,
                user_name As userName
            FROM yjx_user
    WHERE role_id = 3
    GROUP BY user_id, user_name
""")
    List<ReceptionistVO> getAllReceptionist();
}
