package com.sakura.studylife.service.impl;

import com.sakura.studylife.common.enums.ResultCode;
import com.sakura.studylife.dto.studyplan.StudyPlanCreateRequest;
import com.sakura.studylife.dto.studyplan.StudyPlanQueryRequest;
import com.sakura.studylife.dto.studyplan.StudyPlanStatusUpdateRequest;
import com.sakura.studylife.dto.studyplan.StudyPlanUpdateRequest;
import com.sakura.studylife.entity.StudyPlan;
import com.sakura.studylife.exception.BusinessException;
import com.sakura.studylife.mapper.StudyPlanMapper;
import com.sakura.studylife.service.StudyPlanService;
import com.sakura.studylife.vo.common.PageResponse;
import com.sakura.studylife.vo.studyplan.StudyPlanIdVo;
import com.sakura.studylife.vo.studyplan.StudyPlanListItemVo;
import com.sakura.studylife.vo.studyplan.StudyPlanVo;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudyPlanServiceImpl implements StudyPlanService {

    private final StudyPlanMapper studyPlanMapper;

    public StudyPlanServiceImpl(StudyPlanMapper studyPlanMapper) {
        this.studyPlanMapper = studyPlanMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudyPlanIdVo create(Long userId, StudyPlanCreateRequest request) {
        validateTimeRange(request.getStartTime(), request.getEndTime());
        LocalDateTime now = LocalDateTime.now();

        StudyPlan studyPlan = new StudyPlan();
        studyPlan.setUserId(userId);
        studyPlan.setPlanTitle(request.getPlanTitle());
        studyPlan.setPlanContent(request.getPlanContent());
        studyPlan.setPlanDate(request.getPlanDate());
        studyPlan.setStartTime(request.getStartTime());
        studyPlan.setEndTime(request.getEndTime());
        studyPlan.setPriorityLevel(request.getPriorityLevel() == null ? 2 : request.getPriorityLevel());
        studyPlan.setPlanStatus(0);
        studyPlan.setCompletionTime(null);
        studyPlan.setRemark(request.getRemark());
        studyPlan.setCreatedAt(now);
        studyPlan.setUpdatedAt(now);
        studyPlan.setDeletedFlag(0);

        studyPlanMapper.insert(studyPlan);
        return new StudyPlanIdVo(studyPlan.getId());
    }

    @Override
    public PageResponse<StudyPlanListItemVo> queryPage(Long userId, StudyPlanQueryRequest queryRequest) {
        normalizePage(queryRequest);
        validateQuery(queryRequest);

        int offset = (queryRequest.getPageNum() - 1) * queryRequest.getPageSize();
        Long total = studyPlanMapper.countByQuery(userId, queryRequest);
        List<StudyPlanListItemVo> list = studyPlanMapper.selectPageByQuery(userId, queryRequest, offset, queryRequest.getPageSize())
                .stream()
                .map(this::toListItemVo)
                .toList();

        long pages = total == 0 ? 0 : (total + queryRequest.getPageSize() - 1) / queryRequest.getPageSize();
        return PageResponse.<StudyPlanListItemVo>builder()
                .pageNum(queryRequest.getPageNum())
                .pageSize(queryRequest.getPageSize())
                .total(total)
                .pages(pages)
                .list(list)
                .build();
    }

    @Override
    public StudyPlanVo getDetail(Long userId, Long id) {
        StudyPlan studyPlan = getOwnedStudyPlan(userId, id);
        return toDetailVo(studyPlan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long userId, Long id, StudyPlanUpdateRequest request) {
        validateTimeRange(request.getStartTime(), request.getEndTime());
        StudyPlan current = getOwnedStudyPlan(userId, id);

        current.setPlanTitle(request.getPlanTitle());
        current.setPlanContent(request.getPlanContent());
        current.setPlanDate(request.getPlanDate());
        current.setStartTime(request.getStartTime());
        current.setEndTime(request.getEndTime());
        current.setPriorityLevel(request.getPriorityLevel() == null ? 2 : request.getPriorityLevel());
        current.setRemark(request.getRemark());
        current.setUpdatedAt(LocalDateTime.now());

        studyPlanMapper.updateByIdAndUserId(current, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long userId, Long id) {
        getOwnedStudyPlan(userId, id);
        studyPlanMapper.logicalDeleteByIdAndUserId(id, userId, LocalDateTime.now());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long userId, Long id, StudyPlanStatusUpdateRequest request) {
        getOwnedStudyPlan(userId, id);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime completionTime = request.getPlanStatus() == 2 ? now : null;
        studyPlanMapper.updateStatusByIdAndUserId(id, userId, request.getPlanStatus(), completionTime, now);
    }

    private void validateQuery(StudyPlanQueryRequest queryRequest) {
        if (queryRequest.getStartDate() != null && queryRequest.getEndDate() != null
                && queryRequest.getStartDate().isAfter(queryRequest.getEndDate())) {
            throw new BusinessException(ResultCode.STUDY_PLAN_TIME_RANGE_INVALID);
        }
        if (queryRequest.getPlanStatus() != null && (queryRequest.getPlanStatus() < 0 || queryRequest.getPlanStatus() > 3)) {
            throw new BusinessException(ResultCode.BAD_REQUEST);
        }
        if (queryRequest.getPriorityLevel() != null && (queryRequest.getPriorityLevel() < 1 || queryRequest.getPriorityLevel() > 3)) {
            throw new BusinessException(ResultCode.BAD_REQUEST);
        }
    }

    private void normalizePage(StudyPlanQueryRequest queryRequest) {
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

    private void validateTimeRange(java.time.LocalTime startTime, java.time.LocalTime endTime) {
        if (startTime != null && endTime != null && endTime.isBefore(startTime)) {
            throw new BusinessException(ResultCode.STUDY_PLAN_TIME_RANGE_INVALID);
        }
    }

    private StudyPlan getOwnedStudyPlan(Long userId, Long id) {
        Long ownerUserId = studyPlanMapper.selectUserIdById(id);
        if (ownerUserId == null) {
            throw new BusinessException(ResultCode.STUDY_PLAN_NOT_FOUND);
        }
        if (!ownerUserId.equals(userId)) {
            throw new BusinessException(ResultCode.STUDY_PLAN_NOT_BELONG_TO_USER);
        }

        StudyPlan studyPlan = studyPlanMapper.selectByIdAndUserId(id, userId);
        if (studyPlan == null) {
            throw new BusinessException(ResultCode.STUDY_PLAN_NOT_FOUND);
        }
        return studyPlan;
    }

    private StudyPlanListItemVo toListItemVo(StudyPlan studyPlan) {
        return StudyPlanListItemVo.builder()
                .id(studyPlan.getId())
                .planTitle(studyPlan.getPlanTitle())
                .planContent(studyPlan.getPlanContent())
                .planDate(studyPlan.getPlanDate())
                .startTime(studyPlan.getStartTime())
                .endTime(studyPlan.getEndTime())
                .priorityLevel(studyPlan.getPriorityLevel())
                .planStatus(studyPlan.getPlanStatus())
                .completionTime(studyPlan.getCompletionTime())
                .remark(studyPlan.getRemark())
                .createdAt(studyPlan.getCreatedAt())
                .build();
    }

    private StudyPlanVo toDetailVo(StudyPlan studyPlan) {
        return StudyPlanVo.builder()
                .id(studyPlan.getId())
                .planTitle(studyPlan.getPlanTitle())
                .planContent(studyPlan.getPlanContent())
                .planDate(studyPlan.getPlanDate())
                .startTime(studyPlan.getStartTime())
                .endTime(studyPlan.getEndTime())
                .priorityLevel(studyPlan.getPriorityLevel())
                .planStatus(studyPlan.getPlanStatus())
                .completionTime(studyPlan.getCompletionTime())
                .remark(studyPlan.getRemark())
                .createdAt(studyPlan.getCreatedAt())
                .updatedAt(studyPlan.getUpdatedAt())
                .build();
    }
}
