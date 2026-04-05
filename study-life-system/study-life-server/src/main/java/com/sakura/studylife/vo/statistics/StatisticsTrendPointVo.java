package com.sakura.studylife.vo.statistics;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Data;

@Data
@Schema(description = "统计趋势点")
public class StatisticsTrendPointVo {

    @Schema(description = "日期", example = "2026-04-05")
    private LocalDate date;

    @Schema(description = "计划总数", example = "2")
    private Long planCount;

    @Schema(description = "已完成计划数", example = "1")
    private Long completedPlanCount;
}
