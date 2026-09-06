<template>
  <div class="cache-monitor-page art-full-height"
    ><ElSkeleton v-if="loading" :rows="8" animated /><template v-else>
      <ElRow class="cache-monitor-section" :gutter="16"
        ><ElCol v-for="item in overview" :key="item.label" :xs="24" :sm="12" :lg="6"
          ><ElCard class="mb-4"
            ><div class="text-g-500">{{ item.label }}</div
            ><div class="mt-3 text-2xl font-semibold">{{ item.value }}</div></ElCard
          ></ElCol
        ></ElRow
      >
      <ElRow class="cache-monitor-section" :gutter="16"
        ><ElCol :xs="24" :lg="12"
          ><ElCard header="Redis 信息"
            ><ElDescriptions :column="1" border
              ><ElDescriptionsItem
                v-for="item in basicInfo"
                :key="item.label"
                :label="item.label"
                >{{ item.value }}</ElDescriptionsItem
              ></ElDescriptions
            ></ElCard
          ></ElCol
        ><ElCol :xs="24" :lg="12"
          ><ElCard header="内存与网络"
            ><ElDescriptions :column="1" border
              ><ElDescriptionsItem
                v-for="item in memoryInfo"
                :key="item.label"
                :label="item.label"
                >{{ item.value }}</ElDescriptionsItem
              ></ElDescriptions
            ></ElCard
          ></ElCol
        ></ElRow
      >
      <ElCard class="cache-command-card art-table-card" header="命令统计"
        ><ElTable :data="data.commandStats || []" height="100%"
          ><ElTableColumn prop="name" label="命令" /><ElTableColumn
            prop="value"
            label="调用次数" /></ElTable
      ></ElCard> </template
  ></div>
</template>
<script setup lang="ts">
  import { fetchCacheInfo } from '@/api/monitor'
  import type { Entity } from '@/api/system/types'
  defineOptions({ name: 'MonitorCache' })
  const loading = ref(true),
    data = ref<Entity>({})
  const overview = computed(() => [
    { label: '缓存键数量', value: data.value.dbSize ?? 0 },
    { label: 'Redis 版本', value: data.value.info?.redis_version ?? '-' },
    { label: '运行天数', value: data.value.info?.uptime_in_days ?? 0 },
    { label: '内存占用', value: data.value.info?.used_memory_human ?? '-' }
  ])
  const basicInfo = computed(() => [
    { label: 'Redis 版本', value: data.value.info?.redis_version ?? '-' },
    { label: '客户端数量', value: data.value.info?.connected_clients ?? '-' },
    { label: '运行天数', value: data.value.info?.uptime_in_days ?? '-' },
    { label: '缓存键数量', value: data.value.dbSize ?? 0 }
  ])
  const memoryInfo = computed(() => [
    { label: '内存占用', value: data.value.info?.used_memory_human ?? '-' },
    { label: '内存峰值', value: data.value.info?.used_memory_peak_human ?? '-' },
    { label: '网络入口', value: data.value.info?.total_net_input_bytes_human ?? '-' },
    { label: '网络出口', value: data.value.info?.total_net_output_bytes_human ?? '-' }
  ])
  onMounted(async () => {
    try {
      data.value = await fetchCacheInfo()
    } finally {
      loading.value = false
    }
  })
</script>
<style scoped lang="scss">
  .cache-monitor-page {
    min-height: 0;
    overflow: hidden;
  }

  .cache-monitor-section {
    flex-shrink: 0;
  }

  .cache-command-card {
    min-height: 280px;
  }

  @media (max-width: 640px) {
    .cache-monitor-page {
      overflow: visible;
    }

    .cache-command-card {
      min-height: 420px;
    }
  }
</style>
