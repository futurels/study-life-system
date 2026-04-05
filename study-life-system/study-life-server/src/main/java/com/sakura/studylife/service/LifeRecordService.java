package com.sakura.studylife.service;

import com.sakura.studylife.dto.liferecord.LifeRecordCreateRequest;
import com.sakura.studylife.dto.liferecord.LifeRecordQueryRequest;
import com.sakura.studylife.dto.liferecord.LifeRecordUpdateRequest;
import com.sakura.studylife.vo.common.PageResponse;
import com.sakura.studylife.vo.liferecord.LifeRecordIdVo;
import com.sakura.studylife.vo.liferecord.LifeRecordListItemVo;
import com.sakura.studylife.vo.liferecord.LifeRecordVo;

public interface LifeRecordService {

    LifeRecordIdVo create(Long userId, LifeRecordCreateRequest request);

    PageResponse<LifeRecordListItemVo> queryPage(Long userId, LifeRecordQueryRequest queryRequest);

    LifeRecordVo getDetail(Long userId, Long id);

    void update(Long userId, Long id, LifeRecordUpdateRequest request);

    void delete(Long userId, Long id);
}
