<template>
  <div class="leader-summaries">
    <el-card>
      <div class="action-bar">
        <el-button type="primary" @click="goToAdd">撰写总结</el-button>
      </div>
      <el-table :data="summaries">
        <el-table-column prop="activityName" label="活动名称" />
        <el-table-column prop="content" label="总结内容" show-overflow-tooltip />
        <el-table-column prop="attachments" label="附件数量">
          <template #default="scope">
            {{ scope.row.attachments?.length || 0 }} 个
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" />
        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="small" @click="viewSummary(scope.row.id)">查看</el-button>
            <el-button size="small" @click="editSummary(scope.row.id)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteSummary(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { summaryApi, activityApi } from '../api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const summaries = ref([
  { id: 1, activityId: 4, activityName: '志愿者日', content: '活动圆满成功，参与人数50人，活动效果良好', attachments: [], createdAt: '2024-04-16' },
  { id: 2, activityId: 2, activityName: '篮球友谊赛', content: '比赛顺利结束，决出前三名，活动气氛热烈', attachments: ['比赛照片.zip'], createdAt: '2024-04-11' }
])

const goToAdd = () => {
  router.push('/leader/summaries/add')
}

const viewSummary = (id) => {
  router.push(`/leader/summaries/detail/${id}`)
}

const editSummary = (id) => {
  router.push(`/leader/summaries/edit/${id}`)
}

const deleteSummary = (id) => {
  summaries.value = summaries.value.filter(s => s.id !== id)
  ElMessage.success('总结已删除')
}

onMounted(async () => {
  const response = await summaryApi.list()
  summaries.value = response.data || summaries.value
})
</script>

<style scoped>
.action-bar {
  margin-bottom: 20px;
}
</style>