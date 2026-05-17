<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>登录</h2>
      <el-form :model="form" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" @keyup.enter="login" />
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
import axios from 'axios'

const router = useRouter()
const form = ref({ username: '', password: '' })
const formRef = ref(null)
const loading = ref(false)

const login = async () => {
  if (!form.value.username || !form.value.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }

  loading.value = true
  try {
    console.log('正在登录，用户名:', form.value.username)
    const response = await axios.post('/api/auth/login', {
      username: form.value.username,
      password: form.value.password
    })

    console.log('收到响应:', response.data)

    if (response.data.code === 200) {
        const data = response.data.data
        console.log('登录成功，Token:', data.token)
        localStorage.setItem('token', data.token)
        const userData = {
          username: data.username,
          realName: data.realName,
          role: data.role
        }
        localStorage.setItem('user', JSON.stringify(userData))
        ElMessage.success('登录成功！')
        
        let redirectPath = '/student'
        if (data.role === 'ADMIN' || data.role === 'admin') {
          redirectPath = '/admin'
        } else if (data.role === 'CLUB_MANAGER' || data.role === 'leader') {
          redirectPath = '/leader'
        }
        router.push(redirectPath)
      } else {
      console.log('登录失败:', response.data.message)
      ElMessage.error(response.data.message || '登录失败')
    }
  } catch (error) {
    console.error('登录失败:', error)
    console.error('错误响应:', error.response)
    console.error('错误请求:', error.request)
    if (error.response) {
      ElMessage.error(error.response.data?.message || '登录失败')
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
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.login-card h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}
</style>
