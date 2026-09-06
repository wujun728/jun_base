<template>
  <a-modal
    title="文件统计"
    :open="visible"
    width="900px"
    :footer="null"
    @cancel="handleClose"
  >
    <div v-loading="loading" class="stats-container">
      <!-- 总体统计 -->
      <a-row :gutter="16" class="stats-overview">
        <a-col :span="6">
          <a-card class="stat-card">
            <a-statistic
              title="文件总数"
              :value="statsData.totalFiles"
              :value-style="{ color: '#1890ff' }"
            >
              <template #prefix>
                <BearJiaIcon icon="file-outlined" />
              </template>
            </a-statistic>
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card class="stat-card">
            <a-statistic
              title="总存储空间"
              :value="formatFileSize(statsData.totalSize)"
              :value-style="{ color: '#52c41a' }"
            >
              <template #prefix>
                <BearJiaIcon icon="hdd-outlined" />
              </template>
            </a-statistic>
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card class="stat-card">
            <a-statistic
              title="今日上传"
              :value="statsData.todayUploads"
              :value-style="{ color: '#fa8c16' }"
            >
              <template #prefix>
                <BearJiaIcon icon="upload-outlined" />
              </template>
            </a-statistic>
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card class="stat-card">
            <a-statistic
              title="总下载次数"
              :value="statsData.totalDownloads"
              :value-style="{ color: '#722ed1' }"
            >
              <template #prefix>
                <BearJiaIcon icon="download-outlined" />
              </template>
            </a-statistic>
          </a-card>
        </a-col>
      </a-row>

      <!-- 详细统计 -->
      <a-row :gutter="16" class="stats-details">
        <!-- 业务类型分布 -->
        <a-col :span="12">
          <a-card title="业务类型分布" class="chart-card">
            <div ref="businessTypeChartRef" class="chart-container"></div>
          </a-card>
        </a-col>

        <!-- 文件类型分布 -->
        <a-col :span="12">
          <a-card title="文件类型分布" class="chart-card">
            <div ref="fileTypeChartRef" class="chart-container"></div>
          </a-card>
        </a-col>

        <!-- 文件状态统计 -->
        <a-col :span="12">
          <a-card title="文件状态统计" class="chart-card">
            <a-table 
              :data-source="statsData.statusStats" 
              :columns="statusColumns"
              :pagination="false"
              size="small"
            />
          </a-card>
        </a-col>

        <!-- 存储空间使用 -->
        <a-col :span="12">
          <a-card title="存储空间使用" class="chart-card">
            <div class="storage-stats">
              <div class="storage-item" v-for="item in statsData.storageStats" :key="item.type">
                <div class="storage-label">{{ item.label }}</div>
                <div class="storage-bar">
                  <a-progress 
                    :percent="item.percentage" 
                    :stroke-color="item.color"
                    :show-info="false"
                  />
                </div>
                <div class="storage-value">{{ formatFileSize(item.size) }}</div>
              </div>
            </div>
          </a-card>
        </a-col>

        <!-- 上传趋势 -->
        <a-col :span="24">
          <a-card title="上传趋势（最近7天）" class="chart-card">
            <div ref="uploadTrendChartRef" class="chart-container trend-chart"></div>
          </a-card>
        </a-col>
      </a-row>

      <!-- 系统建议 -->
      <a-card title="系统建议" class="suggestions-card">
        <a-list :data-source="suggestions" size="small">
          <template #renderItem="{ item }">
            <a-list-item>
              <a-list-item-meta>
                <template #avatar>
                  <a-avatar :style="{ backgroundColor: item.color }">
                    <BearJiaIcon :icon="item.icon" />
                  </a-avatar>
                </template>
                <template #title>{{ item.title }}</template>
                <template #description>{{ item.description }}</template>
              </a-list-item-meta>
            </a-list-item>
          </template>
        </a-list>
      </a-card>
    </div>

    <template #footer>
      <a-space>
        <a-button @click="handleClose">关闭</a-button>
        <a-button type="primary" @click="handleRefresh">
          <BearJiaIcon icon="reload-outlined"/>
          刷新数据
        </a-button>
        <a-button @click="handleExport">
          <BearJiaIcon icon="export-outlined"/>
          导出报告
        </a-button>
      </a-space>
    </template>
  </a-modal>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick, getCurrentInstance } from 'vue';
import { getFileStats } from '@/api/system/file';

// 获取当前组件实例
const { proxy } = getCurrentInstance();

// 响应式数据
const visible = ref(false);
const loading = ref(false);
const businessTypeChartRef = ref();
const fileTypeChartRef = ref();
const uploadTrendChartRef = ref();

// 统计数据
const statsData = reactive({
  totalFiles: 0,
  totalSize: 0,
  todayUploads: 0,
  totalDownloads: 0,
  statusStats: [],
  storageStats: [],
  businessTypeStats: [],
  fileTypeStats: [],
  uploadTrend: []
});

