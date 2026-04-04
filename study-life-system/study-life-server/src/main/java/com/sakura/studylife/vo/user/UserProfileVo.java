package com.sakura.studylife.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "当前登录用户个人信息")
public class UserProfileVo {

    private Long id;

    private String username;

    private String nickname;

    private String email;

    private Integer accountStatus;

    private LocalDateTime lastLoginTime;

    private LocalDateTime createdAt;
}
