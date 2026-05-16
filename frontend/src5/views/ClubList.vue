<template>
  <div class="club-list">
    <el-card>
      <div style="display: flex; justify-content: space-between; margin-bottom: 20px;">
        <h3>组织列表</h3>
        <el-button type="primary" @click="goAdd">新建组织</el-button>
      </div>
      <el-table :data="clubs">
        <el-table-column prop="id" label="ID" />
        <el-table-column prop="name" label="组织名称" />
        <el-table-column prop="type" label="类型">
          <template #default="scope">
            {{ scope.row.type === 1 ? '社团' : '学生会' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" />
        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="small" @click="goEdit(scope.row.id)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteClub(scope.row.id)">删除</el-button>
            <el-button v-if="scope.row.status === 0" size="small" type="success" @click="auditClub(scope.row.id, 1)">通过</el-button>
            <el-button v-if="scope.row.status === 0" size="small" type="warning" @click="auditClub(scope.row.id, 2)">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { clubApi } from '../api'

const router = useRouter()
const clubs = ref([])

const getStatusText = (status) => {
  const map = { 0: '待审核', 1: '已通过', 2: '已拒绝' }
  return map[status] || '未知'
}

const getStatusType = (status) => {
  const map = { 0: 'warning', 1: 'success', 2: 'danger' }
  return map[status] || ''
}

const goAdd = () => {
  router.push('/clubs/add')
}

const goEdit = (id) => {
  router.push(`/clubs/edit/${id}`)
}

const deleteClub = async (id) => {
  if (confirm('确定删除该组织？')) {
    await clubApi.delete(id)
    loadData()
  }
}

const auditClub = async (id, status) => {
  await clubApi.audit(id, status)
  loadData()
}

const loadData = async () => {
  const response = await clubApi.list()
  clubs.value = response.data || []
}

onMounted(() => {
  loadData()
})
</script>