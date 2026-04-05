package com.sakura.studylife.dto.liferecord;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import lombok.Data;

@Data
@Schema(description = "生活记录分页查询参数")
public class LifeRecordQueryRequest {

    @Min(1)
    @Schema(description = "页码", example = "1")
    private Integer pageNum;

    @Min(1)
    @Max(100)
    @Schema(description = "每页条数", example = "10")
    private Integer pageSize;

    @Schema(description = "指定日期", example = "2026-04-05")
    private LocalDate recordDate;

    @Schema(description = "开始日期", example = "2026-04-01")
    private LocalDate startDate;

    @Schema(description = "结束日期", example = "2026-04-07")
    private LocalDate endDate;

    @Schema(description = "心情状态", example = "平静")
    private String moodStatus;
}
