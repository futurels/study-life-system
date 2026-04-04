package com.sakura.studylife.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "用户登录请求")
public class LoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
