package com.yjx.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjx.mapper.PartMapper;
import com.yjx.mapper.UserMapper; // 导入
import com.yjx.module.CreatePartModule;
import com.yjx.module.DeletePartModule; // 导入
import com.yjx.module.PartQueryModule;
import com.yjx.module.UpdatePartModule;
import com.yjx.pojo.Part;
import com.yjx.pojo.User; // 导入
import com.yjx.service.PartService;
import com.yjx.util.Md5Password; // 导入
import com.yjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PartServiceImpl extends ServiceImpl<PartMapper, Part> implements PartService {

    @Autowired
    private PartMapper partMapper;

    @Autowired
    private UserMapper userMapper; // 注入 UserMapper

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

    @Override
    public Result<Void> createPart(CreatePartModule createModule) {
        Part part = new Part();
        part.setPartName(createModule.getPartName());
        // 【已修正】确保描述可以被设置
        part.setPartDescription(createModule.getPartDescription());
        part.setPartPrice(createModule.getPartPrice());
        part.setStockQuantity(createModule.getStockQuantity());
        part.setSupplierId(createModule.getSupplierId());
        part.setCreatedAt(LocalDateTime.now());
        part.setUpdatedAt(LocalDateTime.now());

        boolean isSuccess = this.save(part);
        return isSuccess ? Result.success() : Result.fail("创建配件记录失败。", 500);
    }

    @Override
    public Result<Void> updatePart(UpdatePartModule updateModule) {
        Part existingPart = this.getById(updateModule.getPartId());
        if (existingPart == null) {
            return Result.fail("未找到指定的配件记录。", 404);
        }

        existingPart.setPartName(updateModule.getPartName());
        existingPart.setPartDescription(updateModule.getPartDescription());
        existingPart.setPartPrice(updateModule.getPartPrice());
        existingPart.setStockQuantity(updateModule.getStockQuantity());
        existingPart.setSupplierId(updateModule.getSupplierId());
        existingPart.setUpdatedAt(LocalDateTime.now());

        boolean isSuccess = this.updateById(existingPart);
        return isSuccess ? Result.success() : Result.fail("更新配件记录失败。", 500);
    }

    /**
     * 【新】实现带密码验证的删除逻辑
     */
    @Override
    public Result<Void> deletePartWithPassword(DeletePartModule deleteModule) {
        Integer partId = deleteModule.getPartId();
        Integer userId = deleteModule.getUserId();
        String password = deleteModule.getPassword();

        // 1. 查询用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.fail("用户不存在。", 404);
        }

        // 2. 验证密码
        String encryptedPassword = Md5Password.generateMD5(password);
        if (!encryptedPassword.equals(user.getUserPasswordHash())) {
            return Result.fail("密码错误。", 401);
        }

        // 3. 密码验证通过，执行删除操作
        boolean isSuccess = this.removeById(partId);
        return isSuccess ? Result.success() : Result.fail("删除配件记录失败。", 500);
    }
}