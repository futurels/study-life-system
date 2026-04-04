package com.sakura.studylife.controller;

import com.sakura.studylife.common.result.Result;
import com.sakura.studylife.common.util.CurrentUserContext;
import com.sakura.studylife.service.UserService;
import com.sakura.studylife.vo.user.UserProfileVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户模块")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/profile")
    public Result<UserProfileVo> getProfile() {
        return Result.success("查询成功", userService.getCurrentUserProfile(CurrentUserContext.getUserId()));
    }
}
