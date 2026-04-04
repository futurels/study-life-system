package com.sakura.studylife.vo.studyplan;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "学习计划主键响应")
public class StudyPlanIdVo {

    private Long id;
}
