package com.sakura.studylife.mapper;

import com.sakura.studylife.entity.SysUser;
import java.time.LocalDateTime;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    SysUser selectByUsername(@Param("username") String username);

    SysUser selectById(@Param("id") Long id);

    int insert(SysUser user);

    int updateLastLoginTime(@Param("id") Long id, @Param("lastLoginTime") LocalDateTime lastLoginTime);
}
