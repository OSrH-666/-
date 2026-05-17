<template>
  <div class="user-form">
    <el-card>
      <h3>{{ isEdit ? '编辑用户' : '添加用户' }}</h3>
      <el-form :model="form" label-width="100px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="form.realName" />
        </el-form-item>
        <el-form-item label="学号">
          <el-input v-model="form.studentId" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role" placeholder="请选择角色">
            <el-option label="学生" value="student" />
            <el-option label="负责人" value="leader" />
            <el-option label="管理员" value="admin" />
          </el-select>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" type="email" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item v-if="!isEdit" label="初始密码">
          <el-input v-model="form.password" type="password" placeholder="不填则默认为123456" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit" :loading="loading">保存</el-button>
          <el-button @click="goBack">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const isEdit = ref(false)
const loading = ref(false)
const form = ref({
  username: '',
  realName: '',
  studentId: '',
  role: 'student',
  email: '',
  phone: '',
  password: ''
})

const submit = async () => {
  if (!form.value.username) {
    ElMessage.warning('请输入用户名')
    return
  }
  if (!form.value.realName) {
    ElMessage.warning('请输入真实姓名')
    return
  }

  loading.value = true
  try {
    if (isEdit.value) {
      ElMessage.success('用户信息已更新')
    } else {
      ElMessage.success('用户添加成功')
    }
    router.push('/admin/users')
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.push('/admin/users')
}

onMounted(() => {
  if (route.params.id) {
    isEdit.value = true
    form.value = {
      username: 'existing_user',
      realName: '现有用户',
      studentId: '202405567000',
      role: 'student',
      email: 'user@example.com',
      phone: '13800138000',
      password: ''
    }
  }
})
</script>

<style scoped>
.user-form {
  padding: 20px;
}

h3 {
  margin-bottom: 20px;
  color: #303133;
}
</style>