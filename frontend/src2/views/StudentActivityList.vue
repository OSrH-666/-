<template>
  <div class="student-activity-list">
    <el-card>
      <div class="filter-bar">
        <el-select v-model="statusFilter" placeholder="状态筛选">
          <el-option label="全部" value="" />
          <el-option label="进行中" value="active" />
          <el-option label="待审核" value="pending" />
          <el-option label="已结束" value="completed" />
        </el-select>
        <el-input v-model="searchKeyword" placeholder="搜索活动名称" class="search-input" />
        <el-button type="primary" @click="search">搜索</el-button>
      </div>
      <el-table :data="filteredActivities">
        <el-table-column prop="title" label="活动名称" />
        <el-table-column prop="clubId" label="所属组织">
          <template #default="scope">
            {{ getClubName(scope.row.clubId) }}
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" />
        <el-table-column prop="endTime" label="结束时间" />
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
            <el-button size="small" @click="goToDetail(scope.row.id)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { activityApi, clubApi } from '../api'

const router = useRouter()
const searchKeyword = ref('')
const statusFilter = ref('')
const activities = ref([])
const clubs = ref([])

const filteredActivities = computed(() => {
  let result = [...activities.value]
  
  if (statusFilter.value) {
    result = result.filter(a => a.status === statusFilter.value)
  }
  
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(a => 
      a.title.toLowerCase().includes(keyword) ||
      a.description.toLowerCase().includes(keyword)
    )
  }
  
  return result
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

const search = () => {}

const goToDetail = (id) => {
  router.push(`/student/activities/detail/${id}`)
}

onMounted(async () => {
  const activitiesRes = await activityApi.list()
  activities.value = activitiesRes.data || []
  
  const clubsRes = await clubApi.list()
  clubs.value = clubsRes.data || []
})
</script>

<style scoped>
.filter-bar {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  align-items: center;
}

.search-input {
  width: 250px;
}
</style>