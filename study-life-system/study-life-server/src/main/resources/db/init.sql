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

CREATE TABLE IF NOT EXISTS study_plan (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '学习计划主键ID',
    user_id BIGINT UNSIGNED NOT NULL COMMENT '所属用户ID',
    plan_title VARCHAR(100) NOT NULL COMMENT '计划标题',
    plan_content TEXT DEFAULT NULL COMMENT '计划详细描述',
    plan_date DATE NOT NULL COMMENT '计划所属日期',
    start_time TIME DEFAULT NULL COMMENT '计划开始时间',
    end_time TIME DEFAULT NULL COMMENT '计划结束时间',
    priority_level TINYINT NOT NULL DEFAULT 2 COMMENT '优先级：1高，2中，3低',
    plan_status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0未开始，1进行中，2已完成，3已取消',
    completion_time DATETIME DEFAULT NULL COMMENT '任务实际完成时间',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注说明',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0未删除，1已删除',
    PRIMARY KEY (id),
    CONSTRAINT fk_study_plan_user_id FOREIGN KEY (user_id) REFERENCES sys_user (id),
    CONSTRAINT ck_study_plan_priority_level CHECK (priority_level IN (1, 2, 3)),
    CONSTRAINT ck_study_plan_plan_status CHECK (plan_status IN (0, 1, 2, 3)),
    CONSTRAINT ck_study_plan_deleted_flag CHECK (deleted_flag IN (0, 1)),
    CONSTRAINT ck_study_plan_time_range CHECK (start_time IS NULL OR end_time IS NULL OR end_time >= start_time)
) COMMENT='学习计划表';

CREATE INDEX idx_study_plan_user_date ON study_plan (user_id, plan_date);
CREATE INDEX idx_study_plan_user_status_date ON study_plan (user_id, plan_status, plan_date);
CREATE INDEX idx_study_plan_user_priority_date ON study_plan (user_id, priority_level, plan_date);
