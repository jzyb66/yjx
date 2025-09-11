package com.yjx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjx.mapper.RepairManagementMapper;
import com.yjx.mapper.RepairMapper;
import com.yjx.mapper.UserMapper;
import com.yjx.module.CreateRepairManagementModule;
import com.yjx.module.DeleteRepairManagementModule;
import com.yjx.module.RepairManagementQueryModule;
import com.yjx.module.UpdateRepairManagementModule;
import com.yjx.pojo.Repair;
import com.yjx.pojo.RepairManagement;
import com.yjx.pojo.User;
import com.yjx.service.RepairManagementService;
import com.yjx.util.Md5Password;
import com.yjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RepairManagementServiceImpl extends ServiceImpl<RepairManagementMapper, RepairManagement> implements RepairManagementService {

    @Autowired
    private RepairManagementMapper repairManagementMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RepairMapper repairMapper;

    @Override
    public Result<Map<String, Object>> getAllRepairManagementList(RepairManagementQueryModule queryModule) {
        int pageNum = queryModule.getPageNum() == null || queryModule.getPageNum() < 1 ? 1 : queryModule.getPageNum();
        int pageSize = queryModule.getPageSize() == null || queryModule.getPageSize() < 1 ? 10 : queryModule.getPageSize();
        Page<RepairManagement> page = new Page<>(pageNum, pageSize);

        if (queryModule.getSortField() == null || queryModule.getSortField().trim().isEmpty()) {
            queryModule.setSortField("createdAt");
        }
        if (queryModule.getSortOrder() == null || (!"asc".equalsIgnoreCase(queryModule.getSortOrder()) && !"desc".equalsIgnoreCase(queryModule.getSortOrder()))) {
            queryModule.setSortOrder("desc");
        }

        IPage<RepairManagement> resultPage = repairManagementMapper.selectRepairManagementByCondition(page, queryModule);

        List<RepairManagement> records = resultPage.getRecords();
        for (RepairManagement record : records) {
            record.setStatusName(convertStatusToString(record.getRequestStatus()));
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("repairManagementList", records);
        responseMap.put("count", resultPage.getTotal());
        return Result.success(responseMap);
    }

    private String convertStatusToString(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 1: return "待维修";
            case 2: return "维修中";
            case 3: return "已完成";
            case 4: return "已取消";
            default: return "未知状态";
        }
    }

    @Override
    @Transactional
    public Result<Void> createRepairManagement(CreateRepairManagementModule createModule) {
        QueryWrapper<RepairManagement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("repair_request_id", createModule.getRepairRequestId());
        if (this.count(queryWrapper) > 0) {
            return Result.fail("该订单已存在维修记录，请勿重复添加。", 409);
        }

        Repair repairRequest = repairMapper.selectById(createModule.getRepairRequestId());
        if (repairRequest == null) {
            return Result.fail("关联的维修请求不存在。", 404);
        }
        repairRequest.setRequestStatus(2); // 设置为维修中
        repairRequest.setUpdatedAt(LocalDateTime.now());
        repairMapper.updateById(repairRequest);

        RepairManagement repairManagement = new RepairManagement();
        repairManagement.setRepairRequestId(createModule.getRepairRequestId());
        repairManagement.setRepairNotes(createModule.getRepairNotes());
        repairManagement.setTechnicianId(createModule.getTechnicianId());
        repairManagement.setPaymentStatus("待支付");
        repairManagement.setRepairPrice(new BigDecimal("0.00"));
        repairManagement.setCreatedAt(LocalDateTime.now());
        repairManagement.setUpdatedAt(LocalDateTime.now());

        boolean isSuccess = this.save(repairManagement);
        return isSuccess ? Result.success() : Result.fail("创建维修记录失败。", 500);
    }

    @Override
    public Result<Void> updateRepairManagement(UpdateRepairManagementModule updateModule) {
        RepairManagement existingRecord = this.getById(updateModule.getRepairId());
        if (existingRecord == null) {
            return Result.fail("未找到指定的维修记录。", 404);
        }

        existingRecord.setRepairPrice(updateModule.getRepairPrice());
        existingRecord.setPaymentStatus(updateModule.getPaymentStatus());
        existingRecord.setRepairNotes(updateModule.getRepairNotes());
        existingRecord.setTechnicianId(updateModule.getTechnicianId());
        existingRecord.setUpdatedAt(LocalDateTime.now());

        boolean isSuccess = this.updateById(existingRecord);
        return isSuccess ? Result.success() : Result.fail("更新维修记录失败。", 500);
    }

    @Override
    public Result<Void> deleteRepairManagement(DeleteRepairManagementModule deleteModule) {
        Integer repairId = deleteModule.getRepairId();
        Integer userId = deleteModule.getUserId();
        String password = deleteModule.getUserPasswd();

        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.fail("用户不存在。", 404);
        }

        String encryptedPassword = Md5Password.generateMD5(password);
        if (!encryptedPassword.equals(user.getUserPasswordHash())) {
            return Result.fail("密码错误。", 401);
        }

        boolean isSuccess = this.removeById(repairId);
        return isSuccess ? Result.success() : Result.fail("删除维修记录失败。", 500);
    }
}