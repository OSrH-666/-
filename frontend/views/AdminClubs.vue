<template>
  <div class="admin-clubs">
    <el-card>
      <div class="action-bar">
        <el-select v-model="statusFilter" placeholder="状态筛选">
          <el-option label="全部" :value="null" />
          <el-option label="待审核" :value="0" />
          <el-option label="已通过" :value="1" />
          <el-option label="已拒绝" :value="2" />
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
            <template v-if="scope.row.status === 0">
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
import { clubApi } from '../src2/api'
import { ElMessage, ElMessageBox } from 'element-plus'

const statusFilter = ref(null)
const clubs = ref([])

const filteredClubs = computed(() => {
  if (statusFilter.value === null) return clubs.value
  return clubs.value.filter(c => c.status === statusFilter.value)
})

const getTypeText = (type) => {
  const map = { 0: '学术科技', 1: '文体艺术', 2: '公益志愿', 3: '其他' }
  return map[type] || '综合'
}

const getStatusText = (status) => {
  const map = { 0: '待审核', 1: '已通过', 2: '已拒绝' }
  return map[status] || '未知'
}

const getStatusType = (status) => {
  const map = { 0: 'warning', 1: 'success', 2: 'danger' }
  return map[status] || ''
}

const refresh = async () => {
  try {
    const response = await clubApi.list()
    clubs.value = response.data || []
  } catch (error) {
    ElMessage.error('获取列表失败')
    console.error(error)
  }
}

const approveClub = async (id) => {
  try {
    await ElMessageBox.confirm('确定要通过该组织的审核吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await clubApi.audit(id, 1)
    ElMessage.success('已通过审核')
    refresh()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
      console.error(error)
    }
  }
}

const rejectClub = async (id) => {
  try {
    await ElMessageBox.confirm('确定要拒绝该组织的审核吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await clubApi.audit(id, 2)
    ElMessage.info('已拒绝审核')
    refresh()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
      console.error(error)
    }
  }
}

const viewDetail = (id) => {}

const deleteClub = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该组织吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await clubApi.delete(id)
    ElMessage.success('组织已删除')
    refresh()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
      console.error(error)
    }
  }
}

onMounted(async () => {
  refresh()
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