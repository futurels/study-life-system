package com.sakura.studylife.service;

import com.sakura.studylife.dto.studyplan.StudyPlanCreateRequest;
import com.sakura.studylife.dto.studyplan.StudyPlanQueryRequest;
import com.sakura.studylife.dto.studyplan.StudyPlanStatusUpdateRequest;
import com.sakura.studylife.dto.studyplan.StudyPlanUpdateRequest;
import com.sakura.studylife.vo.common.PageResponse;
import com.sakura.studylife.vo.studyplan.StudyPlanIdVo;
import com.sakura.studylife.vo.studyplan.StudyPlanListItemVo;
import com.sakura.studylife.vo.studyplan.StudyPlanVo;

public interface StudyPlanService {

    StudyPlanIdVo create(Long userId, StudyPlanCreateRequest request);

    PageResponse<StudyPlanListItemVo> queryPage(Long userId, StudyPlanQueryRequest queryRequest);

    StudyPlanVo getDetail(Long userId, Long id);

    void update(Long userId, Long id, StudyPlanUpdateRequest request);

    void delete(Long userId, Long id);

    void updateStatus(Long userId, Long id, StudyPlanStatusUpdateRequest request);
}
