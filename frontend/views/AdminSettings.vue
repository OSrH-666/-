<template>
  <div class="admin-settings">
    <el-card title="系统配置">
      <el-form :model="settings" label-width="140px">
        <el-form-item label="系统名称">
          <el-input v-model="settings.systemName" />
        </el-form-item>
        <el-form-item label="系统描述">
          <el-textarea v-model="settings.systemDescription" rows="3" />
        </el-form-item>
        <el-form-item label="活动报名限额">
          <el-input v-model="settings.maxParticipants" type="number" />
        </el-form-item>
        <el-form-item label="等待队列长度">
          <el-input v-model="settings.waitingQueueLength" type="number" />
        </el-form-item>
        <el-form-item label="签到有效时长(分钟)">
          <el-input v-model="settings.checkinDuration" type="number" />
        </el-form-item>
        <el-form-item label="允许重复报名">
          <el-switch v-model="settings.allowDuplicateRegistration" />
        </el-form-item>
        <el-form-item label="启用地理位置签到">
          <el-switch v-model="settings.enableLocationCheckin" />
        </el-form-item>
        <el-form-item label="活动审核流程">
          <el-switch v-model="settings.requireActivityApproval" />
        </el-form-item>
        <el-form-item label="组织审核流程">
          <el-switch v-model="settings.requireClubApproval" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveSettings">保存配置</el-button>
          <el-button @click="resetSettings">恢复默认</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card title="邮件配置" style="margin-top: 20px;">
      <el-form :model="emailSettings" label-width="140px">
        <el-form-item label="SMTP服务器">
          <el-input v-model="emailSettings.smtpServer" />
        </el-form-item>
        <el-form-item label="SMTP端口">
          <el-input v-model="emailSettings.smtpPort" type="number" />
        </el-form-item>
        <el-form-item label="发件人邮箱">
          <el-input v-model="emailSettings.senderEmail" />
        </el-form-item>
        <el-form-item label="发件人名称">
          <el-input v-model="emailSettings.senderName" />
        </el-form-item>
        <el-form-item label="SMTP密码">
          <el-input v-model="emailSettings.smtpPassword" type="password" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveEmailSettings">保存邮件配置</el-button>
          <el-button @click="testEmail">测试邮件发送</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const settings = ref({
  systemName: '大学生社团学生会信息管理系统',
  systemDescription: '为高校社团和学生会提供全方位的数字化管理解决方案',
  maxParticipants: 200,
  waitingQueueLength: 50,
  checkinDuration: 30,
  allowDuplicateRegistration: false,
  enableLocationCheckin: true,
  requireActivityApproval: true,
  requireClubApproval: true
})

const emailSettings = ref({
  smtpServer: 'smtp.example.com',
  smtpPort: 587,
  senderEmail: 'noreply@example.com',
  senderName: '社团管理系统',
  smtpPassword: ''
})

const saveSettings = () => {
  ElMessage.success('系统配置已保存')
}

const resetSettings = () => {
  settings.value = {
    systemName: '大学生社团学生会信息管理系统',
    systemDescription: '为高校社团和学生会提供全方位的数字化管理解决方案',
    maxParticipants: 200,
    waitingQueueLength: 50,
    checkinDuration: 30,
    allowDuplicateRegistration: false,
    enableLocationCheckin: true,
    requireActivityApproval: true,
    requireClubApproval: true
  }
  ElMessage.info('已恢复默认配置')
}

const saveEmailSettings = () => {
  ElMessage.success('邮件配置已保存')
}

const testEmail = () => {
  ElMessage.success('测试邮件已发送')
}
</script>