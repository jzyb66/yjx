package com.yjx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjx.module.CreateRepairManagementDTO;
import com.yjx.module.DeleteRepairManagementDTO;
import com.yjx.module.RepairManagementQueryDTO;
import com.yjx.module.UpdateRepairManagementDTO;
import com.yjx.pojo.RepairManagement;
import com.yjx.util.Result;

import java.util.Map;

/**
 * 维修管理服务接口。
 * 定义了维修管理模块的核心业务逻辑。
 */
public interface RepairManagementService extends IService<RepairManagement> {

    /**
     * 根据条件获取维修管理列表（分页）。
     *
     * @param queryModule 查询参数DTO。
     * @return 包含列表和总数的结果。
     */
    Result<Map<String, Object>> getAllRepairManagementList(RepairManagementQueryDTO queryModule);

    /**
     * 创建一个新的维修管理记录。
     *
     * @param createModule 包含新增信息的DTO对象。
     * @return 操作结果。
     */
    Result<Void> createRepairManagement(CreateRepairManagementDTO createModule);

    /**
     * 更新一个已存在的维修管理记录。
     *
     * @param updateModule 包含更新信息的DTO对象。
     * @return 操作结果。
     */
    Result<Void> updateRepairManagement(UpdateRepairManagementDTO updateModule);

    /**
     * 根据ID删除维修管理记录，并进行密码验证。
     *
     * @param deleteModule 包含删除信息的DTO对象。
     * @return 操作结果。
     */
    Result<Void> deleteRepairManagement(DeleteRepairManagementDTO deleteModule);
}