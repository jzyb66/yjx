package com.yjx.controller;

import com.yjx.module.CreateRepairDTO;
import com.yjx.module.DeleteRepairDTO;
import com.yjx.module.ReceptionistVO;
import com.yjx.module.RepairQueryDTO;
import com.yjx.service.RepairService;
import com.yjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 维修请求控制器
 */
@RestController
@RequestMapping("/repair")
public class RepairController {

    @Autowired
    private RepairService repairService;

    /**
     * 获取所有维修请求（支持分页和搜索）
     */
    @GetMapping("/getAllRepair")
    public Result<Map<String, Object>> getAllRepair(@ModelAttribute RepairQueryDTO repairQueryDTO){
        return repairService.getAllRepairListByCondition(repairQueryDTO);
    }

    /**
     * 获取所有接待员
     */
    @GetMapping("/getAllReceptionist")
    public Result<List<ReceptionistVO>> getAllReceptionist() {
        List<ReceptionistVO> receptionists = repairService.getAllReceptionist();
        return Result.success(receptionists);
    }

    /**
     * 新增维修订单 (修改点)
     * 使用 CreateRepairDTO 接收 JSON 请求体
     */
    @PostMapping("/createRepair")
    public Result<Void> createRepair(@RequestBody CreateRepairDTO createRepairDTO) {
        return repairService.createRepair(createRepairDTO);
    }

    /**
     * 删除维修订单 (修改点)
     * 使用 DeleteRepairDTO 接收 URL 参数
     * 注意：这里使用 @ModelAttribute 而不是 @RequestBody 是为了兼容您前端现有的 axios 用法 (params)
     */
    @PostMapping("/deleteRepair")
    public Result<Void> deleteRepair(@ModelAttribute DeleteRepairDTO deleteRepairDTO) {
        return repairService.deleteRepair(deleteRepairDTO);
    }
}