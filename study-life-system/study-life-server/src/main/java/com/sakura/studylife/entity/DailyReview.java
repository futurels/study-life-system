package com.sakura.studylife.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class DailyReview {

    private Long id;

    private Long userId;

    private LocalDate reviewDate;

    private String completedItems;

    private String unfinishedItems;

    private String unfinishedReason;

    private String overallEvaluation;

    private String tomorrowPlan;

    private Integer reviewScore;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer deletedFlag;
}
