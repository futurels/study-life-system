package com.sakura.studylife.mapper;

import com.sakura.studylife.dto.liferecord.LifeRecordQueryRequest;
import com.sakura.studylife.entity.LifeRecord;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LifeRecordMapper {

    int insert(LifeRecord lifeRecord);

    Long selectUserIdById(@Param("id") Long id);

    LifeRecord selectByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    Long countByQuery(@Param("userId") Long userId, @Param("query") LifeRecordQueryRequest query);

    List<LifeRecord> selectPageByQuery(@Param("userId") Long userId,
                                       @Param("query") LifeRecordQueryRequest query,
                                       @Param("offset") int offset,
                                       @Param("pageSize") int pageSize);

    Long selectIdByUserIdAndRecordDate(@Param("userId") Long userId, @Param("recordDate") LocalDate recordDate);

    LifeRecord selectDeletedByUserIdAndRecordDate(@Param("userId") Long userId, @Param("recordDate") LocalDate recordDate);

    int updateByIdAndUserId(@Param("lifeRecord") LifeRecord lifeRecord, @Param("userId") Long userId);

    int restoreDeletedByIdAndUserId(@Param("lifeRecord") LifeRecord lifeRecord, @Param("userId") Long userId);

    int logicalDeleteByIdAndUserId(@Param("id") Long id,
                                   @Param("userId") Long userId,
                                   @Param("updatedAt") LocalDateTime updatedAt);
}
