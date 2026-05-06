<template>
  <div class="summary-list">
    <el-card>
      <h3>活动总结列表</h3>
      <el-table :data="summaries">
        <el-table-column prop="id" label="ID" />
        <el-table-column prop="activityId" label="活动ID" />
        <el-table-column prop="content" label="总结内容" :show-overflow-tooltip="true" />
        <el-table-column prop="attachmentUrl" label="附件" />
        <el-table-column prop="createdAt" label="创建时间" />
        <el-table-column prop="updatedAt" label="更新时间" />
        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="small" type="danger" @click="deleteSummary(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { summaryApi } from '../api'

const summaries = ref([])

const deleteSummary = async (id) => {
  if (confirm('确定删除该总结？')) {
    await summaryApi.delete(id)
    loadData()
  }
}

const loadData = async () => {
  const response = await summaryApi.get(1)
  summaries.value = [response.data].filter(Boolean) || []
}

onMounted(() => {
  loadData()
})
</script>