package com.yjx.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjx.mapper.PartMapper;
import com.yjx.mapper.UserMapper;
import com.yjx.module.CreatePartModule;
import com.yjx.module.DeletePartModule;
import com.yjx.module.PartQueryModule;
import com.yjx.module.UpdatePartModule;
import com.yjx.pojo.Part;
import com.yjx.pojo.User;
import com.yjx.service.PartService;
import com.yjx.util.Md5Password;
import com.yjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PartServiceImpl extends ServiceImpl<PartMapper, Part> implements PartService {

    @Autowired
    private PartMapper partMapper;

    @Autowired
    private UserMapper userMapper;

    // getPartList 方法保持不变
    @Override
    public Result<IPage<Part>> getPartList(PartQueryModule queryModule) {
        int pageNum = queryModule.getPageNum() == null || queryModule.getPageNum() < 1 ? 1 : queryModule.getPageNum();
        int pageSize = queryModule.getPageSize() == null || queryModule.getPageSize() < 1 ? 10 : queryModule.getPageSize();
        Page<Part> page = new Page<>(pageNum, pageSize);
        if (queryModule.getSortField() == null || queryModule.getSortField().trim().isEmpty()) {
            queryModule.setSortField("partId");
        }
        if (queryModule.getSortPart() == null || (!"asc".equalsIgnoreCase(queryModule.getSortPart()) && !"desc".equalsIgnoreCase(queryModule.getSortPart()))) {
            queryModule.setSortPart("asc");
        }
        IPage<Part> resultPage = partMapper.selectPartByCondition(page, queryModule);
        return Result.success(resultPage);
    }

    /**
     * 【最终版】创建配件方法
     * 验证 supplier_id 是否在 yjx_user 表中存在且 role_id 为 5
     */
    @Override
    public Result<Void> createPart(CreatePartModule createModule) {
        // --- 最终验证逻辑开始 ---
        Integer supplierId = createModule.getSupplierId();
        if (supplierId == null) {
            return Result.fail("供应商ID不能为空。", 400);
        }

        User supplier = userMapper.selectById(supplierId);

        // 验证1：检查用户ID是否存在，解决数据库外键约束问题
        if (supplier == null) {
            return Result.fail("供应商ID不存在，请检查后重试。", 404);
        }

        // 验证2：检查该用户角色是否为供应商 (role_id = 5)
        if (supplier.getRoleId() != 5) {
            return Result.fail("指定的用户ID不是供应商，请输入正确的供应商ID。", 400);
        }
        // --- 最终验证逻辑结束 ---

        Part part = new Part();
        part.setPartName(createModule.getPartName());
        part.setPartDescription(createModule.getPartDescription());
        part.setPartPrice(createModule.getPartPrice());
        part.setStockQuantity(createModule.getStockQuantity());
        part.setSupplierId(createModule.getSupplierId());
        part.setCreatedAt(LocalDateTime.now());
        part.setUpdatedAt(LocalDateTime.now());

        boolean isSuccess = this.save(part);
        return isSuccess ? Result.success() : Result.fail("创建配件记录失败。", 500);
    }

    /**
     * 【最终版】更新配件方法
     * 同样验证 supplier_id 是否在 yjx_user 表中存在且 role_id 为 5
     */
    @Override
    public Result<Void> updatePart(UpdatePartModule updateModule) {
        Part existingPart = this.getById(updateModule.getPartId());
        if (existingPart == null) {
            return Result.fail("未找到指定的配件记录。", 404);
        }

        // --- 最终验证逻辑开始 ---
        Integer supplierId = updateModule.getSupplierId();
        if (supplierId != null) {
            User supplier = userMapper.selectById(supplierId);
            if (supplier == null) {
                return Result.fail("供应商ID不存在，请检查后重试。", 404);
            }
            if (supplier.getRoleId() != 5) {
                return Result.fail("指定的用户ID不是供应商，请输入正确的供应商ID。", 400);
            }
        }
        // --- 最终验证逻辑结束 ---

        existingPart.setPartName(updateModule.getPartName());
        existingPart.setPartDescription(updateModule.getPartDescription());
        existingPart.setPartPrice(updateModule.getPartPrice());
        existingPart.setStockQuantity(updateModule.getStockQuantity());
        existingPart.setSupplierId(updateModule.getSupplierId());
        existingPart.setUpdatedAt(LocalDateTime.now());

        boolean isSuccess = this.updateById(existingPart);
        return isSuccess ? Result.success() : Result.fail("更新配件记录失败。", 500);
    }

    // deletePartWithPassword 方法保持不变
    @Override
    public Result<Void> deletePartWithPassword(DeletePartModule deleteModule) {
        Integer partId = deleteModule.getPartId();
        Integer userId = deleteModule.getUserId();
        String password = deleteModule.getPassword();
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.fail("用户不存在。", 404);
        }
        String encryptedPassword = Md5Password.generateMD5(password);
        if (!encryptedPassword.equals(user.getUserPasswordHash())) {
            return Result.fail("密码错误。", 401);
        }
        boolean isSuccess = this.removeById(partId);
        return isSuccess ? Result.success() : Result.fail("删除配件记录失败。", 500);
    }
}