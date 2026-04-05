package com.sakura.studylife.service.impl;

import com.sakura.studylife.common.enums.ResultCode;
import com.sakura.studylife.dto.liferecord.LifeRecordCreateRequest;
import com.sakura.studylife.dto.liferecord.LifeRecordQueryRequest;
import com.sakura.studylife.dto.liferecord.LifeRecordUpdateRequest;
import com.sakura.studylife.entity.LifeRecord;
import com.sakura.studylife.exception.BusinessException;
import com.sakura.studylife.mapper.LifeRecordMapper;
import com.sakura.studylife.service.LifeRecordService;
import com.sakura.studylife.vo.common.PageResponse;
import com.sakura.studylife.vo.liferecord.LifeRecordIdVo;
import com.sakura.studylife.vo.liferecord.LifeRecordListItemVo;
import com.sakura.studylife.vo.liferecord.LifeRecordVo;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LifeRecordServiceImpl implements LifeRecordService {

    private final LifeRecordMapper lifeRecordMapper;

    public LifeRecordServiceImpl(LifeRecordMapper lifeRecordMapper) {
        this.lifeRecordMapper = lifeRecordMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LifeRecordIdVo create(Long userId, LifeRecordCreateRequest request) {
        ensureUniqueRecordDate(userId, request.getRecordDate(), null);
        LocalDateTime now = LocalDateTime.now();
        LifeRecord lifeRecord = buildLifeRecord(userId, request, now);
        LifeRecord deletedRecord = lifeRecordMapper.selectDeletedByUserIdAndRecordDate(userId, request.getRecordDate());

        if (deletedRecord != null) {
            lifeRecord.setId(deletedRecord.getId());
            lifeRecordMapper.restoreDeletedByIdAndUserId(lifeRecord, userId);
        } else {
            lifeRecordMapper.insert(lifeRecord);
        }
        return new LifeRecordIdVo(lifeRecord.getId());
    }

    @Override
    public PageResponse<LifeRecordListItemVo> queryPage(Long userId, LifeRecordQueryRequest queryRequest) {
        normalizePage(queryRequest);
        validateQuery(queryRequest);

        int offset = (queryRequest.getPageNum() - 1) * queryRequest.getPageSize();
        Long total = lifeRecordMapper.countByQuery(userId, queryRequest);
        List<LifeRecordListItemVo> list = lifeRecordMapper
                .selectPageByQuery(userId, queryRequest, offset, queryRequest.getPageSize())
                .stream()
                .map(this::toListItemVo)
                .toList();

        long pages = total == 0 ? 0 : (total + queryRequest.getPageSize() - 1) / queryRequest.getPageSize();
        return PageResponse.<LifeRecordListItemVo>builder()
                .pageNum(queryRequest.getPageNum())
                .pageSize(queryRequest.getPageSize())
                .total(total)
                .pages(pages)
                .list(list)
                .build();
    }

    @Override
    public LifeRecordVo getDetail(Long userId, Long id) {
        LifeRecord lifeRecord = getOwnedLifeRecord(userId, id);
        return toDetailVo(lifeRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long userId, Long id, LifeRecordUpdateRequest request) {
        LifeRecord current = getOwnedLifeRecord(userId, id);
        ensureUniqueRecordDate(userId, request.getRecordDate(), id);

        current.setRecordDate(request.getRecordDate());
        current.setDiaryContent(request.getDiaryContent());
        current.setMoodStatus(request.getMoodStatus());
        current.setSleepHours(request.getSleepHours());
        current.setDietNote(request.getDietNote());
        current.setExerciseNote(request.getExerciseNote());
        current.setUpdatedAt(LocalDateTime.now());

        lifeRecordMapper.updateByIdAndUserId(current, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long userId, Long id) {
        getOwnedLifeRecord(userId, id);
        lifeRecordMapper.logicalDeleteByIdAndUserId(id, userId, LocalDateTime.now());
    }

    private void validateQuery(LifeRecordQueryRequest queryRequest) {
        if (queryRequest.getStartDate() != null && queryRequest.getEndDate() != null
                && queryRequest.getStartDate().isAfter(queryRequest.getEndDate())) {
            throw new BusinessException(ResultCode.LIFE_RECORD_DATE_RANGE_INVALID);
        }
    }

    private void normalizePage(LifeRecordQueryRequest queryRequest) {
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

    private void ensureUniqueRecordDate(Long userId, java.time.LocalDate recordDate, Long currentId) {
        Long existId = lifeRecordMapper.selectIdByUserIdAndRecordDate(userId, recordDate);
        if (existId != null && !existId.equals(currentId)) {
            throw new BusinessException(ResultCode.LIFE_RECORD_DATE_EXISTS);
        }
    }

    private LifeRecord buildLifeRecord(Long userId, LifeRecordCreateRequest request, LocalDateTime now) {
        LifeRecord lifeRecord = new LifeRecord();
        lifeRecord.setUserId(userId);
        lifeRecord.setRecordDate(request.getRecordDate());
        lifeRecord.setDiaryContent(request.getDiaryContent());
        lifeRecord.setMoodStatus(request.getMoodStatus());
        lifeRecord.setSleepHours(request.getSleepHours());
        lifeRecord.setDietNote(request.getDietNote());
        lifeRecord.setExerciseNote(request.getExerciseNote());
        lifeRecord.setCreatedAt(now);
        lifeRecord.setUpdatedAt(now);
        lifeRecord.setDeletedFlag(0);
        return lifeRecord;
    }

    private LifeRecord getOwnedLifeRecord(Long userId, Long id) {
        Long ownerUserId = lifeRecordMapper.selectUserIdById(id);
        if (ownerUserId == null) {
            throw new BusinessException(ResultCode.LIFE_RECORD_NOT_FOUND);
        }
        if (!ownerUserId.equals(userId)) {
            throw new BusinessException(ResultCode.LIFE_RECORD_NOT_BELONG_TO_USER);
        }

        LifeRecord lifeRecord = lifeRecordMapper.selectByIdAndUserId(id, userId);
        if (lifeRecord == null) {
            throw new BusinessException(ResultCode.LIFE_RECORD_NOT_FOUND);
        }
        return lifeRecord;
    }

    private LifeRecordListItemVo toListItemVo(LifeRecord lifeRecord) {
        return LifeRecordListItemVo.builder()
                .id(lifeRecord.getId())
                .recordDate(lifeRecord.getRecordDate())
                .diaryContent(lifeRecord.getDiaryContent())
                .moodStatus(lifeRecord.getMoodStatus())
                .sleepHours(lifeRecord.getSleepHours())
                .dietNote(lifeRecord.getDietNote())
                .exerciseNote(lifeRecord.getExerciseNote())
                .createdAt(lifeRecord.getCreatedAt())
                .build();
    }

    private LifeRecordVo toDetailVo(LifeRecord lifeRecord) {
        return LifeRecordVo.builder()
                .id(lifeRecord.getId())
                .recordDate(lifeRecord.getRecordDate())
                .diaryContent(lifeRecord.getDiaryContent())
                .moodStatus(lifeRecord.getMoodStatus())
                .sleepHours(lifeRecord.getSleepHours())
                .dietNote(lifeRecord.getDietNote())
                .exerciseNote(lifeRecord.getExerciseNote())
                .createdAt(lifeRecord.getCreatedAt())
                .updatedAt(lifeRecord.getUpdatedAt())
                .build();
    }
}
