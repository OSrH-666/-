<template>
  <div class="activity-form">
    <el-card>
      <h3>{{ isEdit ? '编辑活动' : '新建活动' }}</h3>
      <el-form :model="form" label-width="100px">
        <el-form-item label="活动名称">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="所属组织">
          <el-select v-model="form.clubId" placeholder="请选择组织">
            <el-option v-for="club in clubs" :key="club.id" :label="club.name" :value="club.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="活动描述">
          <el-textarea v-model="form.description" rows="4" />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker v-model="form.startTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker v-model="form.endTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
        <el-form-item label="活动地点">
          <el-input v-model="form.location" />
        </el-form-item>
        <el-form-item label="报名限额">
          <el-input v-model="form.quota" type="number" placeholder="0表示不限" />
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
import { activityApi, clubApi } from '../api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const isEdit = ref(false)
const loading = ref(false)
const clubs = ref([])
const form = ref({
  title: '',
  clubId: '',
  description: '',
  startTime: '',
  endTime: '',
  location: '',
  quota: 0
})

const submit = async () => {
  loading.value = true
  try {
    if (isEdit.value) {
      await activityApi.update(route.params.id, form.value)
      ElMessage.success('更新成功')
    } else {
      await activityApi.create(form.value)
      ElMessage.success('创建成功')
    }
    router.push('/leader/activities')
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.push('/leader/activities')
}

onMounted(async () => {
  const clubResponse = await clubApi.list()
  clubs.value = clubResponse.data || []

  if (route.params.id) {
    isEdit.value = true
    const response = await activityApi.get(route.params.id)
    if (response.data) {
      form.value = {
        title: response.data.title,
        clubId: response.data.clubId,
        description: response.data.description,
        startTime: response.data.startTime,
        endTime: response.data.endTime,
        location: response.data.location,
        quota: response.data.quota || 0
      }
    }
  }
})
</script>

<style scoped>
.activity-form {
  padding: 20px;
}

h3 {
  margin-bottom: 20px;
  color: #303133;
}
</style>