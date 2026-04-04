package com.sakura.studylife.service;

import com.sakura.studylife.vo.user.UserProfileVo;

public interface UserService {

    UserProfileVo getCurrentUserProfile(Long userId);
}
