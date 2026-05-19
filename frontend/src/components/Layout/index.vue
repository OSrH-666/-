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
          <template v-for="item in menuItems" :key="item.path">
            <el-menu-item :index="item.path" v-if="!item.children">
              <el-icon class="menu-icon"><component :is="item.icon" /></el-icon>
              <span>{{ item.name }}</span>
            </el-menu-item>
            <el-sub-menu :index="item.path" v-else>
              <template #title>
                <el-icon class="menu-icon"><component :is="item.icon" /></el-icon>
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
            <el-dropdown>
              <span class="user-info">
                <el-icon class="user-icon"><User /></el-icon>
                <span>{{ userInfo.name || '用户' }}</span>
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

<script setup>import { ref, computed, onMounted, markRaw } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { OfficeBuilding, HomeFilled, Briefcase, Calendar, List, Files, User, ArrowDown } from '@element-plus/icons-vue';
const router = useRouter();
const route = useRoute();
const userInfo = ref({});
const menuItems = [
 { path: '/', name: '首页', icon: markRaw(HomeFilled) },
 { path: '/clubs', name: '组织管理', icon: markRaw(Briefcase) },
 { path: '/members', name: '成员管理', icon: markRaw(User) },
 { path: '/activities', name: '活动管理', icon: markRaw(Calendar) },
 { path: '/registrations', name: '报名管理', icon: markRaw(List) },
 { path: '/summaries', name: '活动总结', icon: markRaw(Files) }
];
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
