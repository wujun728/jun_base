<template>
  <div>
    <ElRow :gutter="16">
      <ElCol
        v-for="item in infoCards"
        :key="item.label"
        :sm="12"
        :md="6"
        :lg="4"
        class="mb-4">
        <div class="art-card flex h-24 flex-col justify-center px-5">
          <span class="text-sm text-g-700">{{ item.label }}</span>
          <span class="mt-1.5 truncate text-lg font-medium text-g-900">
            {{ item.value }}
          </span>
        </div>
      </ElCol>
    </ElRow>

    <ElRow :gutter="16">
      <ElCol :sm="24" :md="12" class="mb-4">
        <div class="art-card p-5">
          <div class="art-card-header">
            <div class="title">
              <h4>命令统计</h4>
            </div>
          </div>
          <ArtRingChart
            class="mt-4"
            :data="commandStatsData"
            :radius="['15%', '75%']"
            :border-radius="8"
            :show-label="true"
            :show-legend="false" />
        </div>
      </ElCol>

      <ElCol :sm="24" :md="12" class="mb-4">
        <div class="art-card p-5">
          <div class="art-card-header">
            <div class="title">
              <h4>内存信息</h4>
            </div>
          </div>
          <ArtGaugeChart
            class="mt-4"
            :value="memoryGaugeData.value"
            :max="memoryGaugeData.max"
            :unit="memoryGaugeData.unit"
            :detail-text="memoryGaugeData.detailText"
            name="内存消耗" />
        </div>
      </ElCol>
    </ElRow>
  </div>
</template>

<script setup lang="ts" name="Cache">
import type { PieDataItem } from '@/types/component/chart'

import { loadingService } from '@/utils/ui/loading'

import { getCache } from './api'

interface InfoCard {
  label: string
  value: string
}

interface CacheInfo {
  redis_version?: string
  redis_mode?: string
  tcp_port?: string
  connected_clients?: string
  uptime_in_days?: string
  used_memory?: string
  used_memory_human?: string
  used_cpu_user_children?: string
  maxmemory?: string
  maxmemory_human?: string
  aof_enabled?: string
  rdb_last_bgsave_status?: string
  instantaneous_input_kbps?: string
  instantaneous_output_kbps?: string
}

interface CacheData {
  commandStats?: PieDataItem[]
  info?: CacheInfo
  dbSize?: number
}

const cache = ref<CacheData>({})

const commandStatsData = computed<PieDataItem[]>(() => {
  if (!cache.value.commandStats) return []
  return cache.value.commandStats.map((item) => ({
    name: item.name,
    value: Number(item.value)
  }))
})

const memoryGaugeData = computed(() => {
  const info = cache.value.info
  const usedMemory = Number(info?.used_memory || 0)
  const maxMemory = Number(info?.maxmemory || 0)
  const value =
    usedMemory > 0 && maxMemory > 0
      ? Number(((usedMemory / maxMemory) * 100).toFixed(2))
      : 0

  return {
    value,
    max: 100,
    unit: '%',
    detailText: info?.used_memory_human || '-'
  }
})

const infoCards = computed<InfoCard[]>(() => {
  const info = cache.value.info
  if (!info) return []
  return [
    { label: 'Redis版本', value: info.redis_version || '-' },
    {
      label: '运行模式',
      value: info.redis_mode === 'standalone' ? '单机' : '集群'
    },
    { label: '端口', value: info.tcp_port || '-' },
    { label: '客户端数', value: info.connected_clients || '-' },
    { label: '运行时间(天)', value: info.uptime_in_days || '-' },
    { label: '使用内存', value: info.used_memory_human || '-' },
    {
      label: '使用CPU',
      value: parseFloat(info.used_cpu_user_children || '0').toFixed(2)
    },
    { label: '内存配置', value: info.maxmemory_human || '-' },
    { label: 'AOF是否开启', value: info.aof_enabled === '0' ? '否' : '是' },
    { label: 'RDB是否成功', value: info.rdb_last_bgsave_status || '-' },
    { label: 'Key数量', value: String(cache.value.dbSize ?? '-') },
    {
      label: '网络入口/出口',
      value: `${info.instantaneous_input_kbps || 0}kps / ${info.instantaneous_output_kbps || 0}kps`
    }
  ]
})

function getList(): void {
  loadingService.showLoading('正在加载缓存监控数据，请稍候！')
  getCache()
    .then((response) => {
      loadingService.hideLoading()
      cache.value = response
    })
    .catch(() => {
      loadingService.hideLoading()
    })
}

onMounted(() => {
  getList()
})
</script>
