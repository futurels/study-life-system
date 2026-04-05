package com.sakura.studylife.vo.dailyreview;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "每日复盘主键响应")
public record DailyReviewIdVo(
        @Schema(description = "每日复盘ID", example = "1")
        Long id
) {
}
