package com.yjx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjx.mapper.PartMapper;
import com.yjx.mapper.UserMapper;
import com.yjx.module.CreatePartDTO;
import com.yjx.module.DeletePartDTO;
import com.yjx.module.PartQueryDTO;
import com.yjx.module.UpdatePartDTO;
import com.yjx.pojo.Part;
import com.yjx.pojo.User;
import com.yjx.service.PartService;
import com.yjx.util.Md5Password;
import com.yjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 配件管理服务接口的实现类。
 */
@Service
public class PartServiceImpl extends ServiceImpl<PartMapper, Part> implements PartService {

    @Autowired
    private PartMapper partMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据条件获取配件列表，并进行分页。
     *
     * @param queryModule 查询参数DTO。
     * @return 封装了配件分页结果的Result对象。
     */
    @Override
    public Result<IPage<Part>> getPartList(PartQueryDTO queryModule) {
        // 1. 规范化分页和排序参数
        int pageNum = queryModule.getPageNum() == null || queryModule.getPageNum() < 1 ? 1 : queryModule.getPageNum();
        int pageSize = queryModule.getPageSize() == null || queryModule.getPageSize() < 1 ? 10 : queryModule.getPageSize();
        Page<Part> page = new Page<>(pageNum, pageSize);
        if (queryModule.getSortField() == null || queryModule.getSortField().trim().isEmpty()) {
            queryModule.setSortField("partId");
        }
        if (queryModule.getSortPart() == null || (!"asc".equalsIgnoreCase(queryModule.getSortPart()) && !"desc".equalsIgnoreCase(queryModule.getSortPart()))) {
            queryModule.setSortPart("asc");
        }

        // 2. 调用Mapper执行查询
        IPage<Part> resultPage = partMapper.selectPartByCondition(page, queryModule);
        return Result.success(resultPage);
    }

    /**
     * 创建新的配件记录，包含业务校验。
     *
     * @param createModule 包含创建信息的DTO。
     * @return 操作结果。
     */
    @Override
    public Result<Void> createPart(CreatePartDTO createModule) {
        // 1. 校验配件名称是否已存在
        if (StringUtils.hasText(createModule.getPartName())) {
            QueryWrapper<Part> nameCheckWrapper = new QueryWrapper<>();
            nameCheckWrapper.eq("part_name", createModule.getPartName());
            if (partMapper.exists(nameCheckWrapper)) {
                return Result.fail("配件名称已存在，请勿重复添加", 409); // 409 Conflict
            }
        }

        // 2. 校验供应商ID是否有效（是否存在且角色为供应商）
        Integer supplierId = createModule.getSupplierId();
        User supplier = (supplierId != null) ? userMapper.selectById(supplierId) : null;
        if (supplier == null || supplier.getRoleId() != 5) { // 5 代表供应商角色
            return Result.fail("供应商不存在或无效", 400); // 400 Bad Request
        }

        // 3. 将DTO转换为POJO实体并保存
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
     * 更新配件记录，包含业务校验。
     *
     * @param updateModule 包含更新信息的DTO。
     * @return 操作结果。
     */
    @Override
    public Result<Void> updatePart(UpdatePartDTO updateModule) {
        // 1. 检查待更新的记录是否存在
        Part existingPart = this.getById(updateModule.getPartId());
        if (existingPart == null) {
            return Result.fail("未找到指定的配件记录。", 404); // 404 Not Found
        }

        // 2. 校验更新后的配件名称是否与其它记录冲突
        if (StringUtils.hasText(updateModule.getPartName())) {
            QueryWrapper<Part> nameCheckWrapper = new QueryWrapper<>();
            nameCheckWrapper.eq("part_name", updateModule.getPartName());
            nameCheckWrapper.ne("part_id", updateModule.getPartId()); // 排除自身
            if (partMapper.exists(nameCheckWrapper)) {
                return Result.fail("更新失败，配件名称已存在", 409);
            }
        }

        // 3. 校验更新后的供应商ID是否有效
        Integer supplierId = updateModule.getSupplierId();
        if (supplierId != null) {
            User supplier = userMapper.selectById(supplierId);
            if (supplier == null || supplier.getRoleId() != 5) {
                return Result.fail("供应商不存在或无效", 400);
            }
        }

        // 4. 更新实体属性并保存
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
     * 删除配件记录，需密码验证。
     *
     * @param deleteModule 包含删除信息的DTO。
     * @return 操作结果。
     */
    @Override
    public Result<Void> deletePartWithPassword(DeletePartDTO deleteModule) {
        Integer partId = deleteModule.getPartId();
        Integer userId = deleteModule.getUserId();
        String password = deleteModule.getPassword();

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
        boolean isSuccess = this.removeById(partId);
        return isSuccess ? Result.success() : Result.fail("删除配件记录失败。", 500);
    }
}