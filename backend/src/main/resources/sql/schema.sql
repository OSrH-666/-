-- 大学生学生会管理系统 - 数据库建表SQL (SQLite)

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    real_name VARCHAR(50),
    student_id VARCHAR(20),
    email VARCHAR(100),
    phone VARCHAR(20),
    role VARCHAR(50) DEFAULT 'STUDENT',
    grade VARCHAR(20),
    major VARCHAR(50),
    class_name VARCHAR(50),
    avatar_url VARCHAR(255),
    status INTEGER DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 角色表
CREATE TABLE IF NOT EXISTS role (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS user_role (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, role_id)
);

-- 权限表
CREATE TABLE IF NOT EXISTS permission (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(50) NOT NULL,
    code VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS role_permission (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    role_id INTEGER NOT NULL,
    permission_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(role_id, permission_id)
);

-- 社团表
CREATE TABLE IF NOT EXISTS club (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL,
    type INTEGER NOT NULL,
    description TEXT,
    logo_url VARCHAR(255),
    status INTEGER DEFAULT 0,
    founder_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 社团成员表
CREATE TABLE IF NOT EXISTS club_member (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    club_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    role INTEGER DEFAULT 0,
    status INTEGER DEFAULT 0,
    joined_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(club_id, user_id)
);

-- 活动表
CREATE TABLE IF NOT EXISTS activity (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    club_id INTEGER NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    location VARCHAR(200),
    quota INTEGER DEFAULT 0,
    status INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 报名表
CREATE TABLE IF NOT EXISTS registration (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    activity_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    status INTEGER DEFAULT 0,
    queue_position INTEGER DEFAULT 0,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    confirmed_at TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(activity_id, user_id)
);

-- 签到表
CREATE TABLE IF NOT EXISTS check_in (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    registration_id INTEGER NOT NULL UNIQUE,
    check_in_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    method INTEGER,
    latitude DECIMAL(10,7),
    longitude DECIMAL(10,7),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 活动总结表
CREATE TABLE IF NOT EXISTS summary (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    activity_id INTEGER NOT NULL UNIQUE,
    content TEXT,
    attachment_url VARCHAR(255),
    status INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_user_username ON user(username);
CREATE INDEX IF NOT EXISTS idx_user_role ON user(role);
CREATE INDEX IF NOT EXISTS idx_role_code ON role(code);
CREATE INDEX IF NOT EXISTS idx_permission_code ON permission(code);
CREATE INDEX IF NOT EXISTS idx_club_name ON club(name);
CREATE INDEX IF NOT EXISTS idx_club_status ON club(status);
CREATE INDEX IF NOT EXISTS idx_club_founder_id ON club(founder_id);
CREATE INDEX IF NOT EXISTS idx_club_member_club_id ON club_member(club_id);
CREATE INDEX IF NOT EXISTS idx_club_member_user_id ON club_member(user_id);
CREATE INDEX IF NOT EXISTS idx_club_member_status ON club_member(status);
CREATE INDEX IF NOT EXISTS idx_activity_club_id ON activity(club_id);
CREATE INDEX IF NOT EXISTS idx_activity_status ON activity(status);
CREATE INDEX IF NOT EXISTS idx_activity_start_time ON activity(start_time);
CREATE INDEX IF NOT EXISTS idx_registration_activity_id ON registration(activity_id);
CREATE INDEX IF NOT EXISTS idx_registration_user_id ON registration(user_id);
CREATE INDEX IF NOT EXISTS idx_registration_status ON registration(status);
CREATE INDEX IF NOT EXISTS idx_check_in_registration_id ON check_in(registration_id);
CREATE INDEX IF NOT EXISTS idx_summary_activity_id ON summary(activity_id);
CREATE INDEX IF NOT EXISTS idx_summary_status ON summary(status);
