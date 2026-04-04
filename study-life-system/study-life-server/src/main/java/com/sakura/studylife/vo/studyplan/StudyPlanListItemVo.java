package com.sakura.studylife.vo.studyplan;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "学习计划列表项")
public class StudyPlanListItemVo {

    private Long id;

    private String planTitle;

    private String planContent;

    private LocalDate planDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer priorityLevel;

    private Integer planStatus;

    private LocalDateTime completionTime;

    private String remark;

    private LocalDateTime createdAt;
}
