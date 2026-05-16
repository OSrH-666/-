<template>
  <div class="summary-form">
    <el-card>
      <h3>{{ isEdit ? '编辑总结' : '撰写总结' }}</h3>
      <el-form :model="form" label-width="100px">
        <el-form-item label="关联活动">
          <el-select v-model="form.activityId" placeholder="请选择活动">
            <el-option v-for="activity in activities" :key="activity.id" :label="activity.title" :value="activity.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="总结内容">
          <el-input v-model="form.content" type="textarea" :rows="6" placeholder="请输入活动总结内容" />
        </el-form-item>
        <el-form-item label="上传附件">
          <el-upload
            ref="uploadRef"
            :auto-upload="false"
            :limit="5"
            :on-change="handleFileChange"
            :on-remove="handleFileRemove"
            multiple
          >
            <el-button type="primary">选择文件</el-button>
            <template #tip>
              <div class="el-upload__tip">最多上传5个文件</div>
            </template>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit" :loading="loading">保存</el-button>
          <el-button @click="goBack">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { summaryApi, activityApi } from '../api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const uploadRef = ref(null)

const isEdit = ref(false)
const loading = ref(false)
const form = ref({
  activityId: '',
  content: '',
  attachments: []
})
const activities = ref([])
const fileList = ref([])

const handleFileChange = (file, files) => {
  fileList.value = files
}

const handleFileRemove = (file, files) => {
  fileList.value = files
}

const submit = async () => {
  if (!form.value.activityId) {
    ElMessage.warning('请选择关联活动')
    return
  }
  if (!form.value.content) {
    ElMessage.warning('请输入总结内容')
    return
  }

  loading.value = true
  try {
    const activity = activities.value.find(a => a.id === form.value.activityId)
    const submitData = {
      ...form.value,
      activityName: activity?.title || '',
      attachments: fileList.value.map(f => f.name)
    }

    if (isEdit.value) {
      await summaryApi.update(route.params.id, submitData)
      ElMessage.success('更新成功')
    } else {
      await summaryApi.create(submitData)
      ElMessage.success('创建成功')
    }
    router.push('/leader/summaries')
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.push('/leader/summaries')
}

onMounted(async () => {
  const response = await activityApi.list({ clubId: 1 })
  activities.value = response.data || []

  if (route.params.id) {
    isEdit.value = true
    const summaryResponse = await summaryApi.get(route.params.id)
    if (summaryResponse.data) {
      form.value = {
        activityId: summaryResponse.data.activityId,
        content: summaryResponse.data.content,
        attachments: summaryResponse.data.attachments || []
      }
    }
  }
})
</script>

<style scoped>
.summary-form {
  padding: 20px;
}

h3 {
  margin-bottom: 20px;
  color: #303133;
}
</style>