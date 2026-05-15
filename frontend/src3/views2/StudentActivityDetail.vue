<template>
  <div class="student-activity-detail">
    <el-card>
      <div class="header">
        <el-button @click="goBack">返回</el-button>
      </div>
      <div class="activity-header">
        <h2>{{ activity.title }}</h2>
        <el-tag :type="getActivityStatusType(activity.status)">
          {{ getActivityStatusText(activity.status) }}
        </el-tag>
      </div>
      <el-descriptions :column="2" border class="activity-info">
        <el-descriptions-item label="所属组织">{{ getClubName(activity.clubId) }}</el-descriptions-item>
        <el-descriptions-item label="活动描述">{{ activity.description }}</el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ activity.startTime }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ activity.endTime }}</el-descriptions-item>
        <el-descriptions-item label="活动地点">{{ activity.location || '未指定' }}</el-descriptions-item>
        <el-descriptions-item label="报名限额">
          {{ activity.quota === 0 ? '不限' : activity.quota + '人' }}
        </el-descriptions-item>
        <el-descriptions-item label="已报名人数">{{ activity.participants }}人</el-descriptions-item>
        <el-descriptions-item label="剩余名额">
          <span :class="{ 'low': remainingQuota < 10 }">{{ remainingQuota }}人</span>
        </el-descriptions-item>
      </el-descriptions>
      <div class="action-bar">
        <el-button 
          v-if="canRegister" 
          type="primary" 
          @click="handleRegister"
          :disabled="activity.status !== 'active'"
        >
          {{ registrationStatus === 'waiting' ? '已在等待队列' : '立即报名' }}
        </el-button>
        <el-button 
          v-if="hasRegistered && registrationStatus !== 'completed'" 
          type="danger" 
          @click="handleCancel"
        >
          取消报名
        </el-button>
        <el-tag v-if="registrationStatus === 'completed'" type="success">已完成活动</el-tag>
      </div>
      <el-card title="报名列表" class="registration-section">
        <el-table :data="registrations">
          <el-table-column prop="userName" label="报名人" />
          <el-table-column prop="status" label="状态">
            <template #default="scope">
              <el-tag :type="getRegistrationStatusType(scope.row.status)">
                {{ getRegistrationStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="registeredAt" label="报名时间" />
        </el-table>
      </el-card>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { activityApi, clubApi, registrationApi } from '../api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const activity = ref({})
const clubs = ref([])
const registrations = ref([])
const registrationStatus = ref('')

const remainingQuota = computed(() => {
  if (activity.value.quota === 0) return '不限'
  return Math.max(0, activity.value.quota - (activity.value.participants || 0))
})

const canRegister = computed(() => {
  return activity.value.status === 'active' && 
         registrationStatus.value === '' &&
         remainingQuota.value !== 0
})

const hasRegistered = computed(() => {
  return registrationStatus.value !== ''
})

const getActivityStatusText = (status) => {
  const map = { active: '进行中', pending: '待审核', completed: '已结束' }
  return map[status] || '未知'
}

const getActivityStatusType = (status) => {
  const map = { active: 'success', pending: 'warning', completed: 'info' }
  return map[status] || ''
}

const getClubName = (clubId) => {
  const club = clubs.value.find(c => c.id === clubId)
  return club?.name || '未知组织'
}

const getRegistrationStatusText = (status) => {
  const map = { approved: '已确认', pending: '等待中', completed: '已完成', cancelled: '已取消' }
  return map[status] || '未知'
}

const getRegistrationStatusType = (status) => {
  const map = { approved: 'success', pending: 'warning', completed: 'info', cancelled: 'danger' }
  return map[status] || ''
}

const goBack = () => {
  router.push('/student/activities')
}

const handleRegister = async () => {
  await registrationApi.create({ activityId: route.params.id, userId: 1 })
  registrationStatus.value = 'pending'
  ElMessage.success('报名成功')
  loadRegistrations()
}

const handleCancel = async () => {
  await registrationApi.delete(1)
  registrationStatus.value = ''
  ElMessage.info('已取消报名')
  loadRegistrations()
}

const loadRegistrations = async () => {
  const response = await registrationApi.list({ activityId: route.params.id })
  registrations.value = response.data || []
}

onMounted(async () => {
  const activityRes = await activityApi.get(route.params.id)
  activity.value = activityRes.data || {}
  
  const clubsRes = await clubApi.list()
  clubs.value = clubsRes.data || []
  
  await loadRegistrations()
  
  registrationStatus.value = registrations.value.find(r => r.userId === 1)?.status || ''
})
</script>

<style scoped>
.header {
  margin-bottom: 20px;
}

.activity-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.activity-header h2 {
  margin: 0;
}

.activity-info {
  margin-bottom: 20px;
}

.low {
  color: #E6A23C;
  font-weight: bold;
}

.action-bar {
  margin-bottom: 20px;
}

.registration-section {
  margin-top: 20px;
}
</style>