<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>登录</h2>
      <el-form :model="form" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="login" style="width: 100%" :loading="loading">登录</el-button>
        </el-form-item>
        <el-form-item>
          <span>还没有账号？</span>
          <el-button type="text" @click="goRegister">立即注册</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { authApi } from '../api'

const router = useRouter()
const form = ref({ username: '', password: '' })
const formRef = ref(null)
const loading = ref(false)

const login = async () => {
  loading.value = true
  try {
    const response = await authApi.login(form.value)
    if (response.code === 200) {
      localStorage.setItem('token', response.data.token)
      localStorage.setItem('user', JSON.stringify({
        username: response.data.username,
        realName: response.data.realName,
        role: response.data.role
      }))
      ElMessage.success('登录成功！')
      router.push('/')
    } else {
      if (response.code === 403) {
        ElMessage.error('账号已禁用，请联系管理员')
      } else {
        ElMessage.error(response.message || '登录失败')
      }
    }
  } catch (error) {
    console.error('登录失败:', error)
    if (error.response) {
      if (error.response.data?.code === 403) {
        ElMessage.error('账号已禁用，请联系管理员')
      } else {
        ElMessage.error(error.response.data?.message || '登录失败')
      }
    } else if (error.request) {
      ElMessage.error('网络错误，请检查后端服务是否启动')
    } else {
      ElMessage.error('请求失败: ' + error.message)
    }
  } finally {
    loading.value = false
  }
}

const goRegister = () => {
  router.push('/register')
}
</script>

<style>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.login-card {
  width: 400px;
}
</style>
