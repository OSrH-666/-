<template>
  <div class="activity-form">
    <el-card>
      <h3>{{ isEdit ? '编辑活动' : '新建活动' }}</h3>
      <el-form :model="form" ref="formRef" :rules="rules" label-width="100px">
        <el-form-item label="活动名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="所属组织">
          <el-input v-model="form.clubId" type="number" />
        </el-form-item>
        <el-form-item label="活动描述">
          <el-textarea v-model="form.description" rows="4" />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker v-model="form.startTime" type="datetime" />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker v-model="form.endTime" type="datetime" />
        </el-form-item>
        <el-form-item label="活动地点">
          <el-input v-model="form.location" />
        </el-form-item>
        <el-form-item label="报名限额">
          <el-input v-model="form.quota" type="number" placeholder="0表示不限" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit">保存</el-button>
          <el-button @click="goBack">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { activityApi } from '../api'

const router = useRouter()
const route = useRoute()

const isEdit = ref(false)
const formRef = ref(null)
const form = ref({
  name: '',
  clubId: '',
  description: '',
  startTime: '',
  endTime: '',
  location: '',
  quota: 0
})

const rules = reactive({
  name: [
    { required: true, message: '活动标题不能为空', trigger: 'blur' }
  ]
})

const submit = async () => {
  if (!formRef.value) return
  const valid = await formRef.value.validate()
  if (!valid) return
  if (isEdit.value) {
    await activityApi.update(route.params.id, form.value)
  } else {
    await activityApi.create(form.value)
  }
  router.push('/activities')
}

const goBack = () => {
  router.push('/activities')
}

onMounted(async () => {
  if (route.params.id) {
    isEdit.value = true
    const response = await activityApi.get(route.params.id)
    form.value = response.data || {}
  }
})
</script>
