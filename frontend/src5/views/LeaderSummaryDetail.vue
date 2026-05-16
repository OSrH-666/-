<template>
  <div class="summary-detail">
    <el-card v-if="summary">
      <template #header>
        <div class="card-header">
          <span>活动总结详情</span>
          <el-button @click="goBack">返回</el-button>
        </div>
      </template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="活动名称">{{ summary.activityName }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ summary.createdAt }}</el-descriptions-item>
        <el-descriptions-item label="总结内容" :span="2">
          <div class="content-text">{{ summary.content }}</div>
        </el-descriptions-item>
        <el-descriptions-item label="附件">
          <div v-if="summary.attachments && summary.attachments.length > 0">
            <div v-for="(file, index) in summary.attachments" :key="index" class="attachment-item">
              <el-icon><Document /></el-icon>
              <span>{{ typeof file === 'string' ? file : file.name }}</span>
            </div>
          </div>
          <span v-else>无附件</span>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
    <el-empty v-else description="未找到总结信息" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { summaryApi } from '../api'
import { Document } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

const summary = ref(null)

const goBack = () => {
  router.push('/leader/summaries')
}

onMounted(async () => {
  if (route.params.id) {
    const response = await summaryApi.get(route.params.id)
    summary.value = response.data
  }
})
</script>

<style scoped>
.summary-detail {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.content-text {
  white-space: pre-wrap;
  line-height: 1.8;
}

.attachment-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.attachment-item:last-child {
  margin-bottom: 0;
}
</style>