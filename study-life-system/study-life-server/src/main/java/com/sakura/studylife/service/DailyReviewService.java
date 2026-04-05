package com.sakura.studylife.service;

import com.sakura.studylife.dto.dailyreview.DailyReviewCreateRequest;
import com.sakura.studylife.dto.dailyreview.DailyReviewQueryRequest;
import com.sakura.studylife.dto.dailyreview.DailyReviewUpdateRequest;
import com.sakura.studylife.vo.common.PageResponse;
import com.sakura.studylife.vo.dailyreview.DailyReviewIdVo;
import com.sakura.studylife.vo.dailyreview.DailyReviewListItemVo;
import com.sakura.studylife.vo.dailyreview.DailyReviewVo;

public interface DailyReviewService {

    DailyReviewIdVo create(Long userId, DailyReviewCreateRequest request);

    PageResponse<DailyReviewListItemVo> queryPage(Long userId, DailyReviewQueryRequest queryRequest);

    DailyReviewVo getDetail(Long userId, Long id);

    void update(Long userId, Long id, DailyReviewUpdateRequest request);

    void delete(Long userId, Long id);
}
