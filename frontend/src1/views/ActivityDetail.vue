<template>
  <div class="activity-detail">
    <el-card>
      <div style="display: flex; justify-content: space-between; margin-bottom: 20px;">
        <h3>活动详情</h3>
        <el-button @click="goBack">返回</el-button>
      </div>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="活动名称">{{ activity.name }}</el-descriptions-item>
        <el-descriptions-item label="所属组织">{{ activity.clubId }}</el-descriptions-item>
        <el-descriptions-item label="活动描述">{{ activity.description }}</el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ activity.startTime }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ activity.endTime }}</el-descriptions-item>
        <el-descriptions-item label="活动地点">{{ activity.location }}</el-descriptions-item>
        <el-descriptions-item label="报名限额">{{ activity.quota === 0 ? '不限' : activity.quota }}</el-descriptions-item>
        <el-descriptions-item label="当前状态">
          <el-tag :type="getStatusType(activity.status)">
            {{ getStatusText(activity.status) }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>
      <div style="margin-top: 20px;">
        <el-button v-if="activity.status === 1" type="primary" @click="register">立即报名</el-button>
        <el-button v-if="hasRegistered" type="danger" @click="cancel">取消报名</el-button>
      </div>
      <div style="margin-top: 20px;">
        <h4>报名列表</h4>
        <el-table :data="registrations">
          <el-table-column prop="userId" label="用户ID" />
          <el-table-column prop="status" label="状态">
            <template #default="scope">
              {{ getRegistrationStatus(scope.row.status) }}
            </template>
          </el-table-column>
          <el-table-column prop="queuePosition" label="队列位置">
            <template #default="scope">
              {{ scope.row.queuePosition || '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="registeredAt" label="报名时间" />
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { activityApi, registrationApi } from '../api'

const router = useRouter()
const route = useRoute()

const activity = ref({})
const registrations = ref([])
const hasRegistered = ref(false)

const getStatusText = (status) => {
  const map = { 0: '草稿', 1: '已发布', 2: '已结束' }
  return map[status] || '未知'
}

const getStatusType = (status) => {
  const map = { 0: 'info', 1: 'success', 2: 'warning' }
  return map[status] || ''
}

const getRegistrationStatus = (status) => {
  const map = { 0: '待确认', 1: '已确认', 2: '等待中', 3: '已取消' }
  return map[status] || '未知'
}

const goBack = () => {
  router.push('/activities')
}

const register = async () => {
  await registrationApi.register(route.params.id, 1)
  loadRegistrations()
}

const cancel = async () => {
  await registrationApi.cancel(route.params.id, 1)
  loadRegistrations()
}

const loadRegistrations = async () => {
  const response = await registrationApi.listByActivity(route.params.id)
  registrations.value = response.data || []
}

onMounted(async () => {
  const response = await activityApi.get(route.params.id)
  activity.value = response.data || {}
  await loadRegistrations()
})
</script>