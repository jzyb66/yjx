package com.yjx.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjx.mapper.SupplierManagementMapper;
import com.yjx.mapper.UserMapper;
import com.yjx.module.*;
import com.yjx.pojo.SupplierManagement;
import com.yjx.pojo.User;
import com.yjx.service.SupplierManagementService;
import com.yjx.util.Md5Password; // 【核心修改】导入您项目自定义的MD5工具类
import com.yjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class SupplierManagementServiceImpl extends ServiceImpl<SupplierManagementMapper, SupplierManagement> implements SupplierManagementService {

    @Autowired
    private SupplierManagementMapper supplierManagementMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<Map<String, Object>> getSupplierManagementList(SupplierManagementQueryModule queryModule) {
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

    @Override
    public Result<Void> createSupplierManagement(CreateSupplierManagementModule createModule) {
        SupplierManagement newRecord = new SupplierManagement();
        newRecord.setSupplierId(createModule.getSupplierId());
        newRecord.setPartId(createModule.getPartId());
        newRecord.setSupplyQuantity(createModule.getSupplyQuantity());
        newRecord.setCreatedAt(LocalDateTime.now());
        newRecord.setUpdatedAt(LocalDateTime.now());

        boolean isSuccess = this.save(newRecord);
        return isSuccess ? Result.success() : Result.fail("创建供应记录失败。", 500);
    }

    @Override
    public Result<Void> updateSupplierManagement(UpdateSupplierManagementModule updateModule) {
        SupplierManagement existingRecord = this.getById(updateModule.getSupplierManagementId());
        if (existingRecord == null) {
            return Result.fail("未找到指定的供应记录。", 404);
        }

        existingRecord.setSupplierId(updateModule.getSupplierId());
        existingRecord.setPartId(updateModule.getPartId());
        existingRecord.setSupplyQuantity(updateModule.getSupplyQuantity());
        existingRecord.setUpdatedAt(LocalDateTime.now());

        boolean isSuccess = this.updateById(existingRecord);
        return isSuccess ? Result.success() : Result.fail("更新供应记录失败。", 500);
    }

    /**
     * 【已修复】使用项目自定义的 Md5Password 工具类进行密码验证
     */
    @Override
    public Result<Void> deleteSupplierManagement(DeleteSupplierManagementModule deleteModule) {
        if (deleteModule.getSupplierManagementId() == null || deleteModule.getUserId() == null || deleteModule.getUserPasswd() == null) {
            return Result.fail("参数不完整，无法删除。", 400);
        }

        User user = userMapper.selectById(deleteModule.getUserId());
        if (user == null) {
            return Result.fail("操作用户不存在。", 404);
        }

        // 【核心修改】使用您项目中的 Md5Password.generateMD5 方法进行加密和比较
        String encryptedPassword = Md5Password.generateMD5(deleteModule.getUserPasswd());
        if (!Objects.equals(user.getUserPasswordHash(), encryptedPassword)) {
            return Result.fail("密码错误，无权删除。", 403);
        }

        boolean isSuccess = this.removeById(deleteModule.getSupplierManagementId());
        return isSuccess ? Result.success() : Result.fail("删除供应记录失败。", 500);
    }
}