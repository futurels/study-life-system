package com.sakura.studylife.service.impl;

import com.sakura.studylife.common.enums.ResultCode;
import com.sakura.studylife.dto.statistics.StatisticsMonthlyQueryRequest;
import com.sakura.studylife.dto.statistics.StatisticsWeeklyQueryRequest;
import com.sakura.studylife.exception.BusinessException;
import com.sakura.studylife.mapper.StatisticsMapper;
import com.sakura.studylife.service.StatisticsService;
import com.sakura.studylife.vo.statistics.StatisticsMonthlyVo;
import com.sakura.studylife.vo.statistics.StatisticsOverviewVo;
import com.sakura.studylife.vo.statistics.StatisticsStatusDistributionVo;
import com.sakura.studylife.vo.statistics.StatisticsTrendPointVo;
import com.sakura.studylife.vo.statistics.StatisticsWeeklyVo;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsMapper statisticsMapper;

    public StatisticsServiceImpl(StatisticsMapper statisticsMapper) {
        this.statisticsMapper = statisticsMapper;
    }

    @Override
    public StatisticsOverviewVo getOverview(Long userId) {
        LocalDate today = LocalDate.now();
        DateRange weekRange = resolveWeekRange(new StatisticsWeeklyQueryRequest());
        YearMonth currentMonth = YearMonth.now();
        LocalDate monthStart = currentMonth.atDay(1);
        LocalDate monthEnd = currentMonth.atEndOfMonth();

        Long todayPlanCount = defaultZero(statisticsMapper.countPlanByDateRange(userId, today, today));
        Long todayCompletedPlanCount = defaultZero(statisticsMapper.countCompletedPlanByDateRange(userId, today, today));
        Long weekPlanCount = defaultZero(statisticsMapper.countPlanByDateRange(userId, weekRange.startDate(), weekRange.endDate()));
        Long weekCompletedPlanCount = defaultZero(statisticsMapper.countCompletedPlanByDateRange(userId, weekRange.startDate(), weekRange.endDate()));
        Long monthLifeRecordDays = defaultZero(statisticsMapper.countLifeRecordDaysByDateRange(userId, monthStart, monthEnd));
        Long monthReviewDays = defaultZero(statisticsMapper.countReviewDaysByDateRange(userId, monthStart, monthEnd));

        return StatisticsOverviewVo.builder()
                .todayPlanCount(todayPlanCount)
                .todayCompletedPlanCount(todayCompletedPlanCount)
                .todayCompletionRate(calculateRate(todayCompletedPlanCount, todayPlanCount))
                .thisWeekPlanCount(weekPlanCount)
                .thisWeekCompletedPlanCount(weekCompletedPlanCount)
                .thisWeekCompletionRate(calculateRate(weekCompletedPlanCount, weekPlanCount))
                .thisMonthLifeRecordDays(monthLifeRecordDays)
                .thisMonthReviewDays(monthReviewDays)
                .build();
    }

    @Override
    public StatisticsWeeklyVo getWeeklyStatistics(Long userId, StatisticsWeeklyQueryRequest queryRequest) {
        DateRange range = resolveWeekRange(queryRequest);
        Long planCount = defaultZero(statisticsMapper.countPlanByDateRange(userId, range.startDate(), range.endDate()));
        Long completedPlanCount = defaultZero(statisticsMapper.countCompletedPlanByDateRange(userId, range.startDate(), range.endDate()));
        Long lifeRecordDays = defaultZero(statisticsMapper.countLifeRecordDaysByDateRange(userId, range.startDate(), range.endDate()));
        Long reviewDays = defaultZero(statisticsMapper.countReviewDaysByDateRange(userId, range.startDate(), range.endDate()));

        List<StatisticsTrendPointVo> trend = fillTrend(
                range.startDate(),
                range.endDate(),
                statisticsMapper.selectPlanTrendByDateRange(userId, range.startDate(), range.endDate())
        );

        List<StatisticsStatusDistributionVo> distribution = fillStatusDistribution(
                statisticsMapper.selectPlanStatusDistributionByDateRange(userId, range.startDate(), range.endDate())
        );

        return StatisticsWeeklyVo.builder()
                .planCount(planCount)
                .completedPlanCount(completedPlanCount)
                .completionRate(calculateRate(completedPlanCount, planCount))
                .lifeRecordDays(lifeRecordDays)
                .reviewDays(reviewDays)
                .dailyTrend(trend)
                .statusDistribution(distribution)
                .build();
    }

    @Override
    public StatisticsMonthlyVo getMonthlyStatistics(Long userId, StatisticsMonthlyQueryRequest queryRequest) {
        YearMonth month = resolveMonth(queryRequest);
        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();

        Long lifeRecordDays = defaultZero(statisticsMapper.countLifeRecordDaysByDateRange(userId, startDate, endDate));
        Long reviewDays = defaultZero(statisticsMapper.countReviewDaysByDateRange(userId, startDate, endDate));
        List<StatisticsTrendPointVo> trend = fillTrend(
                startDate,
                endDate,
                statisticsMapper.selectCompletedPlanTrendByDateRange(userId, startDate, endDate)
        );

        return StatisticsMonthlyVo.builder()
                .lifeRecordDays(lifeRecordDays)
                .reviewDays(reviewDays)
                .planCompletionTrend(trend)
                .build();
    }

    private DateRange resolveWeekRange(StatisticsWeeklyQueryRequest queryRequest) {
        LocalDate today = LocalDate.now();
        LocalDate defaultStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate defaultEnd = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        LocalDate startDate = parseDateOrDefault(queryRequest.getWeekStartDate(), defaultStart);
        LocalDate endDate = parseDateOrDefault(queryRequest.getWeekEndDate(), defaultEnd);

        if (queryRequest.getWeekStartDate() != null && !queryRequest.getWeekStartDate().isBlank()
                && (queryRequest.getWeekEndDate() == null || queryRequest.getWeekEndDate().isBlank())) {
            endDate = startDate.plusDays(6);
        }
        if (queryRequest.getWeekEndDate() != null && !queryRequest.getWeekEndDate().isBlank()
                && (queryRequest.getWeekStartDate() == null || queryRequest.getWeekStartDate().isBlank())) {
            startDate = endDate.minusDays(6);
        }
        if (startDate.isAfter(endDate)) {
            throw new BusinessException(ResultCode.STATISTICS_DATE_RANGE_INVALID);
        }
        return new DateRange(startDate, endDate);
    }

    private YearMonth resolveMonth(StatisticsMonthlyQueryRequest queryRequest) {
        if (queryRequest.getMonth() == null || queryRequest.getMonth().isBlank()) {
            return YearMonth.now();
        }
        try {
            return YearMonth.parse(queryRequest.getMonth());
        } catch (DateTimeParseException ex) {
            throw new BusinessException(ResultCode.STATISTICS_DATE_RANGE_INVALID);
        }
    }

    private LocalDate parseDateOrDefault(String value, LocalDate defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException ex) {
            throw new BusinessException(ResultCode.STATISTICS_DATE_RANGE_INVALID);
        }
    }

    private BigDecimal calculateRate(Long numerator, Long denominator) {
        if (denominator == null || denominator == 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.valueOf(numerator == null ? 0 : numerator)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(denominator), 2, RoundingMode.HALF_UP);
    }

    private Long defaultZero(Long value) {
        return value == null ? 0L : value;
    }

    private List<StatisticsTrendPointVo> fillTrend(LocalDate startDate,
                                                   LocalDate endDate,
                                                   List<StatisticsTrendPointVo> rawList) {
        Map<LocalDate, StatisticsTrendPointVo> rawMap = new LinkedHashMap<>();
        for (StatisticsTrendPointVo item : rawList) {
            rawMap.put(item.getDate(), item);
        }

        List<StatisticsTrendPointVo> result = new ArrayList<>();
        LocalDate cursor = startDate;
        while (!cursor.isAfter(endDate)) {
            StatisticsTrendPointVo raw = rawMap.get(cursor);
            StatisticsTrendPointVo point = new StatisticsTrendPointVo();
            point.setDate(cursor);
            point.setPlanCount(raw == null || raw.getPlanCount() == null ? 0L : raw.getPlanCount());
            point.setCompletedPlanCount(raw == null || raw.getCompletedPlanCount() == null ? 0L : raw.getCompletedPlanCount());
            result.add(point);
            cursor = cursor.plusDays(1);
        }
        return result;
    }

    private List<StatisticsStatusDistributionVo> fillStatusDistribution(List<StatisticsStatusDistributionVo> rawList) {
        Map<Integer, Long> rawMap = new LinkedHashMap<>();
        for (StatisticsStatusDistributionVo item : rawList) {
            rawMap.put(item.getPlanStatus(), item.getCount());
        }

        List<StatisticsStatusDistributionVo> result = new ArrayList<>();
        for (int status = 0; status <= 3; status++) {
            StatisticsStatusDistributionVo item = new StatisticsStatusDistributionVo();
            item.setPlanStatus(status);
            item.setCount(rawMap.getOrDefault(status, 0L));
            result.add(item);
        }
        return result;
    }

    private record DateRange(LocalDate startDate, LocalDate endDate) {
    }
}
