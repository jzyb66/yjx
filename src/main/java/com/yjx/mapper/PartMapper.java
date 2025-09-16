package com.yjx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjx.module.PartQueryDTO;
import com.yjx.pojo.Part;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 配件信息数据访问接口 (Mapper)。
 * 负责与 yjx_parts 表进行数据库交互。
 */
@Mapper
public interface PartMapper extends BaseMapper<Part> {

    /**
     * 根据动态条件分页查询配件列表。
     *
     * @param page  分页对象。
     * @param query 查询条件DTO。
     * @return 配件的分页结果。
     */
    @Select("""
            <script>
            SELECT
                part_id,
                part_name,
                part_description,
                part_price,
                stock_quantity,
                supplier_id,
                created_at,
                updated_at
            FROM
                yjx_parts
            <where>
                <if test="query.searchKeyword != null and query.searchKeyword != ''">
                    (
                        part_name LIKE CONCAT('%', #{query.searchKeyword}, '%') OR
                        part_description LIKE CONCAT('%', #{query.searchKeyword}, '%')
                    )
                </if>
            </where>
            ORDER BY
                <choose>
                    <when test="query.sortField == 'partId'">part_id</when>
                    <when test="query.sortField == 'partPrice'">part_price</when>
                    <when test="query.sortField == 'stockQuantity'">stock_quantity</when>
                    <otherwise>part_id</otherwise>
                </choose>
                <if test="query.sortPart == 'asc'">ASC</if>
                <if test="query.sortPart == 'desc'">DESC</if>
            </script>
            """)
    IPage<Part> selectPartByCondition(
            Page<Part> page,
            @Param("query") PartQueryDTO query
    );
}