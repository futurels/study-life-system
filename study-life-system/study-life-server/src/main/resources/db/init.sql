CREATE DATABASE IF NOT EXISTS study_life_db
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_0900_ai_ci;

USE study_life_db;

CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户主键ID',
    username VARCHAR(50) NOT NULL COMMENT '登录用户名',
    password_hash VARCHAR(255) NOT NULL COMMENT '加密后的密码',
    nickname VARCHAR(50) NOT NULL COMMENT '用户昵称',
    email VARCHAR(100) DEFAULT NULL COMMENT '邮箱地址',
    account_status TINYINT NOT NULL DEFAULT 1 COMMENT '账号状态：0禁用，1正常',
    last_login_time DATETIME DEFAULT NULL COMMENT '最近登录时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0未删除，1已删除',
    PRIMARY KEY (id),
    CONSTRAINT uq_sys_user_username UNIQUE (username),
    CONSTRAINT uq_sys_user_email UNIQUE (email),
    CONSTRAINT ck_sys_user_account_status CHECK (account_status IN (0, 1)),
    CONSTRAINT ck_sys_user_deleted_flag CHECK (deleted_flag IN (0, 1))
) COMMENT='用户表';
