<template>
  <div class="admin-statistics">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon blue">
            <el-icon><Users /></el-icon>
          </div>
          <div class="stat-content">
            <p class="stat-value">{{ userStats.total }}</p>
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
            <p class="stat-value">{{ clubStats.total }}</p>
            <p class="stat-label">组织总数</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon orange">
            <el-icon><Calendar /></el-icon>
          </div>
          <div class="stat-content">
            <p class="stat-value">{{ activityStats.total }}</p>
            <p class="stat-label">活动总数</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon purple">
            <el-icon><List /></el-icon>
          </div>
          <div class="stat-content">
            <p class="stat-value">{{ registrationStats.total }}</p>
            <p class="stat-label">报名总数</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row style="margin-top: 20px;">
      <el-col :span="12">
        <el-card title="用户增长趋势">
          <div class="chart-container">
            <svg viewBox="0 0 400 200" class="line-chart">
              <path 
                d="M 20 180 L 60 150 L 100 160 L 140 130 L 180 145 L 220 110 L 260 125 L 300 90 L 340 100 L 380 80" 
                fill="none" 
                stroke="#409EFF" 
                stroke-width="2"
              />
              <circle v-for="(point, index) in chartPoints" :key="index" :cx="point.x" :cy="point.y" r="4" fill="#409EFF" />
            </svg>
            <div class="chart-labels">
              <span>1月</span><span>2月</span><span>3月</span><span>4月</span><span>5月</span><span>6月</span><span>7月</span><span>8月</span><span>9月</span><span>10月</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card title="组织类型分布">
          <div class="pie-chart-container">
            <svg viewBox="0 0 200 200" class="pie-chart">
              <circle cx="100" cy="100" r="80" fill="#409EFF" stroke="#409EFF" stroke-width="40" stroke-dasharray="201 503" stroke-dashoffset="0" transform="rotate(-90 100 100)" style="fill: none;" />
              <circle cx="100" cy="100" r="80" fill="#67C23A" stroke="#67C23A" stroke-width="40" stroke-dasharray="151 503" stroke-dashoffset="-201" transform="rotate(-90 100 100)" style="fill: none;" />
              <circle cx="100" cy="100" r="80" fill="#E6A23C" stroke="#E6A23C" stroke-width="40" stroke-dasharray="101 503" stroke-dashoffset="-352" transform="rotate(-90 100 100)" style="fill: none;" />
              <circle cx="100" cy="100" r="80" fill="#909399" stroke="#909399" stroke-width="40" stroke-dasharray="50 503" stroke-dashoffset="-453" transform="rotate(-90 100 100)" style="fill: none;" />
            </svg>
            <div class="pie-legend">
              <div class="legend-item"><span class="legend-color" style="background-color: #409EFF;"></span>学术科技 (40%)</div>
              <div class="legend-item"><span class="legend-color" style="background-color: #67C23A;"></span>文体艺术 (30%)</div>
              <div class="legend-item"><span class="legend-color" style="background-color: #E6A23C;"></span>公益志愿 (20%)</div>
              <div class="legend-item"><span class="legend-color" style="background-color: #909399;"></span>其他 (10%)</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Users, Briefcase, Calendar, List } from '@element-plus/icons-vue'

const userStats = ref({ total: 1256, active: 1180, inactive: 76 })
const clubStats = ref({ total: 60, approved: 55, pending: 5 })
const activityStats = ref({ total: 234, active: 89, completed: 145 })
const registrationStats = ref({ total: 5678, checkedIn: 4890, waiting: 788 })

const chartPoints = [
  { x: 20, y: 180 }, { x: 60, y: 150 }, { x: 100, y: 160 }, 
  { x: 140, y: 130 }, { x: 180, y: 145 }, { x: 220, y: 110 },
  { x: 260, y: 125 }, { x: 300, y: 90 }, { x: 340, y: 100 },
  { x: 380, y: 80 }
]
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

.chart-container {
  padding: 20px;
}

.line-chart {
  width: 100%;
  height: 150px;
}

.chart-labels {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  font-size: 12px;
  color: #909399;
}

.pie-chart-container {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 20px;
}

.pie-chart {
  width: 150px;
  height: 150px;
}

.pie-legend {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
}

.legend-color {
  width: 12px;
  height: 12px;
  border-radius: 3px;
}
</style>