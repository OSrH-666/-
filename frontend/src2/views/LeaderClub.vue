<template>
  <div class="leader-club">
    <el-card>
      <h3>组织信息维护</h3>
      <el-form :model="form" label-width="120px">
        <el-form-item label="组织名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="组织类型">
          <el-select v-model="form.type">
            <el-option label="学术科技" value="academic" />
            <el-option label="文体艺术" value="art" />
            <el-option label="公益志愿" value="volunteer" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="组织描述">
          <el-textarea v-model="form.description" rows="4" />
        </el-form-item>
        <el-form-item label="组织Logo">
          <el-upload 
            :action="''" 
            :show-file-list="false"
            @change="handleLogoUpload"
          >
            <el-button size="small" type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="form.leaderName" disabled />
        </el-form-item>
        <el-form-item label="联系邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="save">保存修改</el-button>
          <el-button @click="reset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const form = ref({
  name: '计算机协会',
  type: 'academic',
  description: '专注于计算机技术交流与学习',
  leaderName: '张三',
  email: 'club@example.com',
  phone: '13800138000'
})

const handleLogoUpload = () => {}

const save = () => {
  ElMessage.success('组织信息已更新')
}

const reset = () => {
  form.value = {
    name: '计算机协会',
    type: 'academic',
    description: '专注于计算机技术交流与学习',
    leaderName: '张三',
    email: 'club@example.com',
    phone: '13800138000'
  }
}

onMounted(() => {})
</script>