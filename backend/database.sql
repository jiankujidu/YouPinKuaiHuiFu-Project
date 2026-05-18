-- 优品快回复数据库结构

CREATE DATABASE IF NOT EXISTS youpin_kuaihuifu CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE youpin_kuaihuifu;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50),
    team_id VARCHAR(36),
    role ENUM('admin', 'member') DEFAULT 'member',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE SET NULL
);

-- 团队表
CREATE TABLE IF NOT EXISTS teams (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    owner_id VARCHAR(36) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 话术表
CREATE TABLE IF NOT EXISTS phrases (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36),
    team_id VARCHAR(36),
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    type ENUM('company', 'team', 'private') DEFAULT 'private',
    category_id VARCHAR(36),
    usage_count INT DEFAULT 0,
    sort_order INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE
);

-- 话术分类表
CREATE TABLE IF NOT EXISTS categories (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36),
    team_id VARCHAR(36),
    name VARCHAR(100) NOT NULL,
    type ENUM('company', 'team', 'private') DEFAULT 'private',
    sort_order INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE
);

-- 快捷设置表
CREATE TABLE IF NOT EXISTS settings (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) UNIQUE NOT NULL,
    float_window_enabled BOOLEAN DEFAULT TRUE,
    float_window_position VARCHAR(20) DEFAULT 'right',
    auto_copy BOOLEAN DEFAULT TRUE,
    show_notification BOOLEAN DEFAULT TRUE,
    theme VARCHAR(20) DEFAULT 'wechat',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 使用统计表
CREATE TABLE IF NOT EXISTS usage_stats (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    phrase_id VARCHAR(36) NOT NULL,
    used_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (phrase_id) REFERENCES phrases(id) ON DELETE CASCADE
);

-- 创建索引
CREATE INDEX idx_phrases_user_id ON phrases(user_id);
CREATE INDEX idx_phrases_team_id ON phrases(team_id);
CREATE INDEX idx_phrases_type ON phrases(type);
CREATE INDEX idx_phrases_category ON phrases(category_id);
CREATE INDEX idx_usage_stats_user_id ON usage_stats(user_id);
CREATE INDEX idx_usage_stats_phrase_id ON usage_stats(phrase_id);

-- 插入默认分类
INSERT INTO categories (id, name, type, sort_order) VALUES
('cat-001', '欢迎语', 'company', 1),
('cat-002', '常见问题', 'company', 2),
('cat-003', '售后处理', 'company', 3),
('cat-004', '催单话术', 'team', 1),
('cat-005', '好评回复', 'team', 2);
