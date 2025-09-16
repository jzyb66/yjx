package com.yjx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjx.module.ReceptionistVO;
import com.yjx.module.RepairQueryDTO;
import com.yjx.pojo.Repair;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 维修请求数据访问接口 (Mapper)。
 * 负责与 yjx_repair_request 表进行数据库交互。
 */
@Mapper
public interface RepairMapper extends BaseMapper<Repair> {

    /**
     * 根据动态条件分页查询维修请求列表。
     * SQL语句连接了用户表以获取接待员姓名，并包含权限控制逻辑。
     *
     * @param page 分页对象。
     * @param query 查询条件DTO。
     * @return 维修请求的分页结果。
     */
    @Select("""
        <script>
        SELECT
            rr.*,
            u.user_name AS receptionistName
        FROM yjx_repair_request rr
        LEFT JOIN yjx_user u ON rr.receptionist_id = u.user_id
        LEFT JOIN yjx_user cu ON cu.user_id = #{query.userId}
        WHERE 1=1
        AND (cu.role_id IN (1, 3) OR rr.user_id = #{query.userId})
        <if test="query.requestId != null">
            AND rr.request_id = #{query.requestId}
        </if>
        <if test="query.phoneModel != null and query.phoneModel != ''">
            AND rr.phone_model LIKE CONCAT('%', #{query.phoneModel}, '%')
        </if>
        <if test="query.phoneIssueDescription != null and query.phoneIssueDescription != ''">
            AND rr.phone_issue_description LIKE CONCAT('%', #{query.phoneIssueDescription}, '%')
        </if>
        <if test="query.requestStatus != null">
            AND rr.request_status = #{query.requestStatus}
        </if>
        <if test="query.receptionistId != null">
            AND rr.receptionist_id = #{query.receptionistId}
        </if>
        <if test="query.startDate != null">
            AND rr.created_at &gt;= #{query.startDate}
        </if>
        <if test="query.endDate != null">
            AND rr.created_at &lt;= #{query.endDate}
        </if>
        ORDER BY
            <choose>
                <when test="query.sortField == 'requestId'">rr.request_id</when>
                <otherwise>rr.created_at</otherwise>
            </choose>
            <if test="query.sortOrder == 'asc'">ASC</if>
            <if test="query.sortOrder == 'desc'">DESC</if>
        </script>
    """)
    IPage<Repair> selectRepairByCondition(
            Page<Repair> page,
            @Param("query") RepairQueryDTO query
    );

    /**
     * 查询所有角色为接待员(role_id = 3)的用户。
     *
     * @return 接待员信息列表。
     */
    @Select("""
        SELECT
            user_id AS userId,
            user_name AS userName
        FROM yjx_user
        WHERE role_id = 3
        GROUP BY user_id, user_name
    """)
    List<ReceptionistVO> getAllReceptionist();
}