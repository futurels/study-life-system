package com.sakura.studylife.service.impl;

import com.sakura.studylife.common.enums.ResultCode;
import com.sakura.studylife.dto.dailyreview.DailyReviewCreateRequest;
import com.sakura.studylife.dto.dailyreview.DailyReviewQueryRequest;
import com.sakura.studylife.dto.dailyreview.DailyReviewUpdateRequest;
import com.sakura.studylife.entity.DailyReview;
import com.sakura.studylife.exception.BusinessException;
import com.sakura.studylife.mapper.DailyReviewMapper;
import com.sakura.studylife.service.DailyReviewService;
import com.sakura.studylife.vo.common.PageResponse;
import com.sakura.studylife.vo.dailyreview.DailyReviewIdVo;
import com.sakura.studylife.vo.dailyreview.DailyReviewListItemVo;
import com.sakura.studylife.vo.dailyreview.DailyReviewVo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DailyReviewServiceImpl implements DailyReviewService {

    private final DailyReviewMapper dailyReviewMapper;

    public DailyReviewServiceImpl(DailyReviewMapper dailyReviewMapper) {
        this.dailyReviewMapper = dailyReviewMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DailyReviewIdVo create(Long userId, DailyReviewCreateRequest request) {
        ensureUniqueReviewDate(userId, request.getReviewDate(), null);
        LocalDateTime now = LocalDateTime.now();
        DailyReview dailyReview = buildDailyReview(userId, request, now);
        DailyReview deletedReview = dailyReviewMapper.selectDeletedByUserIdAndReviewDate(userId, request.getReviewDate());

        if (deletedReview != null) {
            dailyReview.setId(deletedReview.getId());
            dailyReviewMapper.restoreDeletedByIdAndUserId(dailyReview, userId);
        } else {
            dailyReviewMapper.insert(dailyReview);
        }
        return new DailyReviewIdVo(dailyReview.getId());
    }

    @Override
    public PageResponse<DailyReviewListItemVo> queryPage(Long userId, DailyReviewQueryRequest queryRequest) {
        normalizePage(queryRequest);
        validateQuery(queryRequest);

        int offset = (queryRequest.getPageNum() - 1) * queryRequest.getPageSize();
        Long total = dailyReviewMapper.countByQuery(userId, queryRequest);
        List<DailyReviewListItemVo> list = dailyReviewMapper
                .selectPageByQuery(userId, queryRequest, offset, queryRequest.getPageSize())
                .stream()
                .map(this::toListItemVo)
                .toList();

        long pages = total == 0 ? 0 : (total + queryRequest.getPageSize() - 1) / queryRequest.getPageSize();
        return PageResponse.<DailyReviewListItemVo>builder()
                .pageNum(queryRequest.getPageNum())
                .pageSize(queryRequest.getPageSize())
                .total(total)
                .pages(pages)
                .list(list)
                .build();
    }

    @Override
    public DailyReviewVo getDetail(Long userId, Long id) {
        DailyReview dailyReview = getOwnedDailyReview(userId, id);
        return toDetailVo(dailyReview);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long userId, Long id, DailyReviewUpdateRequest request) {
        DailyReview current = getOwnedDailyReview(userId, id);
        ensureUniqueReviewDate(userId, request.getReviewDate(), id);

        current.setReviewDate(request.getReviewDate());
        current.setCompletedItems(request.getCompletedItems());
        current.setUnfinishedItems(request.getUnfinishedItems());
        current.setUnfinishedReason(request.getUnfinishedReason());
        current.setOverallEvaluation(request.getOverallEvaluation());
        current.setTomorrowPlan(request.getTomorrowPlan());
        current.setReviewScore(request.getReviewScore());
        current.setUpdatedAt(LocalDateTime.now());

        dailyReviewMapper.updateByIdAndUserId(current, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long userId, Long id) {
        getOwnedDailyReview(userId, id);
        dailyReviewMapper.logicalDeleteByIdAndUserId(id, userId, LocalDateTime.now());
    }

    private void validateQuery(DailyReviewQueryRequest queryRequest) {
        if (queryRequest.getStartDate() != null && queryRequest.getEndDate() != null
                && queryRequest.getStartDate().isAfter(queryRequest.getEndDate())) {
            throw new BusinessException(ResultCode.DAILY_REVIEW_DATE_RANGE_INVALID);
        }
    }

    private void normalizePage(DailyReviewQueryRequest queryRequest) {
        if (queryRequest.getPageNum() == null || queryRequest.getPageNum() < 1) {
            queryRequest.setPageNum(1);
        }
        if (queryRequest.getPageSize() == null || queryRequest.getPageSize() < 1) {
            queryRequest.setPageSize(10);
        }
        if (queryRequest.getPageSize() > 100) {
            queryRequest.setPageSize(100);
        }
    }

    private void ensureUniqueReviewDate(Long userId, LocalDate reviewDate, Long currentId) {
        Long existId = dailyReviewMapper.selectIdByUserIdAndReviewDate(userId, reviewDate);
        if (existId != null && !existId.equals(currentId)) {
                throw new BusinessException(ResultCode.DAILY_REVIEW_DATE_EXISTS);
        }
    }

    private DailyReview buildDailyReview(Long userId, DailyReviewCreateRequest request, LocalDateTime now) {
        DailyReview dailyReview = new DailyReview();
        dailyReview.setUserId(userId);
        dailyReview.setReviewDate(request.getReviewDate());
        dailyReview.setCompletedItems(request.getCompletedItems());
        dailyReview.setUnfinishedItems(request.getUnfinishedItems());
        dailyReview.setUnfinishedReason(request.getUnfinishedReason());
        dailyReview.setOverallEvaluation(request.getOverallEvaluation());
        dailyReview.setTomorrowPlan(request.getTomorrowPlan());
        dailyReview.setReviewScore(request.getReviewScore());
        dailyReview.setCreatedAt(now);
        dailyReview.setUpdatedAt(now);
        dailyReview.setDeletedFlag(0);
        return dailyReview;
    }

    private DailyReview getOwnedDailyReview(Long userId, Long id) {
        Long ownerUserId = dailyReviewMapper.selectUserIdById(id);
        if (ownerUserId == null) {
            throw new BusinessException(ResultCode.DAILY_REVIEW_NOT_FOUND);
        }
        if (!ownerUserId.equals(userId)) {
            throw new BusinessException(ResultCode.DAILY_REVIEW_NOT_BELONG_TO_USER);
        }

        DailyReview dailyReview = dailyReviewMapper.selectByIdAndUserId(id, userId);
        if (dailyReview == null) {
            throw new BusinessException(ResultCode.DAILY_REVIEW_NOT_FOUND);
        }
        return dailyReview;
    }

    private DailyReviewListItemVo toListItemVo(DailyReview dailyReview) {
        return DailyReviewListItemVo.builder()
                .id(dailyReview.getId())
                .reviewDate(dailyReview.getReviewDate())
                .completedItems(dailyReview.getCompletedItems())
                .unfinishedItems(dailyReview.getUnfinishedItems())
                .unfinishedReason(dailyReview.getUnfinishedReason())
                .overallEvaluation(dailyReview.getOverallEvaluation())
                .tomorrowPlan(dailyReview.getTomorrowPlan())
                .reviewScore(dailyReview.getReviewScore())
                .createdAt(dailyReview.getCreatedAt())
                .build();
    }

    private DailyReviewVo toDetailVo(DailyReview dailyReview) {
        return DailyReviewVo.builder()
                .id(dailyReview.getId())
                .reviewDate(dailyReview.getReviewDate())
                .completedItems(dailyReview.getCompletedItems())
                .unfinishedItems(dailyReview.getUnfinishedItems())
                .unfinishedReason(dailyReview.getUnfinishedReason())
                .overallEvaluation(dailyReview.getOverallEvaluation())
                .tomorrowPlan(dailyReview.getTomorrowPlan())
                .reviewScore(dailyReview.getReviewScore())
                .createdAt(dailyReview.getCreatedAt())
                .updatedAt(dailyReview.getUpdatedAt())
                .build();
    }
}
