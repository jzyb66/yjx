package com.yjx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjx.module.CreateSupplierManagementDTO;
import com.yjx.module.DeleteSupplierManagementDTO;
import com.yjx.module.SupplierManagementQueryDTO;
import com.yjx.module.UpdateSupplierManagementDTO;
import com.yjx.pojo.SupplierManagement;
import com.yjx.util.Result;

import java.util.Map; // 引入 Map

public interface SupplierManagementService extends IService<SupplierManagement> {

    /**
     * 根据条件获取供应商管理列表（分页）
     * @param queryModule 查询参数
     * @return 自定义Map分页结果
     */
    Result<Map<String, Object>> getSupplierManagementList(SupplierManagementQueryDTO queryModule); // <--- 返回类型已修改

    /**
     * 创建一个新的供应记录
     * @param createModule 包含新增信息的对象
     * @return 操作结果
     */
    Result<Void> createSupplierManagement(CreateSupplierManagementDTO createModule);

    /**
     * 更新一个已存在的供应记录
     * @param updateModule 包含更新信息的对象
     * @return 操作结果
     */
    Result<Void> updateSupplierManagement(UpdateSupplierManagementDTO updateModule);

    /**
     * 根据ID删除供应记录，并校验用户密码
     * @param deleteModule 包含ID和用户验证信息的对象
     * @return 操作结果
     */
    Result<Void> deleteSupplierManagement(DeleteSupplierManagementDTO deleteModule);
}