package com.sakura.studylife.dto.statistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "月统计查询参数")
public class StatisticsMonthlyQueryRequest {

    @Schema(description = "月份，格式 yyyy-MM，默认当前月", example = "2026-04")
    private String month;
}
