package com.sakura.studylife.dto.studyplan;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "学习计划状态更新请求")
public class StudyPlanStatusUpdateRequest {

    @NotNull
    @Min(0)
    @Max(3)
    @Schema(description = "状态：0未开始，1进行中，2已完成，3已取消", example = "2")
    private Integer planStatus;
}
