package com.yjx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yjx.module.*;
import com.yjx.service.SupplierManagementService;
import com.yjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map; // 引入 Map

/**
 * 供应商管理控制器
 */
@RestController
@RequestMapping("/supplier")
public class SupplierManagementController {

    @Autowired
    private SupplierManagementService supplierManagementService;

    /**
     * 获取所有供应记录（支持分页和搜索）
     * @param queryModule 查询参数
     * @return 包含列表和总数的自定义Map结果
     */
    @GetMapping("/getAllSupplierManagement")
    public Result<Map<String, Object>> getList(@ModelAttribute SupplierManagementQueryModule queryModule) { // <--- 返回类型已修改
        return supplierManagementService.getSupplierManagementList(queryModule);
    }

    /**
     * 创建新的供应记录
     * @param createModule JSON请求体
     * @return 操作结果
     */
    @PostMapping("/createSupplierManagement")
    public Result<Void> create(@RequestBody CreateSupplierManagementModule createModule) {
        return supplierManagementService.createSupplierManagement(createModule);
    }

    /**
     * 更新已有的供应记录
     * @param updateModule JSON请求体
     * @return 操作结果
     */
    @PostMapping("/updateSupplierManagement")
    public Result<Void> update(@RequestBody UpdateSupplierManagementModule updateModule) {
        return supplierManagementService.updateSupplierManagement(updateModule);
    }

    /**
     * 根据ID删除供应记录
     * @param deleteModule 表单参数
     * @return 操作结果
     */
    @PostMapping("/deleteSupplierManagement")
    public Result<Void> delete(@ModelAttribute DeleteSupplierManagementModule deleteModule) {
        return supplierManagementService.deleteSupplierManagement(deleteModule);
    }
}