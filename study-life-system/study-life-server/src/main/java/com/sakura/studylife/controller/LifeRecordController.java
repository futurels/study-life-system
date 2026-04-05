package com.sakura.studylife.controller;

import com.sakura.studylife.common.result.Result;
import com.sakura.studylife.common.util.CurrentUserContext;
import com.sakura.studylife.dto.liferecord.LifeRecordCreateRequest;
import com.sakura.studylife.dto.liferecord.LifeRecordQueryRequest;
import com.sakura.studylife.dto.liferecord.LifeRecordUpdateRequest;
import com.sakura.studylife.service.LifeRecordService;
import com.sakura.studylife.vo.common.PageResponse;
import com.sakura.studylife.vo.liferecord.LifeRecordIdVo;
import com.sakura.studylife.vo.liferecord.LifeRecordListItemVo;
import com.sakura.studylife.vo.liferecord.LifeRecordVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "生活记录模块")
@RestController
@RequestMapping("/api/life-record")
public class LifeRecordController {

    private final LifeRecordService lifeRecordService;

    public LifeRecordController(LifeRecordService lifeRecordService) {
        this.lifeRecordService = lifeRecordService;
    }

    @Operation(summary = "新建生活记录")
    @PostMapping
    public Result<LifeRecordIdVo> create(@Valid @RequestBody LifeRecordCreateRequest request) {
        return Result.success("创建成功", lifeRecordService.create(CurrentUserContext.getUserId(), request));
    }

    @Operation(summary = "分页查询生活记录列表")
    @GetMapping("/list")
    public Result<PageResponse<LifeRecordListItemVo>> queryPage(@Valid LifeRecordQueryRequest queryRequest) {
        return Result.success("查询成功", lifeRecordService.queryPage(CurrentUserContext.getUserId(), queryRequest));
    }

    @Operation(summary = "查看生活记录详情")
    @GetMapping("/{id}")
    public Result<LifeRecordVo> getDetail(@PathVariable @Positive Long id) {
        return Result.success("查询成功", lifeRecordService.getDetail(CurrentUserContext.getUserId(), id));
    }

    @Operation(summary = "编辑生活记录")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable @Positive Long id, @Valid @RequestBody LifeRecordUpdateRequest request) {
        lifeRecordService.update(CurrentUserContext.getUserId(), id, request);
        return Result.successMessage("修改成功");
    }

    @Operation(summary = "删除生活记录")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable @Positive Long id) {
        lifeRecordService.delete(CurrentUserContext.getUserId(), id);
        return Result.successMessage("删除成功");
    }
}
