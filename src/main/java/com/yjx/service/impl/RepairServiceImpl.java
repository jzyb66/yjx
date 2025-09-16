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

/**
 * 维修请求服务接口的实现类。
 */
@Service
public class RepairServiceImpl extends ServiceImpl<RepairMapper, Repair> implements RepairService {

    @Autowired
    private RepairMapper repairMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据条件获取维修请求列表，并进行分页。
     *
     * @param repairQueryDTO 包含查询条件的DTO。
     * @return 封装了查询结果和总数的Result对象。
     */
    @Override
    public Result<Map<String, Object>> getAllRepairListByCondition(RepairQueryDTO repairQueryDTO) {
        // 1. 处理并规范化分页参数
        int pageNum = repairQueryDTO.getPageNum() == null || repairQueryDTO.getPageNum() < 1 ? 1 : repairQueryDTO.getPageNum();
        int pageSize = repairQueryDTO.getPageSize() == null || repairQueryDTO.getPageSize() < 1 ? 10 : repairQueryDTO.getPageSize();
        Page<Repair> page = new Page<>(pageNum, pageSize);

        // 2. 处理并规范化排序参数
        if (repairQueryDTO.getSortField() == null || repairQueryDTO.getSortField().trim().isEmpty()) {
            repairQueryDTO.setSortField("createdAt");
        }
        if (repairQueryDTO.getSortOrder() == null || (!"asc".equalsIgnoreCase(repairQueryDTO.getSortOrder()) && !"desc".equalsIgnoreCase(repairQueryDTO.getSortOrder()))) {
            repairQueryDTO.setSortOrder("desc");
        }

        // 3. 调用Mapper执行查询
        IPage<Repair> resultPage = repairMapper.selectRepairByCondition(page, repairQueryDTO);

        // 4. 封装返回结果
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("repairRequest", resultPage.getRecords());
        responseMap.put("count", resultPage.getTotal());
        return Result.success(responseMap);
    }

    /**
     * 获取所有角色为接待员的用户。
     *
     * @return 接待员信息列表。
     */
    @Override
    public List<ReceptionistVO> getAllReceptionist() {
        return repairMapper.getAllReceptionist();
    }

    /**
     * 创建新的维修请求订单。
     *
     * @param createRepairDTO 包含订单信息的DTO。
     * @return 表示操作结果的Result对象。
     */
    @Override
    public Result<Void> createRepair(CreateRepairDTO createRepairDTO) {
        // 1. 将DTO转换为POJO实体
        Repair repair = new Repair();
        repair.setUserId(createRepairDTO.getUserId());
        repair.setReceptionistId(createRepairDTO.getReceptionistId());
        repair.setPhoneModel(createRepairDTO.getPhoneModel());
        repair.setPhoneIssueDescription(createRepairDTO.getPhoneIssueDescription());

        // 2. 设置服务器端生成的默认值
        repair.setRequestStatus(1); // 默认为“待维修”状态
        repair.setCreatedAt(LocalDateTime.now());
        repair.setUpdatedAt(LocalDateTime.now());

        // 3. 保存到数据库
        boolean isSuccess = this.save(repair);

        return isSuccess ? Result.success() : Result.fail("创建订单失败。", 500);
    }

    /**
     * 删除维修请求订单，需密码验证。
     *
     * @param deleteRepairDTO 包含订单ID和用户验证信息的DTO。
     * @return 表示操作结果的Result对象。
     */
    @Override
    public Result<Void> deleteRepair(DeleteRepairDTO deleteRepairDTO) {
        Integer repairId = deleteRepairDTO.getRepairId();
        Integer userId = deleteRepairDTO.getUserId();
        String password = deleteRepairDTO.getPassword();

        // 1. 根据 userId 查找用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.fail("用户不存在。", 404);
        }

        // 2. 验证密码是否正确
        String encryptedPassword = Md5Password.generateMD5(password);
        if (!encryptedPassword.equals(user.getUserPasswordHash())) {
            return Result.fail("密码错误。", 401);
        }

        // 3. 执行删除操作
        boolean isSuccess = this.removeById(repairId);

        return isSuccess ? Result.success() : Result.fail("删除订单失败。", 500);
    }
}