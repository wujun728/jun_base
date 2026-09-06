<template>
  <div class="art-full-height"
    ><ElRow :gutter="16" class="h-full">
      <ElCol :span="7"
        ><ElCard header="缓存名称" class="h-full"
          ><ElButton v-if="canRemove" type="danger" class="mb-3" @click="clearAll"
            >清空全部</ElButton
          ><ElTable :data="names" highlight-current-row @current-change="selectName"
            ><ElTableColumn prop="remark" label="名称" /><ElTableColumn
              prop="cacheName"
              label="缓存键"
            /><ElTableColumn v-if="canRemove" label="操作" width="72"
              ><template #default="{ row }"
                ><ElButton link type="danger" @click.stop="clearName(row)">清理</ElButton></template
              ></ElTableColumn
            ></ElTable
          ></ElCard
        ></ElCol
      >
      <ElCol :span="7"
        ><ElCard header="键名列表" class="h-full"
          ><ElTable
            :data="keys.map((cacheKey) => ({ cacheKey }))"
            highlight-current-row
            @current-change="selectKey"
            ><ElTableColumn prop="cacheKey" label="缓存键名" /><ElTableColumn
              v-if="canRemove"
              label="操作"
              width="72"
              ><template #default="{ row }"
                ><ElButton link type="danger" @click.stop="clearKey(row)">清理</ElButton></template
              ></ElTableColumn
            ></ElTable
          ></ElCard
        ></ElCol
      >
      <ElCol v-if="canQuery" :span="10"
        ><ElCard header="缓存内容" class="h-full"
          ><ElDescriptions :column="1" border
            ><ElDescriptionsItem label="缓存名称">{{ value.cacheName }}</ElDescriptionsItem
            ><ElDescriptionsItem label="缓存键名">{{ value.cacheKey }}</ElDescriptionsItem
            ><ElDescriptionsItem label="缓存内容">
              <pre class="whitespace-pre-wrap">{{ value.cacheValue }}</pre>
            </ElDescriptionsItem></ElDescriptions
          ></ElCard
        ></ElCol
      >
    </ElRow></div
  >
</template>
<script setup lang="ts">
  import { ElMessageBox } from 'element-plus'
  import {
    clearCacheAll,
    clearCacheKey,
    clearCacheName,
    fetchCacheKeys,
    fetchCacheNames,
    fetchCacheValue
  } from '@/api/monitor'
  import type { Entity } from '@/api/system/types'
  import { useAuth } from '@/hooks/core/useAuth'
  defineOptions({ name: 'MonitorCacheList' })
  const { hasAuth } = useAuth()
  const canQuery = computed(() => hasAuth('monitor:cache:query'))
  const canRemove = computed(() => hasAuth('monitor:cache:remove'))
  const names = ref<Entity[]>([]),
    keys = ref<string[]>([]),
    value = ref<Entity>({})
  async function selectName(row?: Entity) {
    if (!row) return
    keys.value = await fetchCacheKeys(row.cacheName)
    value.value = {}
  }
  async function selectKey(row?: Entity) {
    if (!row || !canQuery.value) return
    const name = names.value.find((item) => row.cacheKey.startsWith(item.cacheName))
    if (name) value.value = await fetchCacheValue(name.cacheName, row.cacheKey)
  }
  async function clearAll() {
    await ElMessageBox.confirm('确定清空全部缓存吗？该操作会导致当前登录失效。', '提示', {
      type: 'warning'
    })
    await clearCacheAll()
    names.value = await fetchCacheNames()
    keys.value = []
    value.value = {}
  }
  async function clearName(row: Entity) {
    await ElMessageBox.confirm(`确定清理缓存“${row.remark}”吗？`, '提示', { type: 'warning' })
    await clearCacheName(row.cacheName)
    names.value = await fetchCacheNames()
    keys.value = []
    value.value = {}
  }
  async function clearKey(row: Entity) {
    await ElMessageBox.confirm(`确定清理缓存键“${row.cacheKey}”吗？`, '提示', { type: 'warning' })
    await clearCacheKey(row.cacheKey)
    keys.value = keys.value.filter((cacheKey) => cacheKey !== row.cacheKey)
    value.value = {}
  }
  onMounted(async () => {
    names.value = await fetchCacheNames()
  })
</script>
