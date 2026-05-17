<template>
  <div class="admin-clubs">
    <el-card>
      <div class="action-bar">
        <el-select v-model="statusFilter" placeholder="状态筛选">
          <el-option label="全部" value="" />
          <el-option label="待审核" value="pending" />
          <el-option label="已通过" value="approved" />
          <el-option label="已拒绝" value="rejected" />
        </el-select>
        <el-button type="primary" @click="refresh">刷新列表</el-button>
      </div>
      <el-table :data="filteredClubs">
        <el-table-column prop="name" label="组织名称" />
        <el-table-column prop="type" label="类型">
          <template #default="scope">
            <el-tag>{{ getTypeText(scope.row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="createdAt" label="创建时间" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <template v-if="scope.row.status === 'pending'">
              <el-button size="small" type="primary" @click="approveClub(scope.row.id)">通过</el-button>
              <el-button size="small" @click="rejectClub(scope.row.id)">拒绝</el-button>
            </template>
            <template v-else>
              <el-button size="small" @click="viewDetail(scope.row.id)">查看详情</el-button>
              <el-button size="small" type="danger" @click="deleteClub(scope.row.id)">删除</el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { clubApi } from '../api'
import { ElMessage } from 'element-plus'

const statusFilter = ref('')
const clubs = ref([])

const filteredClubs = computed(() => {
  if (!statusFilter.value) return clubs.value
  return clubs.value.filter(c => c.status === statusFilter.value)
})

const getTypeText = (type) => {
  const map = { academic: '学术科技', art: '文体艺术', volunteer: '公益志愿', other: '其他' }
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

const refresh = async () => {
  const response = await clubApi.list()
  clubs.value = response.data || []
}

const approveClub = (id) => {
  const club = clubs.value.find(c => c.id === id)
  if (club) {
    club.status = 'approved'
    ElMessage.success('已通过审核')
  }
}

const rejectClub = (id) => {
  const club = clubs.value.find(c => c.id === id)
  if (club) {
    club.status = 'rejected'
    ElMessage.info('已拒绝审核')
  }
}

const viewDetail = (id) => {}

const deleteClub = (id) => {
  clubs.value = clubs.value.filter(c => c.id !== id)
  ElMessage.success('组织已删除')
}

onMounted(async () => {
  const response = await clubApi.list()
  clubs.value = response.data || []
})
</script>

<style scoped>
.action-bar {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  align-items: center;
}
</style>