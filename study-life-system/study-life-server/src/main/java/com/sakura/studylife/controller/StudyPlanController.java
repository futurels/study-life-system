package com.sakura.studylife.controller;

import com.sakura.studylife.common.result.Result;
import com.sakura.studylife.common.util.CurrentUserContext;
import com.sakura.studylife.dto.studyplan.StudyPlanCreateRequest;
import com.sakura.studylife.dto.studyplan.StudyPlanQueryRequest;
import com.sakura.studylife.dto.studyplan.StudyPlanStatusUpdateRequest;
import com.sakura.studylife.dto.studyplan.StudyPlanUpdateRequest;
import com.sakura.studylife.service.StudyPlanService;
import com.sakura.studylife.vo.common.PageResponse;
import com.sakura.studylife.vo.studyplan.StudyPlanIdVo;
import com.sakura.studylife.vo.studyplan.StudyPlanListItemVo;
import com.sakura.studylife.vo.studyplan.StudyPlanVo;
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
@Tag(name = "学习计划模块")
@RestController
@RequestMapping("/api/study-plan")
public class StudyPlanController {

    private final StudyPlanService studyPlanService;

    public StudyPlanController(StudyPlanService studyPlanService) {
        this.studyPlanService = studyPlanService;
    }

    @Operation(summary = "新建学习计划")
    @PostMapping
    public Result<StudyPlanIdVo> create(@Valid @RequestBody StudyPlanCreateRequest request) {
        return Result.success("创建成功", studyPlanService.create(CurrentUserContext.getUserId(), request));
    }

    @Operation(summary = "分页查询学习计划列表")
    @GetMapping("/list")
    public Result<PageResponse<StudyPlanListItemVo>> queryPage(@Valid StudyPlanQueryRequest queryRequest) {
        return Result.success("查询成功", studyPlanService.queryPage(CurrentUserContext.getUserId(), queryRequest));
    }

    @Operation(summary = "查看学习计划详情")
    @GetMapping("/{id}")
    public Result<StudyPlanVo> getDetail(@PathVariable @Positive Long id) {
        return Result.success("查询成功", studyPlanService.getDetail(CurrentUserContext.getUserId(), id));
    }

    @Operation(summary = "编辑学习计划")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable @Positive Long id, @Valid @RequestBody StudyPlanUpdateRequest request) {
        studyPlanService.update(CurrentUserContext.getUserId(), id, request);
        return Result.successMessage("修改成功");
    }

    @Operation(summary = "删除学习计划")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable @Positive Long id) {
        studyPlanService.delete(CurrentUserContext.getUserId(), id);
        return Result.successMessage("删除成功");
    }

    @Operation(summary = "修改学习计划状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable @Positive Long id,
                                     @Valid @RequestBody StudyPlanStatusUpdateRequest request) {
        studyPlanService.updateStatus(CurrentUserContext.getUserId(), id, request);
        return Result.successMessage("状态更新成功");
    }
}
