package com.sakura.studylife.vo.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "用户注册响应")
public class RegisterResponse {

    private Long userId;

    private String username;
}
