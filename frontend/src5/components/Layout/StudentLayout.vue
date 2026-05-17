<template>
  <div class="app-container">
    <el-container>
      <el-aside width="200px" class="sidebar">
        <div class="logo">
          <el-icon class="logo-icon"><OfficeBuilding /></el-icon>
          <span>学生会管理系统</span>
        </div>
        <el-menu 
          :default-active="activeMenu" 
          mode="vertical" 
          @select="handleMenuSelect"
          class="menu"
        >
          <el-menu-item index="/">
            <el-icon class="menu-icon"><component :is="markRaw(HomeFilled)" /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/union/info">
            <el-icon class="menu-icon"><component :is="markRaw(OfficeBuilding)" /></el-icon>
            <span>学生会信息</span>
          </el-menu-item>
          <el-menu-item index="/union/members">
            <el-icon class="menu-icon"><component :is="markRaw(User)" /></el-icon>
            <span>学生会成员</span>
          </el-menu-item>
          <el-menu-item index="/union/news">
            <el-icon class="menu-icon"><component :is="markRaw(Document)" /></el-icon>
            <span>学生会动态</span>
          </el-menu-item>
          <el-menu-item index="/activities">
            <el-icon class="menu-icon"><component :is="markRaw(Calendar)" /></el-icon>
            <span>活动浏览</span>
          </el-menu-item>
          <el-menu-item index="/my-registrations">
            <el-icon class="menu-icon"><component :is="markRaw(Ticket)" /></el-icon>
            <span>我的报名</span>
          </el-menu-item>
          <el-menu-item index="/clubs">
            <el-icon class="menu-icon"><component :is="markRaw(Briefcase)" /></el-icon>
            <span>社团列表</span>
          </el-menu-item>
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
import { ref, computed, onMounted, markRaw } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { OfficeBuilding, HomeFilled, Briefcase, Calendar, Ticket, User, ArrowDown, Document } from '@element-plus/icons-vue';

const router = useRouter();
const route = useRoute();
const userInfo = ref({});

const activeMenu = computed(() => route.path);

const breadcrumbList = computed(() => {
  const matched = route.matched;
  return matched.map(item => ({
    name: item.name,
    path: item.path
  }));
});

const handleMenuSelect = (key) => {
  router.push(key);
};

const handleBreadcrumbClick = (path) => {
  router.push(path);
};

const goToProfile = () => {
  router.push('/profile');
};

const handleLogout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
  router.push('/login');
};

onMounted(() => {
  const user = localStorage.getItem('user');
  if (user) {
    userInfo.value = JSON.parse(user);
  }
});
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

.menu-icon {
  font-size: 16px;
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
  background-color: #409EFF;
  color: white;
  border-left-color: #409EFF;
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
  gap: 20px;
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
