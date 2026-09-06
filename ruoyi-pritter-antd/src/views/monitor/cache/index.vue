<script setup name="Cache">
import * as echarts from 'echarts'
import { getCache } from '@/api/monitor/cache'
import {
  DashboardOutlined,
  PieChartOutlined,
  DashboardTwoTone,
} from '@ant-design/icons-vue'

const cache = ref({})
const commandstatsRef = useTemplateRef('commandstatsRef')
const usedmemoryRef = useTemplateRef('usedmemoryRef')
const proxy = inject('proxy')

let commandChart
let memoryChart

const layout = {
  xl: 12,
  lg: 12,
  md: 12,
  sm: 24,
  xs: 24,
}

const infoItems = computed(() => {
  const info = cache.value.info || {}
  return [
    { label: 'Redis版本', value: info.redis_version },
    {
      label: '运行模式',
      value: info.redis_mode === 'standalone' ? '单机' : '集群',
    },
    { label: '端口', value: info.tcp_port },
    { label: '客户端数', value: info.connected_clients },
    { label: '运行时间(天)', value: info.uptime_in_days },
    { label: '使用内存', value: info.used_memory_human },
    {
      label: '使用CPU',
      value: info.used_cpu_user_children
        ? parseFloat(info.used_cpu_user_children).toFixed(2)
        : '',
    },
    { label: '内存配置', value: info.maxmemory_human },
    { label: 'AOF是否开启', value: info.aof_enabled === '0' ? '否' : '是' },
    { label: 'RDB是否成功', value: info.rdb_last_bgsave_status },
    { label: 'Key数量', value: cache.value.dbSize },
    {
      label: '网络入口/出口',
      value: info.instantaneous_input_kbps
        ? `${info.instantaneous_input_kbps}kps/${info.instantaneous_output_kbps}kps`
        : '',
    },
  ]
})

const initCharts = (data) => {
  if (commandstatsRef.value) {
    commandChart?.dispose()
    commandChart = echarts.init(commandstatsRef.value)
    commandChart.setOption({
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b} : {c} ({d}%)',
      },
      series: [
        {
          name: '命令',
          type: 'pie',
          roseType: 'radius',
          radius: [15, 95],
          center: ['50%', '38%'],
          data: data.commandStats,
          animationEasing: 'cubicInOut',
          animationDuration: 1000,
        },
      ],
    })
  }

  if (usedmemoryRef.value) {
    memoryChart?.dispose()
    memoryChart = echarts.init(usedmemoryRef.value)
    memoryChart.setOption({
      tooltip: {
        formatter: `{b} <br/>{a} : ${data.info?.used_memory_human}`,
      },
      series: [
        {
          name: '峰值',
          type: 'gauge',
          min: 0,
          max: 1000,
          detail: {
            formatter: data.info?.used_memory_human,
          },
          data: [
            {
              value: parseFloat(data.info?.used_memory_human) || 0,
              name: '内存消耗',
            },
          ],
        },
      ],
    })
  }
}

const handleResize = () => {
  commandChart?.resize()
  memoryChart?.resize()
}

const getList = () => {
  proxy.$modal.loading('正在加载缓存监控数据，请稍候！')
  getCache().then((response) => {
    proxy.$modal.closeLoading()
    cache.value = response.data
    nextTick(() => initCharts(response.data))
  })
}

onMounted(() => {
  getList()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  commandChart?.dispose()
  memoryChart?.dispose()
})
</script>

<template>
  <div class="cache-page default-main">
    <a-row :gutter="10">
      <a-col :span="24" class="mb10">
        <a-card>
          <template #title>
            <DashboardOutlined />
            <span class="card-title">基本信息</span>
          </template>
          <a-descriptions bordered :column="4" size="small">
            <a-descriptions-item
              v-for="item in infoItems"
              :key="item.label"
              :label="item.label"
            >
              {{ item.value ?? '-' }}
            </a-descriptions-item>
          </a-descriptions>
        </a-card>
      </a-col>

      <a-col v-bind="layout" class="mb10">
        <a-card>
          <template #title>
            <PieChartOutlined />
            <span class="card-title">命令统计</span>
          </template>
          <div ref="commandstatsRef" class="chart-box" />
        </a-card>
      </a-col>

      <a-col v-bind="layout" class="mb10">
        <a-card>
          <template #title>
            <DashboardTwoTone />
            <span class="card-title">内存信息</span>
          </template>
          <div ref="usedmemoryRef" class="chart-box" />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<style scoped lang="scss">
.card-title {
  margin-left: 6px;
}

.chart-box {
  height: 420px;
}

.mb10 {
  margin-bottom: 10px;
}
</style>
