package com.yjx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjx.mapper.RepairManagementMapper;
import com.yjx.mapper.RepairMapper;
import com.yjx.mapper.UserMapper;
import com.yjx.module.CreateRepairManagementDTO;
import com.yjx.module.DeleteRepairManagementDTO;
import com.yjx.module.RepairManagementQueryDTO;
import com.yjx.module.UpdateRepairManagementDTO;
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

/**
 * 维修管理服务接口的实现类。
 */
@Service
public class RepairManagementServiceImpl extends ServiceImpl<RepairManagementMapper, RepairManagement> implements RepairManagementService {

    @Autowired
    private RepairManagementMapper repairManagementMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RepairMapper repairMapper;

    /**
     * 根据条件获取维修管理列表。
     *
     * @param queryModule 查询参数DTO。
     * @return 封装了查询结果和总数的Result对象。
     */
    @Override
    public Result<Map<String, Object>> getAllRepairManagementList(RepairManagementQueryDTO queryModule) {
        // 1. 规范化分页和排序参数
        int pageNum = queryModule.getPageNum() == null || queryModule.getPageNum() < 1 ? 1 : queryModule.getPageNum();
        int pageSize = queryModule.getPageSize() == null || queryModule.getPageSize() < 1 ? 10 : queryModule.getPageSize();
        Page<RepairManagement> page = new Page<>(pageNum, pageSize);

        if (queryModule.getSortField() == null || queryModule.getSortField().trim().isEmpty()) {
            queryModule.setSortField("createdAt");
        }
        if (queryModule.getSortOrder() == null || (!"asc".equalsIgnoreCase(queryModule.getSortOrder()) && !"desc".equalsIgnoreCase(queryModule.getSortOrder()))) {
            queryModule.setSortOrder("desc");
        }

        // 2. 调用Mapper执行查询
        IPage<RepairManagement> resultPage = repairManagementMapper.selectRepairManagementByCondition(page, queryModule);

        // 3. 数据后处理：将状态码转换为可读的文本
        List<RepairManagement> records = resultPage.getRecords();
        for (RepairManagement record : records) {
            record.setStatusName(convertStatusToString(record.getRequestStatus()));
        }

        // 4. 封装返回结果
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("repairManagementList", records);
        responseMap.put("count", resultPage.getTotal());
        return Result.success(responseMap);
    }

    /**
     * 将维修状态码转换为中文描述。
     *
     * @param status 状态码 (例如 1, 2, 3)。
     * @return 对应的中文描述。
     */
    private String convertStatusToString(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 1: return "提交订单";
            case 2: return "正在维修";
            case 3: return "维修成功等待取件";
            case 4: return "订单已完成";
            case 5: return "订单异常";
            default: return "未知状态";
        }
    }

    /**
     * 创建新的维修管理记录。
     * 此操作是事务性的：既要创建新记录，也要更新原维修请求的状态。
     *
     * @param createModule 包含创建信息的DTO。
     * @return 操作结果。
     */
    @Override
    @Transactional // 开启事务，确保数据一致性
    public Result<Void> createRepairManagement(CreateRepairManagementDTO createModule) {
        // 1. 检查此维修请求是否已存在管理记录，防止重复添加
        QueryWrapper<RepairManagement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("repair_request_id", createModule.getRepairRequestId());
        if (this.count(queryWrapper) > 0) {
            return Result.fail("该订单已存在维修记录，请勿重复添加。", 409); // 409 Conflict
        }

        // 2. 查找关联的维修请求是否存在
        Repair repairRequest = repairMapper.selectById(createModule.getRepairRequestId());
        if (repairRequest == null) {
            return Result.fail("关联的维修请求不存在。", 404); // 404 Not Found
        }

        // 3. 更新原维修请求的状态为“正在维修”(2)
        repairRequest.setRequestStatus(2);
        repairRequest.setUpdatedAt(LocalDateTime.now());
        repairMapper.updateById(repairRequest);

        // 4. 创建并保存新的维修管理记录
        RepairManagement repairManagement = new RepairManagement();
        repairManagement.setRepairRequestId(createModule.getRepairRequestId());
        repairManagement.setRepairNotes(createModule.getRepairNotes());
        repairManagement.setTechnicianId(createModule.getTechnicianId());
        repairManagement.setPaymentStatus("待支付"); // 默认状态
        repairManagement.setRepairPrice(new BigDecimal("0.00")); // 初始价格为0
        repairManagement.setCreatedAt(LocalDateTime.now());
        repairManagement.setUpdatedAt(LocalDateTime.now());

        boolean isSuccess = this.save(repairManagement);
        return isSuccess ? Result.success() : Result.fail("创建维修记录失败。", 500);
    }

    /**
     * 更新维修管理记录。
     *
     * @param updateModule 包含更新信息的DTO。
     * @return 操作结果。
     */
    @Override
    public Result<Void> updateRepairManagement(UpdateRepairManagementDTO updateModule) {
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

    /**
     * 删除维修管理记录，需密码验证。
     *
     * @param deleteModule 包含删除信息的DTO。
     * @return 操作结果。
     */
    @Override
    public Result<Void> deleteRepairManagement(DeleteRepairManagementDTO deleteModule) {
        Integer repairId = deleteModule.getRepairId();
        Integer userId = deleteModule.getUserId();
        String password = deleteModule.getUserPasswd();

        // 1. 验证用户和密码
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.fail("用户不存在。", 404);
        }
        String encryptedPassword = Md5Password.generateMD5(password);
        if (!encryptedPassword.equals(user.getUserPasswordHash())) {
            return Result.fail("密码错误。", 401); // 401 Unauthorized
        }

        // 2. 执行删除
        boolean isSuccess = this.removeById(repairId);
        return isSuccess ? Result.success() : Result.fail("删除维修记录失败。", 500);
    }
}