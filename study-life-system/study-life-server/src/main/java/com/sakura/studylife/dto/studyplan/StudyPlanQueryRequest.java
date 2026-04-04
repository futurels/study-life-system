package com.sakura.studylife.dto.studyplan;

import io.swagger.v3.oas.annotations.Parameter;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class StudyPlanQueryRequest {

    @Parameter(description = "当前页码")
    private Integer pageNum = 1;

    @Parameter(description = "每页条数")
    private Integer pageSize = 10;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Parameter(description = "指定日期")
    private LocalDate planDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Parameter(description = "开始日期")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Parameter(description = "结束日期")
    private LocalDate endDate;

    @Parameter(description = "计划状态：0未开始，1进行中，2已完成，3已取消")
    private Integer planStatus;

    @Parameter(description = "优先级：1高，2中，3低")
    private Integer priorityLevel;
}
