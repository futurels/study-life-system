package com.sakura.studylife.exception;

import com.sakura.studylife.common.enums.ResultCode;
import com.sakura.studylife.common.result.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final Map<String, String> FIELD_LABELS = new LinkedHashMap<>();

    static {
        FIELD_LABELS.put("username", "用户名");
        FIELD_LABELS.put("password", "密码");
        FIELD_LABELS.put("confirmPassword", "确认密码");
        FIELD_LABELS.put("nickname", "昵称");
        FIELD_LABELS.put("email", "邮箱");
        FIELD_LABELS.put("planTitle", "计划标题");
        FIELD_LABELS.put("planContent", "计划内容");
        FIELD_LABELS.put("planDate", "计划日期");
        FIELD_LABELS.put("startTime", "开始时间");
        FIELD_LABELS.put("endTime", "结束时间");
        FIELD_LABELS.put("priorityLevel", "优先级");
        FIELD_LABELS.put("planStatus", "计划状态");
        FIELD_LABELS.put("remark", "备注");
        FIELD_LABELS.put("recordDate", "记录日期");
        FIELD_LABELS.put("diaryContent", "日记正文");
        FIELD_LABELS.put("moodStatus", "心情状态");
        FIELD_LABELS.put("sleepHours", "睡眠时长");
        FIELD_LABELS.put("dietNote", "饮食简述");
        FIELD_LABELS.put("exerciseNote", "运动简述");
        FIELD_LABELS.put("reviewDate", "复盘日期");
        FIELD_LABELS.put("completedItems", "今日完成事项");
        FIELD_LABELS.put("unfinishedItems", "今日未完成事项");
        FIELD_LABELS.put("unfinishedReason", "未完成原因");
        FIELD_LABELS.put("overallEvaluation", "整体评价");
        FIELD_LABELS.put("tomorrowPlan", "明日计划");
        FIELD_LABELS.put("reviewScore", "今日评分");
        FIELD_LABELS.put("pageNum", "页码");
        FIELD_LABELS.put("pageSize", "每页条数");
        FIELD_LABELS.put("startDate", "开始日期");
        FIELD_LABELS.put("endDate", "结束日期");
        FIELD_LABELS.put("id", "记录ID");
    }

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException ex) {
        return Result.failure(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
    public Result<Void> handleValidationException(Exception ex) {
        return Result.failure(ResultCode.BAD_REQUEST.getCode(), resolveValidationMessage(ex));
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class
    })
    public Result<Void> handleRequestFormatException(Exception ex) {
        return Result.failure(ResultCode.BAD_REQUEST.getCode(), resolveRequestFormatMessage(ex));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result<Void> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.warn("数据库约束异常", ex);
        return Result.failure(ResultCode.CONFLICT.getCode(), "数据已存在或不符合约束，请检查后重试");
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception ex) {
        String errorId = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        log.error("未处理系统异常, errorId={}", errorId, ex);
        return Result.failure(ResultCode.INTERNAL_ERROR.getCode(),
                "系统内部异常，请稍后再试。如需排查请提供错误编号：" + errorId);
    }

    private String resolveValidationMessage(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            FieldError fieldError = methodArgumentNotValidException.getBindingResult().getFieldError();
            if (fieldError != null) {
                return buildFieldMessage(fieldError.getField(), fieldError.getDefaultMessage());
            }
        }
        if (ex instanceof BindException bindException) {
            FieldError fieldError = bindException.getBindingResult().getFieldError();
            if (fieldError != null) {
                return buildFieldMessage(fieldError.getField(), fieldError.getDefaultMessage());
            }
        }
        if (ex instanceof ConstraintViolationException constraintViolationException
                && !constraintViolationException.getConstraintViolations().isEmpty()) {
            return constraintViolationException.getConstraintViolations().stream()
                    .map(this::buildConstraintViolationMessage)
                    .collect(Collectors.joining("; "));
        }
        return ResultCode.BAD_REQUEST.getMessage();
    }

    private String resolveRequestFormatMessage(Exception ex) {
        if (ex instanceof MissingServletRequestParameterException missingServletRequestParameterException) {
            return mapFieldLabel(missingServletRequestParameterException.getParameterName()) + "不能为空";
        }
        if (ex instanceof MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {
            return mapFieldLabel(methodArgumentTypeMismatchException.getName()) + "格式不正确";
        }
        return "请求体格式错误，请检查 JSON、日期或时间格式是否正确";
    }

    private String buildConstraintViolationMessage(ConstraintViolation<?> violation) {
        String propertyPath = violation.getPropertyPath().toString();
        String fieldName = propertyPath.contains(".")
                ? propertyPath.substring(propertyPath.lastIndexOf('.') + 1)
                : propertyPath;
        return buildFieldMessage(fieldName, violation.getMessage());
    }

    private String buildFieldMessage(String fieldName, String rawMessage) {
        return mapFieldLabel(fieldName) + normalizeValidationMessage(rawMessage);
    }

    private String mapFieldLabel(String fieldName) {
        return FIELD_LABELS.getOrDefault(fieldName, fieldName);
    }

    private String normalizeValidationMessage(String rawMessage) {
        if (rawMessage == null || rawMessage.isBlank()) {
            return "参数不合法";
        }
        if (rawMessage.contains("must not be blank") || rawMessage.contains("must not be empty")
                || rawMessage.contains("must not be null")) {
            return "不能为空";
        }
        if (rawMessage.contains("must be greater than or equal to")) {
            return "不能小于" + rawMessage.replace("must be greater than or equal to", "").trim();
        }
        if (rawMessage.contains("must be less than or equal to")) {
            return "不能大于" + rawMessage.replace("must be less than or equal to", "").trim();
        }
        if (rawMessage.contains("must be a well-formed email address")) {
            return "格式不正确";
        }
        if (rawMessage.contains("must match")) {
            return "格式不正确";
        }
        if (rawMessage.contains("size must be between")) {
            String range = rawMessage.replace("size must be between", "").trim().replace(" and ", "到");
            return "长度必须在" + range + "之间";
        }
        return rawMessage;
    }
}
