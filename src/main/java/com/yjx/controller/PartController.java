package com.yjx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yjx.module.CreatePartModule;
import com.yjx.module.DeletePartModule; // 导入
import com.yjx.module.PartQueryModule;
import com.yjx.module.UpdatePartModule;
import com.yjx.pojo.Part;
import com.yjx.service.PartService;
import com.yjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parts")
public class PartController {

    @Autowired
    private PartService partService;

    @GetMapping("/list")
    public Result<IPage<Part>> getPartList(@ModelAttribute PartQueryModule queryModule) {
        return partService.getPartList(queryModule);
    }

    @PostMapping("/addPart")
    public Result<Void> addPart(@RequestBody CreatePartModule createModule) {
        return partService.createPart(createModule);
    }

    @PostMapping("/updatePart")
    public Result<Void> updatePart(@RequestBody UpdatePartModule updateModule) {
        return partService.updatePart(updateModule);
    }

    /**
     * 【新】根据ID删除配件记录（需要密码验证）
     * @param deleteModule 包含配件ID、用户ID和密码的请求体
     * @return 操作结果
     */
    @PostMapping("/deletePart")
    public Result<Void> deletePartWithPassword(@RequestBody DeletePartModule deleteModule) {
        return partService.deletePartWithPassword(deleteModule);
    }
}