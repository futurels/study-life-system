package com.sakura.studylife.mapper;

import com.sakura.studylife.vo.statistics.StatisticsStatusDistributionVo;
import com.sakura.studylife.vo.statistics.StatisticsTrendPointVo;
import java.time.LocalDate;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StatisticsMapper {

    Long countPlanByDateRange(@Param("userId") Long userId,
                              @Param("startDate") LocalDate startDate,
                              @Param("endDate") LocalDate endDate);

    Long countCompletedPlanByDateRange(@Param("userId") Long userId,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);

    Long countLifeRecordDaysByDateRange(@Param("userId") Long userId,
                                        @Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);

    Long countReviewDaysByDateRange(@Param("userId") Long userId,
                                    @Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate);

    List<StatisticsTrendPointVo> selectPlanTrendByDateRange(@Param("userId") Long userId,
                                                            @Param("startDate") LocalDate startDate,
                                                            @Param("endDate") LocalDate endDate);

    List<StatisticsStatusDistributionVo> selectPlanStatusDistributionByDateRange(@Param("userId") Long userId,
                                                                                 @Param("startDate") LocalDate startDate,
                                                                                 @Param("endDate") LocalDate endDate);

    List<StatisticsTrendPointVo> selectCompletedPlanTrendByDateRange(@Param("userId") Long userId,
                                                                     @Param("startDate") LocalDate startDate,
                                                                     @Param("endDate") LocalDate endDate);
}
