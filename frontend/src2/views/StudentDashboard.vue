<template>
  <div class="student-dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon blue">
            <el-icon><Briefcase /></el-icon>
          </div>
          <div class="stat-content">
            <p class="stat-value">{{ joinedClubsCount }}</p>
            <p class="stat-label">已加入组织</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon green">
            <el-icon><Calendar /></el-icon>
          </div>
          <div class="stat-content">
            <p class="stat-value">{{ upcomingActivities }}</p>
            <p class="stat-label">即将参与活动</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon orange">
            <el-icon><CheckSquare /></el-icon>
          </div>
          <div class="stat-content">
            <p class="stat-value">{{ completedActivities }}</p>
            <p class="stat-label">已完成活动</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon purple">
            <el-icon><Clock /></el-icon>
          </div>
          <div class="stat-content">
            <p class="stat-value">{{ waitingRegistrations }}</p>
            <p class="stat-label">等待队列中</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row style="margin-top: 20px;">
      <el-col :span="12">
        <el-card title="即将开始的活动">
          <el-timeline>
            <el-timeline-item 
              v-for="activity in upcomingActivityList" 
              :key="activity.id"
              :timestamp="formatDate(activity.startTime)"
            >
              <el-card size="small" @click="goToActivity(activity.id)">
                <h4>{{ activity.title }}</h4>
                <p class="activity-desc">{{ activity.description }}</p>
                <div class="activity-meta">
                  <span class="club-name">{{ getClubName(activity.clubId) }}</span>
                  <el-tag :type="getActivityStatusType(activity.status)" size="small">
                    {{ getActivityStatusText(activity.status) }}
                  </el-tag>
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card title="热门组织推荐">
          <div class="club-list">
            <el-card 
              v-for="club in recommendedClubs" 
              :key="club.id" 
              class="club-card"
              @click="goToClub(club.id)"
            >
              <div class="club-header">
                <div class="club-icon">
                  <el-icon><Briefcase /></el-icon>
                </div>
                <div class="club-info">
                  <h4>{{ club.name }}</h4>
                  <p>{{ club.description }}</p>
                </div>
              </div>
              <div class="club-footer">
                <span>成员数: {{ getClubMemberCount(club.id) }}</span>
                <el-button v-if="!isJoined(club.id)" type="primary" size="small">申请加入</el-button>
                <el-tag v-else type="success" size="small">已加入</el-tag>
              </div>
            </el-card>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Briefcase, Calendar, CheckSquare, Clock } from '@element-plus/icons-vue'
import { clubApi, activityApi, registrationApi } from '../api'

const router = useRouter()
const joinedClubsCount = ref(0)
const upcomingActivities = ref(0)
const completedActivities = ref(0)
const waitingRegistrations = ref(0)
const upcomingActivityList = ref([])
const recommendedClubs = ref([])
const userJoinedClubs = ref([])

const getActivityStatusText = (status) => {
  const map = { active: '进行中', pending: '待审核', completed: '已结束' }
  return map[status] || '未知'
}

const getActivityStatusType = (status) => {
  const map = { active: 'success', pending: 'warning', completed: 'info' }
  return map[status] || ''
}

const formatDate = (date) => {
  return date?.split('T')[0] || date
}

const getClubName = (clubId) => {
  const club = recommendedClubs.value.find(c => c.id === clubId)
  return club?.name || '未知组织'
}

const getClubMemberCount = (clubId) => {
  const counts = { 1: 156, 2: 89, 3: 67, 4: 203, 5: 45 }
  return counts[clubId] || Math.floor(Math.random() * 100) + 50
}

const isJoined = (clubId) => {
  return userJoinedClubs.value.includes(clubId)
}

const goToActivity = (id) => {
  router.push(`/student/activities/detail/${id}`)
}

const goToClub = (id) => {
  router.push(`/student/clubs/detail/${id}`)
}

onMounted(async () => {
  const clubsRes = await clubApi.list({ status: 'approved' })
  recommendedClubs.value = clubsRes.data || []
  joinedClubsCount.value = Math.min(recommendedClubs.value.length, 3)
  userJoinedClubs.value = [1, 2]

  const activitiesRes = await activityApi.list()
  const activities = activitiesRes.data || []
  upcomingActivityList.value = activities.filter(a => a.status === 'active').slice(0, 3)
  upcomingActivities.value = activities.filter(a => a.status === 'active').length
  completedActivities.value = activities.filter(a => a.status === 'completed').length

  const registrationsRes = await registrationApi.list()
  const registrations = registrationsRes.data || []
  waitingRegistrations.value = registrations.filter(r => r.status === 'pending').length
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

.activity-desc {
  color: #606266;
  font-size: 13px;
  margin: 5px 0;
}

.activity-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.club-name {
  color: #409EFF;
  font-size: 12px;
}

.club-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.club-card {
  cursor: pointer;
  transition: all 0.3s;
}

.club-card:hover {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.club-header {
  display: flex;
  gap: 12px;
}

.club-icon {
  font-size: 40px;
  color: #409EFF;
}

.club-info h4 {
  margin: 0 0 5px 0;
}

.club-info p {
  margin: 0;
  color: #606266;
  font-size: 13px;
}

.club-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
  padding-top: 10px;
  border-top: 1px solid #EBEEF5;
}

.club-footer span {
  color: #909399;
  font-size: 12px;
}
</style>