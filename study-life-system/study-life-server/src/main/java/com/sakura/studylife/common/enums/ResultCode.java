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
    STUDY_PLAN_TIME_RANGE_INVALID(11003, "学习计划时间范围非法"),
    LIFE_RECORD_NOT_FOUND(12001, "生活记录不存在"),
    LIFE_RECORD_NOT_BELONG_TO_USER(12002, "生活记录不属于当前用户"),
    LIFE_RECORD_DATE_EXISTS(12003, "当前日期的生活记录已存在"),
    LIFE_RECORD_DATE_RANGE_INVALID(12004, "生活记录日期范围非法"),
    DAILY_REVIEW_NOT_FOUND(13001, "每日复盘不存在"),
    DAILY_REVIEW_NOT_BELONG_TO_USER(13002, "每日复盘不属于当前用户"),
    DAILY_REVIEW_DATE_EXISTS(13003, "当前日期的每日复盘已存在"),
    DAILY_REVIEW_DATE_RANGE_INVALID(13004, "每日复盘日期范围非法"),
    STATISTICS_DATE_RANGE_INVALID(14001, "统计日期范围非法");

    private final int code;

    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
