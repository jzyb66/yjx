package com.yjx.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjx.mapper.PartMapper;
import com.yjx.module.CreatePartModule;
import com.yjx.module.PartQueryModule;
import com.yjx.module.UpdatePartModule;
import com.yjx.pojo.Part;
import com.yjx.service.PartService;
import com.yjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PartServiceImpl extends ServiceImpl<PartMapper, Part> implements PartService {

    @Autowired
    private PartMapper partMapper;

    @Override
    public Result<IPage<Part>> getPartList(PartQueryModule queryModule) {
        // 1. 设置分页参数，并提供默认值
        int pageNum = queryModule.getPageNum() == null || queryModule.getPageNum() < 1 ? 1 : queryModule.getPageNum();
        int pageSize = queryModule.getPageSize() == null || queryModule.getPageSize() < 1 ? 10 : queryModule.getPageSize();
        Page<Part> page = new Page<>(pageNum, pageSize);

        // 2. 设置默认排序规则
        if (queryModule.getSortField() == null || queryModule.getSortField().trim().isEmpty()) {
            queryModule.setSortField("partId");
        }
        if (queryModule.getSortPart() == null || (!"asc".equalsIgnoreCase(queryModule.getSortPart()) && !"desc".equalsIgnoreCase(queryModule.getSortPart()))) {
            queryModule.setSortPart("asc");
        }

        // 3. 调用Mapper执行查询
        IPage<Part> resultPage = partMapper.selectPartByCondition(page, queryModule);

        // 4. 返回成功结果，将分页对象直接放入Result的data中，以匹配前端JS的解析逻辑
        return Result.success(resultPage);
    }

    @Override
    public Result<Void> createPart(CreatePartModule createModule) {
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

    @Override
    public Result<Void> updatePart(UpdatePartModule updateModule) {
        // 检查记录是否存在
        Part existingPart = this.getById(updateModule.getPartId());
        if (existingPart == null) {
            return Result.fail("未找到指定的配件记录。", 404);
        }

        // 更新字段
        existingPart.setPartName(updateModule.getPartName());
        existingPart.setPartDescription(updateModule.getPartDescription());
        existingPart.setPartPrice(updateModule.getPartPrice());
        existingPart.setStockQuantity(updateModule.getStockQuantity());
        existingPart.setSupplierId(updateModule.getSupplierId());
        existingPart.setUpdatedAt(LocalDateTime.now());

        boolean isSuccess = this.updateById(existingPart);
        return isSuccess ? Result.success() : Result.fail("更新配件记录失败。", 500);
    }

    @Override
    public Result<Void> deletePartById(Integer partId) {
        if (partId == null) {
            return Result.fail("配件ID不能为空。", 400);
        }
        boolean isSuccess = this.removeById(partId);
        return isSuccess ? Result.success() : Result.fail("删除配件记录失败。", 500);
    }
}