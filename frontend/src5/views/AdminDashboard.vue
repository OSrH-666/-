<template>
  <div class="admin-dashboard">
    <el-row :gutter="20">
      <el-col :span="24">
        <div class="welcome-card">
          <h2>系统管理后台</h2>
          <p>管理员 · {{ userInfo.realName || userInfo.username }}</p>
          <p class="date">{{ currentDate }}</p>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon blue">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-info">
              <h3>{{ stats.userCount }}</h3>
              <p>系统用户</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon green">
              <el-icon><OfficeBuilding /></el-icon>
            </div>
            <div class="stat-info">
              <h3>{{ stats.clubCount }}</h3>
              <p>学生社团</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon orange">
              <el-icon><Calendar /></el-icon>
            </div>
            <div class="stat-info">
              <h3>{{ stats.activityCount }}</h3>
              <p>活动总数</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon purple">
              <el-icon><Ticket /></el-icon>
            </div>
            <div class="stat-info">
              <h3>{{ stats.registrationCount }}</h3>
              <p>报名记录</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="8">
        <el-card>
          <template #header>
            <span>系统概览</span>
          </template>
          <div class="overview-list">
            <div class="overview-item">
              <span>学生会成员</span>
              <span class="value">{{ stats.unionMembers }}</span>
            </div>
            <div class="overview-item">
              <span>学生会动态</span>
              <span class="value">{{ stats.unionNews }}</span>
            </div>
            <div class="overview-item">
              <span>活跃社团</span>
              <span class="value">{{ stats.activeClubs }}</span>
            </div>
            <div class="overview-item">
              <span>本月活动</span>
              <span class="value">{{ stats.monthlyActivities }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>待处理事项</span>
            </div>
          </template>
          <div class="todo-list">
            <div v-for="todo in todos" :key="todo.id" class="todo-item">
              <div class="todo-info">
                <el-tag :type="todo.type" size="small">{{ todo.tag }}</el-tag>
                <span>{{ todo.content }}</span>
              </div>
              <el-button size="small" type="primary" plain @click="goToPage(todo.path)">处理</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <span>快捷管理</span>
          </template>
          <div class="quick-actions">
            <el-button type="primary" @click="goTo('/users')">用户管理</el-button>
            <el-button type="success" @click="goTo('/clubs')">社团管理</el-button>
            <el-button type="warning" @click="goTo('/activities')">活动管理</el-button>
            <el-button type="info" @click="goTo('/union/info')">学生会设置</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>最新活动报名</span>
          </template>
          <el-table :data="recentActivities" border>
            <el-table-column prop="activityName" label="活动名称" />
            <el-table-column prop="registeredCount" label="报名人数" width="100" />
            <el-table-column prop="maxParticipants" label="上限" width="80" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>最新用户注册</span>
          </template>
          <el-table :data="recentUsers" border>
            <el-table-column prop="username" label="用户名" />
            <el-table-column prop="realName" label="姓名" />
            <el-table-column prop="role" label="角色">
              <template #default="scope">
                <el-tag :type="getRoleTagType(scope.row.role)">{{ getRoleText(scope.row.role) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="注册时间" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { User, OfficeBuilding, Calendar, Ticket } from '@element-plus/icons-vue'

const router = useRouter()
const userInfo = ref({})
const currentDate = new Date().toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' })

const stats = ref({
  userCount: 156,
  clubCount: 12,
  activityCount: 48,
  registrationCount: 892,
  unionMembers: 24,
  unionNews: 18,
  activeClubs: 10,
  monthlyActivities: 8
})

const todos = ref([
  { id: 1, type: 'warning', tag: '报名', content: '12 个报名待处理', path: '/registrations' },
  { id: 2, type: 'success', tag: '社团', content: '3 个社团待审核', path: '/clubs' },
  { id: 3, type: 'info', tag: '用户', content: '5 个新用户待激活', path: '/users' }
])

const recentActivities = ref([
  { activityName: 'AI技术分享会', registeredCount: 45, maxParticipants: 50, status: '报名中' },
  { activityName: '编程大赛', registeredCount: 30, maxParticipants: 30, status: '已满' },
  { activityName: '迎新晚会', registeredCount: 200, maxParticipants: 300, status: '报名中' },
  { activityName: '志愿服务', registeredCount: 25, maxParticipants: 20, status: '已满' }
])

const recentUsers = ref([
  { username: 'wangwu', realName: '王五', role: 'STUDENT', createdAt: '2024-12-10' },
  { username: 'zhaoliu', realName: '赵六', role: 'STUDENT', createdAt: '2024-12-09' },
  { username: 'sunqi', realName: '孙七', role: 'CLUB_MANAGER', createdAt: '2024-12-08' },
  { username: 'zhouba', realName: '周八', role: 'STUDENT', createdAt: '2024-12-07' }
])

const getStatusType = (status) => {
  const map = { '报名中': 'success', '已满': 'warning', '已结束': 'info' }
  return map[status] || ''
}

const getRoleText = (role) => {
  const map = { 'STUDENT': '学生', 'CLUB_MANAGER': '负责人', 'ADMIN': '管理员' }
  return map[role] || '未知'
}

const getRoleTagType = (role) => {
  const map = { 'STUDENT': 'info', 'CLUB_MANAGER': 'success', 'ADMIN': 'warning' }
  return map[role] || 'info'
}

const goTo = (path) => {
  router.push(path)
}

const goToPage = (path) => {
  router.push(path)
}

onMounted(() => {
  const user = localStorage.getItem('user')
  if (user) {
    userInfo.value = JSON.parse(user)
  }
})
</script>

<style scoped>
.welcome-card {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
  padding: 30px;
  border-radius: 8px;
}

.welcome-card h2 {
  margin: 0 0 5px 0;
}

.welcome-card p {
  margin: 0 0 5px 0;
  opacity: 0.9;
}

.welcome-card .date {
  margin: 0;
  font-size: 14px;
  opacity: 0.8;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 15px;
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-icon .el-icon {
  font-size: 24px;
  color: white;
}

.stat-icon.blue { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.stat-icon.green { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); }
.stat-icon.orange { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
.stat-icon.purple { background: linear-gradient(135deg, #a855f7 0%, #6366f1 100%); }

.stat-info h3 {
  margin: 0;
  font-size: 28px;
  font-weight: bold;
}

.stat-info p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.overview-list {
  padding: 10px 0;
}

.overview-item {
  display: flex;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #eee;
}

.overview-item:last-child {
  border-bottom: none;
}

.overview-item .value {
  font-weight: bold;
  color: #409EFF;
}

.todo-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #eee;
}

.todo-item:last-child {
  border-bottom: none;
}

.todo-info {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
}

.quick-actions {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
</style>
