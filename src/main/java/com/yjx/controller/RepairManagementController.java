package com.yjx.controller;

import com.yjx.module.CreateRepairManagementDTO;
import com.yjx.module.DeleteRepairManagementDTO;
import com.yjx.module.RepairManagementQueryDTO;
import com.yjx.module.UpdateRepairManagementDTO;
import com.yjx.service.RepairManagementService;
import com.yjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 维修管理控制器
 */
@RestController
@RequestMapping("/management") // 基础路径与前端JS代码中的URL匹配
public class RepairManagementController {

    @Autowired
    private RepairManagementService repairManagementService;

    /**
     * 获取所有维修管理记录（支持分页和搜索）
     * @param queryModule 查询参数
     * @return 包含列表和总数的结果
     */
    @GetMapping("/getAllRepairManagement")
    public Result<Map<String, Object>> getAllRepairManagement(@ModelAttribute RepairManagementQueryDTO queryModule) {
        return repairManagementService.getAllRepairManagementList(queryModule);
    }

    /**
     * 创建新的维修管理记录
     * @param createModule JSON请求体
     * @return 操作结果
     */
    @PostMapping("/createRepairManagement")
    public Result<Void> createRepairManagement(@RequestBody CreateRepairManagementDTO createModule) {
        return repairManagementService.createRepairManagement(createModule);
    }

    /**
     * 更新已有的维修管理记录
     * @param updateModule JSON请求体
     * @return 操作结果
     */
    @PostMapping("/updateRepairManagement")
    public Result<Void> updateRepairManagement(@RequestBody UpdateRepairManagementDTO updateModule) {
        return repairManagementService.updateRepairManagement(updateModule);
    }

    /**
     * 删除维修管理记录
     * @param deleteModule URL参数
     * @return 操作结果
     */
    @PostMapping("/deleteRepairManagement")
    public Result<Void> deleteRepairManagement(@ModelAttribute DeleteRepairManagementDTO deleteModule) {
        return repairManagementService.deleteRepairManagement(deleteModule);
    }
}