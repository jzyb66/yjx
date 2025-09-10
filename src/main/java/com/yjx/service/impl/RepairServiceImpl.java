package com.yjx.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjx.mapper.RepairMapper;
import com.yjx.mapper.UserMapper;
import com.yjx.pojo.User;
import com.yjx.module.ReceptionistVO;
import com.yjx.module.RepairQueryModule;
import com.yjx.pojo.Repair;
import com.yjx.service.RepairService;
import com.yjx.util.Result;
// Import your application's MD5 password utility
import com.yjx.util.Md5Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class RepairServiceImpl extends ServiceImpl<RepairMapper, Repair> implements RepairService {

    @Autowired
    private RepairMapper repairMapper;

    @Autowired
    private UserMapper userMapper;

    // ... (getAllRepairListByCondition, getAllReceptionist, and createRepair methods remain unchanged)
    @Override
    public Result<Map<String, Object>> getAllRepairListByCondition(RepairQueryModule repairQueryModule) {
        // ... (existing code is correct)
        // 1.处理分页参数：避免null或负数，设置默认值(页码默认1，每页默认10条)
        int pageNum = repairQueryModule.getPageNum() == null || repairQueryModule.getPageNum() < 1 ? 1 : repairQueryModule.getPageNum();
        int pageSize = repairQueryModule.getPageSize() == null || repairQueryModule.getPageSize() < 1 ? 10 : repairQueryModule.getPageSize();

        // 注意：Page泛型用维修单实体类(Repair)
        Page<Repair> page = new Page<>(pageNum, pageSize);

        // 2.处理搜索关键词：避免null导致SQL语法错误，null时设为空字符串
        String searchKeyword = repairQueryModule.getSearchKeyword() == null ? "" : repairQueryModule.getSearchKeyword().trim();

        // 3.处理排序参数：默认按"创建时间(createdAt)降序"，避免null
        String sortField = repairQueryModule.getSortField();
        String sortOrder = repairQueryModule.getSortOrder();

        // 若未传排序字段/方向，设置默认值(按创建时间降序)
        if (sortField == null || sortField.trim().isEmpty()) {
            sortField = "createdAt"; // 对应数据库字段 created_at(MP会自动驼峰转下划线)
        }
        if (sortOrder == null || (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder))) {
            sortOrder = "desc"; // 默认降序
        }

        // 4.调用Mapper层查询：传递分页参数+权限条件 + 搜索/排序条件
        // 核心权限逻辑：roleId=1或3查全部，其他查自己(通过Mapper的SQL实现过滤)
        IPage<Repair> resultPage = repairMapper.selectRepairByCondition(
                page,
                repairQueryModule.getUserId(), // 当前登录用户ID(用于权限过滤)
                searchKeyword, // 搜索关键词(模糊查手机型号/问题描述)
                sortField, // 排序字段(如createdAt、requestId)
                sortOrder // 排序方向(asc/desc)
        );

        // 5.封装响应数据：与前端期望的结构对齐(repairRequest列表 + count总条数)
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("repairRequest", resultPage.getRecords()); // 维修单列表数据
        responseMap.put("count", resultPage.getTotal()); // 总记录数(用于分页组件)

        // 6.返回成功响应(用自定义Result工具类，含code、msg、data)
        return Result.success(responseMap);
    }

    @Override
    public List<ReceptionistVO> getAllReceptionist() {
        return repairMapper.getAllReceptionist();
    }


    @Override
    public Result<Void> createRepair(Repair repair) {

        repair.setRequestStatus(1);
        repair.setCreatedAt(LocalDateTime.now());
        repair.setUpdatedAt(LocalDateTime.now());

        boolean isSuccess = this.save(repair);

        if (isSuccess) {
            return Result.success();
        } else {
            return Result.fail("Failed to create repair order.", 500);
        }
    }


    @Override
    public Result<Void> deleteRepair(Integer repairId, Integer userId, String password) {
        // 1. 根据 userId 查找用户
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.fail("用户不存在。", 404);
        }

        // 2. 使用与登录时完全相同的逻辑来验证密码
        //    将用户输入的明文密码进行MD5加密
        String encryptedPassword = Md5Password.generateMD5(password);

        //    将加密后的密码与数据库中存储的哈希进行比较
        if (!encryptedPassword.equals(user.getUserPasswordHash())) {
            return Result.fail("密码错误。", 401);
        }

        // 3. 密码验证通过后，执行删除操作
        boolean isSuccess = this.removeById(repairId);

        if (isSuccess) {
            return Result.success();
        } else {
            return Result.fail("删除订单失败。", 500);
        }
    }
}