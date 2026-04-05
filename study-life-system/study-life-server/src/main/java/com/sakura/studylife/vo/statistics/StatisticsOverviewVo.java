package com.sakura.studylife.vo.statistics;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "统计概览")
public class StatisticsOverviewVo {

    @Schema(description = "今日计划总数", example = "3")
    private Long todayPlanCount;

    @Schema(description = "今日已完成计划数", example = "2")
    private Long todayCompletedPlanCount;

    @Schema(description = "今日完成率", example = "66.67")
    private BigDecimal todayCompletionRate;

    @Schema(description = "本周计划总数", example = "12")
    private Long thisWeekPlanCount;

    @Schema(description = "本周已完成计划数", example = "9")
    private Long thisWeekCompletedPlanCount;

    @Schema(description = "本周完成率", example = "75.00")
    private BigDecimal thisWeekCompletionRate;

    @Schema(description = "本月生活记录天数", example = "10")
    private Long thisMonthLifeRecordDays;

    @Schema(description = "本月复盘天数", example = "8")
    private Long thisMonthReviewDays;
}
