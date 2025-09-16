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
 * 维修请求服务接口。
 * 定义了前台接待模块的核心业务逻辑。
 */
public interface RepairService extends IService<Repair> {

    /**
     * 根据条件分页查询维修请求列表。
     *
     * @param repairQueryDTO 查询条件的数据传输对象。
     * @return 包含维修请求列表和总数的Result对象。
     */
    Result<Map<String, Object>> getAllRepairListByCondition(RepairQueryDTO repairQueryDTO);

    /**
     * 获取所有接待员列表。
     *
     * @return 接待员视图对象列表。
     */
    List<ReceptionistVO> getAllReceptionist();

    /**
     * 创建一个新的维修请求。
     *
     * @param createRepairDTO 包含新增信息的DTO对象。
     * @return 表示操作成功或失败的Result对象。
     */
    Result<Void> createRepair(CreateRepairDTO createRepairDTO);

    /**
     * 根据ID删除维修请求，并进行密码验证。
     *
     * @param deleteRepairDTO 包含删除信息的DTO对象。
     * @return 表示操作成功或失败的Result对象。
     */
    Result<Void> deleteRepair(DeleteRepairDTO deleteRepairDTO);
}