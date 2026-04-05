package com.sakura.studylife.mapper;

import com.sakura.studylife.dto.dailyreview.DailyReviewQueryRequest;
import com.sakura.studylife.entity.DailyReview;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DailyReviewMapper {

    int insert(DailyReview dailyReview);

    Long selectUserIdById(@Param("id") Long id);

    DailyReview selectByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    Long countByQuery(@Param("userId") Long userId, @Param("query") DailyReviewQueryRequest query);

    List<DailyReview> selectPageByQuery(@Param("userId") Long userId,
                                        @Param("query") DailyReviewQueryRequest query,
                                        @Param("offset") int offset,
                                        @Param("pageSize") int pageSize);

    Long selectIdByUserIdAndReviewDate(@Param("userId") Long userId, @Param("reviewDate") LocalDate reviewDate);

    DailyReview selectDeletedByUserIdAndReviewDate(@Param("userId") Long userId,
                                                   @Param("reviewDate") LocalDate reviewDate);

    int updateByIdAndUserId(@Param("dailyReview") DailyReview dailyReview, @Param("userId") Long userId);

    int restoreDeletedByIdAndUserId(@Param("dailyReview") DailyReview dailyReview, @Param("userId") Long userId);

    int logicalDeleteByIdAndUserId(@Param("id") Long id,
                                   @Param("userId") Long userId,
                                   @Param("updatedAt") LocalDateTime updatedAt);
}
