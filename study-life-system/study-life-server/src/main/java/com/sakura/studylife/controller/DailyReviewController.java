package com.sakura.studylife.controller;

import com.sakura.studylife.common.result.Result;
import com.sakura.studylife.common.util.CurrentUserContext;
import com.sakura.studylife.dto.dailyreview.DailyReviewCreateRequest;
import com.sakura.studylife.dto.dailyreview.DailyReviewQueryRequest;
import com.sakura.studylife.dto.dailyreview.DailyReviewUpdateRequest;
import com.sakura.studylife.service.DailyReviewService;
import com.sakura.studylife.vo.common.PageResponse;
import com.sakura.studylife.vo.dailyreview.DailyReviewIdVo;
import com.sakura.studylife.vo.dailyreview.DailyReviewListItemVo;
import com.sakura.studylife.vo.dailyreview.DailyReviewVo;
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
@Tag(name = "每日复盘模块")
@RestController
@RequestMapping("/api/daily-review")
public class DailyReviewController {

    private final DailyReviewService dailyReviewService;

    public DailyReviewController(DailyReviewService dailyReviewService) {
        this.dailyReviewService = dailyReviewService;
    }

    @Operation(summary = "新建每日复盘")
    @PostMapping
    public Result<DailyReviewIdVo> create(@Valid @RequestBody DailyReviewCreateRequest request) {
        return Result.success("创建成功", dailyReviewService.create(CurrentUserContext.getUserId(), request));
    }

    @Operation(summary = "分页查询每日复盘列表")
    @GetMapping("/list")
    public Result<PageResponse<DailyReviewListItemVo>> queryPage(@Valid DailyReviewQueryRequest queryRequest) {
        return Result.success("查询成功", dailyReviewService.queryPage(CurrentUserContext.getUserId(), queryRequest));
    }

    @Operation(summary = "查看每日复盘详情")
    @GetMapping("/{id}")
    public Result<DailyReviewVo> getDetail(@PathVariable @Positive Long id) {
        return Result.success("查询成功", dailyReviewService.getDetail(CurrentUserContext.getUserId(), id));
    }

    @Operation(summary = "编辑每日复盘")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable @Positive Long id, @Valid @RequestBody DailyReviewUpdateRequest request) {
        dailyReviewService.update(CurrentUserContext.getUserId(), id, request);
        return Result.successMessage("修改成功");
    }

    @Operation(summary = "删除每日复盘")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable @Positive Long id) {
        dailyReviewService.delete(CurrentUserContext.getUserId(), id);
        return Result.successMessage("删除成功");
    }
}
