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
@Schema(description = "创建生活记录请求")
public class LifeRecordCreateRequest {

    @NotNull
    @Schema(description = "记录日期", example = "2026-04-05")
    private LocalDate recordDate;

    @NotBlank
    @Schema(description = "日记正文", example = "今天上午状态一般，下午完成了学习任务，晚上去散步了。")
    private String diaryContent;

    @Size(max = 20)
    @Schema(description = "心情状态", example = "平静")
    private String moodStatus;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "24.0")
    @Schema(description = "睡眠时长，单位小时", example = "7.5")
    private BigDecimal sleepHours;

    @Size(max = 500)
    @Schema(description = "饮食简述", example = "早餐简单，晚饭较清淡")
    private String dietNote;

    @Size(max = 500)
    @Schema(description = "运动简述", example = "散步 30 分钟")
    private String exerciseNote;
}
