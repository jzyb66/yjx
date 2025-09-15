package com.yjx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yjx.module.CreatePartModule;
import com.yjx.module.DeletePartModule;
import com.yjx.module.PartQueryModule;
import com.yjx.module.UpdatePartModule;
import com.yjx.pojo.Part;
import com.yjx.service.PartService;
import com.yjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parts")
public class PartController {

    @Autowired
    private PartService partService;

    // 查询列表的方法保持不变，它通常总是返回成功的结果
    @GetMapping("/list")
    public Result<IPage<Part>> getPartList(@ModelAttribute PartQueryModule queryModule) {
        return partService.getPartList(queryModule);
    }

    /**
     * 【改】创建配件，并根据结果返回相应的HTTP状态码
     */
    @PostMapping("/addPart")
    public ResponseEntity<Result<Void>> addPart(@RequestBody CreatePartModule createModule) {
        Result<Void> result = partService.createPart(createModule);

        // 检查您Result对象中的code字段，如果不是200，则返回对应的HTTP错误状态码
        if (result.getCode() != 200) {
            return new ResponseEntity<>(result, HttpStatus.valueOf(result.getCode()));
        }

        // 如果code是200，则返回HTTP OK
        return ResponseEntity.ok(result);
    }

    /**
     * 【改】更新配件，并根据结果返回相应的HTTP状态码
     */
    @PostMapping("/updatePart")
    public ResponseEntity<Result<Void>> updatePart(@RequestBody UpdatePartModule updateModule) {
        Result<Void> result = partService.updatePart(updateModule);

        // 检查您Result对象中的code字段
        if (result.getCode() != 200) {
            return new ResponseEntity<>(result, HttpStatus.valueOf(result.getCode()));
        }

        return ResponseEntity.ok(result);
    }

    /**
     * 【改】根据ID删除配件记录，并根据结果返回相应的HTTP状态码
     */
    @PostMapping("/deletePart")
    public ResponseEntity<Result<Void>> deletePartWithPassword(@RequestBody DeletePartModule deleteModule) {
        Result<Void> result = partService.deletePartWithPassword(deleteModule);

        // 检查您Result对象中的code字段
        if (result.getCode() != 200) {
            return new ResponseEntity<>(result, HttpStatus.valueOf(result.getCode()));
        }

        return ResponseEntity.ok(result);
    }
}