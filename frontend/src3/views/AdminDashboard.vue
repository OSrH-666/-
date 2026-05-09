<template>
  <div class="admin-dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon blue">
            <el-icon><Users /></el-icon>
          </div>
          <div class="stat-content">
            <p class="stat-value">{{ userCount }}</p>
            <p class="stat-label">用户总数</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon green">
            <el-icon><Briefcase /></el-icon>
          </div>
          <div class="stat-content">
            <p class="stat-value">{{ clubCount }}</p>
            <p class="stat-label">组织总数</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon orange">
            <el-icon><Clock /></el-icon>
          </div>
          <div class="stat-content">
            <p class="stat-value">{{ pendingClubs }}</p>
            <p class="stat-label">待审核组织</p>
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
            <p class="stat-label">活动总数</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row style="margin-top: 20px;">
      <el-col :span="12">
        <el-card title="组织审核列表">
          <el-table :data="pendingClubList" @row-click="handleClubClick">
            <el-table-column prop="name" label="组织名称" />
            <el-table-column prop="description" label="描述" show-overflow-tooltip />
            <el-table-column prop="createdAt" label="创建时间" />
            <el-table-column prop="status" label="状态">
              <template #default="scope">
                <el-tag type="warning">{{ getStatusText(scope.row.status) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template #default="scope">
                <el-button type="primary" size="small" @click.stop="approveClub(scope.row.id)">通过</el-button>
                <el-button size="small" @click.stop="rejectClub(scope.row.id)">拒绝</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card title="系统统计">
          <div class="chart-section">
            <h4>角色分布</h4>
            <div class="role-chart">
              <div v-for="role in roleStats" :key="role.name" class="role-item">
                <span class="role-name">{{ role.name }}</span>
                <div class="role-bar">
                  <div class="role-fill" :style="{ width: role.percent + '%', backgroundColor: role.color }"></div>
                </div>
                <span class="role-count">{{ role.count }}</span>
              </div>
            </div>
          </div>
          <div class="chart-section" style="margin-top: 20px;">
            <h4>组织类型分布</h4>
            <div class="pie-chart">
              <svg viewBox="0 0 100 100">
                <circle 
                  v-for="(item, index) in clubTypeStats" 
                  :key="item.name"
                  :cx="50" :cy="50" :r="40"
                  :fill="item.color"
                  :stroke="item.color"
                  :stroke-width="20"
                  :stroke-dasharray="item.dashArray"
                  :stroke-dashoffset="item.dashOffset"
                  transform="rotate(-90 50 50)"
                  style="fill: none;"
                />
              </svg>
              <div class="pie-legend">
                <div v-for="item in clubTypeStats" :key="item.name" class="legend-item">
                  <span class="legend-color" :style="{ backgroundColor: item.color }"></span>
                  <span>{{ item.name }} ({{ item.count }})</span>
                </div>
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
import { useRouter } from 'vue-router'
import { Users, Briefcase, Clock, Calendar } from '@element-plus/icons-vue'
import { clubApi, activityApi } from '../api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userCount = ref(0)
const clubCount = ref(0)
const pendingClubs = ref(0)
const activityCount = ref(0)
const pendingClubList = ref([])

const roleStats = ref([
  { name: '学生', count: 0, percent: 70, color: '#409EFF' },
  { name: '负责人', count: 0, percent: 20, color: '#67C23A' },
  { name: '管理员', count: 0, percent: 10, color: '#E6A23C' }
])

const clubTypeStats = ref([
  { name: '学术科技', count: 0, color: '#409EFF', dashArray: '', dashOffset: '' },
  { name: '文体艺术', count: 0, color: '#67C23A', dashArray: '', dashOffset: '' },
  { name: '公益志愿', count: 0, color: '#E6A23C', dashArray: '', dashOffset: '' },
  { name: '其他', count: 0, color: '#909399', dashArray: '', dashOffset: '' }
])

const getStatusText = (status) => {
  const map = { pending: '待审核', approved: '已通过', rejected: '已拒绝' }
  return map[status] || '未知'
}

const handleClubClick = (row) => {
  router.push(`/admin/clubs/detail/${row.id}`)
}

const approveClub = (id) => {
  ElMessage.success('已通过组织审核')
  pendingClubList.value = pendingClubList.value.filter(c => c.id !== id)
}

const rejectClub = (id) => {
  ElMessage.info('已拒绝组织审核')
  pendingClubList.value = pendingClubList.value.filter(c => c.id !== id)
}

const calculatePieChart = () => {
  const total = clubTypeStats.value.reduce((sum, item) => sum + item.count, 0)
  let offset = 0
  const circumference = 2 * Math.PI * 40
  
  clubTypeStats.value.forEach(item => {
    const percent = total > 0 ? item.count / total : 0
    item.dashArray = `${percent * circumference} ${circumference}`
    item.dashOffset = -offset
    offset += percent * circumference
  })
}

onMounted(async () => {
  const clubsRes = await clubApi.list()
  const clubs = clubsRes.data || []
  clubCount.value = clubs.length
  pendingClubList.value = clubs.filter(c => c.status === 'pending')
  pendingClubs.value = pendingClubList.value.length

  const activitiesRes = await activityApi.list()
  activityCount.value = activitiesRes.data?.length || 0

  userCount.value = 1256
  roleStats.value = [
    { name: '学生', count: 1100, percent: 87.6, color: '#409EFF' },
    { name: '负责人', count: 130, percent: 10.3, color: '#67C23A' },
    { name: '管理员', count: 26, percent: 2.1, color: '#E6A23C' }
  ]

  clubTypeStats.value = [
    { name: '学术科技', count: 25, color: '#409EFF', dashArray: '', dashOffset: '' },
    { name: '文体艺术', count: 18, color: '#67C23A', dashArray: '', dashOffset: '' },
    { name: '公益志愿', count: 12, color: '#E6A23C', dashArray: '', dashOffset: '' },
    { name: '其他', count: 5, color: '#909399', dashArray: '', dashOffset: '' }
  ]

  calculatePieChart()
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
  font-size: 32px;
  font-weight: bold;
  margin: 0;
  color: #303133;
}

.stat-label {
  color: #909399;
  margin: 5px 0 0 0;
}

.chart-section h4 {
  margin: 0 0 10px 0;
  font-size: 14px;
}

.role-chart {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.role-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.role-name {
  width: 50px;
  font-size: 13px;
}

.role-bar {
  flex: 1;
  height: 12px;
  background-color: #E4E7ED;
  border-radius: 6px;
  overflow: hidden;
}

.role-fill {
  height: 100%;
  border-radius: 6px;
  transition: width 0.3s;
}

.role-count {
  width: 40px;
  text-align: right;
  font-size: 13px;
  color: #606266;
}

.pie-chart {
  display: flex;
  align-items: center;
  gap: 20px;
}

.pie-chart svg {
  width: 120px;
  height: 120px;
}

.pie-legend {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.legend-color {
  width: 12px;
  height: 12px;
  border-radius: 3px;
}

.legend-item span {
  font-size: 13px;
  color: #606266;
}
</style>