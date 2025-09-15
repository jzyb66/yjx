package com.yjx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjx.module.CreateRepairDTO;
import com.yjx.module.DeleteRepairDTO;
import com.yjx.module.ReceptionistVO;
import com.yjx.module.RepairQueryDTO;
import com.yjx.pojo.Repair;
import com.yjx.util.Result;

import java.util.List;
import java.util.Map;

/**
 * 维修服务接口
 */
public interface RepairService extends IService<Repair> {

    Result<Map<String, Object>> getAllRepairListByCondition(RepairQueryDTO repairQueryDTO);

    List<ReceptionistVO> getAllReceptionist();

    /**
     * 创建一个新的维修请求 (修改点)
     * @param createRepairDTO 包含新增信息的对象
     * @return Result object indicating success or failure
     */
    Result<Void> createRepair(CreateRepairDTO createRepairDTO);

    /**
     * 根据ID删除维修请求，并进行密码验证 (修改点)
     * @param deleteRepairDTO 包含删除信息的对象
     * @return Result object indicating success or failure
     */
    Result<Void> deleteRepair(DeleteRepairDTO deleteRepairDTO);
}