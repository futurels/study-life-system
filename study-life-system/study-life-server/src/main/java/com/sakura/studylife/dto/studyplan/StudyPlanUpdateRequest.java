package com.sakura.studylife.dto.studyplan;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
@Schema(description = "编辑学习计划请求")
public class StudyPlanUpdateRequest {

    @NotBlank
    @Size(min = 1, max = 100)
    @Schema(description = "计划标题", example = "刷 SQL 题")
    private String planTitle;

    @Schema(description = "计划内容")
    private String planContent;

    @NotNull
    @Schema(description = "计划日期", example = "2026-04-04")
    private LocalDate planDate;

    @Schema(description = "开始时间", example = "20:00:00")
    private LocalTime startTime;

    @Schema(description = "结束时间", example = "21:30:00")
    private LocalTime endTime;

    @Min(1)
    @Max(3)
    @Schema(description = "优先级：1高，2中，3低", example = "2")
    private Integer priorityLevel;

    @Size(max = 500)
    @Schema(description = "备注")
    private String remark;
}
