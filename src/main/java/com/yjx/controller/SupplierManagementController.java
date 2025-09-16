package com.yjx.controller;

import com.yjx.module.*;
import com.yjx.service.SupplierManagementService;
import com.yjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 供应商供应记录管理API控制器。
 * 负责处理供应商供应信息的增删改查。
 */
@RestController
@RequestMapping("/supplier")
public class SupplierManagementController {

    /**
     * 自动注入供应商管理服务层的Bean。
     */
    @Autowired
    private SupplierManagementService supplierManagementService;

    /**
     * 获取所有供应商供应记录，支持分页和关键字搜索。
     *
     * @param queryDTO 包含分页、排序和搜索关键字的查询参数对象。
     * @return 包含供应记录列表和总记录数的结果Map。
     */
    @GetMapping("/getAllSupplierManagement")
    public Result<Map<String, Object>> getList(@ModelAttribute SupplierManagementQueryDTO queryDTO) {
        return supplierManagementService.getSupplierManagementList(queryDTO);
    }

    /**
     * 创建一条新的供应商供应记录。
     *
     * @param createDTO 包含创建供应记录所需信息的DTO对象。
     * @return 表示操作成功或失败的结果对象。
     */
    @PostMapping("/createSupplierManagement")
    public Result<Void> create(@RequestBody CreateSupplierManagementDTO createDTO) {
        return supplierManagementService.createSupplierManagement(createDTO);
    }

    /**
     * 更新一条已有的供应商供应记录。
     *
     * @param updateDTO 包含待更新记录ID和新信息的DTO对象。
     * @return 表示操作成功或失败的结果对象。
     */
    @PostMapping("/updateSupplierManagement")
    public Result<Void> update(@RequestBody UpdateSupplierManagementDTO updateDTO) {
        return supplierManagementService.updateSupplierManagement(updateDTO);
    }

    /**
     * 删除一条供应商供应记录。
     *
     * @param deleteDTO 包含待删除记录ID和用户验证信息的DTO对象。
     * @return 表示操作成功或失败的结果对象。
     */
    @PostMapping("/deleteSupplierManagement")
    public Result<Void> delete(@ModelAttribute DeleteSupplierManagementDTO deleteDTO) {
        return supplierManagementService.deleteSupplierManagement(deleteDTO);
    }
}