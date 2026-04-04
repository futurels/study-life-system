package com.sakura.studylife.common.enums;

import lombok.Getter;

@Getter
public enum ResultCode {

    SUCCESS(200, "success"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或令牌无效"),
    FORBIDDEN(403, "无权访问"),
    NOT_FOUND(404, "数据不存在"),
    CONFLICT(409, "数据冲突"),
    BUSINESS_ERROR(422, "业务校验失败"),
    INTERNAL_ERROR(500, "系统内部异常"),
    USERNAME_EXISTS(10001, "用户名已存在"),
    USERNAME_OR_PASSWORD_ERROR(10002, "用户名或密码错误"),
    ACCOUNT_DISABLED(10005, "账号已被禁用"),
    STUDY_PLAN_NOT_FOUND(11001, "学习计划不存在"),
    STUDY_PLAN_NOT_BELONG_TO_USER(11002, "学习计划不属于当前用户"),
    STUDY_PLAN_TIME_RANGE_INVALID(11003, "学习计划时间范围非法");

    private final int code;

    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
