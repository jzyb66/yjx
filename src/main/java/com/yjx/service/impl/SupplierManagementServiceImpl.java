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
import com.yjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

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

        // 1. 从Mapper获取标准分页结果
        IPage<SupplierManagementVO> resultPage = supplierManagementMapper.selectSupplierManagementWithDetails(page, queryModule);

        // 2. 创建一个新的Map，用于存放自定义格式的数据
        Map<String, Object> responseData = new HashMap<>();

        // 3. 将分页数据放入Map中，并使用前端期望的键名
        responseData.put("supplierManagementList", resultPage.getRecords()); // 使用 "supplierManagementList" 作为列表的键
        responseData.put("count", resultPage.getTotal());                   // 使用 "count" 作为总数的键

        // 4. 返回成功结果，将自定义的Map作为数据返回
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

    @Override
    public Result<Void> deleteSupplierManagement(DeleteSupplierManagementModule deleteModule) {
        if (deleteModule.getSupplierManagementId() == null || deleteModule.getUserId() == null || deleteModule.getUserPasswd() == null) {
            return Result.fail("参数不完整，无法删除。", 400);
        }

        User user = userMapper.selectById(deleteModule.getUserId());
        if (user == null) {
            return Result.fail("操作用户不存在。", 404);
        }
        String encryptedPassword = DigestUtils.md5DigestAsHex(deleteModule.getUserPasswd().getBytes());
        if (!Objects.equals(user.getUserPasswordHash(), encryptedPassword)) {
            return Result.fail("密码错误，无权删除。", 403);
        }

        boolean isSuccess = this.removeById(deleteModule.getSupplierManagementId());
        return isSuccess ? Result.success() : Result.fail("删除供应记录失败。", 500);
    }
}