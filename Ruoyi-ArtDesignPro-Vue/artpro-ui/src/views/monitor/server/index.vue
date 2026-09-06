<template>
  <div class="art-full-height">
    <ElSkeleton v-if="loading" :rows="8" animated />
    <template v-else>
      <ElRow :gutter="16">
        <ElCol v-for="item in overview" :key="item.label" :span="6"
          ><ElCard class="mb-4"
            ><div class="text-g-500">{{ item.label }}</div
            ><div class="mt-3 text-2xl font-semibold">{{ item.value }}</div></ElCard
          ></ElCol
        >
      </ElRow>
      <ElRow :gutter="16">
        <ElCol :span="12"
          ><ElCard header="服务器信息"
            ><ElDescriptions :column="1" border
              ><ElDescriptionsItem v-for="item in system" :key="item.label" :label="item.label">{{
                item.value
              }}</ElDescriptionsItem></ElDescriptions
            ></ElCard
          ></ElCol
        >
        <ElCol :span="12"
          ><ElCard header="Java 虚拟机"
            ><ElDescriptions :column="1" border
              ><ElDescriptionsItem v-for="item in jvm" :key="item.label" :label="item.label">{{
                item.value
              }}</ElDescriptionsItem></ElDescriptions
            ></ElCard
          ></ElCol
        >
      </ElRow>
      <ElCard class="mt-4" header="磁盘状态"
        ><ElTable :data="data.sysFiles"
          ><ElTableColumn prop="dirName" label="盘符路径" /><ElTableColumn
            prop="sysTypeName"
            label="文件系统"
          /><ElTableColumn prop="total" label="总大小" /><ElTableColumn
            prop="free"
            label="可用大小"
          /><ElTableColumn prop="used" label="已用大小" /><ElTableColumn prop="usage" label="使用率"
            ><template #default="{ row }">{{ row.usage }}%</template></ElTableColumn
          ></ElTable
        ></ElCard
      >
    </template>
  </div>
</template>
<script setup lang="ts">
  import { fetchServerInfo } from '@/api/monitor'
  import type { Entity } from '@/api/system/types'
  defineOptions({ name: 'MonitorServer' })
  const loading = ref(true),
    data = ref<Entity>({})
  const overview = computed(() => [
    { label: 'CPU 核心数', value: data.value.cpu?.cpuNum ?? '-' },
    { label: 'CPU 使用率', value: `${data.value.cpu?.used ?? 0}%` },
    { label: '内存使用率', value: `${data.value.mem?.usage ?? 0}%` },
    { label: 'JVM 使用率', value: `${data.value.jvm?.usage ?? 0}%` }
  ])
  const system = computed(() => [
    { label: '服务器名称', value: data.value.sys?.computerName },
    { label: '服务器 IP', value: data.value.sys?.computerIp },
    { label: '操作系统', value: data.value.sys?.osName },
    { label: '系统架构', value: data.value.sys?.osArch }
  ])
  const jvm = computed(() => [
    { label: 'Java 名称', value: data.value.jvm?.name },
    { label: 'Java 版本', value: data.value.jvm?.version },
    { label: '启动时间', value: data.value.jvm?.startTime },
    { label: '运行时长', value: data.value.jvm?.runTime },
    { label: '安装路径', value: data.value.jvm?.home }
  ])
  onMounted(async () => {
    try {
      data.value = await fetchServerInfo()
    } finally {
      loading.value = false
    }
  })
</script>
