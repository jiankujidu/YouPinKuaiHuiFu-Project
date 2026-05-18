<template>
  <div class="phrases-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>话术管理</span>
          <el-button type="primary" @click="showAddDialog = true">
            <el-icon><Plus /></el-icon>新增话术
          </el-button>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索话术"
          clearable
          style="width: 300px"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button @click="handleSearch">
              <el-icon><Search /></el-icon>
            </el-button>
          </template>
        </el-input>
        
        <el-select v-model="filterType" placeholder="分类筛选" clearable style="width: 150px; margin-left: 10px;">
          <el-option label="公司话术" value="company" />
          <el-option label="团队话术" value="team" />
          <el-option label="个人话术" value="private" />
        </el-select>
      </div>
      
      <!-- 话术列表 -->
      <el-table :data="phraseList" v-loading="loading" style="margin-top: 20px;">
        <el-table-column prop="title" label="标题" min-width="150" />
        <el-table-column prop="content" label="内容" min-width="300" show-overflow-tooltip />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getTypeTag(row.type)">{{ getTypeText(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="usage_count" label="使用次数" width="100" />
        <el-table-column prop="updated_at" label="更新时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    
    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="showAddDialog" :title="isEdit ? '编辑话术' : '新增话术'" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入话术标题" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="5"
            placeholder="请输入话术内容"
          />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio label="company">公司话术</el-radio>
            <el-radio label="team">团队话术</el-radio>
            <el-radio label="private">个人话术</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPhrases, createPhrase, updatePhrase, deletePhrase } from '../api/phrases'

const loading = ref(false)
const phraseList = ref([])
const searchKeyword = ref('')
const filterType = ref('')
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)

const showAddDialog = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref()

const form = reactive({
  id: '',
  title: '',
  content: '',
  type: 'private'
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
  type: [{ required: true, message: '请选择类型', trigger: 'change' }]
}

const getTypeTag = (type) => {
  const map = { company: 'danger', team: 'warning', private: 'success' }
  return map[type] || 'info'
}

const getTypeText = (type) => {
  const map = { company: '公司', team: '团队', private: '个人' }
  return map[type] || type
}

const fetchPhrases = async () => {
  loading.value = true
  try {
    const res = await getPhrases({
      keyword: searchKeyword.value,
      type: filterType.value
    })
    phraseList.value = res.data
    total.value = res.data.length
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  fetchPhrases()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  fetchPhrases()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchPhrases()
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, row)
  showAddDialog.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除这条话术吗？', '提示', { type: 'warning' })
    await deletePhrase(row.id)
    ElMessage.success('删除成功')
    fetchPhrases()
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
    }
  }
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    if (isEdit.value) {
      await updatePhrase(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await createPhrase(form)
      ElMessage.success('创建成功')
    }
    showAddDialog.value = false
    fetchPhrases()
  } catch (error) {
    console.error(error)
  } finally {
    submitLoading.value = false
  }
}

onMounted(() => {
  fetchPhrases()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-bar {
  display: flex;
  align-items: center;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
