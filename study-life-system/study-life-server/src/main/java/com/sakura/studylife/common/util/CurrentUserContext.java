package com.sakura.studylife.common.util;

public final class CurrentUserContext {

    private static final ThreadLocal<Long> USER_HOLDER = new ThreadLocal<>();

    private CurrentUserContext() {
    }

    public static void setUserId(Long userId) {
        USER_HOLDER.set(userId);
    }

    public static Long getUserId() {
        return USER_HOLDER.get();
    }

    public static void clear() {
        USER_HOLDER.remove();
    }
}
