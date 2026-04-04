package com.sakura.studylife.vo.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "登录用户信息")
public class LoginUserInfoVo {

    private Long id;

    private String username;

    private String nickname;
}
