<template>
  <div class="registration-list">
    <el-card>
      <h3>报名管理</h3>
      <el-table :data="registrations">
        <el-table-column prop="id" label="ID" />
        <el-table-column prop="activityId" label="活动ID" />
        <el-table-column prop="userId" label="用户ID" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="queuePosition" label="队列位置">
          <template #default="scope">
            {{ scope.row.queuePosition || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="registeredAt" label="报名时间" />
        <el-table-column prop="confirmedAt" label="确认时间" />
        <el-table-column label="操作">
          <template #default="scope">
            <el-button v-if="scope.row.status === 1" size="small" type="success" @click="checkIn(scope.row.id)">签到</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { registrationApi, checkInApi } from '../api'

const route = useRoute()
const registrations = ref([])

const getStatusText = (status) => {
  const map = { 0: '待确认', 1: '已确认', 2: '等待中', 3: '已取消' }
  return map[status] || '未知'
}

const getStatusType = (status) => {
  const map = { 0: 'warning', 1: 'success', 2: 'info', 3: 'danger' }
  return map[status] || ''
}

const checkIn = async (registrationId) => {
  await checkInApi.manualCheckIn(registrationId)
  loadData()
}

const loadData = async () => {
  const activityId = route.params.id
  const response = await registrationApi.listByActivity(activityId)
  registrations.value = response.data || []
}

onMounted(() => {
  loadData()
})
</script>