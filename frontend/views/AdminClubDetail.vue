<template>
  <div class="admin-club-detail">
    <el-card v-if="club">
      <template #header>
        <div class="card-header">
          <span>组织详情</span>
          <el-button @click="goBack">返回</el-button>
        </div>
      </template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="组织名称">{{ club.name }}</el-descriptions-item>
        <el-descriptions-item label="组织类型">
          <el-tag>{{ getTypeText(club.type) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(club.status)">
            {{ getStatusText(club.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ club.createdAt }}</el-descriptions-item>
        <el-descriptions-item label="组织描述" :span="2">
          <div class="description-text">{{ club.description }}</div>
        </el-descriptions-item>
      </el-descriptions>
      <div class="action-bar" v-if="club.status === 'pending'">
        <el-button type="primary" @click="approveClub">通过审核</el-button>
        <el-button type="danger" @click="rejectClub">拒绝审核</el-button>
      </div>
      <div class="action-bar" v-else>
        <el-button @click="goToEdit">编辑组织</el-button>
        <el-button type="danger" @click="deleteClub">删除组织</el-button>
      </div>
    </el-card>
    <el-empty v-else description="未找到组织信息" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { clubApi } from '../api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const club = ref(null)

const getTypeText = (type) => {
  const map = { academic: '学术科技', art: '文体艺术', volunteer: '公益志愿', other: '其他', 1: '社团', 2: '学生会' }
  return map[type] || '综合'
}

const getStatusText = (status) => {
  const map = { pending: '待审核', approved: '已通过', rejected: '已拒绝' }
  return map[status] || '未知'
}

const getStatusType = (status) => {
  const map = { pending: 'warning', approved: 'success', rejected: 'danger' }
  return map[status] || ''
}

const goBack = () => {
  router.push('/admin/clubs')
}

const goToEdit = () => {
  router.push(`/admin/clubs/edit/${route.params.id}`)
}

const approveClub = async () => {
  await clubApi.update(route.params.id, { status: 'approved' })
  club.value.status = 'approved'
  ElMessage.success('已通过审核')
}

const rejectClub = async () => {
  await clubApi.update(route.params.id, { status: 'rejected' })
  club.value.status = 'rejected'
  ElMessage.info('已拒绝审核')
}

const deleteClub = async () => {
  await clubApi.delete(route.params.id)
  ElMessage.success('组织已删除')
  router.push('/admin/clubs')
}

onMounted(async () => {
  if (route.params.id) {
    const response = await clubApi.get(route.params.id)
    club.value = response.data
  }
})
</script>

<style scoped>
.admin-club-detail {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.description-text {
  white-space: pre-wrap;
  line-height: 1.6;
}

.action-bar {
  margin-top: 20px;
  display: flex;
  gap: 10px;
}
</style>