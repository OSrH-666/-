<template>
  <div class="leader-checkin">
    <el-card>
      <div class="filter-bar">
        <el-select v-model="activityFilter" placeholder="选择活动">
          <el-option label="全部活动" value="" />
          <el-option v-for="activity in activities" :key="activity.id" :label="activity.title" :value="activity.id" />
        </el-select>
        <el-select v-model="checkinType" placeholder="签到方式">
          <el-option label="全部" value="" />
          <el-option label="扫码签到" value="scan" />
          <el-option label="地理位置签到" value="location" />
          <el-option label="手动签到" value="manual" />
        </el-select>
      </div>
      <div class="qrcode-section">
        <el-card title="扫码签到">
          <div class="qrcode-container">
            <div class="qrcode">
              <el-icon class="qrcode-icon"><QrCode /></el-icon>
            </div>
            <p>活动签到二维码</p>
          </div>
          <el-button type="primary" @click="refreshQrcode">刷新二维码</el-button>
        </el-card>
      </div>
      <el-table :data="checkinRecords">
        <el-table-column prop="userName" label="姓名" />
        <el-table-column prop="studentId" label="学号" />
        <el-table-column prop="activityName" label="活动名称" />
        <el-table-column prop="checkinTime" label="签到时间" />
        <el-table-column prop="checkinType" label="签到方式">
          <template #default="scope">
            <el-tag :type="getCheckinTypeTag(scope.row.checkinType)">
              {{ getCheckinTypeText(scope.row.checkinType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button v-if="!scope.row.checkedIn" size="small" type="primary" @click="manualCheckin(scope.row.id)">
              手动签到
            </el-button>
            <el-tag v-else type="success">已签到</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { QrCode } from '@element-plus/icons-vue'
import { activityApi, registrationApi } from '../api'
import { ElMessage } from 'element-plus'

const activityFilter = ref('')
const checkinType = ref('')
const activities = ref([])
const checkinRecords = ref([
  { id: 1, userName: '李四', studentId: '202405567001', activityName: '编程大赛', checkinTime: '2024-04-01 09:30', checkinType: 'scan', checkedIn: true },
  { id: 2, userName: '王五', studentId: '202405567002', activityName: '编程大赛', checkinTime: '', checkinType: '', checkedIn: false },
  { id: 3, userName: '赵六', studentId: '202405567003', activityName: '编程大赛', checkinTime: '2024-04-01 09:35', checkinType: 'location', checkedIn: true },
  { id: 4, userName: '钱七', studentId: '202405567004', activityName: '编程大赛', checkinTime: '', checkinType: '', checkedIn: false }
])

const getCheckinTypeText = (type) => {
  const map = { scan: '扫码签到', location: '地理位置签到', manual: '手动签到' }
  return map[type] || '未签到'
}

const getCheckinTypeTag = (type) => {
  const map = { scan: 'success', location: 'info', manual: 'warning' }
  return map[type] || ''
}

const refreshQrcode = () => {
  ElMessage.success('二维码已刷新')
}

const manualCheckin = (id) => {
  const record = checkinRecords.value.find(r => r.id === id)
  if (record) {
    record.checkedIn = true
    record.checkinType = 'manual'
    record.checkinTime = new Date().toLocaleString('zh-CN')
    ElMessage.success('签到成功')
  }
}

onMounted(async () => {
  const response = await activityApi.list({ clubId: 1 })
  activities.value = response.data || []
})
</script>

<style scoped>
.filter-bar {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.qrcode-section {
  margin-bottom: 20px;
}

.qrcode-container {
  text-align: center;
  margin-bottom: 15px;
}

.qrcode {
  width: 200px;
  height: 200px;
  background-color: white;
  border: 1px solid #EBEEF5;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
}

.qrcode-icon {
  font-size: 120px;
  color: #409EFF;
}

.qrcode-container p {
  margin-top: 10px;
  color: #606266;
}
</style>