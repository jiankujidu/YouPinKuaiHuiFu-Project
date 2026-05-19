<template>
  <div class="settings-page">
    <el-card>
      <template #header>系统设置</template>
      
      <el-form :model="form" label-width="120px">
        <el-form-item label="悬浮窗位置">
          <el-radio-group v-model="form.floatPosition">
            <el-radio label="left">左侧</el-radio>
            <el-radio label="right">右侧</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="自动复制">
          <el-switch v-model="form.autoCopy" />
        </el-form-item>
        
        <el-form-item label="显示通知">
          <el-switch v-model="form.showNotification" />
        </el-form-item>
        
        <el-form-item label="主题风格">
          <el-radio-group v-model="form.theme">
            <el-radio label="wechat">微信绿</el-radio>
            <el-radio label="dark">深色模式</el-radio>
            <el-radio label="light">浅色模式</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSave">保存设置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card style="margin-top: 20px;">
      <template #header>账号安全</template>
      
      <el-form label-width="120px">
        <el-form-item label="修改密码">
          <el-button type="primary" plain @click="showPasswordDialog = true">修改密码</el-button>
        </el-form-item>
        
        <el-form-item label="绑定手机">
          <el-button type="primary" plain>绑定手机</el-button>
        </el-form-item>
        
        <el-form-item label="绑定邮箱">
          <el-button type="primary" plain>绑定邮箱</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card style="margin-top: 20px;">
      <template #header>数据管理</template>
      
      <el-form label-width="120px">
        <el-form-item label="数据备份">
          <el-button type="primary" plain @click="handleBackup">备份数据</el-button>
        </el-form-item>
        
        <el-form-item label="数据恢复">
          <el-button type="primary" plain @click="handleRestore">恢复数据</el-button>
        </el-form-item>
        
        <el-form-item label="清除数据">
          <el-button type="danger" plain @click="handleClear">清除所有数据</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 修改密码对话框 -->
    <el-dialog v-model="showPasswordDialog" title="修改密码" width="400px">
      <el-form :model="passwordForm" :rules="passwordRules" ref="passwordRef" label-width="100px">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showPasswordDialog = false">取消</el-button>
        <el-button type="primary" @click="handlePasswordChange">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const form = reactive({
  floatPosition: 'right',
  autoCopy: true,
  showNotification: true,
  theme: 'wechat'
})

const showPasswordDialog = ref(false)
const passwordRef = ref()

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const passwordRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }, { min: 6, message: '密码至少6位', trigger: 'blur' }],
  confirmPassword: [{ required: true, message: '请确认新密码', trigger: 'blur' }]
}

const handleSave = () => {
  ElMessage.success('设置已保存')
}

const handlePasswordChange = async () => {
  const valid = await passwordRef.value.validate().catch(() => false)
  if (!valid) return
  
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    ElMessage.error('两次输入的密码不一致')
    return
  }
  
  ElMessage.success('密码修改成功')
  showPasswordDialog.value = false
}

const handleBackup = () => {
  ElMessage.success('数据备份成功')
}

const handleRestore = () => {
  ElMessageBox.confirm('确定要恢复数据吗？这将覆盖当前数据', '提示', { type: 'warning' })
    .then(() => {
      ElMessage.success('数据恢复成功')
    })
    .catch(() => {})
}

const handleClear = () => {
  ElMessageBox.confirm('确定要清除所有数据吗？此操作不可恢复', '警告', { type: 'warning' })
    .then(() => {
      ElMessage.success('数据已清除')
    })
    .catch(() => {})
}
</script>

<style scoped>
.settings-page {
  max-width: 800px;
}
</style>
