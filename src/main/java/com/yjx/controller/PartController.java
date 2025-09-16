package com.yjx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yjx.module.CreatePartDTO;
import com.yjx.module.DeletePartDTO;
import com.yjx.module.PartQueryDTO;
import com.yjx.module.UpdatePartDTO;
import com.yjx.pojo.Part;
import com.yjx.service.PartService;
import com.yjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 配件信息管理API控制器。
 * 负责处理与配件相关的HTTP请求，包括查询、新增、更新和删除。
 */
@RestController
@RequestMapping("/parts")
public class PartController {

    /**
     * 自动注入配件服务层的Bean。
     */
    @Autowired
    private PartService partService;

    /**
     * 根据条件分页查询配件列表。
     *
     * @param queryDTO 包含分页、排序和搜索条件的查询参数对象。
     * @return 包含配件分页信息的结果对象。
     */
    @GetMapping("/list")
    public Result<IPage<Part>> getPartList(@ModelAttribute PartQueryDTO queryDTO) {
        return partService.getPartList(queryDTO);
    }

    /**
     * 创建新的配件记录。
     *
     * @param createDTO 包含配件信息的创建参数对象。
     * @return 根据业务处理结果返回相应的HTTP状态码和结果信息。
     */
    @PostMapping("/addPart")
    public ResponseEntity<Result<Void>> addPart(@RequestBody CreatePartDTO createDTO) {
        Result<Void> result = partService.createPart(createDTO);
        if (result.getCode() != 200) {
            return new ResponseEntity<>(result, HttpStatus.valueOf(result.getCode()));
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 更新指定ID的配件信息。
     *
     * @param updateDTO 包含配件更新信息的参数对象。
     * @return 根据业务处理结果返回相应的HTTP状态码和结果信息。
     */
    @PostMapping("/updatePart")
    public ResponseEntity<Result<Void>> updatePart(@RequestBody UpdatePartDTO updateDTO) {
        Result<Void> result = partService.updatePart(updateDTO);
        if (result.getCode() != 200) {
            return new ResponseEntity<>(result, HttpStatus.valueOf(result.getCode()));
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 根据ID删除配件记录（需要密码验证）。
     *
     * @param deleteDTO 包含配件ID和用户验证信息的参数对象。
     * @return 根据业务处理结果返回相应的HTTP状态码和结果信息。
     */
    @PostMapping("/deletePart")
    public ResponseEntity<Result<Void>> deletePartWithPassword(@RequestBody DeletePartDTO deleteDTO) {
        Result<Void> result = partService.deletePartWithPassword(deleteDTO);
        if (result.getCode() != 200) {
            return new ResponseEntity<>(result, HttpStatus.valueOf(result.getCode()));
        }
        return ResponseEntity.ok(result);
    }
}