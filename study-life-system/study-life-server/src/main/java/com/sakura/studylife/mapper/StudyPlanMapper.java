package com.sakura.studylife.mapper;

import com.sakura.studylife.dto.studyplan.StudyPlanQueryRequest;
import com.sakura.studylife.entity.StudyPlan;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StudyPlanMapper {

    int insert(StudyPlan studyPlan);

    Long selectUserIdById(@Param("id") Long id);

    StudyPlan selectByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    Long countByQuery(@Param("userId") Long userId, @Param("query") StudyPlanQueryRequest query);

    List<StudyPlan> selectPageByQuery(@Param("userId") Long userId,
                                      @Param("query") StudyPlanQueryRequest query,
                                      @Param("offset") int offset,
                                      @Param("pageSize") int pageSize);

    int updateByIdAndUserId(@Param("studyPlan") StudyPlan studyPlan, @Param("userId") Long userId);

    int logicalDeleteByIdAndUserId(@Param("id") Long id,
                                   @Param("userId") Long userId,
                                   @Param("updatedAt") LocalDateTime updatedAt);

    int updateStatusByIdAndUserId(@Param("id") Long id,
                                  @Param("userId") Long userId,
                                  @Param("planStatus") Integer planStatus,
                                  @Param("completionTime") LocalDateTime completionTime,
                                  @Param("updatedAt") LocalDateTime updatedAt);
}
