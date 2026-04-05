package com.sakura.studylife.vo.statistics;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "周统计结果")
public class StatisticsWeeklyVo {

    @Schema(description = "计划总数", example = "12")
    private Long planCount;

    @Schema(description = "已完成计划数", example = "9")
    private Long completedPlanCount;

    @Schema(description = "完成率", example = "75.00")
    private BigDecimal completionRate;

    @Schema(description = "生活记录天数", example = "5")
    private Long lifeRecordDays;

    @Schema(description = "复盘天数", example = "4")
    private Long reviewDays;

    @Schema(description = "按天趋势")
    private List<StatisticsTrendPointVo> dailyTrend;

    @Schema(description = "任务状态分布")
    private List<StatisticsStatusDistributionVo> statusDistribution;
}
