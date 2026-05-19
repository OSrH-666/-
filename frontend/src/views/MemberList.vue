<template>
  <div class="member-list">
    <el-card>
      <div class="card-header">
        <h3>社团成员管理</h3>
        <el-button type="primary" @click="showAddForm = true">添加成员</el-button>
      </div>
      
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="社团">
          <el-select v-model="searchForm.clubId" placeholder="请选择社团">
            <el-option label="全部" :value="''" />
            <el-option v-for="club in clubs" :key="club.id" :label="club.name" :value="club.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="searchForm.role" placeholder="请选择角色">
            <el-option label="全部" :value="''" />
            <el-option label="负责人" :value="1" />
            <el-option label="成员" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态">
            <el-option label="全部" :value="''" />
            <el-option label="正常" :value="1" />
            <el-option label="退出" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">搜索</el-button>
          <el-button @click="reset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="members" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="clubName" label="所属社团" />
        <el-table-column prop="userName" label="成员姓名" />
        <el-table-column prop="studentId" label="学号" />
        <el-table-column prop="role" label="角色" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.role === 1 ? 'primary' : 'success'">
              {{ scope.row.role === 1 ? '负责人' : '成员' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '正常' : '已退出' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="joinedAt" label="加入时间" />
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button size="small" @click="editMember(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="removeMember(scope.row)">移除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        :current-page="pagination.page"
        :page-size="pagination.size"
        :total="pagination.total"
        @current-change="handlePageChange"
        layout="prev, pager, next, jumper, ->, total"
      />
    </el-card>

    <el-dialog :title="isEdit ? '编辑成员' : '添加成员'" :visible.sync="showAddForm" width="400px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="社团" prop="clubId">
          <el-select v-model="form.clubId" placeholder="请选择社团">
            <el-option v-for="club in clubs" :key="club.id" :label="club.name" :value="club.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="学号" prop="studentId">
          <el-input v-model="form.studentId" placeholder="请输入学号" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-radio-group v-model="form.role">
            <el-radio :label="1">负责人</el-radio>
            <el-radio :label="2">成员</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddForm = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { clubApi } from '../api'

const members = ref([])
const clubs = ref([])
const showAddForm = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const searchForm = reactive({
  clubId: '',
  role: '',
  status: ''
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const form = reactive({
  id: '',
  clubId: '',
  studentId: '',
  role: 2
})

const rules = reactive({
  clubId: [
    { required: true, message: '请选择社团', trigger: 'blur' }
  ],
  studentId: [
    { required: true, message: '请输入学号', trigger: 'blur' }
  ]
})

const mockMembers = [
  { id: 1, clubId: 1, clubName: '计算机协会', userId: 2, userName: '张三', studentId: '2021001', role: 1, status: 1, joinedAt: '2024-09-01' },
  { id: 2, clubId: 1, clubName: '计算机协会', userId: 3, userName: '李四', studentId: '2021002', role: 2, status: 1, joinedAt: '2024-09-05' },
  { id: 3, clubId: 1, clubName: '计算机协会', userId: 4, userName: '王五', studentId: '2021003', role: 2, status: 1, joinedAt: '2024-09-10' },
  { id: 4, clubId: 2, clubName: '篮球社', userId: 5, userName: '赵六', studentId: '2021004', role: 1, status: 1, joinedAt: '2024-09-02' },
  { id: 5, clubId: 2, clubName: '篮球社', userId: 6, userName: '钱七', studentId: '2021005', role: 2, status: 1, joinedAt: '2024-09-08' },
  { id: 6, clubId: 3, clubName: '音乐社', userId: 7, userName: '孙八', studentId: '2021006', role: 1, status: 1, joinedAt: '2024-09-03' },
  { id: 7, clubId: 3, clubName: '音乐社', userId: 8, userName: '周九', studentId: '2021007', role: 2, status: 0, joinedAt: '2024-09-06' },
  { id: 8, clubId: 4, clubName: '读书社', userId: 9, userName: '吴十', studentId: '2021008', role: 1, status: 1, joinedAt: '2024-09-04' }
]

const loadMembers = () => {
  let data = [...mockMembers]
  if (searchForm.clubId) {
    data = data.filter(m => m.clubId === parseInt(searchForm.clubId))
  }
  if (searchForm.role) {
    data = data.filter(m => m.role === parseInt(searchForm.role))
  }
  if (searchForm.status) {
    data = data.filter(m => m.status === parseInt(searchForm.status))
  }
  pagination.total = data.length
  const start = (pagination.page - 1) * pagination.size
  const end = start + pagination.size
  members.value = data.slice(start, end)
}

const loadClubs = async () => {
  const response = await clubApi.list()
  if (response.code === 200) {
    clubs.value = response.data.filter(c => c.status === 'approved')
  }
}

const search = () => {
  pagination.page = 1
  loadMembers()
}

const reset = () => {
  searchForm.clubId = ''
  searchForm.role = ''
  searchForm.status = ''
  pagination.page = 1
  loadMembers()
}

const handlePageChange = (page) => {
  pagination.page = page
  loadMembers()
}

const editMember = (row) => {
  isEdit.value = true
  form.id = row.id
  form.clubId = row.clubId
  form.studentId = row.studentId
  form.role = row.role
  showAddForm.value = true
}

const removeMember = (row) => {
  ElMessage.confirm('确定要移除该成员吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(() => {
    const index = members.value.findIndex(m => m.id === row.id)
    if (index !== -1) {
      members.value.splice(index, 1)
      pagination.total--
    }
    ElMessage.success('移除成功')
  }).catch(() => {})
}

const submitForm = async () => {
  if (!formRef.value) return
  const valid = await formRef.value.validate()
  if (!valid) return

  if (isEdit.value) {
    const index = members.value.findIndex(m => m.id === form.id)
    if (index !== -1) {
      members.value[index] = {
        ...members.value[index],
        clubId: form.clubId,
        studentId: form.studentId,
        role: form.role
      }
    }
    ElMessage.success('修改成功')
  } else {
    const newMember = {
      id: Date.now(),
      clubId: form.clubId,
      clubName: clubs.value.find(c => c.id === form.clubId)?.name || '',
      userId: Date.now(),
      userName: '新成员',
      studentId: form.studentId,
      role: form.role,
      status: 1,
      joinedAt: new Date().toISOString().split('T')[0]
    }
    members.value.unshift(newMember)
    pagination.total++
    ElMessage.success('添加成功')
  }
  
  showAddForm.value = false
  resetForm()
}

const resetForm = () => {
  form.id = ''
  form.clubId = ''
  form.studentId = ''
  form.role = 2
  isEdit.value = false
}

onMounted(() => {
  loadClubs()
  loadMembers()
})
</script>

<style scoped>
.member-list {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-form {
  margin-bottom: 20px;
}
</style>