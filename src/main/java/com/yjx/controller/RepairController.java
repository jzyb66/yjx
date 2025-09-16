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
 * 维修请求API控制器。
 * 负责处理客户维修订单的创建、查询和删除等请求，是前台接待功能的核心入口。
 */
@RestController
@RequestMapping("/repair")
public class RepairController {

    /**
     * 自动注入维修服务层的Bean。
     */
    @Autowired
    private RepairService repairService;

    /**
     * 根据多种条件组合查询维修请求列表，支持分页。
     *
     * @param repairQueryDTO 封装了查询条件的DTO对象。
     * @return 包含维修单列表和总记录数的结果Map。
     */
    @GetMapping("/getAllRepair")
    public Result<Map<String, Object>> getAllRepair(@ModelAttribute RepairQueryDTO repairQueryDTO){
        return repairService.getAllRepairListByCondition(repairQueryDTO);
    }

    /**
     * 获取系统中所有角色为“接待员”的用户列表。
     * 用于前端创建订单时选择接待人员。
     *
     * @return 接待员信息列表 (VO)。
     */
    @GetMapping("/getAllReceptionist")
    public Result<List<ReceptionistVO>> getAllReceptionist() {
        List<ReceptionistVO> receptionists = repairService.getAllReceptionist();
        return Result.success(receptionists);
    }

    /**
     * 新增一个维修订单。
     * 请求体应为JSON格式。
     *
     * @param createRepairDTO 包含创建维修单所需信息的DTO对象。
     * @return 表示操作成功或失败的结果对象。
     */
    @PostMapping("/createRepair")
    public Result<Void> createRepair(@RequestBody CreateRepairDTO createRepairDTO) {
        return repairService.createRepair(createRepairDTO);
    }

    /**
     * 删除一个维修订单。
     * 此操作需要提供用户密码进行验证。
     *
     * @param deleteRepairDTO 包含待删除维修单ID和用户验证信息的DTO对象。
     * @return 表示操作成功或失败的结果对象。
     */
    @PostMapping("/deleteRepair")
    public Result<Void> deleteRepair(@ModelAttribute DeleteRepairDTO deleteRepairDTO) {
        return repairService.deleteRepair(deleteRepairDTO);
    }
}