package com.yjx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjx.module.SupplierManagementQueryModule;
import com.yjx.pojo.SupplierManagement;
import com.yjx.module.SupplierManagementVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SupplierManagementMapper extends BaseMapper<SupplierManagement> {

    /**
     * 根据条件分页查询供应商管理列表
     * SQL连接了 yjx_user 和 yjx_parts 表以获取供应商和配件的名称
     */
    @Select("""
            <script>
            SELECT
                sm.supplier_management_id,
                sm.supplier_id,
                u.user_name AS supplierName,
                sm.part_id,
                p.part_name AS partName,
                sm.supply_quantity,
                sm.created_at
            FROM
                yjx_supplier_management sm
            LEFT JOIN
                yjx_user u ON sm.supplier_id = u.user_id
            LEFT JOIN
                yjx_parts p ON sm.part_id = p.part_id
            <where>
                <if test="query.searchKeyword != null and query.searchKeyword != ''">
                    (
                        u.user_name LIKE CONCAT('%', #{query.searchKeyword}, '%') OR
                        p.part_name LIKE CONCAT('%', #{query.searchKeyword}, '%')
                    )
                </if>
            </where>
            ORDER BY
                <choose>
                    <when test="query.sortField == 'supplierManagementId'">sm.supplier_management_id</when>
                    <when test="query.sortField == 'createdAt'">sm.created_at</when>
                    <otherwise>sm.supplier_management_id</otherwise>
                </choose>
                <if test="query.sortOrder == 'asc'">ASC</if>
                <if test="query.sortOrder == 'desc'">DESC</if>
            </script>
            """)
    IPage<SupplierManagementVO> selectSupplierManagementWithDetails(
            Page<SupplierManagementVO> page,
            @Param("query") SupplierManagementQueryModule query
    );
}