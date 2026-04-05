package com.sakura.studylife.vo.dailyreview;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "每日复盘列表项")
public class DailyReviewListItemVo {

    @Schema(description = "每日复盘ID", example = "1")
    private Long id;

    @Schema(description = "复盘日期", example = "2026-04-05")
    private LocalDate reviewDate;

    @Schema(description = "今日已完成事项", example = "完成 Java 集合学习、刷了 3 道 SQL 题")
    private String completedItems;

    @Schema(description = "今日未完成事项", example = "英语单词没背完")
    private String unfinishedItems;

    @Schema(description = "未完成原因分析", example = "下午注意力不够集中")
    private String unfinishedReason;

    @Schema(description = "今日整体评价", example = "整体效率还可以，但专注度一般")
    private String overallEvaluation;

    @Schema(description = "明日计划", example = "上午补英语单词，下午继续刷题")
    private String tomorrowPlan;

    @Schema(description = "今日评分", example = "7")
    private Integer reviewScore;

    @Schema(description = "创建时间", example = "2026-04-05T22:30:00")
    private LocalDateTime createdAt;
}
