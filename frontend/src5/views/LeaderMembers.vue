<template>
  <div class="leader-members">
    <el-card>
      <div class="filter-bar">
        <el-select v-model="statusFilter" placeholder="状态筛选">
          <el-option label="全部" value="" />
          <el-option label="待审核" value="pending" />
          <el-option label="已通过" value="approved" />
          <el-option label="已拒绝" value="rejected" />
        </el-select>
      </div>
      <el-table :data="members">
        <el-table-column prop="userName" label="姓名" />
        <el-table-column prop="studentId" label="学号" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="applyTime" label="申请时间" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="role" label="角色">
          <template #default="scope">
            <el-select 
              v-model="scope.row.role" 
              size="small"
              @change="updateRole(scope.row.id, scope.row.role)"
            >
              <el-option label="成员" value="member" />
              <el-option label="管理员" value="admin" />
              <el-option label="副部长" value="vice_leader" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <template v-if="scope.row.status === 'pending'">
              <el-button size="small" type="primary" @click="approve(scope.row.id)">通过</el-button>
              <el-button size="small" @click="reject(scope.row.id)">拒绝</el-button>
            </template>
            <template v-else-if="scope.row.status === 'approved'">
              <el-button size="small" type="danger" @click="remove(scope.row.id)">移除</el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const statusFilter = ref('')
const members = ref([
  { id: 1, userName: '李四', studentId: '202405567001', email: 'lisi@example.com', phone: '13900139001', applyTime: '2024-03-20', status: 'pending', role: 'member' },
  { id: 2, userName: '王五', studentId: '202405567002', email: 'wangwu@example.com', phone: '13900139002', applyTime: '2024-03-19', status: 'approved', role: 'member' },
  { id: 3, userName: '赵六', studentId: '202405567003', email: 'zhaoliu@example.com', phone: '13900139003', applyTime: '2024-03-18', status: 'approved', role: 'admin' },
  { id: 4, userName: '钱七', studentId: '202405567004', email: 'qianqi@example.com', phone: '13900139004', applyTime: '2024-03-17', status: 'pending', role: 'member' },
  { id: 5, userName: '孙八', studentId: '202405567005', email: 'sunba@example.com', phone: '13900139005', applyTime: '2024-03-16', status: 'approved', role: 'vice_leader' }
])

const getStatusText = (status) => {
  const map = { pending: '待审核', approved: '已通过', rejected: '已拒绝' }
  return map[status] || '未知'
}

const getStatusType = (status) => {
  const map = { pending: 'warning', approved: 'success', rejected: 'danger' }
  return map[status] || ''
}

const approve = (id) => {
  const member = members.value.find(m => m.id === id)
  if (member) {
    member.status = 'approved'
    ElMessage.success('已通过申请')
  }
}

const reject = (id) => {
  const member = members.value.find(m => m.id === id)
  if (member) {
    member.status = 'rejected'
    ElMessage.info('已拒绝申请')
  }
}

const remove = (id) => {
  members.value = members.value.filter(m => m.id !== id)
  ElMessage.info('已移除成员')
}

const updateRole = (id, role) => {
  ElMessage.success('角色已更新')
}
</script>

<style scoped>
.filter-bar {
  margin-bottom: 20px;
}
</style>