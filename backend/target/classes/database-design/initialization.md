# 数据初始化说明

## 状态码说明

### 用户状态 (user.status)
| 状态码 | 说明 |
|--------|------|
| 0 | 禁用 |
| 1 | 启用 |

### 社团状态 (club.status)
| 状态码 | 说明 |
|--------|------|
| 0 | 待审核 |
| 1 | 审核通过 |
| 2 | 审核驳回 |

### 社团类型 (club.type)
| 类型码 | 说明 |
|--------|------|
| 0 | 社团 |
| 1 | 学生会 |

### 社团成员角色 (club_member.role)
| 角色码 | 说明 |
|--------|------|
| 0 | 普通成员 |
| 1 | 负责人 |
| 2 | 管理员 |

### 社团成员状态 (club_member.status)
| 状态码 | 说明 |
|--------|------|
| 0 | 待审核 |
| 1 | 已通过 |
| 2 | 已退出 |

### 活动状态 (activity.status)
| 状态码 | 说明 |
|--------|------|
| 0 | 草稿 |
| 1 | 已发布 |
| 2 | 已结束 |

### 报名状态 (registration.status)
| 状态码 | 说明 |
|--------|------|
| 0 | 待确认 |
| 1 | 已确认 |
| 2 | 等待中 |
| 3 | 已取消 |

### 签到方式 (check_in.method)
| 方式码 | 说明 |
|--------|------|
| 0 | 扫码签到 |
| 1 | 定位签到 |
| 2 | 手动签到 |

## 数据初始化顺序

1. 首先创建所有表结构（执行 schema.sql）
2. 插入角色数据 (role)
3. 插入权限数据 (permission)
4. 插入角色权限关联数据 (role_permission)
5. 插入用户数据 (user)
6. 插入用户角色关联数据 (user_role)
7. 插入社团数据 (club)
8. 插入社团成员数据 (club_member)
9. 插入活动数据 (activity)
10. 插入报名数据 (registration)
11. 插入签到数据 (check_in)
12. 插入活动总结数据 (summary)

## 数据库配置说明

- **数据库类型**: SQLite
- **数据库文件**: `./data/example_db.db`
- **字符集**: UTF-8
- **时区**: Asia/Shanghai

## 索引优化说明

已创建以下索引以提升查询性能：

| 索引名称 | 表名 | 用途 |
|----------|------|------|
| idx_user_username | user | 用户登录查询 |
| idx_club_founder | club | 按创建人查询社团 |
| idx_club_member_club | club_member | 查询社团成员列表 |
| idx_club_member_user | club_member | 查询用户加入的社团 |
| idx_activity_club | activity | 查询社团活动列表 |
| idx_registration_activity | registration | 查询活动报名列表 |
| idx_registration_user | registration | 查询用户报名记录 |
| idx_checkin_registration | check_in | 查询报名签到记录 |
| idx_summary_activity | summary | 查询活动总结 |

## 初始化数据说明

初始化数据文件 `../data.sql` 包含以下示例数据：

### 角色数据
- 系统管理员 (ADMIN)
- 社团负责人 (CLUB_MANAGER)
- 普通用户 (STUDENT)

### 权限数据
共11个权限：用户管理、社团管理、活动管理、报名管理、签到管理、总结管理、查看用户、查看社团、查看活动、报名活动、签到

### 用户数据
共8个用户：
- admin - 系统管理员（密码：123456）
- yangsiqin - 社团负责人
- guhua - 社团负责人
- helinhan - 普通用户
- jiangnanzhu - 普通用户
- pengzihao - 普通用户
- zhangsan - 普通用户
- lisi - 普通用户

### 社团数据
共4个社团：
- 计算机科技协会
- 文艺表演社团
- 志愿者协会
- 学生会

### 活动数据
共5个活动示例

### 报名数据
共10条报名记录

### 签到数据
共4条签到记录

### 活动总结数据
共1条活动总结