// 表格列配置
const statusColumns = [
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    render: (text) => {
      const statusMap = {
        0: '已删除',
        1: '正常',
        2: '处理中',
        3: '处理失败'
      };
      return statusMap[text] || '未知';
    }
  },
  {
    title: '数量',
    dataIndex: 'count',
    key: 'count'
  },
  {
    title: '占比',
    dataIndex: 'percentage',
    key: 'percentage',
    render: (text) => `${text}%`
  }
];

// 系统建议
const suggestions = ref([
  {
    icon: 'warning-outlined',
    color: '#fa8c16',
    title: '存储空间优化',
    description: '建议定期清理已删除的文件记录，释放存储空间'
  },
  {
    icon: 'safety-certificate-outlined',
    color: '#52c41a',
    title: '文件安全',
    description: '建议对重要文件进行备份，确保数据安全'
  },
  {
    icon: 'thunderbolt-outlined',
    color: '#1890ff',
    title: '性能优化',
    description: '建议对大文件进行压缩处理，提升传输效率'
  }
]);

// 方法
const open = () => {
  visible.value = true;
  loadStats();
};

const handleClose = () => {
  visible.value = false;
};

const handleRefresh = () => {
  loadStats();
};

const handleExport = () => {
  // 导出统计报告
  proxy.$message.info('导出功能开发中...');
};

const loadStats = async () => {
  loading.value = true;
  try {
    // 模拟数据，实际应该调用API
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    // 更新统计数据
    Object.assign(statsData, {
      totalFiles: 1234,
      totalSize: 5368709120, // 5GB
      todayUploads: 23,
      totalDownloads: 5678,
      statusStats: [
        { status: 1, count: 1100, percentage: 89.1 },
        { status: 2, count: 34, percentage: 2.8 },
        { status: 3, count: 50, percentage: 4.1 },
        { status: 0, count: 50, percentage: 4.1 }
      ],
      storageStats: [
        { type: 'rag', label: 'RAG文档', size: 3221225472, percentage: 60, color: '#1890ff' },
        { type: 'avatar', label: '用户头像', size: 536870912, percentage: 10, color: '#52c41a' },
        { type: 'attachment', label: '系统附件', size: 1073741824, percentage: 20, color: '#fa8c16' },
        { type: 'common', label: '通用文件', size: 536870912, percentage: 10, color: '#722ed1' }
      ],
      businessTypeStats: [
        { name: 'RAG文档', value: 740 },
        { name: '用户头像', value: 234 },
        { name: '系统附件', value: 160 },
        { name: '通用文件', value: 100 }
      ],
      fileTypeStats: [
        { name: 'PDF', value: 450 },
        { name: 'Word', value: 320 },
        { name: '图片', value: 280 },
        { name: 'Excel', value: 184 }
      ],
      uploadTrend: [
        { date: '2025-09-19', count: 12 },
        { date: '2025-09-20', count: 19 },
        { date: '2025-09-21', count: 15 },
        { date: '2025-09-22', count: 22 },
        { date: '2025-09-23', count: 18 },
        { date: '2025-09-24', count: 25 },
        { date: '2025-09-25', count: 23 }
      ]
    });

    // 渲染图表
    nextTick(() => {
      renderCharts();
    });

  } catch (error) {
    proxy.$message.error('加载统计数据失败');
  } finally {
    loading.value = false;
  }
};

const renderCharts = () => {
  // 这里应该使用图表库（如ECharts）来渲染图表
  // 由于没有引入图表库，这里只是占位
  console.log('渲染图表...');
};

const formatFileSize = (size) => {
  if (!size) return '0 B';
  const units = ['B', 'KB', 'MB', 'GB', 'TB'];
  let index = 0;
  while (size >= 1024 && index < units.length - 1) {
    size /= 1024;
    index++;
  }
  return `${size.toFixed(2)} ${units[index]}`;
};

// 暴露方法
defineExpose({
  open
});
</script>

<style scoped>
.stats-container {
  min-height: 400px;
}

.stats-overview {
  margin-bottom: 24px;
}

.stat-card {
  text-align: center;
}

.stats-details {
  margin-bottom: 24px;
}

.chart-card {
  height: 300px;
}

.chart-container {
  height: 200px;
  width: 100%;
}

.trend-chart {
  height: 250px;
}

.storage-stats {
  padding: 16px 0;
}

.storage-item {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.storage-item:last-child {
  margin-bottom: 0;
}

.storage-label {
  width: 80px;
  font-size: 14px;
  color: #666;
}

.storage-bar {
  flex: 1;
  margin: 0 12px;
}

.storage-value {
  width: 80px;
  text-align: right;
  font-size: 12px;
  color: #999;
}

.suggestions-card {
  margin-top: 24px;
}
</style>
