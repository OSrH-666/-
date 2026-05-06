<template>
  <div class="activity-list">
    <el-card>
      <div style="display: flex; justify-content: space-between; margin-bottom: 20px;">
        <h3>活动列表</h3>
        <el-button type="primary" @click="goAdd">新建活动</el-button>
      </div>
      <el-table :data="activities">
        <el-table-column prop="id" label="ID" />
        <el-table-column prop="name" label="活动名称" />
        <el-table-column prop="clubId" label="所属组织" />
        <el-table-column prop="startTime" label="开始时间" />
        <el-table-column prop="endTime" label="结束时间" />
        <el-table-column prop="location" label="地点" />
        <el-table-column prop="quota" label="限额" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="small" @click="goDetail(scope.row.id)">详情</el-button>
            <el-button size="small" @click="goEdit(scope.row.id)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteActivity(scope.row.id)">删除</el-button>
            <el-button v-if="scope.row.status === 0" size="small" type="success" @click="publishActivity(scope.row.id)">发布</el-button>
            <el-button v-if="scope.row.status === 1" size="small" type="warning" @click="endActivity(scope.row.id)">结束</el-button>
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

const router = useRouter()
const activities = ref([])

const getStatusText = (status) => {
  const map = { 0: '草稿', 1: '已发布', 2: '已结束' }
  return map[status] || '未知'
}

const getStatusType = (status) => {
  const map = { 0: 'info', 1: 'success', 2: 'warning' }
  return map[status] || ''
}

const goAdd = () => {
  router.push('/activities/add')
}

const goEdit = (id) => {
  router.push(`/activities/edit/${id}`)
}

const goDetail = (id) => {
  router.push(`/activities/detail/${id}`)
}

const deleteActivity = async (id) => {
  if (confirm('确定删除该活动？')) {
    await activityApi.delete(id)
    loadData()
  }
}

const publishActivity = async (id) => {
  await activityApi.publish(id)
  loadData()
}

const endActivity = async (id) => {
  await activityApi.end(id)
  loadData()
}

const loadData = async () => {
  const response = await activityApi.list()
  activities.value = response.data || []
}

onMounted(() => {
  loadData()
})
</script>