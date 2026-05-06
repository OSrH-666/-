<template>
  <div class="student-profile">
    <el-card>
      <div class="profile-header">
        <div class="avatar">
          <el-icon class="avatar-icon"><User /></el-icon>
        </div>
        <div class="profile-info">
          <h2>{{ userInfo.realName || userInfo.username }}</h2>
          <p>{{ userInfo.email || '暂无邮箱' }}</p>
        </div>
      </div>
      <el-descriptions :column="2" border class="profile-details">
        <el-descriptions-item label="用户名">{{ userInfo.username }}</el-descriptions-item>
        <el-descriptions-item label="角色">{{ getRoleText(userInfo.role) }}</el-descriptions-item>
        <el-descriptions-item label="学号">{{ userInfo.studentId || '未填写' }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ userInfo.phone || '未填写' }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ userInfo.email || '未填写' }}</el-descriptions-item>
        <el-descriptions-item label="注册时间">{{ userInfo.createdAt || '未知' }}</el-descriptions-item>
      </el-descriptions>
      <el-card title="我的组织" class="joined-clubs">
        <el-table :data="joinedClubs">
          <el-table-column prop="name" label="组织名称" />
          <el-table-column prop="role" label="角色">
            <template #default="scope">
              <el-tag type="info">{{ scope.row.role || '成员' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button size="small" @click="goToClub(scope.row.id)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
      <div class="action-bar">
        <el-button type="primary" @click="editProfile">修改资料</el-button>
        <el-button @click="changePassword">修改密码</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { User } from '@element-plus/icons-vue'
import { clubApi } from '../api'

const router = useRouter()
const userInfo = ref({})
const joinedClubs = ref([])

const getRoleText = (role) => {
  const map = { student: '学生', leader: '负责人', admin: '管理员' }
  return map[role] || '未知'
}

const goToClub = (id) => {
  router.push(`/student/clubs/detail/${id}`)
}

const editProfile = () => {
  router.push('/student/profile/edit')
}

const changePassword = () => {
  router.push('/student/profile/change-password')
}

onMounted(() => {
  const user = localStorage.getItem('user')
  if (user) {
    userInfo.value = JSON.parse(user)
  }
  
  joinedClubs.value = [
    { id: 1, name: '计算机协会', role: '成员' },
    { id: 2, name: '篮球俱乐部', role: '成员' }
  ]
})
</script>

<style scoped>
.profile-header {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background-color: #409EFF;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-icon {
  font-size: 48px;
  color: white;
}

.profile-info h2 {
  margin: 0 0 5px 0;
}

.profile-info p {
  margin: 0;
  color: #606266;
}

.profile-details {
  margin-bottom: 20px;
}

.joined-clubs {
  margin-bottom: 20px;
}

.action-bar {
  display: flex;
  gap: 10px;
  justify-content: center;
}
</style>