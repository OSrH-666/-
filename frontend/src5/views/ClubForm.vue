<template>
  <div class="club-form">
    <el-card>
      <h3>{{ isEdit ? '编辑组织' : '新建组织' }}</h3>
      <el-form :model="form" label-width="100px">
        <el-form-item label="组织名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="组织类型">
          <el-radio-group v-model="form.type">
            <el-radio :label="1">社团</el-radio>
            <el-radio :label="2">学生会</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="组织描述">
          <el-textarea v-model="form.description" rows="4" />
        </el-form-item>
        <el-form-item label="Logo地址">
          <el-input v-model="form.logoUrl" />
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
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { clubApi } from '../api'

const router = useRouter()
const route = useRoute()

const isEdit = ref(false)
const form = ref({
  name: '',
  type: 1,
  description: '',
  logoUrl: ''
})

const submit = async () => {
  if (isEdit.value) {
    await clubApi.update(route.params.id, form.value)
  } else {
    await clubApi.create(form.value)
  }
  router.push('/clubs')
}

const goBack = () => {
  router.push('/clubs')
}

onMounted(async () => {
  if (route.params.id) {
    isEdit.value = true
    const response = await clubApi.get(route.params.id)
    form.value = response.data || {}
  }
})
</script>