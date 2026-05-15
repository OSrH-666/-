<template>
  <div class="student-club-detail">
    <el-card>
      <div class="header">
        <el-button @click="goBack">返回</el-button>
      </div>
      <div class="club-header">
        <div class="club-icon-large">
          <el-icon><Briefcase /></el-icon>
        </div>
        <div class="club-title">
          <h2>{{ club.name }}</h2>
          <el-tag :type="getStatusType(club.status)">
            {{ getStatusText(club.status) }}
          </el-tag>
        </div>
      </div>
      <el-descriptions :column="2" border class="club-info">
        <el-descriptions-item label="组织描述">{{ club.description }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ club.createdAt }}</el-descriptions-item>
        <el-descriptions-item label="成员数量">{{ memberCount }}</el-descriptions-item>
        <el-descriptions-item label="组织类型">{{ club.type || '综合' }}</el-descriptions-item>
      </el-descriptions>
      <div class="action-bar">
        <el-button v-if="!isJoined" type="primary" @click="applyJoin">申请加入</el-button>
        <el-tag v-else type="success">已加入该组织</el-tag>
      </div>
      <el-card title="组织活动" class="activity-section">
        <el-table :data="clubActivities">
          <el-table-column prop="title" label="活动名称" />
          <el-table-column prop="startTime" label="活动时间" />
          <el-table-column prop="status" label="状态">
            <template #default="scope">
              <el-tag :type="getActivityStatusType(scope.row.status)">
                {{ getActivityStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button size="small" @click="goToActivity(scope.row.id)">查看详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Briefcase } from '@element-plus/icons-vue'
import { clubApi, activityApi } from '../api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const club = ref({})
const memberCount = ref(0)
const isJoined = ref(false)
const clubActivities = ref([])

const getStatusText = (status) => {
  const map = { approved: '已通过', pending: '审核中', rejected: '已拒绝' }
  return map[status] || '未知'
}

const getStatusType = (status) => {
  const map = { approved: 'success', pending: 'warning', rejected: 'danger' }
  return map[status] || ''
}

const getActivityStatusText = (status) => {
  const map = { active: '进行中', pending: '待审核', completed: '已结束' }
  return map[status] || '未知'
}

const getActivityStatusType = (status) => {
  const map = { active: 'success', pending: 'warning', completed: 'info' }
  return map[status] || ''
}

const goBack = () => {
  router.push('/student/clubs')
}

const applyJoin = () => {
  ElMessage.success('申请已提交，请等待审核')
  isJoined.value = true
}

const goToActivity = (id) => {
  router.push(`/student/activities/detail/${id}`)
}

onMounted(async () => {
  const clubRes = await clubApi.get(route.params.id)
  club.value = clubRes.data || {}
  
  const activitiesRes = await activityApi.list({ clubId: route.params.id })
  clubActivities.value = activitiesRes.data || []
  
  memberCount.value = Math.floor(Math.random() * 100) + 50
  isJoined.value = [1, 2].includes(parseInt(route.params.id))
})
</script>

<style scoped>
.header {
  margin-bottom: 20px;
}

.club-header {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 20px;
}

.club-icon-large {
  font-size: 80px;
  color: #409EFF;
}

.club-title h2 {
  margin: 0 0 10px 0;
}

.club-info {
  margin-bottom: 20px;
}

.action-bar {
  margin-bottom: 20px;
}

.activity-section {
  margin-top: 20px;
}
</style>