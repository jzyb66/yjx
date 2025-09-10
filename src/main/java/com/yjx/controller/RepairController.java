package com.yjx.controller;

import com.yjx.module.ReceptionistVO;
import com.yjx.module.RepairQueryModule;
import com.yjx.service.RepairService;
import com.yjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Result<Map<String, Object>> getAllRepair(@ModelAttribute RepairQueryModule repairQueryModule){
        return repairService.getAllRepairListByCondition(repairQueryModule);
    }
    /**
     * 获取所有接待员
     */
    @GetMapping("/getAllReceptionist")
    public Result<List<ReceptionistVO>> getAllReceptionist() {
        List<ReceptionistVO> receptionists = repairService.getAllReceptionist();
        return Result.success(receptionists);
    }
}
