<template>
  <div class="student-club-list">
    <el-card>
      <div class="search-bar">
        <el-input v-model="searchKeyword" placeholder="搜索组织名称" class="search-input" />
        <el-button type="primary" @click="search">搜索</el-button>
      </div>
      <div class="club-grid">
        <el-card 
          v-for="club in filteredClubs" 
          :key="club.id" 
          class="club-card"
          @click="goToDetail(club.id)"
        >
          <div class="club-header">
            <div class="club-icon">
              <el-icon><Briefcase /></el-icon>
            </div>
            <div class="club-info">
              <h3>{{ club.name }}</h3>
              <p>{{ club.description }}</p>
            </div>
          </div>
          <div class="club-footer">
            <span class="member-count">成员: {{ getMemberCount(club.id) }}</span>
            <el-tag :type="getStatusType(club.status)">
              {{ getStatusText(club.status) }}
            </el-tag>
          </div>
          <div class="club-action">
            <el-button v-if="!isJoined(club.id)" type="primary" size="small">申请加入</el-button>
            <el-tag v-else type="success">已加入</el-tag>
          </div>
        </el-card>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { Briefcase } from '@element-plus/icons-vue'
import { clubApi } from '../api'

const router = useRouter()
const searchKeyword = ref('')
const clubs = ref([])
const joinedClubs = ref([1, 2])

const filteredClubs = computed(() => {
  return clubs.value.filter(club => 
    club.name.toLowerCase().includes(searchKeyword.value.toLowerCase()) ||
    club.description.toLowerCase().includes(searchKeyword.value.toLowerCase())
  )
})

const getStatusText = (status) => {
  const map = { approved: '已通过', pending: '审核中', rejected: '已拒绝' }
  return map[status] || '未知'
}

const getStatusType = (status) => {
  const map = { approved: 'success', pending: 'warning', rejected: 'danger' }
  return map[status] || ''
}

const getMemberCount = (clubId) => {
  const counts = { 1: 156, 2: 89, 3: 67, 4: 203, 5: 45 }
  return counts[clubId] || Math.floor(Math.random() * 100) + 50
}

const isJoined = (clubId) => {
  return joinedClubs.value.includes(clubId)
}

const search = () => {}

const goToDetail = (id) => {
  router.push(`/student/clubs/detail/${id}`)
}

import { onMounted } from 'vue'
onMounted(async () => {
  const response = await clubApi.list()
  clubs.value = response.data || []
})
</script>

<style scoped>
.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.search-input {
  width: 300px;
}

.club-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.club-card {
  cursor: pointer;
  transition: all 0.3s;
}

.club-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.club-header {
  display: flex;
  gap: 15px;
}

.club-icon {
  font-size: 48px;
  color: #409EFF;
}

.club-info h3 {
  margin: 0 0 5px 0;
}

.club-info p {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.club-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #EBEEF5;
}

.member-count {
  color: #909399;
  font-size: 13px;
}

.club-action {
  margin-top: 15px;
  text-align: right;
}
</style>