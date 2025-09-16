package com.yjx.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjx.mapper.PartMapper;
import com.yjx.mapper.SupplierManagementMapper;
import com.yjx.mapper.UserMapper;
import com.yjx.module.*;
import com.yjx.pojo.Part;
import com.yjx.pojo.SupplierManagement;
import com.yjx.pojo.User;
import com.yjx.service.SupplierManagementService;
import com.yjx.util.Md5Password;
import com.yjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 供应商管理服务接口的实现类。
 */
@Service
public class SupplierManagementServiceImpl extends ServiceImpl<SupplierManagementMapper, SupplierManagement> implements SupplierManagementService {

    @Autowired
    private SupplierManagementMapper supplierManagementMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 新增注入: 用于校验配件ID是否存在。
     */
    @Autowired
    private PartMapper partMapper;

    /**
     * 根据条件获取供应商供应记录列表。
     *
     * @param queryModule 查询参数DTO。
     * @return 封装了查询结果和总数的Result对象。
     */
    @Override
    public Result<Map<String, Object>> getSupplierManagementList(SupplierManagementQueryDTO queryModule) {
        int pageNum = queryModule.getPageNum() == null || queryModule.getPageNum() < 1 ? 1 : queryModule.getPageNum();
        int pageSize = queryModule.getPageSize() == null || queryModule.getPageSize() < 1 ? 10 : queryModule.getPageSize();
        Page<SupplierManagementVO> page = new Page<>(pageNum, pageSize);

        if (queryModule.getSortField() == null || queryModule.getSortField().trim().isEmpty()) {
            queryModule.setSortField("createdAt");
        }
        if (queryModule.getSortOrder() == null || (!"asc".equalsIgnoreCase(queryModule.getSortOrder()) && !"desc".equalsIgnoreCase(queryModule.getSortOrder()))) {
            queryModule.setSortOrder("desc");
        }

        IPage<SupplierManagementVO> resultPage = supplierManagementMapper.selectSupplierManagementWithDetails(page, queryModule);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("supplierManagementList", resultPage.getRecords());
        responseData.put("count", resultPage.getTotal());

        return Result.success(responseData);
    }

    /**
     * 创建新的供应记录，增加ID有效性校验。
     *
     * @param createModule 包含创建信息的DTO。
     * @return 操作结果。
     */
    @Override
    public Result<Void> createSupplierManagement(CreateSupplierManagementDTO createModule) {
        // --- 新增：校验供应商ID ---
        User supplier = userMapper.selectById(createModule.getSupplierId());
        // 供应商必须存在，且其角色ID必须为5（供应商）
        if (supplier == null || !supplier.getRoleId().equals(5)) {
            return Result.fail("供应商ID无效或该用户不是供应商", 400);
        }

        // --- 新增：校验配件ID ---
        Part part = partMapper.selectById(createModule.getPartId());
        if (part == null) {
            return Result.fail("配件ID不存在", 400);
        }

        // 校验通过后，执行创建逻辑
        SupplierManagement newRecord = new SupplierManagement();
        newRecord.setSupplierId(createModule.getSupplierId());
        newRecord.setPartId(createModule.getPartId());
        newRecord.setSupplyQuantity(createModule.getSupplyQuantity());
        newRecord.setCreatedAt(LocalDateTime.now());
        newRecord.setUpdatedAt(LocalDateTime.now());

        boolean isSuccess = this.save(newRecord);
        return isSuccess ? Result.success() : Result.fail("创建供应记录失败。", 500);
    }

    /**
     * 更新供应记录，增加ID有效性校验。
     *
     * @param updateModule 包含更新信息的DTO。
     * @return 操作结果。
     */
    @Override
    public Result<Void> updateSupplierManagement(UpdateSupplierManagementDTO updateModule) {
        SupplierManagement existingRecord = this.getById(updateModule.getSupplierManagementId());
        if (existingRecord == null) {
            return Result.fail("未找到指定的供应记录。", 404);
        }

        // --- 新增：校验供应商ID ---
        User supplier = userMapper.selectById(updateModule.getSupplierId());
        if (supplier == null || !supplier.getRoleId().equals(5)) {
            return Result.fail("供应商ID无效或该用户不是供应商", 400);
        }

        // --- 新增：校验配件ID ---
        Part part = partMapper.selectById(updateModule.getPartId());
        if (part == null) {
            return Result.fail("配件ID不存在", 400);
        }

        // 校验通过后，执行更新逻辑
        existingRecord.setSupplierId(updateModule.getSupplierId());
        existingRecord.setPartId(updateModule.getPartId());
        existingRecord.setSupplyQuantity(updateModule.getSupplyQuantity());
        existingRecord.setUpdatedAt(LocalDateTime.now());

        boolean isSuccess = this.updateById(existingRecord);
        return isSuccess ? Result.success() : Result.fail("更新供应记录失败。", 500);
    }

    /**
     * 删除供应记录，需密码验证。
     *
     * @param deleteModule 包含删除信息的DTO。
     * @return 操作结果。
     */
    @Override
    public Result<Void> deleteSupplierManagement(DeleteSupplierManagementDTO deleteModule) {
        if (deleteModule.getSupplierManagementId() == null || deleteModule.getUserId() == null || deleteModule.getUserPasswd() == null) {
            return Result.fail("参数不完整，无法删除。", 400);
        }

        User user = userMapper.selectById(deleteModule.getUserId());
        if (user == null) {
            return Result.fail("操作用户不存在。", 404);
        }

        String encryptedPassword = Md5Password.generateMD5(deleteModule.getUserPasswd());
        if (!Objects.equals(user.getUserPasswordHash(), encryptedPassword)) {
            return Result.fail("密码错误，无权删除。", 403);
        }

        boolean isSuccess = this.removeById(deleteModule.getSupplierManagementId());
        return isSuccess ? Result.success() : Result.fail("删除供应记录失败。", 500);
    }
}