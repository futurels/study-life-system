package com.sakura.studylife.dto.statistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "周统计查询参数")
public class StatisticsWeeklyQueryRequest {

    @Schema(description = "开始日期，格式 yyyy-MM-dd，默认当前周开始", example = "2026-04-06")
    private String weekStartDate;

    @Schema(description = "结束日期，格式 yyyy-MM-dd，默认当前周结束", example = "2026-04-12")
    private String weekEndDate;
}
