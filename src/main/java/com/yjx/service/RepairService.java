package com.yjx.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
     * 创建一个新的维修请求
     * @param repair 包含维修信息的对象
     * @return Result object indicating success or failure
     */
    Result<Void> createRepair(Repair repair);

    /**
     * 根据ID删除维修请求，并进行密码验证
     * @param repairId 要删除的维修请求ID
     * @param userId   执行操作的用户ID
     * @param password 用户密码用于验证
     * @return Result object indicating success or failure
     */
    Result<Void> deleteRepair(Integer repairId, Integer userId, String password);
}