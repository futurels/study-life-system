package com.sakura.studylife.vo.liferecord;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "生活记录主键响应")
public record LifeRecordIdVo(
        @Schema(description = "生活记录ID", example = "1")
        Long id
) {
}
