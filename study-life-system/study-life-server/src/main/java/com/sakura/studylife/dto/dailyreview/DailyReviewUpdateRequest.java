package com.sakura.studylife.dto.dailyreview;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;

@Data
@Schema(description = "编辑每日复盘请求")
public class DailyReviewUpdateRequest {

    @NotNull
    @Schema(description = "复盘日期", example = "2026-04-05")
    private LocalDate reviewDate;

    @Schema(description = "今日已完成事项", example = "完成 Java 集合学习、刷了 3 道 SQL 题")
    private String completedItems;

    @Schema(description = "今日未完成事项", example = "英语单词没背完")
    private String unfinishedItems;

    @Schema(description = "未完成原因分析", example = "下午注意力不够集中")
    private String unfinishedReason;

    @Size(max = 500)
    @Schema(description = "今日整体评价", example = "整体效率还可以，但专注度一般")
    private String overallEvaluation;

    @Schema(description = "明日计划", example = "上午补英语单词，下午继续刷题")
    private String tomorrowPlan;

    @Min(1)
    @Max(10)
    @Schema(description = "今日评分，1 到 10 分", example = "8")
    private Integer reviewScore;
}
