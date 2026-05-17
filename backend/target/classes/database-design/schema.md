# 建表SQL

## 表结构说明

本系统使用SQLite数据库，包含以下11张表：

| 表名 | 说明 |
|------|------|
| user | 用户表 |
| club | 社团表 |
| club_member | 社团成员表 |
| activity | 活动表 |
| registration | 报名表 |
| check_in | 签到表 |
| summary | 活动总结表 |
| role | 角色表 |
| permission | 权限表 |
| role_permission | 角色权限关联表 |
| user_role | 用户角色关联表 |

## 建表SQL

完整的建表SQL请参考：`../schema.sql`

### 表结构概览

#### 用户表 (user)
| 字段 | 类型 | 约束 |
|------|------|------|
| id | INTEGER | PRIMARY KEY AUTOINCREMENT |
| username | VARCHAR(50) | NOT NULL UNIQUE |
| password | VARCHAR(255) | NOT NULL |
| real_name | VARCHAR(50) | - |
| student_id | VARCHAR(20) | - |
| email | VARCHAR(100) | - |
| phone | VARCHAR(20) | - |
| status | INTEGER | DEFAULT 1 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |

#### 社团表 (club)
| 字段 | 类型 | 约束 |
|------|------|------|
| id | INTEGER | PRIMARY KEY AUTOINCREMENT |
| name | VARCHAR(100) | NOT NULL |
| type | INTEGER | DEFAULT 0 |
| description | TEXT | - |
| logo_url | VARCHAR(500) | - |
| status | INTEGER | DEFAULT 0 |
| founder_id | INTEGER | FOREIGN KEY |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |

#### 活动表 (activity)
| 字段 | 类型 | 约束 |
|------|------|------|
| id | INTEGER | PRIMARY KEY AUTOINCREMENT |
| club_id | INTEGER | FOREIGN KEY |
| name | VARCHAR(100) | NOT NULL |
| description | TEXT | - |
| start_time | TIMESTAMP | - |
| end_time | TIMESTAMP | - |
| location | VARCHAR(200) | - |
| quota | INTEGER | DEFAULT 50 |
| status | INTEGER | DEFAULT 0 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |

#### 报名表 (registration)
| 字段 | 类型 | 约束 |
|------|------|------|
| id | INTEGER | PRIMARY KEY AUTOINCREMENT |
| activity_id | INTEGER | FOREIGN KEY |
| user_id | INTEGER | FOREIGN KEY |
| status | INTEGER | DEFAULT 0 |
| queue_position | INTEGER | DEFAULT 0 |
| registered_at | TIMESTAMP | - |
| confirmed_at | TIMESTAMP | - |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |

#### 签到表 (check_in)
| 字段 | 类型 | 约束 |
|------|------|------|
| id | INTEGER | PRIMARY KEY AUTOINCREMENT |
| registration_id | INTEGER | FOREIGN KEY |
| check_in_time | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |
| method | INTEGER | DEFAULT 2 |
| latitude | DECIMAL(10,7) | - |
| longitude | DECIMAL(10,7) | - |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |

#### 角色权限相关表
- **role** - 角色表
- **permission** - 权限表  
- **role_permission** - 角色权限关联表
- **user_role** - 用户角色关联表

详细字段说明请参考 `fields.md`。

## 索引设计

| 索引名称 | 表名 | 字段 | 用途 |
|----------|------|------|------|
| idx_user_username | user | username | 用户登录查询 |
| idx_club_founder | club | founder_id | 按创建人查询社团 |
| idx_club_member_club | club_member | club_id | 查询社团成员列表 |
| idx_club_member_user | club_member | user_id | 查询用户加入的社团 |
| idx_activity_club | activity | club_id | 查询社团活动列表 |
| idx_registration_activity | registration | activity_id | 查询活动报名列表 |
| idx_registration_user | registration | user_id | 查询用户报名记录 |
| idx_checkin_registration | check_in | registration_id | 查询报名签到记录 |
| idx_summary_activity | summary | activity_id | 查询活动总结 |
