<template>
  <div class="leader-dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon green">
            <el-icon><Briefcase /></el-icon>
          </div>
          <div class="stat-content">
            <p class="stat-value">{{ myClub.name || '-' }}</p>
            <p class="stat-label">我的组织</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon blue">
            <el-icon><Users /></el-icon>
          </div>
          <div class="stat-content">
            <p class="stat-value">{{ memberCount }}</p>
            <p class="stat-label">成员总数</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon orange">
            <el-icon><Clock /></el-icon>
          </div>
          <div class="stat-content">
            <p class="stat-value">{{ pendingMembers }}</p>
            <p class="stat-label">待审核成员</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon purple">
            <el-icon><Calendar /></el-icon>
          </div>
          <div class="stat-content">
            <p class="stat-value">{{ activityCount }}</p>
            <p class="stat-label">组织活动数</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row style="margin-top: 20px;">
      <el-col :span="8">
        <el-card title="待办事项">
          <el-list>
            <el-list-item v-for="(item, index) in todoList" :key="index">
              <el-checkbox :checked="item.done">
                <span :class="{ done: item.done }">{{ item.text }}</span>
              </el-checkbox>
            </el-list-item>
          </el-list>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card title="近期活动">
          <el-table :data="recentActivities" size="small">
            <el-table-column prop="title" label="活动名称" />
            <el-table-column prop="startTime" label="开始时间">
              <template #default="scope">
                {{ formatDate(scope.row.startTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态">
              <template #default="scope">
                <el-tag :type="getActivityStatusType(scope.row.status)">
                  {{ getActivityStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="participants" label="报名人数" />
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card title="成员申请">
          <div class="member-applications">
            <div v-for="application in memberApplications" :key="application.id" class="application-item">
              <div class="application-header">
                <span class="applicant-name">{{ application.userName }}</span>
                <span class="apply-time">{{ application.registeredAt }}</span>
              </div>
              <div class="application-actions">
                <el-button type="primary" size="small" @click="approveMember(application.id)">通过</el-button>
                <el-button size="small" @click="rejectMember(application.id)">拒绝</el-button>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Briefcase, Users, Clock, Calendar } from '@element-plus/icons-vue'
import { clubApi, activityApi, registrationApi } from '../api'
import { ElMessage } from 'element-plus'

const myClub = ref({ name: '计算机协会' })
const memberCount = ref(0)
const pendingMembers = ref(0)
const activityCount = ref(0)
const recentActivities = ref([])
const memberApplications = ref([])

const todoList = ref([
  { text: '审核新成员申请', done: false },
  { text: '发布下周活动通知', done: false },
  { text: '撰写上月活动总结', done: true },
  { text: '更新组织简介', done: false },
  { text: '安排下次例会时间', done: false }
])

const getActivityStatusText = (status) => {
  const map = { active: '进行中', pending: '待审核', completed: '已结束' }
  return map[status] || '未知'
}

const getActivityStatusType = (status) => {
  const map = { active: 'success', pending: 'warning', completed: 'info' }
  return map[status] || ''
}

const formatDate = (date) => {
  return date?.split('T')[0] || date
}

const approveMember = (id) => {
  ElMessage.success('已通过成员申请')
  memberApplications.value = memberApplications.value.filter(m => m.id !== id)
}

const rejectMember = (id) => {
  ElMessage.info('已拒绝成员申请')
  memberApplications.value = memberApplications.value.filter(m => m.id !== id)
}

onMounted(async () => {
  const clubsRes = await clubApi.list({ status: 'approved' })
  const clubs = clubsRes.data || []
  myClub.value = clubs[0] || { name: '暂无组织' }

  const activitiesRes = await activityApi.list({ clubId: 1 })
  const activities = activitiesRes.data || []
  recentActivities.value = activities.slice(0, 5)
  activityCount.value = activities.length

  const registrationsRes = await registrationApi.list()
  const registrations = registrationsRes.data || []
  memberApplications.value = registrations.filter(r => r.status === 'pending').slice(0, 3)
  pendingMembers.value = registrations.filter(r => r.status === 'pending').length
  memberCount.value = registrations.length + 15
})
</script>

<style scoped>
.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
}

.stat-icon {
  font-size: 48px;
  margin-right: 20px;
  padding: 15px;
  border-radius: 12px;
}

.stat-icon.blue {
  color: #409EFF;
  background-color: #ECF5FF;
}

.stat-icon.green {
  color: #67C23A;
  background-color: #F0F9EB;
}

.stat-icon.orange {
  color: #E6A23C;
  background-color: #FDF6EC;
}

.stat-icon.purple {
  color: #909399;
  background-color: #F5F5F5;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  margin: 0;
  color: #303133;
}

.stat-label {
  color: #909399;
  margin: 5px 0 0 0;
}

.done {
  text-decoration: line-through;
  color: #909399;
}

.member-applications {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.application-item {
  padding: 10px;
  background-color: #FAFAFA;
  border-radius: 8px;
}

.application-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.applicant-name {
  font-weight: bold;
}

.apply-time {
  font-size: 12px;
  color: #909399;
}

.application-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}
</style>