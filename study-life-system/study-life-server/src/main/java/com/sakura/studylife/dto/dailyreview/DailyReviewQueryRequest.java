package com.sakura.studylife.dto.dailyreview;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import lombok.Data;

@Data
@Schema(description = "每日复盘分页查询参数")
public class DailyReviewQueryRequest {

    @Min(1)
    @Schema(description = "页码", example = "1")
    private Integer pageNum;

    @Min(1)
    @Max(100)
    @Schema(description = "每页条数", example = "10")
    private Integer pageSize;

    @Schema(description = "指定日期", example = "2026-04-05")
    private LocalDate reviewDate;

    @Schema(description = "开始日期", example = "2026-04-01")
    private LocalDate startDate;

    @Schema(description = "结束日期", example = "2026-04-07")
    private LocalDate endDate;
}
