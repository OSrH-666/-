<template>
  <div class="change-password">
    <el-card>
      <h3>修改密码</h3>
      <el-form :model="form" label-width="100px">
        <el-form-item label="原密码">
          <el-input v-model="form.oldPassword" type="password" />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="form.newPassword" type="password" />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="form.confirmPassword" type="password" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit" :loading="loading">修改密码</el-button>
          <el-button @click="goBack">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const form = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const submit = async () => {
  if (!form.value.oldPassword) {
    ElMessage.warning('请输入原密码')
    return
  }
  if (!form.value.newPassword) {
    ElMessage.warning('请输入新密码')
    return
  }
  if (form.value.newPassword !== form.value.confirmPassword) {
    ElMessage.warning('两次输入的密码不一致')
    return
  }
  if (form.value.newPassword.length < 6) {
    ElMessage.warning('密码长度不能少于6位')
    return
  }

  loading.value = true
  try {
    ElMessage.success('密码修改成功，请重新登录')
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    router.push('/login')
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.push('/student/profile')
}
</script>

<style scoped>
.change-password {
  padding: 20px;
}

h3 {
  margin-bottom: 20px;
  color: #303133;
}
</style>