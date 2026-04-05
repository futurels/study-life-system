package com.sakura.studylife.controller;

import com.sakura.studylife.common.result.Result;
import com.sakura.studylife.common.util.CurrentUserContext;
import com.sakura.studylife.dto.statistics.StatisticsMonthlyQueryRequest;
import com.sakura.studylife.dto.statistics.StatisticsWeeklyQueryRequest;
import com.sakura.studylife.service.StatisticsService;
import com.sakura.studylife.vo.statistics.StatisticsMonthlyVo;
import com.sakura.studylife.vo.statistics.StatisticsOverviewVo;
import com.sakura.studylife.vo.statistics.StatisticsWeeklyVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "统计分析模块")
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Operation(summary = "查询统计概览")
    @GetMapping("/overview")
    public Result<StatisticsOverviewVo> overview() {
        return Result.success("查询成功", statisticsService.getOverview(CurrentUserContext.getUserId()));
    }

    @Operation(summary = "查询周统计")
    @GetMapping("/weekly")
    public Result<StatisticsWeeklyVo> weekly(StatisticsWeeklyQueryRequest queryRequest) {
        return Result.success("查询成功", statisticsService.getWeeklyStatistics(CurrentUserContext.getUserId(), queryRequest));
    }

    @Operation(summary = "查询月统计")
    @GetMapping("/monthly")
    public Result<StatisticsMonthlyVo> monthly(StatisticsMonthlyQueryRequest queryRequest) {
        return Result.success("查询成功", statisticsService.getMonthlyStatistics(CurrentUserContext.getUserId(), queryRequest));
    }
}
