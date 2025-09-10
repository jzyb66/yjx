package com.yjx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjx.module.CreateRepairModule;
import com.yjx.module.DeleteRepairModule;
import com.yjx.module.ReceptionistVO;
import com.yjx.module.RepairQueryModule;
import com.yjx.pojo.Repair;
import com.yjx.util.Result;

import java.util.List;
import java.util.Map;

/**
 * 维修服务接口
 */
public interface RepairService extends IService<Repair> {

    Result<Map<String, Object>> getAllRepairListByCondition(RepairQueryModule repairQueryModule);

    List<ReceptionistVO> getAllReceptionist();

    /**
     * 创建一个新的维修请求 (修改点)
     * @param createRepairModule 包含新增信息的对象
     * @return Result object indicating success or failure
     */
    Result<Void> createRepair(CreateRepairModule createRepairModule);

    /**
     * 根据ID删除维修请求，并进行密码验证 (修改点)
     * @param deleteRepairModule 包含删除信息的对象
     * @return Result object indicating success or failure
     */
    Result<Void> deleteRepair(DeleteRepairModule deleteRepairModule);
}