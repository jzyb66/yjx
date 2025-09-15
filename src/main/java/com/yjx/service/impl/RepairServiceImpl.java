package com.yjx.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjx.mapper.RepairMapper;
import com.yjx.mapper.UserMapper;
import com.yjx.module.CreateRepairDTO;
import com.yjx.module.DeleteRepairDTO;
import com.yjx.module.ReceptionistVO;
import com.yjx.module.RepairQueryDTO;
import com.yjx.pojo.Repair;
import com.yjx.pojo.User;
import com.yjx.service.RepairService;
import com.yjx.util.Md5Password;
import com.yjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RepairServiceImpl extends ServiceImpl<RepairMapper, Repair> implements RepairService {

    @Autowired
    private RepairMapper repairMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<Map<String, Object>> getAllRepairListByCondition(RepairQueryDTO repairQueryDTO) {
        // --- 修改点：规范化分页和排序参数 ---
        int pageNum = repairQueryDTO.getPageNum() == null || repairQueryDTO.getPageNum() < 1 ? 1 : repairQueryDTO.getPageNum();
        int pageSize = repairQueryDTO.getPageSize() == null || repairQueryDTO.getPageSize() < 1 ? 10 : repairQueryDTO.getPageSize();
        Page<Repair> page = new Page<>(pageNum, pageSize);

        if (repairQueryDTO.getSortField() == null || repairQueryDTO.getSortField().trim().isEmpty()) {
            repairQueryDTO.setSortField("createdAt");
        }
        if (repairQueryDTO.getSortOrder() == null || (!"asc".equalsIgnoreCase(repairQueryDTO.getSortOrder()) && !"desc".equalsIgnoreCase(repairQueryDTO.getSortOrder()))) {
            repairQueryDTO.setSortOrder("desc");
        }

        // --- 修改点：直接传递整个Module对象到Mapper ---
        IPage<Repair> resultPage = repairMapper.selectRepairByCondition(page, repairQueryDTO);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("repairRequest", resultPage.getRecords());
        responseMap.put("count", resultPage.getTotal());
        return Result.success(responseMap);
    }


    @Override
    public List<ReceptionistVO> getAllReceptionist() {
        return repairMapper.getAllReceptionist();
    }


    /**
     * 新增逻辑 (修改点)
     * 从 Module 映射到 Entity
     */
    @Override
    public Result<Void> createRepair(CreateRepairDTO createRepairDTO) {
        // 1. 创建数据库实体对象
        Repair repair = new Repair();

        // 2. 从 Module 中获取数据，设置到实体对象中
        repair.setUserId(createRepairDTO.getUserId());
        repair.setReceptionistId(createRepairDTO.getReceptionistId());
        repair.setPhoneModel(createRepairDTO.getPhoneModel());
        repair.setPhoneIssueDescription(createRepairDTO.getPhoneIssueDescription());

        // 3. 设置服务器端生成的默认值
        repair.setRequestId(null); // 强制使用数据库自增ID
        repair.setRequestStatus(1);
        repair.setCreatedAt(LocalDateTime.now());
        repair.setUpdatedAt(LocalDateTime.now());

        // 4. 保存到数据库
        boolean isSuccess = this.save(repair);

        if (isSuccess) {
            return Result.success();
        } else {
            return Result.fail("创建订单失败。", 500);
        }
    }

    /**
     * 删除逻辑 (修改点)
     * 从 Module 中获取参数
     */
    @Override
    public Result<Void> deleteRepair(DeleteRepairDTO deleteRepairDTO) {
        // 从 Module 对象中获取参数
        Integer repairId = deleteRepairDTO.getRepairId();
        Integer userId = deleteRepairDTO.getUserId();
        String password = deleteRepairDTO.getPassword();

        // 1. 根据 userId 查找用户
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.fail("用户不存在。", 404);
        }

        // 2. 验证密码
        String encryptedPassword = Md5Password.generateMD5(password);
        if (!encryptedPassword.equals(user.getUserPasswordHash())) {
            return Result.fail("密码错误。", 401);
        }

        // 3. 执行删除
        boolean isSuccess = this.removeById(repairId);

        if (isSuccess) {
            return Result.success();
        } else {
            return Result.fail("删除订单失败。", 500);
        }
    }
}