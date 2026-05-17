<template>
  <div class="edit-profile">
    <el-card>
      <h3>修改个人资料</h3>
      <el-form :model="form" label-width="100px">
        <el-form-item label="真实姓名">
          <el-input v-model="form.realName" />
        </el-form-item>
        <el-form-item label="学号">
          <el-input v-model="form.studentId" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" type="email" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit" :loading="loading">保存修改</el-button>
          <el-button @click="goBack">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const form = ref({
  realName: '',
  studentId: '',
  email: '',
  phone: ''
})

const submit = async () => {
  loading.value = true
  try {
    const user = JSON.parse(localStorage.getItem('user') || '{}')
    const updatedUser = { ...user, ...form.value }
    localStorage.setItem('user', JSON.stringify(updatedUser))
    ElMessage.success('个人资料已更新')
    router.push('/student/profile')
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.push('/student/profile')
}

onMounted(() => {
  const user = localStorage.getItem('user')
  if (user) {
    const userData = JSON.parse(user)
    form.value = {
      realName: userData.realName || '',
      studentId: userData.studentId || '',
      email: userData.email || '',
      phone: userData.phone || ''
    }
  }
})
</script>

<style scoped>
.edit-profile {
  padding: 20px;
}

h3 {
  margin-bottom: 20px;
  color: #303133;
}
</style>