package com.sakura.studylife.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Data;

@Data
public class StudyPlan {

    private Long id;

    private Long userId;

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

    private LocalDateTime updatedAt;

    private Integer deletedFlag;
}
