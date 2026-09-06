<script setup name="Server">
import { getServer } from '@/api/monitor/server'
import {
  DesktopOutlined,
  DatabaseOutlined,
  MonitorOutlined,
  CoffeeOutlined,
  HddOutlined,
} from '@ant-design/icons-vue'

const server = ref({})
const proxy = inject('proxy')
const isMobile = ref(false)
const pageRef = useTemplateRef('pageRef')
const pageStyle = ref({})

const layout = {
  xl: 12,
  lg: 12,
  md: 12,
  sm: 24,
  xs: 24,
}

const cpuColumns = [
  { title: '属性', dataIndex: 'property', width: 120 },
  { title: '值', dataIndex: 'value' },
]

const memoryColumns = [
  { title: '属性', dataIndex: 'property', width: 120 },
  { title: '内存', dataIndex: 'memory' },
  { title: 'JVM', dataIndex: 'jvm' },
]

const serverInfoColumns = [
  { title: '属性', dataIndex: 'property1', width: 120 },
  { title: '值', dataIndex: 'value1' },
  { title: '属性', dataIndex: 'property2', width: 120 },
  { title: '值', dataIndex: 'value2' },
]

const jvmColumns = [
  { title: '属性', dataIndex: 'property1', width: 120 },
  { title: '值', dataIndex: 'value1' },
  { title: '属性', dataIndex: 'property2', width: 120 },
  { title: '值', dataIndex: 'value2' },
]

const diskColumns = [
  { title: '盘符路径', dataIndex: 'dirName' },
  { title: '文件系统', dataIndex: 'sysTypeName' },
  { title: '盘符类型', dataIndex: 'typeName' },
  { title: '总大小', dataIndex: 'total' },
  { title: '可用大小', dataIndex: 'free' },
  { title: '已用大小', dataIndex: 'used' },
  { title: '已用百分比', dataIndex: 'usage', key: 'usage' },
]

const cpuData = computed(() => {
  if (!server.value.cpu) return []
  return [
    { property: '核心数', value: server.value.cpu.cpuNum },
    { property: '用户使用率', value: `${server.value.cpu.used}%` },
    { property: '系统使用率', value: `${server.value.cpu.sys}%` },
    { property: '当前空闲率', value: `${server.value.cpu.free}%` },
  ]
})

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

const diskData = computed(() => server.value.sysFiles || [])

const checkMobile = () => {
  isMobile.value = window.innerWidth < 768
}

const updatePageHeight = () => {
  const el = pageRef.value
  if (!el) return
  const { top } = el.getBoundingClientRect()
  const bottomSpace =
    Number.parseFloat(
      getComputedStyle(document.documentElement).getPropertyValue(
        '--ba-main-space'
      )
    ) || 16
  pageStyle.value = {
    maxHeight: `${window.innerHeight - top - bottomSpace}px`,
  }
}

const getList = () => {
  proxy.$modal.loading('正在加载服务监控数据，请稍候！')
  getServer().then((response) => {
    server.value = response.data
    proxy.$modal.closeLoading()
  })
}

onMounted(() => {
  checkMobile()
  window.addEventListener('resize', checkMobile)
  window.addEventListener('resize', updatePageHeight)
  getList()
  nextTick(updatePageHeight)
})

onActivated(() => {
  nextTick(updatePageHeight)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', checkMobile)
  window.removeEventListener('resize', updatePageHeight)
})
</script>

<template>
  <div ref="pageRef" class="server-page default-main" :style="pageStyle">
    <a-row :gutter="10">
      <a-col v-bind="layout" class="mb10">
        <a-card>
          <template #title>
            <DesktopOutlined />
            <span class="card-title">CPU</span>
          </template>
          <a-table
            v-show="!isMobile"
            :columns="cpuColumns"
            :data-source="cpuData"
            :pagination="false"
            bordered
            size="small"
            row-key="property"
          />
          <div v-show="isMobile" class="mobile-list">
            <div
              v-for="item in cpuData"
              :key="item.property"
              class="mobile-item"
            >
              <div class="mobile-label">{{ item.property }}</div>
              <div class="mobile-value">{{ item.value }}</div>
            </div>
          </div>
        </a-card>
      </a-col>

      <a-col v-bind="layout" class="mb10">
        <a-card>
          <template #title>
            <DatabaseOutlined />
            <span class="card-title">内存</span>
          </template>
          <a-table
            v-show="!isMobile"
            :columns="memoryColumns"
            :data-source="memoryData"
            :pagination="false"
            bordered
            size="small"
            row-key="property"
          />
          <div v-show="isMobile" class="mobile-list">
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
        </a-card>
      </a-col>

      <a-col :span="24" class="mb10">
        <a-card>
          <template #title>
            <MonitorOutlined />
            <span class="card-title">服务器信息</span>
          </template>
          <a-table
            v-show="!isMobile"
            :columns="serverInfoColumns"
            :data-source="serverInfoData"
            :pagination="false"
            bordered
            size="small"
            row-key="property1"
          />
          <div v-show="isMobile" class="mobile-list">
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
        </a-card>
      </a-col>

      <a-col :span="24" class="mb10">
        <a-card>
          <template #title>
            <CoffeeOutlined />
            <span class="card-title">Java虚拟机信息</span>
          </template>
          <a-table
            v-show="!isMobile"
            :columns="jvmColumns"
            :data-source="jvmData"
            :pagination="false"
            bordered
            size="small"
            row-key="property1"
          />
          <div v-show="isMobile" class="mobile-list">
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
        </a-card>
      </a-col>

      <a-col :span="24" class="mb10">
        <a-card>
          <template #title>
            <HddOutlined />
            <span class="card-title">磁盘状态</span>
          </template>
          <a-table
            v-show="!isMobile"
            :columns="diskColumns"
            :data-source="diskData"
            :pagination="false"
            bordered
            size="small"
            row-key="dirName"
          >
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'usage'">
                <span :class="{ 'text-danger': record.usage > 80 }">
                  {{ record.usage }}%
                </span>
              </template>
            </template>
          </a-table>
          <div v-show="isMobile" class="mobile-list">
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
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<style scoped lang="scss">
.server-page {
  overflow-x: hidden;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
}

.card-title {
  margin-left: 6px;
}

.text-danger {
  color: #ff4d4f;
}

.mobile-list {
  .mobile-item {
    padding: 12px;
    border-bottom: 1px solid var(--ba-border-color);

    &:last-child {
      border-bottom: none;
    }

    .mobile-label {
      font-weight: 600;
      margin-bottom: 8px;
      font-size: 14px;
    }

    .mobile-value {
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
          font-size: 14px;
          min-width: 80px;
        }

        .value-text {
          font-size: 14px;
          text-align: right;
          flex: 1;
          word-break: break-all;
        }
      }
    }
  }
}

.disk-item {
  .disk-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;
    padding-bottom: 8px;
    border-bottom: 1px solid var(--ba-border-color);

    .disk-path {
      font-weight: 600;
      font-size: 15px;
    }

    .disk-usage {
      font-weight: 600;
      font-size: 15px;
      color: #52c41a;

      &.text-danger {
        color: #ff4d4f;
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
        font-size: 13px;
        min-width: 70px;
      }

      .detail-value {
        font-size: 13px;
        text-align: right;
        flex: 1;
        word-break: break-all;
      }
    }
  }
}

@media (max-width: 767px) {
  .server-page {
    margin: 10px;
  }

  .mobile-item {
    transition: background-color 0.2s ease;

    &:active {
      background-color: rgba(0, 0, 0, 0.04);
    }
  }
}
</style>
