# 大学生社团学生会信息管理系统

基于 Vue3 + Element Plus 的大学生社团学生会信息管理系统，提供学生、负责人、管理员三种角色的完整功能。

## 技术栈

- **前端框架**: Vue 3 (Composition API)
- **UI框架**: Element Plus
- **路由**: Vue Router
- **构建工具**: Vite
- **图标**: Element Plus Icons

## 项目结构

```
frontend2/
├── src2/
│   ├── api/           # API接口封装
│   │   ├── index.js   # API导出
│   │   ├── mock.js    # Mock数据
│   │   └── request.js # 请求封装
│   ├── components/    # 公共组件
│   │   └── Layout/    # 布局组件
│   ├── router/        # 路由配置
│   ├── views/         # 页面组件
│   ├── App.vue        # 根组件
│   ├── main.js        # 入口文件
│   └── style.css      # 全局样式
├── index.html
├── package.json
└── vite.config.js
```

## 功能模块

### 学生端 (/student)
- 学生首页 - 展示个人信息和快捷入口
- 组织浏览 - 浏览所有社团组织
- 活动列表 - 查看和报名活动
- 我的报名 - 管理个人报名记录
- 个人中心 - 查看和修改个人资料

### 负责人端 (/leader)
- 负责人首页 - 组织概览和待办事项
- 组织信息 - 维护组织基本信息
- 成员审核 - 审核成员加入申请
- 活动管理 - 发布、编辑、管理活动
- 签到管理 - 活动签到和签到记录
- 活动总结 - 撰写和管理活动总结

### 管理员端 (/admin)
- 管理员首页 - 系统概览和统计
- 用户管理 - 管理系统用户
- 组织审核 - 审核组织创建申请
- 系统统计 - 数据统计和图表展示
- 系统配置 - 系统参数配置

## 路由配置

| 角色 | 路由 | 页面 |
|------|------|------|
| 学生 | /student | 学生首页 |
| 学生 | /student/clubs | 组织浏览 |
| 学生 | /student/activities | 活动列表 |
| 学生 | /student/registrations | 我的报名 |
| 学生 | /student/profile | 个人中心 |
| 负责人 | /leader | 负责人首页 |
| 负责人 | /leader/club | 组织信息 |
| 负责人 | /leader/members | 成员审核 |
| 负责人 | /leader/activities | 活动管理 |
| 负责人 | /leader/checkin | 签到管理 |
| 负责人 | /leader/summaries | 活动总结 |
| 管理员 | /admin | 管理员首页 |
| 管理员 | /admin/users | 用户管理 |
| 管理员 | /admin/clubs | 组织审核 |
| 管理员 | /admin/statistics | 系统统计 |
| 管理员 | /admin/settings | 系统配置 |

## 安装和运行

```bash
# 安装依赖
npm install

# 开发模式运行
npm run dev

# 构建生产版本
npm run build

# 预览生产版本
npm run preview
```

## 登录测试账号

系统提供以下测试账号（Mock数据）：

- **学生**: username: student, password: 123456
- **负责人**: username: leader, password: 123456  
- **管理员**: username: admin, password: 123456

## 核心特性

1. **角色权限管理** - 基于角色的访问控制
2. **活动报名系统** - 支持限额报名和等待队列
3. **签到管理** - 支持地理位置签到
4. **组织审核** - 组织创建需要管理员审核
5. **数据统计** - 系统数据可视化统计

## License

MIT License