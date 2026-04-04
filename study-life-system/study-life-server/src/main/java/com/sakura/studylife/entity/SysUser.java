package com.sakura.studylife.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SysUser {

    private Long id;

    private String username;

    private String passwordHash;

    private String nickname;

    private String email;

    private Integer accountStatus;

    private LocalDateTime lastLoginTime;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer deletedFlag;
}
