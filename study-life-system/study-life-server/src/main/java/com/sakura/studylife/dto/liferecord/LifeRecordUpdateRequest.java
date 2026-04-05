package com.sakura.studylife.dto.liferecord;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
@Schema(description = "编辑生活记录请求")
public class LifeRecordUpdateRequest {

    @NotNull
    @Schema(description = "记录日期", example = "2026-04-05")
    private LocalDate recordDate;

    @NotBlank
    @Schema(description = "日记正文", example = "今天把白天的节奏重新梳理了一遍，晚上去快走了。")
    private String diaryContent;

    @Size(max = 20)
    @Schema(description = "心情状态", example = "开心")
    private String moodStatus;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "24.0")
    @Schema(description = "睡眠时长，单位小时", example = "8.0")
    private BigDecimal sleepHours;

    @Size(max = 500)
    @Schema(description = "饮食简述", example = "三餐规律，晚饭较清淡")
    private String dietNote;

    @Size(max = 500)
    @Schema(description = "运动简述", example = "慢跑 20 分钟")
    private String exerciseNote;
}
