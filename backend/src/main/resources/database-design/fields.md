# 字段说明

## 用户表 (user)

| 字段名 | 类型 | 长度 | 约束 | 说明 |
|--------|------|------|------|------|
| id | INTEGER | - | PRIMARY KEY, AUTOINCREMENT | 用户唯一标识 |
| username | VARCHAR | 50 | NOT NULL, UNIQUE | 登录用户名 |
| password | VARCHAR | 255 | NOT NULL | BCrypt加密密码 |
| real_name | VARCHAR | 50 | - | 用户真实姓名 |
| student_id | VARCHAR | 20 | - | 学号 |
| email | VARCHAR | 100 | - | 邮箱地址 |
| phone | VARCHAR | 20 | - | 手机号码 |
| role | VARCHAR | 50 | DEFAULT 'STUDENT' | 角色：ADMIN-管理员，CLUB_MANAGER-社团负责人，STUDENT-普通用户 |
| grade | VARCHAR | 20 | - | 年级 |
| major | VARCHAR | 50 | - | 专业 |
| class_name | VARCHAR | 50 | - | 班级 |
| avatar_url | VARCHAR | 255 | - | 头像URL |
| status | INTEGER | - | DEFAULT 1 | 状态：0-禁用，1-启用 |
| created_at | TIMESTAMP | - | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | - | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

## 社团表 (club)

| 字段名 | 类型 | 长度 | 约束 | 说明 |
|--------|------|------|------|------|
| id | INTEGER | - | PRIMARY KEY, AUTOINCREMENT | 社团唯一标识 |
| name | VARCHAR | 100 | NOT NULL | 社团名称 |
| type | INTEGER | - | DEFAULT 0 | 类型：0-社团，1-学生会 |
| description | TEXT | - | - | 社团简介描述 |
| logo_url | VARCHAR | 500 | - | Logo图片URL |
| status | INTEGER | - | DEFAULT 0 | 状态：0-待审核，1-通过，2-驳回 |
| founder_id | INTEGER | - | FOREIGN KEY | 创建人ID，关联user表 |
| created_at | TIMESTAMP | - | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | - | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

## 社团成员表 (club_member)

| 字段名 | 类型 | 长度 | 约束 | 说明 |
|--------|------|------|------|------|
| id | INTEGER | - | PRIMARY KEY, AUTOINCREMENT | 成员记录ID |
| club_id | INTEGER | - | FOREIGN KEY | 社团ID，关联club表 |
| user_id | INTEGER | - | FOREIGN KEY | 用户ID，关联user表 |
| role | INTEGER | - | DEFAULT 0 | 角色：0-普通成员，1-负责人，2-管理员 |
| status | INTEGER | - | DEFAULT 0 | 状态：0-待审核，1-通过，2-退出 |
| joined_at | TIMESTAMP | - | - | 加入时间 |
| created_at | TIMESTAMP | - | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | - | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

## 活动表 (activity)

| 字段名 | 类型 | 长度 | 约束 | 说明 |
|--------|------|------|------|------|
| id | INTEGER | - | PRIMARY KEY, AUTOINCREMENT | 活动唯一标识 |
| club_id | INTEGER | - | FOREIGN KEY | 社团ID，关联club表 |
| name | VARCHAR | 100 | NOT NULL | 活动名称 |
| description | TEXT | - | - | 活动描述 |
| start_time | TIMESTAMP | - | - | 活动开始时间 |
| end_time | TIMESTAMP | - | - | 活动结束时间 |
| location | VARCHAR | 200 | - | 活动地点 |
| quota | INTEGER | - | DEFAULT 50 | 报名限额，0表示不限 |
| status | INTEGER | - | DEFAULT 0 | 状态：0-草稿，1-发布，2-结束 |
| created_at | TIMESTAMP | - | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | - | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

## 报名表 (registration)

