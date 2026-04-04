package com.sakura.studylife.vo.common;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "分页响应")
public class PageResponse<T> {

    private Integer pageNum;

    private Integer pageSize;

    private Long total;

    private Long pages;

    private List<T> list;
}
