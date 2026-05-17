# ER图设计

## 实体关系图

```mermaid
erDiagram
    USER ||--o{ CLUB_MEMBER : "参与"
    USER ||--o{ REGISTRATION : "报名"
    USER ||--o{ CLUB : "创建"
    
    CLUB ||--o{ CLUB_MEMBER : "包含"
    CLUB ||--o{ ACTIVITY : "举办"
    
    ACTIVITY ||--o{ REGISTRATION : "接收"
    ACTIVITY ||--o{ SUMMARY : "关联"
    
    REGISTRATION ||--o{ CHECK_IN : "签到"
    
    ROLE ||--|| PERMISSION : "拥有"
    
    USER {
        INTEGER id PK "用户ID"
        VARCHAR username UK "用户名"
        VARCHAR password "加密密码"
        VARCHAR real_name "真实姓名"
        VARCHAR student_id "学号"
        VARCHAR email "邮箱"
        VARCHAR phone "手机号"
        INTEGER status "状态(0禁用/1启用)"
        DATETIME created_at "创建时间"
        DATETIME updated_at "更新时间"
    }
    
    CLUB {
        INTEGER id PK "社团ID"
        VARCHAR name "社团名称"
        INTEGER type "类型(0社团/1学生会)"
        VARCHAR description "社团简介"
        VARCHAR logo_url "Logo地址"
        INTEGER status "状态(0待审核/1通过/2驳回)"
        INTEGER founder_id FK "创建人ID"
        DATETIME created_at "创建时间"
        DATETIME updated_at "更新时间"
    }
    
    CLUB_MEMBER {
        INTEGER id PK "成员ID"
        INTEGER club_id FK "社团ID"
        INTEGER user_id FK "用户ID"
        INTEGER role "角色(0成员/1负责人/2管理员)"
        INTEGER status "状态(0待审核/1通过/2退出)"
        DATETIME joined_at "加入时间"
        DATETIME created_at "创建时间"
        DATETIME updated_at "更新时间"
    }
    
    ACTIVITY {
        INTEGER id PK "活动ID"
        INTEGER club_id FK "社团ID"
        VARCHAR name "活动名称"
        VARCHAR description "活动描述"
        DATETIME start_time "开始时间"
        DATETIME end_time "结束时间"
        VARCHAR location "活动地点"
        INTEGER quota "报名限额"
        INTEGER status "状态(0草稿/1发布/2结束)"
        DATETIME created_at "创建时间"
        DATETIME updated_at "更新时间"
    }
    
    REGISTRATION {
        INTEGER id PK "报名ID"
        INTEGER activity_id FK "活动ID"
        INTEGER user_id FK "用户ID"
        INTEGER status "状态(0待确认/1已确认/2等待/3取消)"
        INTEGER queue_position "等待队列位置"
        DATETIME registered_at "报名时间"
        DATETIME confirmed_at "确认时间"
        DATETIME updated_at "更新时间"
    }
    
    CHECK_IN {
        INTEGER id PK "签到ID"
        INTEGER registration_id FK "报名ID"
        DATETIME check_in_time "签到时间"
        INTEGER method "签到方式(0扫码/1定位/2手动)"
        DECIMAL latitude "纬度"
        DECIMAL longitude "经度"
        DATETIME created_at "创建时间"
    }
    
    SUMMARY {
        INTEGER id PK "总结ID"
        INTEGER activity_id FK "活动ID"
        TEXT content "总结内容"
        VARCHAR attachment_url "附件地址"
        DATETIME created_at "创建时间"
        DATETIME updated_at "更新时间"
    }
    
    ROLE {
        INTEGER id PK "角色ID"
        VARCHAR name "角色名称"
        VARCHAR code "角色编码"
        VARCHAR description "角色描述"
        DATETIME created_at "创建时间"
        DATETIME updated_at "更新时间"
    }
    
    PERMISSION {
        INTEGER id PK "权限ID"
        VARCHAR name "权限名称"
        VARCHAR code "权限编码"
        VARCHAR description "权限描述"
        DATETIME created_at "创建时间"
        DATETIME updated_at "更新时间"
    }
```

## 关系说明

| 关系 | 说明 | 基数 |
|------|------|------|
| USER -> CLUB | 用户创建社团 | 1:N |
| USER -> CLUB_MEMBER | 用户加入社团 | 1:N |
| USER -> REGISTRATION | 用户报名活动 | 1:N |
| CLUB -> CLUB_MEMBER | 社团包含成员 | 1:N |
| CLUB -> ACTIVITY | 社团举办活动 | 1:N |
| ACTIVITY -> REGISTRATION | 活动接收报名 | 1:N |
| ACTIVITY -> SUMMARY | 活动关联总结 | 1:1 |
| REGISTRATION -> CHECK_IN | 报名关联签到 | 1:1 |
| ROLE -> PERMISSION | 角色拥有权限 | N:M |
