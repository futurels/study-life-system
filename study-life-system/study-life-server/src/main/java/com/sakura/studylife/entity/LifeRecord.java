package com.sakura.studylife.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class LifeRecord {

    private Long id;

    private Long userId;

    private LocalDate recordDate;

    private String diaryContent;

    private String moodStatus;

    private BigDecimal sleepHours;

    private String dietNote;

    private String exerciseNote;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer deletedFlag;
}
