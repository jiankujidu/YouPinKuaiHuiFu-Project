<template>
  <div class="stats-page">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.totalPhrases }}</div>
          <div class="stat-label">话术总数</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.totalUsage }}</div>
          <div class="stat-label">使用次数</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.teamMembers }}</div>
          <div class="stat-label">团队成员</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.todayUsage }}</div>
          <div class="stat-label">今日使用</div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>热门话术 TOP10</template>
          <el-table :data="hotPhrases" size="small">
            <el-table-column type="index" width="50" />
            <el-table-column prop="title" label="话术标题" />
            <el-table-column prop="usage_count" label="使用次数" width="100" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>使用趋势</template>
          <div class="chart-placeholder">
            <el-empty description="图表功能开发中" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const stats = ref({
  totalPhrases: 0,
  totalUsage: 0,
  teamMembers: 0,
  todayUsage: 0
})

const hotPhrases = ref([])

onMounted(() => {
  // TODO: 获取统计数据
  stats.value = {
    totalPhrases: 156,
    totalUsage: 3289,
    teamMembers: 8,
    todayUsage: 45
  }
  
  hotPhrases.value = [
    { title: '欢迎光临，请问有什么可以帮您？', usage_count: 523 },
    { title: '亲，您的订单已发货', usage_count: 412 },
    { title: '感谢您的购买，期待下次光临', usage_count: 389 },
    { title: '请问还有其他问题吗？', usage_count: 298 },
    { title: '好的，马上为您处理', usage_count: 256 }
  ]
})
</script>

<style scoped>
.stat-card {
  text-align: center;
  padding: 20px;
}

.stat-value {
  font-size: 36px;
  font-weight: bold;
  color: #409EFF;
}

.stat-label {
  margin-top: 10px;
  color: #909399;
}

.chart-placeholder {
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
