<template>
  <div class="app-container">
    <el-container>
      <el-aside width="200px" class="sidebar">
        <div class="logo">
          <el-icon class="logo-icon"><OfficeBuilding /></el-icon>
          <span>社团管理系统</span>
        </div>
        <el-menu 
          :default-active="activeMenu" 
          mode="vertical" 
          @select="handleMenuSelect"
          class="menu"
        >
          <template v-for="item in filteredMenuItems" :key="item.path">
            <el-menu-item :index="item.path" v-if="!item.children">
              <component :is="item.icon" />
              <span>{{ item.name }}</span>
            </el-menu-item>
            <el-sub-menu :index="item.path" v-else>
              <template #title>
                <component :is="item.icon" />
                <span>{{ item.name }}</span>
              </template>
              <el-menu-item 
                v-for="child in item.children" 
                :key="child.path" 
                :index="child.path"
              >
                {{ child.name }}
              </el-menu-item>
            </el-sub-menu>
          </template>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header class="header">
          <div class="header-left">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item v-for="(item, index) in breadcrumbList" :key="index">
                <a v-if="item.path" @click="handleBreadcrumbClick(item.path)">{{ item.name }}</a>
                <span v-else>{{ item.name }}</span>
              </el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <div class="header-right">
            <el-tag :type="getRoleTagType(userInfo.role)" size="small">
              {{ getRoleText(userInfo.role) }}
            </el-tag>
            <el-dropdown>
              <span class="user-info">
                <el-icon class="user-icon"><User /></el-icon>
                <span>{{ userInfo.realName || userInfo.username || '用户' }}</span>
                <el-icon class="arrow-icon"><ArrowDown /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="goToProfile">个人中心</el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        <el-main class="main-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, markRaw } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { 
  OfficeBuilding, HomeFilled, Briefcase, Calendar, List, Files, 
  User, ArrowDown, Users, Settings, CheckSquare, BarChart3
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userInfo = ref({ role: 'student' })

const allMenuItems = {
  student: [
    { path: '/student', name: '学生首页', icon: markRaw(HomeFilled) },
    { path: '/student/clubs', name: '组织浏览', icon: markRaw(Briefcase) },
    { path: '/student/activities', name: '活动列表', icon: markRaw(Calendar) },
    { path: '/student/registrations', name: '我的报名', icon: markRaw(List) },
    { path: '/student/profile', name: '个人中心', icon: markRaw(User) }
  ],
  leader: [
    { path: '/leader', name: '负责人首页', icon: markRaw(HomeFilled) },
    { path: '/leader/club', name: '组织信息', icon: markRaw(Briefcase) },
    { path: '/leader/members', name: '成员审核', icon: markRaw(Users) },
    { path: '/leader/activities', name: '活动管理', icon: markRaw(Calendar) },
    { path: '/leader/checkin', name: '签到管理', icon: markRaw(CheckSquare) },
    { path: '/leader/summaries', name: '活动总结', icon: markRaw(Files) }
  ],
  admin: [
    { path: '/admin', name: '管理员首页', icon: markRaw(HomeFilled) },
    { path: '/admin/users', name: '用户管理', icon: markRaw(Users) },
    { path: '/admin/clubs', name: '组织审核', icon: markRaw(Briefcase) },
    { path: '/admin/statistics', name: '系统统计', icon: markRaw(BarChart3) },
    { path: '/admin/settings', name: '系统配置', icon: markRaw(Settings) }
  ]
}

const filteredMenuItems = computed(() => {
  const role = userInfo.value.role || 'student'
  return allMenuItems[role] || allMenuItems.student
})

const activeMenu = computed(() => route.path)

const breadcrumbList = computed(() => {
  const matched = route.matched
  return matched.map(item => ({
    name: item.meta.title || item.name,
    path: item.path
  }))
})

const getRoleText = (role) => {
  const map = { student: '学生', leader: '负责人', admin: '管理员' }
  return map[role] || '未知'
}

const getRoleTagType = (role) => {
  const map = { student: 'info', leader: 'success', admin: 'warning' }
  return map[role] || 'info'
}

const handleMenuSelect = (key) => {
  router.push(key)
}

const handleBreadcrumbClick = (path) => {
  router.push(path)
}

const goToProfile = () => {
  const role = userInfo.value.role || 'student'
  router.push(`/${role}/profile`)
}

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}

onMounted(() => {
  const user = localStorage.getItem('user')
  if (user) {
    userInfo.value = JSON.parse(user)
  }
})
</script>

<style scoped>
.app-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.sidebar {
  background-color: #2f4050;
  color: white;
  display: flex;
  flex-direction: column;
}

.logo {
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
  border-bottom: 1px solid #1f2d3d;
}

.logo-icon {
  font-size: 24px;
}

.logo span {
  font-size: 18px;
  font-weight: bold;
}

.menu {
  flex: 1;
  border-right: none;
  background-color: transparent;
}

.menu :deep(.el-menu-item) {
  color: #a7b1c2;
  border-left: 3px solid transparent;
}

.menu :deep(.el-menu-item:hover) {
  background-color: #1f2d3d;
  color: white;
}

.menu :deep(.el-menu-item.is-active) {
  background-color: #1ab394;
  color: white;
  border-left-color: #1ab394;
}

.menu :deep(.el-sub-menu__title) {
  color: #a7b1c2;
}

.menu :deep(.el-sub-menu__title:hover) {
  background-color: #1f2d3d;
}

.header {
  background-color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-left {
  flex: 1;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.user-info:hover {
  background-color: #f5f7fa;
}

.user-icon {
  font-size: 18px;
}

.arrow-icon {
  font-size: 14px;
}

.main-content {
  background-color: #f3f3f4;
  padding: 20px;
  overflow: auto;
}
</style>