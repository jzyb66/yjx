package com.yjx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yjx.module.CreatePartModule;
import com.yjx.module.DeletePartModule;
import com.yjx.module.PartQueryModule;
import com.yjx.module.UpdatePartModule;
import com.yjx.pojo.Part;
import com.yjx.util.Result;

/**
 * 配件管理服务接口
 */
public interface PartService extends IService<Part> {

    /**
     * 根据条件获取配件列表（分页）
     * @param queryModule 查询参数
     * @return 分页结果
     */
    Result<IPage<Part>> getPartList(PartQueryModule queryModule);

    /**
     * 创建一个新的配件记录
     * @param createModule 包含新增信息的对象
     * @return 操作结果
     */
    Result<Void> createPart(CreatePartModule createModule);

    /**
     * 更新一个已存在的配件记录
     * @param updateModule 包含更新信息的对象
     * @return 操作结果
     */
    Result<Void> updatePart(UpdatePartModule updateModule);

    /**
     * 【新】根据ID删除配件记录（带密码验证）
     * @param deleteModule 包含删除信息的对象
     * @return 操作结果
     */
    Result<Void> deletePartWithPassword(DeletePartModule deleteModule);
}