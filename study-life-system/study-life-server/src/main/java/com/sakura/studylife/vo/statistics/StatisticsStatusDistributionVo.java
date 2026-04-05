package com.sakura.studylife.vo.statistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "任务状态分布")
public class StatisticsStatusDistributionVo {

    @Schema(description = "计划状态", example = "2")
    private Integer planStatus;

    @Schema(description = "数量", example = "5")
    private Long count;
}
