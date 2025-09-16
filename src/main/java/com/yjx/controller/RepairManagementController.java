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
 * 后台维修管理API控制器。
 * 负责处理维修流程中的具体管理操作，如分配技师、更新状态、记录价格等。
 */
@RestController
@RequestMapping("/management")
public class RepairManagementController {

    /**
     * 自动注入维修管理服务层的Bean。
     */
    @Autowired
    private RepairManagementService repairManagementService;

    /**
     * 获取所有维修管理记录，支持分页和关键字搜索。
     *
     * @param queryDTO 包含分页、排序和搜索关键字的查询参数对象。
     * @return 包含维修管理记录列表和总记录数的结果Map。
     */
    @GetMapping("/getAllRepairManagement")
    public Result<Map<String, Object>> getAllRepairManagement(@ModelAttribute RepairManagementQueryDTO queryDTO) {
        return repairManagementService.getAllRepairManagementList(queryDTO);
    }

    /**
     * 创建一条新的维修管理记录。
     *
     * @param createDTO 包含创建维修管理记录所需信息的DTO对象。
     * @return 表示操作成功或失败的结果对象。
     */
    @PostMapping("/createRepairManagement")
    public Result<Void> createRepairManagement(@RequestBody CreateRepairManagementDTO createDTO) {
        return repairManagementService.createRepairManagement(createDTO);
    }

    /**
     * 更新一条已有的维修管理记录。
     *
     * @param updateDTO 包含待更新记录ID和新信息的DTO对象。
     * @return 表示操作成功或失败的结果对象。
     */
    @PostMapping("/updateRepairManagement")
    public Result<Void> updateRepairManagement(@RequestBody UpdateRepairManagementDTO updateDTO) {
        return repairManagementService.updateRepairManagement(updateDTO);
    }

    /**
     * 删除一条维修管理记录。
     *
     * @param deleteDTO 包含待删除记录ID和用户验证信息的DTO对象。
     * @return 表示操作成功或失败的结果对象。
     */
    @PostMapping("/deleteRepairManagement")
    public Result<Void> deleteRepairManagement(@ModelAttribute DeleteRepairManagementDTO deleteDTO) {
        return repairManagementService.deleteRepairManagement(deleteDTO);
    }
}