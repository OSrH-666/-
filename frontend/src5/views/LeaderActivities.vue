<template>
  <div class="leader-activities">
    <el-card>
      <div class="action-bar">
        <el-button type="primary" @click="goToAdd">发布活动</el-button>
      </div>
      <el-table :data="activities">
        <el-table-column prop="title" label="活动名称" />
        <el-table-column prop="startTime" label="开始时间" />
        <el-table-column prop="endTime" label="结束时间" />
        <el-table-column prop="location" label="活动地点" />
        <el-table-column prop="quota" label="报名限额">
          <template #default="scope">
            {{ scope.row.quota === 0 ? '不限' : scope.row.quota + '人' }}
          </template>
        </el-table-column>
        <el-table-column prop="participants" label="报名人数" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="getActivityStatusType(scope.row.status)">
              {{ getActivityStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="small" @click="goToDetail(scope.row.id)">查看</el-button>
            <el-button size="small" @click="goToEdit(scope.row.id)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteActivity(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { activityApi } from '../api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const activities = ref([])

const getActivityStatusText = (status) => {
  const map = { active: '进行中', pending: '待审核', completed: '已结束' }
  return map[status] || '未知'
}

const getActivityStatusType = (status) => {
  const map = { active: 'success', pending: 'warning', completed: 'info' }
  return map[status] || ''
}

const goToAdd = () => {
  router.push('/leader/activities/add')
}

const goToDetail = (id) => {
  router.push(`/leader/activities/detail/${id}`)
}

const goToEdit = (id) => {
  router.push(`/leader/activities/edit/${id}`)
}

const deleteActivity = (id) => {
  activities.value = activities.value.filter(a => a.id !== id)
  ElMessage.success('活动已删除')
}

onMounted(async () => {
  const response = await activityApi.list({ clubId: 1 })
  activities.value = response.data || []
})
</script>

<style scoped>
.action-bar {
  margin-bottom: 20px;
}
</style>