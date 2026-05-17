<template>
  <div class="admin-users">
    <el-card>
      <div class="action-bar">
        <el-input v-model="searchKeyword" placeholder="搜索用户名或姓名" class="search-input" />
        <el-select v-model="roleFilter" placeholder="角色筛选">
          <el-option label="全部" value="" />
          <el-option label="学生" value="student" />
          <el-option label="负责人" value="leader" />
          <el-option label="管理员" value="admin" />
        </el-select>
        <el-button type="primary" @click="search">搜索</el-button>
        <el-button type="success" @click="goToAdd">添加用户</el-button>
      </div>
      <el-table :data="filteredUsers">
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="realName" label="真实姓名" />
        <el-table-column prop="studentId" label="学号" />
        <el-table-column prop="role" label="角色">
          <template #default="scope">
            <el-tag :type="getRoleTagType(scope.row.role)">
              {{ getRoleText(scope.row.role) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-switch 
              :value="scope.row.status === 'active'" 
              @change="toggleStatus(scope.row.id, $event)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="small" @click="editUser(scope.row.id)">编辑</el-button>
            <el-button size="small" @click="resetPassword(scope.row.id)">重置密码</el-button>
            <el-button size="small" type="danger" @click="deleteUser(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const searchKeyword = ref('')
const roleFilter = ref('')
const users = ref([
  { id: 1, username: 'student', realName: '学生用户', studentId: '202405567014', role: 'student', email: 'student@example.com', phone: '13800138001', status: 'active' },
  { id: 2, username: 'leader', realName: '负责人用户', studentId: '202405567034', role: 'leader', email: 'leader@example.com', phone: '13800138002', status: 'active' },
  { id: 3, username: 'admin', realName: '管理员用户', studentId: '202405567008', role: 'admin', email: 'admin@example.com', phone: '13800138003', status: 'active' },
  { id: 4, username: 'test1', realName: '测试用户1', studentId: '202405567007', role: 'student', email: 'test1@example.com', phone: '13800138004', status: 'inactive' }
])

const filteredUsers = computed(() => {
  let result = [...users.value]
  
  if (roleFilter.value) {
    result = result.filter(u => u.role === roleFilter.value)
  }
  
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(u => 
      u.username.toLowerCase().includes(keyword) ||
      u.realName.toLowerCase().includes(keyword)
    )
  }
  
  return result
})

const getRoleText = (role) => {
  const map = { student: '学生', leader: '负责人', admin: '管理员' }
  return map[role] || '未知'
}

const getRoleTagType = (role) => {
  const map = { student: 'info', leader: 'success', admin: 'warning' }
  return map[role] || ''
}

const search = () => {}

const goToAdd = () => {
  router.push('/admin/users/add')
}

const editUser = (id) => {
  router.push(`/admin/users/edit/${id}`)
}

const resetPassword = (id) => {
  ElMessage.success('密码已重置为默认值')
}

const deleteUser = (id) => {
  users.value = users.value.filter(u => u.id !== id)
  ElMessage.success('用户已删除')
}

const toggleStatus = (id, value) => {
  const user = users.value.find(u => u.id === id)
  if (user) {
    user.status = value ? 'active' : 'inactive'
    ElMessage.success(value ? '用户已启用' : '用户已禁用')
  }
}
</script>

<style scoped>
.action-bar {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  align-items: center;
}

.search-input {
  width: 250px;
}
</style>