| 字段名 | 类型 | 长度 | 约束 | 说明 |
|--------|------|------|------|------|
| id | INTEGER | - | PRIMARY KEY, AUTOINCREMENT | 报名记录ID |
| activity_id | INTEGER | - | FOREIGN KEY | 活动ID，关联activity表 |
| user_id | INTEGER | - | FOREIGN KEY | 用户ID，关联user表 |
| status | INTEGER | - | DEFAULT 0 | 状态：0-待确认，1-已确认，2-等待，3-取消 |
| queue_position | INTEGER | - | DEFAULT 0 | 等待队列位置 |
| registered_at | TIMESTAMP | - | - | 报名时间 |
| confirmed_at | TIMESTAMP | - | - | 确认时间 |
| updated_at | TIMESTAMP | - | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

## 签到表 (check_in)

| 字段名 | 类型 | 长度 | 约束 | 说明 |
|--------|------|------|------|------|
| id | INTEGER | - | PRIMARY KEY, AUTOINCREMENT | 签到记录ID |
| registration_id | INTEGER | - | FOREIGN KEY | 报名ID，关联registration表 |
| check_in_time | TIMESTAMP | - | DEFAULT CURRENT_TIMESTAMP | 签到时间 |
| method | INTEGER | - | DEFAULT 2 | 签到方式：0-扫码，1-定位，2-手动 |
| latitude | DECIMAL | 10,7 | - | 签到时的纬度 |
| longitude | DECIMAL | 10,7 | - | 签到时的经度 |
| created_at | TIMESTAMP | - | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

## 活动总结表 (summary)

| 字段名 | 类型 | 长度 | 约束 | 说明 |
|--------|------|------|------|------|
| id | INTEGER | - | PRIMARY KEY, AUTOINCREMENT | 总结ID |
| activity_id | INTEGER | - | FOREIGN KEY | 活动ID，关联activity表 |
| content | TEXT | - | - | 总结内容 |
| attachment_url | VARCHAR | 500 | - | 附件URL |
| status | INTEGER | - | DEFAULT 0 | 状态：0-待审核，1-通过，2-拒绝 |
| created_at | TIMESTAMP | - | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | - | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

## 角色表 (role)

| 字段名 | 类型 | 长度 | 约束 | 说明 |
|--------|------|------|------|------|
| id | INTEGER | - | PRIMARY KEY, AUTOINCREMENT | 角色ID |
| name | VARCHAR | 50 | NOT NULL | 角色名称 |
| code | VARCHAR | 50 | NOT NULL, UNIQUE | 角色编码 |
| description | VARCHAR | 200 | - | 角色描述 |
| created_at | TIMESTAMP | - | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | - | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

## 权限表 (permission)

| 字段名 | 类型 | 长度 | 约束 | 说明 |
|--------|------|------|------|------|
| id | INTEGER | - | PRIMARY KEY, AUTOINCREMENT | 权限ID |
| name | VARCHAR | 50 | NOT NULL | 权限名称 |
| code | VARCHAR | 50 | NOT NULL, UNIQUE | 权限编码 |
| description | VARCHAR | 200 | - | 权限描述 |
| created_at | TIMESTAMP | - | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | - | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

## 角色权限关联表 (role_permission)

| 字段名 | 类型 | 长度 | 约束 | 说明 |
|--------|------|------|------|------|
| id | INTEGER | - | PRIMARY KEY, AUTOINCREMENT | 关联记录ID |
| role_id | INTEGER | - | FOREIGN KEY | 角色ID，关联role表 |
| permission_id | INTEGER | - | FOREIGN KEY | 权限ID，关联permission表 |

## 用户角色关联表 (user_role)

| 字段名 | 类型 | 长度 | 约束 | 说明 |
|--------|------|------|------|------|
| id | INTEGER | - | PRIMARY KEY, AUTOINCREMENT | 关联记录ID |
| user_id | INTEGER | - | FOREIGN KEY | 用户ID，关联user表 |
| role_id | INTEGER | - | FOREIGN KEY | 角色ID，关联role表 |
