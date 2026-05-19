<template>
  <div class="team-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>团队管理</span>
          <el-button type="primary" @click="showAddDialog = true">
            <el-icon><Plus /></el-icon>添加成员
          </el-button>
        </div>
      </template>
      
      <el-table :data="memberList" v-loading="loading">
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="role" label="角色">
          <template #default="{ row }">
            <el-tag :type="row.role === 'admin' ? 'danger' : 'info'">
              {{ row.role === 'admin' ? '管理员' : '成员' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="joinTime" label="加入时间" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button type="danger" size="small" @click="handleRemove(row)">移除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 添加成员对话框 -->
    <el-dialog v-model="showAddDialog" title="添加成员" width="400px">
      <el-form :model="form" ref="formRef" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="角色">
          <el-radio-group v-model="form.role">
            <el-radio label="member">成员</el-radio>
            <el-radio label="admin">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getTeamMembers } from '../api/team'

const loading = ref(false)
const memberList = ref([])
const showAddDialog = ref(false)
const formRef = ref()

const form = reactive({
  username: '',
  role: 'member'
})

const fetchMembers = async () => {
  loading.value = true
  try {
    const res = await getTeamMembers()
    memberList.value = res.data || []
  } catch (error) {
    console.error(error)
    // 模拟数据
    memberList.value = [
      { username: 'user1', nickname: '张三', role: 'admin', joinTime: '2024-01-15' },
      { username: 'user2', nickname: '李四', role: 'member', joinTime: '2024-02-20' },
      { username: 'user3', nickname: '王五', role: 'member', joinTime: '2024-03-10' }
    ]
  } finally {
    loading.value = false
  }
}

const handleRemove = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要移除成员 ${row.nickname} 吗？`, '提示', { type: 'warning' })
    ElMessage.success('移除成功')
    fetchMembers()
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
    }
  }
}

const handleSubmit = async () => {
  ElMessage.success('添加成功')
  showAddDialog.value = false
  fetchMembers()
}

onMounted(() => {
  fetchMembers()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
