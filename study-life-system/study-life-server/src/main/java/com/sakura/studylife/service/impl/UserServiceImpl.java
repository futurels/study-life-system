package com.sakura.studylife.service.impl;

import com.sakura.studylife.common.enums.ResultCode;
import com.sakura.studylife.entity.SysUser;
import com.sakura.studylife.exception.BusinessException;
import com.sakura.studylife.mapper.UserMapper;
import com.sakura.studylife.service.UserService;
import com.sakura.studylife.vo.user.UserProfileVo;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserProfileVo getCurrentUserProfile(Long userId) {
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "用户不存在");
        }
        return UserProfileVo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .accountStatus(user.getAccountStatus())
                .lastLoginTime(user.getLastLoginTime())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
