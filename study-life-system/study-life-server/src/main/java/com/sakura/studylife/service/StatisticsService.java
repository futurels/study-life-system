package com.sakura.studylife.service;

import com.sakura.studylife.dto.statistics.StatisticsMonthlyQueryRequest;
import com.sakura.studylife.dto.statistics.StatisticsWeeklyQueryRequest;
import com.sakura.studylife.vo.statistics.StatisticsMonthlyVo;
import com.sakura.studylife.vo.statistics.StatisticsOverviewVo;
import com.sakura.studylife.vo.statistics.StatisticsWeeklyVo;

public interface StatisticsService {

    StatisticsOverviewVo getOverview(Long userId);

    StatisticsWeeklyVo getWeeklyStatistics(Long userId, StatisticsWeeklyQueryRequest queryRequest);

    StatisticsMonthlyVo getMonthlyStatistics(Long userId, StatisticsMonthlyQueryRequest queryRequest);
}
