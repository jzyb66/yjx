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
}
