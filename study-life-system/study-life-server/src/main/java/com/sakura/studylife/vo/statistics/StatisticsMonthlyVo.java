package com.sakura.studylife.vo.statistics;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "月统计结果")
public class StatisticsMonthlyVo {

    @Schema(description = "生活记录天数", example = "18")
    private Long lifeRecordDays;

    @Schema(description = "复盘天数", example = "15")
    private Long reviewDays;

    @Schema(description = "按天计划完成趋势")
    private List<StatisticsTrendPointVo> planCompletionTrend;
}
