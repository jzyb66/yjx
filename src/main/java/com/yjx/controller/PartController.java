package com.yjx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yjx.module.CreatePartModule;
import com.yjx.module.PartQueryModule;
import com.yjx.module.UpdatePartModule;
import com.yjx.pojo.Part;
import com.yjx.service.PartService;
import com.yjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 配件管理控制器
 */
@RestController
@RequestMapping("/parts") // 基础路径与前端JS代码中的URL匹配 (/yjx/parts)
public class PartController {

    @Autowired
    private PartService partService;

    /**
     * 获取所有配件记录（支持分页和搜索）
     * @param queryModule 查询参数
     * @return 包含列表和总数的分页结果
     */
    @GetMapping("/list")
    public Result<IPage<Part>> getPartList(@ModelAttribute PartQueryModule queryModule) {
        return partService.getPartList(queryModule);
    }

    /**
     * 创建新的配件记录
     * @param createModule JSON请求体
     * @return 操作结果
     */
    @PostMapping("/addPart")
    public Result<Void> addPart(@RequestBody CreatePartModule createModule) {
        return partService.createPart(createModule);
    }

    /**
     * 更新已有的配件记录
     * @param updateModule JSON请求体
     * @return 操作结果
     */
    @PostMapping("/updatePart")
    public Result<Void> updatePart(@RequestBody UpdatePartModule updateModule) {
        return partService.updatePart(updateModule);
    }

    /**
     * 根据ID删除配件记录
     * @param partId 配件ID，从URL路径中获取
     * @return 操作结果
     */
    @DeleteMapping("/delete/{partId}")
    public Result<Void> deletePart(@PathVariable("partId") Integer partId) {
        return partService.deletePartById(partId);
    }
}