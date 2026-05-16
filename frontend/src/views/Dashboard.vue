<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon">
            <el-icon><User /></el-icon>
          </div>
          <div class="stat-content">
            <p class="stat-value">{{ clubCount }}</p>
            <p class="stat-label">组织数量</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon">
            <el-icon><Calendar /></el-icon>
          </div>
          <div class="stat-content">
            <p class="stat-value">{{ activityCount }}</p>
            <p class="stat-label">活动数量</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon">
            <el-icon><List /></el-icon>
          </div>
          <div class="stat-content">
            <p class="stat-value">{{ registrationCount }}</p>
            <p class="stat-label">报名人数</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon">
            <el-icon><Files /></el-icon>
          </div>
          <div class="stat-content">
            <p class="stat-value">{{ summaryCount }}</p>
            <p class="stat-label">活动总结</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row style="margin-top: 20px;">
      <el-col :span="12">
        <el-card title="最近活动">
          <el-table :data="recentActivities">
            <el-table-column prop="name" label="活动名称" />
            <el-table-column prop="startTime" label="开始时间" />
            <el-table-column prop="status" label="状态">
              <template #default="scope">
                <el-tag :type="getActivityStatusType(scope.row.status)">
                  {{ getActivityStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card title="待审核组织">
          <el-table :data="pendingClubs">
            <el-table-column prop="name" label="组织名称" />
            <el-table-column prop="type" label="类型">
              <template #default="scope">
                {{ getClubTypeText(scope.row.type) }}
              </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="创建时间" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { User, Calendar, List, Files } from '@element-plus/icons-vue'
import { clubApi, activityApi } from '../api'

const clubCount = ref(0)
const activityCount = ref(0)
const registrationCount = ref(0)
const summaryCount = ref(0)
const recentActivities = ref([])
const pendingClubs = ref([])

const getActivityStatusText = (status) => {
  const map = { 
    'active': '进行中', 
    'pending': '待审核', 
    'completed': '已结束',
    0: '草稿',
    1: '进行中',
    2: '已结束'
  }
  return map[status] || status
}

const getActivityStatusType = (status) => {
  const map = { 
    'active': 'success', 
    'pending': 'warning', 
    'completed': 'info',
    0: 'info',
    1: 'success',
    2: 'info'
  }
  return map[status] || ''
}

const getClubTypeText = (type) => {
  const map = { 
    'academic': '学术科技', 
    'art': '文体艺术', 
    'volunteer': '公益志愿', 
    'other': '其他',
    0: '学术科技',
    1: '文体艺术',
    2: '公益志愿',
    3: '其他'
  }
  return map[type] || type
}

onMounted(async () => {
  const clubs = await clubApi.list()
  clubCount.value = clubs.data?.length || 0
  pendingClubs.value = clubs.data?.filter(c => c.status === 0 || c.status === 'pending') || []

  const activities = await activityApi.list()
  activityCount.value = activities.data?.length || 0
  recentActivities.value = activities.data?.slice(0, 5) || []
})
</script>

<style>
.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
}

.stat-icon {
  font-size: 48px;
  color: #409EFF;
  margin-right: 20px;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  margin: 0;
}

.stat-label {
  color: #999;
  margin: 0;
}
</style>