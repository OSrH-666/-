<template>
  <div class="leader-activities">
    <el-card>
      <div class="action-bar">
        <el-button type="primary" @click="goToAdd">发布活动</el-button>
      </div>
      <el-table :data="activities">
        <el-table-column prop="name" label="活动名称" />
        <el-table-column prop="startTime" label="开始时间" />
        <el-table-column prop="endTime" label="结束时间" />
        <el-table-column prop="location" label="活动地点" />
        <el-table-column prop="quota" label="报名限额">
          <template #default="scope">
            {{ scope.row.quota === 0 ? '不限' : scope.row.quota + '人' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="getActivityStatusType(scope.row.status)">
              {{ getActivityStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="small" @click="goToDetail(scope.row.id)">查看</el-button>
            <el-button size="small" @click="goToEdit(scope.row.id)">编辑</el-button>
            <template v-if="scope.row.status === 0">
              <el-button size="small" type="success" @click="publishActivity(scope.row.id)">发布</el-button>
            </template>
            <template v-else-if="scope.row.status === 1">
              <el-button size="small" type="warning" @click="endActivity(scope.row.id)">结束</el-button>
            </template>
            <el-button size="small" type="danger" @click="deleteActivity(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { activityApi } from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const activities = ref([])

const getActivityStatusText = (status) => {
  const map = { 0: '草稿', 1: '进行中', 2: '已结束' }
  return map[status] || '未知'
}

const getActivityStatusType = (status) => {
  const map = { 0: 'info', 1: 'success', 2: 'warning' }
  return map[status] || ''
}

const refresh = async () => {
  try {
    const response = await activityApi.list()
    activities.value = response.data || []
  } catch (error) {
    ElMessage.error('获取活动列表失败')
    console.error(error)
  }
}

const goToAdd = () => {
  router.push('/leader/activities/add')
}

const goToDetail = (id) => {
  router.push(`/leader/activities/detail/${id}`)
}

const goToEdit = (id) => {
  router.push(`/leader/activities/edit/${id}`)
}

const publishActivity = async (id) => {
  try {
    await ElMessageBox.confirm('确定要发布该活动吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await activityApi.publish(id)
    ElMessage.success('活动已发布')
    refresh()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('发布失败')
      console.error(error)
    }
  }
}

const endActivity = async (id) => {
  try {
    await ElMessageBox.confirm('确定要结束该活动吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await activityApi.end(id)
    ElMessage.success('活动已结束')
    refresh()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('结束失败')
      console.error(error)
    }
  }
}

const deleteActivity = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该活动吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await activityApi.delete(id)
    ElMessage.success('活动已删除')
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
  margin-bottom: 20px;
}
</style>