package com.sakura.studylife.service.impl;

import com.sakura.studylife.common.enums.ResultCode;
import com.sakura.studylife.config.JwtProperties;
import com.sakura.studylife.dto.auth.LoginRequest;
import com.sakura.studylife.dto.auth.RegisterRequest;
import com.sakura.studylife.entity.SysUser;
import com.sakura.studylife.exception.BusinessException;
import com.sakura.studylife.mapper.UserMapper;
import com.sakura.studylife.service.AuthService;
import com.sakura.studylife.util.JwtTokenUtil;
import com.sakura.studylife.vo.auth.LoginResponse;
import com.sakura.studylife.vo.auth.LoginUserInfoVo;
import com.sakura.studylife.vo.auth.RegisterResponse;
import java.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtProperties jwtProperties;

    public AuthServiceImpl(
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            JwtTokenUtil jwtTokenUtil,
            JwtProperties jwtProperties) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtProperties = jwtProperties;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegisterResponse register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR.getCode(), "确认密码必须与密码一致");
        }

        SysUser existedUser = userMapper.selectByUsername(request.getUsername());
        if (existedUser != null) {
            throw new BusinessException(ResultCode.USERNAME_EXISTS);
        }

        LocalDateTime now = LocalDateTime.now();
        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setAccountStatus(1);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        user.setDeletedFlag(0);
        userMapper.insert(user);

        return new RegisterResponse(user.getId(), user.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(LoginRequest request) {
        SysUser user = userMapper.selectByUsername(request.getUsername());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BusinessException(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }
        if (Integer.valueOf(0).equals(user.getAccountStatus())) {
            throw new BusinessException(ResultCode.ACCOUNT_DISABLED);
        }

        LocalDateTime now = LocalDateTime.now();
        userMapper.updateLastLoginTime(user.getId(), now);
        user.setLastLoginTime(now);

        String token = jwtTokenUtil.generateToken(user.getId(), user.getUsername());
        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtProperties.getExpireSeconds())
                .userInfo(LoginUserInfoVo.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .nickname(user.getNickname())
                        .build())
                .build();
    }
}
