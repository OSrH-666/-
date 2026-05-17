<template>
  <div class="student-dashboard">
    <el-row :gutter="20">
      <el-col :span="24">
        <div class="welcome-card">
          <h2>欢迎回来，{{ userInfo.realName || userInfo.username }}</h2>
          <p class="date">{{ currentDate }}</p>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon blue">
              <el-icon><Calendar /></el-icon>
            </div>
            <div class="stat-info">
              <h3>{{ stats.upcomingActivities }}</h3>
              <p>即将开始的活动</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon green">
              <el-icon><Ticket /></el-icon>
            </div>
            <div class="stat-info">
              <h3>{{ stats.myRegistrations }}</h3>
              <p>我的报名</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon orange">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-info">
              <h3>{{ stats.unionNews }}</h3>
              <p>学生会动态</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="16">
        <el-card>
          <template #header>
            <span>最新活动</span>
          </template>
          <div class="activity-list">
            <div v-for="activity in recentActivities" :key="activity.id" class="activity-item">
              <div class="activity-info">
                <h4>{{ activity.name }}</h4>
                <p>{{ activity.time }}</p>
              </div>
              <el-button size="small" type="primary" @click="goToActivity(activity.id)">查看详情</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <span>学生会动态</span>
          </template>
          <div class="news-list">
            <div v-for="news in recentNews" :key="news.id" class="news-item">
              <h4>{{ news.title }}</h4>
              <p>{{ news.date }}</p>
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
import { Calendar, Ticket, Document } from '@element-plus/icons-vue'

const router = useRouter()
const userInfo = ref({})
const currentDate = new Date().toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' })

const stats = ref({
  upcomingActivities: 3,
  myRegistrations: 2,
  unionNews: 8
})

const recentActivities = ref([
  { id: 1, name: '校园歌手大赛', time: '2024-12-15 18:00' },
  { id: 2, name: '编程马拉松', time: '2024-12-20 09:00' },
  { id: 3, name: '志愿服务活动', time: '2024-12-25 08:00' }
])

const recentNews = ref([
  { id: 1, title: '学生会换届选举圆满成功', date: '2024-12-01' },
  { id: 2, title: '迎新晚会精彩回顾', date: '2024-11-28' },
  { id: 3, title: '期末复习讲座通知', date: '2024-11-25' }
])

const goToActivity = (id) => {
  router.push(`/activities/detail/${id}`)
}

onMounted(() => {
  const user = localStorage.getItem('user')
  if (user) {
    userInfo.value = JSON.parse(user)
  }
})
</script>

<style scoped>
.welcome-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 30px;
  border-radius: 8px;
}

.welcome-card h2 {
  margin: 0 0 10px 0;
}

.welcome-card .date {
  margin: 0;
  opacity: 0.9;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-icon .el-icon {
  font-size: 30px;
  color: white;
}

.stat-icon.blue { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.stat-icon.green { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); }
.stat-icon.orange { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }

.stat-info h3 {
  margin: 0;
  font-size: 32px;
  font-weight: bold;
}

.stat-info p {
  margin: 5px 0 0 0;
  color: #666;
}

.activity-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-info h4 {
  margin: 0 0 5px 0;
}

.activity-info p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.news-item {
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}

.news-item:last-child {
  border-bottom: none;
}

.news-item h4 {
  margin: 0 0 5px 0;
  font-size: 14px;
}

.news-item p {
  margin: 0;
  color: #999;
  font-size: 12px;
}
</style>
