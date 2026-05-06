<template>
  <div class="student-registration-list">
    <el-card>
      <div class="filter-bar">
        <el-select v-model="statusFilter" placeholder="状态筛选">
          <el-option label="全部" value="" />
          <el-option label="已确认" value="approved" />
          <el-option label="等待中" value="pending" />
          <el-option label="已完成" value="completed" />
          <el-option label="已取消" value="cancelled" />
        </el-select>
      </div>
      <el-table :data="filteredRegistrations">
        <el-table-column prop="activityId" label="活动名称">
          <template #default="scope">
            {{ getActivityName(scope.row.activityId) }}
          </template>
        </el-table-column>
        <el-table-column prop="activityId" label="所属组织">
          <template #default="scope">
            {{ getClubName(scope.row.activityId) }}
          </template>
        </el-table-column>
        <el-table-column prop="registeredAt" label="报名时间" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="getRegistrationStatusType(scope.row.status)">
              {{ getRegistrationStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="queuePosition" label="队列位置">
          <template #default="scope">
            {{ scope.row.queuePosition || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button 
              v-if="scope.row.status === 'approved'" 
              size="small" 
              type="primary"
              @click="goToActivity(scope.row.activityId)"
            >
              查看详情
            </el-button>
            <el-button 
              v-if="scope.row.status !== 'completed' && scope.row.status !== 'cancelled'" 
              size="small" 
              type="danger"
              @click="cancelRegistration(scope.row.id)"
            >
              取消报名
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { registrationApi, activityApi, clubApi } from '../api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const statusFilter = ref('')
const registrations = ref([])
const activities = ref([])
const clubs = ref([])

const filteredRegistrations = computed(() => {
  let result = [...registrations.value]
  
  if (statusFilter.value) {
    result = result.filter(r => r.status === statusFilter.value)
  }
  
  return result
})

const getActivityName = (activityId) => {
  const activity = activities.value.find(a => a.id === activityId)
  return activity?.title || '未知活动'
}

const getClubName = (activityId) => {
  const activity = activities.value.find(a => a.id === activityId)
  if (activity) {
    const club = clubs.value.find(c => c.id === activity.clubId)
    return club?.name || '未知组织'
  }
  return '未知组织'
}

const getRegistrationStatusText = (status) => {
  const map = { approved: '已确认', pending: '等待中', completed: '已完成', cancelled: '已取消' }
  return map[status] || '未知'
}

const getRegistrationStatusType = (status) => {
  const map = { approved: 'success', pending: 'warning', completed: 'info', cancelled: 'danger' }
  return map[status] || ''
}

const goToActivity = (id) => {
  router.push(`/student/activities/detail/${id}`)
}

const cancelRegistration = async (id) => {
  await registrationApi.delete(id)
  registrations.value = registrations.value.filter(r => r.id !== id)
  ElMessage.info('已取消报名')
}

onMounted(async () => {
  const registrationsRes = await registrationApi.list()
  registrations.value = registrationsRes.data || []
  
  const activitiesRes = await activityApi.list()
  activities.value = activitiesRes.data || []
  
  const clubsRes = await clubApi.list()
  clubs.value = clubsRes.data || []
})
</script>

<style scoped>
.filter-bar {
  margin-bottom: 20px;
}
</style>