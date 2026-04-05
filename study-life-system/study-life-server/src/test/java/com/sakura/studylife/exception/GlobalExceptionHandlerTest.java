package com.sakura.studylife.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sakura.studylife.common.enums.ResultCode;
import com.sakura.studylife.common.result.Result;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleValidationExceptionShouldReturnFriendlyFieldMessage() {
        RegisterForm form = new RegisterForm();
        DataBinder dataBinder = new DataBinder(form, "registerForm");
        dataBinder.getBindingResult().addError(new FieldError("registerForm", "password", "",
                false, null, null, "must not be blank"));

        BindException bindException = new BindException(dataBinder.getBindingResult());

        Result<Void> result = globalExceptionHandler.handleValidationException(bindException);

        assertEquals(ResultCode.BAD_REQUEST.getCode(), result.getCode());
        assertEquals("密码不能为空", result.getMessage());
    }

    @Test
    void handleDataIntegrityViolationExceptionShouldReturnConflictMessage() {
        Result<Void> result = globalExceptionHandler
                .handleDataIntegrityViolationException(new DataIntegrityViolationException("duplicate key"));

        assertEquals(ResultCode.CONFLICT.getCode(), result.getCode());
        assertEquals("数据已存在或不符合约束，请检查后重试", result.getMessage());
    }

    @Test
    void handleExceptionShouldContainErrorId() {
        Result<Void> result = globalExceptionHandler.handleException(new RuntimeException("boom"));

        assertEquals(ResultCode.INTERNAL_ERROR.getCode(), result.getCode());
        assertTrue(result.getMessage().startsWith("系统内部异常，请稍后再试。如需排查请提供错误编号："));
    }

    @SuppressWarnings("unused")
    private static class RegisterForm {
        private String password;
    }
}
