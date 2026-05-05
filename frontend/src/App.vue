<template>
  <div class="app-container">
    <el-container>
      <el-aside width="200px">
        <el-menu :default-active="activeMenu" mode="vertical" @select="handleMenuSelect">
          <el-menu-item index="/">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/clubs">
            <el-icon><User /></el-icon>
            <span>组织管理</span>
          </el-menu-item>
          <el-menu-item index="/activities">
            <el-icon><Calendar /></el-icon>
            <span>活动管理</span>
          </el-menu-item>
          <el-menu-item index="/registrations">
            <el-icon><List /></el-icon>
            <span>报名管理</span>
          </el-menu-item>
          <el-menu-item index="/summaries">
            <el-icon><Files /></el-icon>
            <span>活动总结</span>
          </el-menu-item>
          <el-menu-item index="/login">
            <el-icon><Setting /></el-icon>
            <span>登录/注册</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header style="display: flex; justify-content: center; align-items: center;">
          <h1 style="margin: 0;">大学生社团学生会信息管理系统</h1>
        </el-header>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { User, Calendar, List, Files, Setting, HomeFilled } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

const userInfo = ref({})

const activeMenu = computed(() => route.path)

const handleMenuSelect = (key) => {
  router.push(key)
}

onMounted(() => {
  const user = localStorage.getItem('user')
  if (user) {
    userInfo.value = JSON.parse(user)
  }
})
</script>

<style>
.el-menu {
  height: 100vh;
  border-right: none;
}

.el-header {
  background-color: #409EFF;
  color: white;
}
</style>