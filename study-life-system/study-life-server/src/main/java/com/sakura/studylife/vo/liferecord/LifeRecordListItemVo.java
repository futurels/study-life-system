package com.sakura.studylife.vo.liferecord;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "生活记录列表项")
public class LifeRecordListItemVo {

    @Schema(description = "生活记录ID", example = "1")
    private Long id;

    @Schema(description = "记录日期", example = "2026-04-05")
    private LocalDate recordDate;

    @Schema(description = "日记正文", example = "今天上午状态一般，下午完成了学习任务，晚上去散步了。")
    private String diaryContent;

    @Schema(description = "心情状态", example = "平静")
    private String moodStatus;

    @Schema(description = "睡眠时长", example = "7.5")
    private BigDecimal sleepHours;

    @Schema(description = "饮食简述", example = "早餐简单，晚饭较清淡")
    private String dietNote;

    @Schema(description = "运动简述", example = "散步 30 分钟")
    private String exerciseNote;

    @Schema(description = "创建时间", example = "2026-04-05T21:30:00")
    private LocalDateTime createdAt;
}
