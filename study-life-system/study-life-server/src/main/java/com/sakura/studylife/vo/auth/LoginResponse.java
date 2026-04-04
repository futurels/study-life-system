package com.sakura.studylife.vo.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "用户登录响应")
public class LoginResponse {

    private String accessToken;

    private String tokenType;

    private Long expiresIn;

    private LoginUserInfoVo userInfo;
}
