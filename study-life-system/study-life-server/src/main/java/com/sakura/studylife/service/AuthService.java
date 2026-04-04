package com.sakura.studylife.service;

import com.sakura.studylife.dto.auth.LoginRequest;
import com.sakura.studylife.dto.auth.RegisterRequest;
import com.sakura.studylife.vo.auth.LoginResponse;
import com.sakura.studylife.vo.auth.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}
