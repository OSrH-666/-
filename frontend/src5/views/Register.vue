<template>
  <div class="register-container">
    <el-card class="register-card">
      <h2>注册</h2>
      <el-form :model="form" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="学号" prop="studentId">
          <el-input v-model="form.studentId" placeholder="请输入学号" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="register" style="width: 100%" :loading="loading">注册</el-button>
        </el-form-item>
        <el-form-item>
          <span>已有账号？</span>
          <el-button type="text" @click="goLogin">立即登录</el-button>
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
const form = ref({ 
  username: '', 
  password: '',
  realName: '',
  studentId: '',
  email: '',
  phone: ''
})
const formRef = ref(null)
const loading = ref(false)

const register = async () => {
  loading.value = true
  try {
    const response = await authApi.register(form.value)
    if (response.code === 200) {
      ElMessage.success('注册成功！')
      router.push('/login')
    } else {
      ElMessage.error(response.message || '注册失败')
    }
  } catch (error) {
    console.error('注册失败:', error)
    if (error.response) {
      ElMessage.error(error.response.data?.message || '注册失败')
    } else if (error.request) {
      ElMessage.error('网络错误，请检查后端服务是否启动')
    } else {
      ElMessage.error('请求失败: ' + error.message)
    }
  } finally {
    loading.value = false
  }
}

const goLogin = () => {
  router.push('/login')
}
</script>

<style>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.register-card {
  width: 400px;
}
</style>