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
    
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>话术类型分布</template>
          <div class="type-distribution">
            <div class="type-item">
              <span class="type-name">公司话术</span>
              <el-progress :percentage="45" color="#07C160" />
            </div>
            <div class="type-item">
              <span class="type-name">小组话术</span>
              <el-progress :percentage="30" color="#409EFF" />
            </div>
            <div class="type-item">
              <span class="type-name">私人话术</span>
              <el-progress :percentage="25" color="#E6A23C" />
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>最近活动</template>
          <el-timeline>
            <el-timeline-item
              v-for="(activity, index) in activities"
              :key="index"
              :timestamp="activity.time">
              {{ activity.content }}
            </el-timeline-item>
          </el-timeline>
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
const activities = ref([])

onMounted(() => {
  // 模拟统计数据
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
    { title: '好的，马上为您处理', usage_count: 256 },
    { title: '抱歉让您久等了', usage_count: 198 },
    { title: '请稍等，正在为您查询', usage_count: 187 },
    { title: '感谢您的耐心等待', usage_count: 165 },
    { title: '祝您生活愉快', usage_count: 143 },
    { title: '欢迎再次光临', usage_count: 132 }
  ]
  
  activities.value = [
    { content: '张三添加了新话术', time: '2024-05-18 10:30' },
    { content: '李四使用了话术"欢迎光临"', time: '2024-05-18 09:15' },
    { content: '王五加入了团队', time: '2024-05-17 16:20' },
    { content: '系统更新了话术库', time: '2024-05-17 08:00' },
    { content: '张三修改了话术', time: '2024-05-16 14:30' }
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
  color: #07C160;
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

.type-distribution {
  padding: 20px;
}

.type-item {
  margin-bottom: 20px;
}

.type-name {
  display: block;
  margin-bottom: 8px;
  color: #606266;
}
</style>
