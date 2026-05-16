<template>
  <div class="activity-detail">
    <el-card v-if="activity">
      <template #header>
        <div class="card-header">
          <span>活动详情</span>
          <el-button @click="goBack">返回</el-button>
        </div>
      </template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="活动名称">{{ activity.title }}</el-descriptions-item>
        <el-descriptions-item label="所属组织">{{ activity.clubName }}</el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ activity.startTime }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ activity.endTime }}</el-descriptions-item>
        <el-descriptions-item label="活动地点">{{ activity.location }}</el-descriptions-item>
        <el-descriptions-item label="报名限额">
          {{ activity.quota === 0 ? '不限' : activity.quota + '人' }}
        </el-descriptions-item>
        <el-descriptions-item label="报名人数">{{ activity.participants || 0 }}人</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(activity.status)">
            {{ getStatusText(activity.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="活动描述" :span="2">
          <div class="description-text">{{ activity.description }}</div>
        </el-descriptions-item>
      </el-descriptions>
      <div class="action-bar">
        <el-button type="primary" @click="goToEdit">编辑活动</el-button>
        <el-button type="success" @click="viewRegistrations">查看报名</el-button>
      </div>
    </el-card>
    <el-empty v-else description="未找到活动信息" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { activityApi, clubApi } from '../api'

const router = useRouter()
const route = useRoute()

const activity = ref(null)
const club = ref(null)

const getStatusText = (status) => {
  const map = { active: '进行中', pending: '待审核', completed: '已结束' }
  return map[status] || '未知'
}

const getStatusType = (status) => {
  const map = { active: 'success', pending: 'warning', completed: 'info' }
  return map[status] || ''
}

const goBack = () => {
  router.push('/leader/activities')
}

const goToEdit = () => {
  router.push(`/leader/activities/edit/${route.params.id}`)
}

const viewRegistrations = () => {
  router.push(`/leader/activities/registrations/${route.params.id}`)
}

onMounted(async () => {
  if (route.params.id) {
    const response = await activityApi.get(route.params.id)
    if (response.data) {
      activity.value = response.data
      const clubResponse = await clubApi.get(response.data.clubId)
      if (clubResponse.data) {
        activity.value.clubName = clubResponse.data.name
      }
    }
  }
})
</script>

<style scoped>
.activity-detail {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.description-text {
  white-space: pre-wrap;
  line-height: 1.6;
}

.action-bar {
  margin-top: 20px;
  display: flex;
  gap: 10px;
}
</style>