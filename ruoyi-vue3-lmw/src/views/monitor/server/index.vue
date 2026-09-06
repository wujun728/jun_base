<template>
  <div class="serverPage">
    <el-row :gutter="10">
      <!-- CPU 信息卡片 -->
      <el-col v-bind="layout" class="mb10">
        <el-card>
          <template #header>
            <Cpu style="width: 1em; height: 1em; vertical-align: middle" />
            <span style="vertical-align: middle" class="ml5">CPU</span>
          </template>
          <!-- 桌面端表格 -->
          <el-table
            :data="cpuData"
            border
            stripe
            class="hidden-xs-only"
            :size="isMobile ? 'small' : 'default'"
          >
            <el-table-column prop="property" label="属性" width="120" />
            <el-table-column prop="value" label="值" />
          </el-table>
          <!-- 移动端卡片列表 -->
          <div class="mobile-list visible-xs-only">
            <div
              v-for="item in cpuData"
              :key="item.property"
              class="mobile-item"
            >
              <div class="mobile-label">{{ item.property }}</div>
              <div class="mobile-value">{{ item.value }}</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 内存信息卡片 -->
      <el-col v-bind="layout" class="mb10">
        <el-card>
          <template #header>
            <Tickets style="width: 1em; height: 1em; vertical-align: middle" />
            <span style="vertical-align: middle" class="ml5">内存</span>
          </template>
          <!-- 桌面端表格 -->
          <el-table
            :data="memoryData"
            border
            stripe
            class="hidden-xs-only"
            :size="isMobile ? 'small' : 'default'"
          >
            <el-table-column prop="property" label="属性" width="120" />
            <el-table-column prop="memory" label="内存" />
            <el-table-column prop="jvm" label="JVM" />
          </el-table>
          <!-- 移动端卡片列表 -->
          <div class="mobile-list visible-xs-only">
            <div
              v-for="item in memoryData"
              :key="item.property"
              class="mobile-item"
            >
              <div class="mobile-label">{{ item.property }}</div>
              <div class="mobile-values">
                <div class="mobile-value-item">
                  <span class="value-label">内存:</span>
                  <span class="value-text">{{ item.memory }}</span>
                </div>
                <div class="mobile-value-item">
                  <span class="value-label">JVM:</span>
                  <span class="value-text">{{ item.jvm }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 服务器信息卡片 -->
      <el-col :span="24" class="mb10">
        <el-card>
          <template #header>
            <Monitor style="width: 1em; height: 1em; vertical-align: middle" />
            <span style="vertical-align: middle" class="ml5">服务器信息</span>
          </template>
          <!-- 桌面端表格 -->
          <el-table
            :data="serverInfoData"
            border
            stripe
            class="hidden-xs-only"
            :size="isMobile ? 'small' : 'default'"
          >
            <el-table-column prop="property1" label="属性" width="120" />
            <el-table-column prop="value1" label="值" />
            <el-table-column prop="property2" label="属性" width="120" />
            <el-table-column prop="value2" label="值" />
          </el-table>
          <!-- 移动端卡片列表 -->
          <div class="mobile-list visible-xs-only">
            <div
              v-for="item in serverInfoData"
              :key="item.property1"
              class="mobile-item"
            >
              <div class="mobile-values">
                <div class="mobile-value-item">
                  <span class="value-label">{{ item.property1 }}:</span>
                  <span class="value-text">{{ item.value1 }}</span>
                </div>
                <div class="mobile-value-item">
                  <span class="value-label">{{ item.property2 }}:</span>
                  <span class="value-text">{{ item.value2 }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- JVM信息卡片 -->
      <el-col :span="24" class="mb10">
        <el-card>
          <template #header>
            <CoffeeCup
              style="width: 1em; height: 1em; vertical-align: middle"
            />
            <span style="vertical-align: middle" class="ml5">
              Java虚拟机信息
            </span>
          </template>
          <!-- 桌面端表格 -->
          <el-table
            :data="jvmData"
            border
            stripe
            class="hidden-xs-only"
            :size="isMobile ? 'small' : 'default'"
          >
            <el-table-column prop="property1" label="属性" width="120" />
            <el-table-column prop="value1" label="值" />
            <el-table-column prop="property2" label="属性" width="120" />
            <el-table-column prop="value2" label="值" />
          </el-table>
          <!-- 移动端卡片列表 -->
          <div class="mobile-list visible-xs-only">
            <div
              v-for="item in jvmData"
              :key="item.property1"
              class="mobile-item"
            >
              <div class="mobile-values">
                <div class="mobile-value-item">
                  <span class="value-label">{{ item.property1 }}:</span>
                  <span class="value-text">{{ item.value1 }}</span>
                </div>
                <div v-if="item.property2" class="mobile-value-item">
                  <span class="value-label">{{ item.property2 }}:</span>
                  <span class="value-text">{{ item.value2 }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 磁盘状态卡片 -->
      <el-col :span="24" class="mb10">
        <el-card>
          <template #header>
            <MessageBox
              style="width: 1em; height: 1em; vertical-align: middle"
            />
            <span style="vertical-align: middle" class="ml5">磁盘状态 </span>
          </template>
          <!-- 桌面端表格 -->
          <el-table
            :data="diskData"
            border
            stripe
            class="hidden-xs-only"
            :size="isMobile ? 'small' : 'default'"
          >
            <el-table-column prop="dirName" label="盘符路径" />
            <el-table-column prop="sysTypeName" label="文件系统" />
            <el-table-column prop="typeName" label="盘符类型" />
            <el-table-column prop="total" label="总大小" />
            <el-table-column prop="free" label="可用大小" />
            <el-table-column prop="used" label="已用大小" />
            <el-table-column prop="usage" label="已用百分比">
              <template #default="scope">
                <span :class="{ 'text-danger': scope.row.usage > 80 }">
                  {{ scope.row.usage }}%
                </span>
              </template>
            </el-table-column>
          </el-table>
          <!-- 移动端卡片列表 -->
          <div class="mobile-list visible-xs-only">
            <div
              v-for="(item, index) in diskData"
              :key="index"
              class="mobile-item disk-item"
            >
              <div class="disk-header">
                <span class="disk-path">{{ item.dirName }}</span>
                <span
                  class="disk-usage"
                  :class="{ 'text-danger': item.usage > 80 }"
                >
                  {{ item.usage }}%
                </span>
              </div>
              <div class="disk-details">
                <div class="disk-detail-item">
                  <span class="detail-label">文件系统:</span>
                  <span class="detail-value">{{ item.sysTypeName }}</span>
                </div>
                <div class="disk-detail-item">
                  <span class="detail-label">盘符类型:</span>
                  <span class="detail-value">{{ item.typeName }}</span>
                </div>
                <div class="disk-detail-item">
                  <span class="detail-label">总大小:</span>
                  <span class="detail-value">{{ item.total }}</span>
                </div>
                <div class="disk-detail-item">
                  <span class="detail-label">可用大小:</span>
                  <span class="detail-value">{{ item.free }}</span>
                </div>
                <div class="disk-detail-item">
                  <span class="detail-label">已用大小:</span>
                  <span class="detail-value">{{ item.used }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { getServer } from '@/api/monitor/server'

const server = ref([])
const proxy = inject('proxy')

// 响应式布局配置
const layout = computed(() => {
  return {
    xl: 12,
    lg: 12,
    md: 12,
    sm: 24,
    xs: 24,
  }
})

// 检测是否为移动端
const isMobile = ref(false)

// 监听窗口大小变化
const checkMobile = () => {
  isMobile.value = window.innerWidth < 768
}

onMounted(() => {
  checkMobile()
  window.addEventListener('resize', checkMobile)
})

onUnmounted(() => {
  window.removeEventListener('resize', checkMobile)
})

// 计算属性：CPU数据
const cpuData = computed(() => {
  if (!server.value.cpu) return []
  return [
    { property: '核心数', value: server.value.cpu.cpuNum },
    { property: '用户使用率', value: `${server.value.cpu.used}%` },
    { property: '系统使用率', value: `${server.value.cpu.sys}%` },
    { property: '当前空闲率', value: `${server.value.cpu.free}%` },
  ]
})

// 计算属性：内存数据
const memoryData = computed(() => {
  if (!server.value.mem || !server.value.jvm) return []
  return [
    {
      property: '总内存',
      memory: `${server.value.mem.total}G`,
      jvm: `${server.value.jvm.total}M`,
    },
    {
      property: '已用内存',
      memory: `${server.value.mem.used}G`,
      jvm: `${server.value.jvm.used}M`,
    },
    {
      property: '剩余内存',
      memory: `${server.value.mem.free}G`,
      jvm: `${server.value.jvm.free}M`,
    },
    {
      property: '使用率',
      memory: `${server.value.mem.usage}%`,
      jvm: `${server.value.jvm.usage}%`,
    },
  ]
})

// 计算属性：服务器信息数据
const serverInfoData = computed(() => {
  if (!server.value.sys) return []
  return [
    {
      property1: '服务器名称',
      value1: server.value.sys.computerName,
      property2: '操作系统',
      value2: server.value.sys.osName,
    },
    {
      property1: '服务器IP',
      value1: server.value.sys.computerIp,
      property2: '系统架构',
      value2: server.value.sys.osArch,
    },
  ]
})

// 计算属性：JVM数据
const jvmData = computed(() => {
  if (!server.value.jvm || !server.value.sys) return []
  return [
    {
      property1: 'Java名称',
      value1: server.value.jvm.name,
      property2: 'Java版本',
      value2: server.value.jvm.version,
    },
    {
      property1: '启动时间',
      value1: server.value.jvm.startTime,
      property2: '运行时长',
      value2: server.value.jvm.runTime,
    },
    {
      property1: '安装路径',
      value1: server.value.jvm.home,
      property2: '',
      value2: '',
    },
    {
      property1: '项目路径',
      value1: server.value.sys.userDir,
      property2: '',
      value2: '',
    },
    {
      property1: '运行参数',
      value1: server.value.jvm.inputArgs,
      property2: '',
      value2: '',
    },
  ]
})

// 计算属性：磁盘数据
const diskData = computed(() => {
  if (!server.value.sysFiles) return []
  return server.value.sysFiles
})

function getList() {
  proxy.$modal.loading('正在加载服务监控数据，请稍候！')
  getServer().then((response) => {
    server.value = response.data
    proxy.$modal.closeLoading()
  })
}

getList()
</script>

<style lang="scss" scoped>
.serverPage {
  margin: var(--ba-main-space) var(--ba-main-space) 5px var(--ba-main-space);
}

.text-danger {
  color: #f56c6c;
}

// 移动端样式
.mobile-list {
  .mobile-item {
    padding: 12px;
    border-bottom: 1px solid #ebeef5;

    &:last-child {
      border-bottom: none;
    }

    .mobile-label {
      font-weight: 600;
      color: #606266;
      margin-bottom: 8px;
      font-size: 14px;
    }

    .mobile-value {
      color: #303133;
      font-size: 14px;
    }

    .mobile-values {
      .mobile-value-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 8px;

        &:last-child {
          margin-bottom: 0;
        }

        .value-label {
          font-weight: 600;
          color: #606266;
          font-size: 14px;
          min-width: 80px;
        }

        .value-text {
          color: #303133;
          font-size: 14px;
          text-align: right;
          flex: 1;
          word-break: break-all;
        }
      }
    }
  }
}

// 磁盘项特殊样式
.disk-item {
  .disk-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;
    padding-bottom: 8px;
    border-bottom: 1px solid #f0f0f0;

    .disk-path {
      font-weight: 600;
      color: #303133;
      font-size: 15px;
    }

    .disk-usage {
      font-weight: 600;
      font-size: 15px;
      color: #67c23a;

      &.text-danger {
        color: #f56c6c;
      }
    }
  }

  .disk-details {
    .disk-detail-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 6px;

      &:last-child {
        margin-bottom: 0;
      }

      .detail-label {
        color: #606266;
        font-size: 13px;
        min-width: 70px;
      }

      .detail-value {
        color: #303133;
        font-size: 13px;
        text-align: right;
        flex: 1;
        word-break: break-all;
      }
    }
  }
}

// 响应式隐藏/显示类
.hidden-xs-only {
  @media (max-width: 767px) {
    display: none !important;
  }
}

.visible-xs-only {
  @media (min-width: 768px) {
    display: none !important;
  }
}

// 移动端卡片间距优化
@media (max-width: 767px) {
  .serverPage {
    margin: 10px;
  }

  .mb10 {
    margin-bottom: 10px !important;
  }

  .el-card {
    margin-bottom: 10px;

    :deep(.el-card__header) {
      padding: 12px 15px;
    }

    :deep(.el-card__body) {
      padding: 15px;
    }
  }
}

// 触摸优化
@media (max-width: 767px) {
  .mobile-item {
    transition: background-color 0.2s ease;

    &:active {
      background-color: #f5f7fa;
    }
  }
}
</style>
