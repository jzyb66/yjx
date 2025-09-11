package com.yjx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjx.module.RepairManagementQueryModule;
import com.yjx.pojo.RepairManagement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RepairManagementMapper extends BaseMapper<RepairManagement> {

    /**
     * 根据条件分页查询维修管理列表 (已修改SQL以匹配您的数据库)
     */
    @Select("""
            <script>
            SELECT
                rm.repair_id,
                rm.repair_request_id,
                rm.repair_notes,
                rm.repair_price,
                rm.payment_status,
                rm.created_at,
                rr.phone_model,
                rr.request_status,  -- 获取数字状态
                u.user_name
            FROM
                yjx_repair_management rm
            LEFT JOIN yjx_repair_request rr ON rm.repair_request_id = rr.request_id
            LEFT JOIN yjx_user u ON rr.user_id = u.user_id
            <where>
                <if test="query.searchKeyword != null and query.searchKeyword != ''">
                    (
                        rr.phone_model LIKE CONCAT('%', #{query.searchKeyword}, '%') OR
                        rm.repair_notes LIKE CONCAT('%', #{query.searchKeyword}, '%') OR
                        u.user_name LIKE CONCAT('%', #{query.searchKeyword}, '%')
                    )
                </if>
            </where>
            ORDER BY
                <choose>
                    <when test="query.sortField == 'repairId'">rm.repair_id</when>
                    <when test="query.sortField == 'paymentStatus'">rm.payment_status</when>
                    <otherwise>rm.created_at</otherwise>
                </choose>
                <if test="query.sortOrder == 'asc'">ASC</if>
                <if test="query.sortOrder == 'desc'">DESC</if>
            </script>
            """)
    IPage<RepairManagement> selectRepairManagementByCondition(
            Page<RepairManagement> page,
            @Param("query") RepairManagementQueryModule query
    );
}