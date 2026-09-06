<template>
  <div>
    <ElRow :gutter="16" class="mb-4">
      <ElCol :span="12">
        <div class="art-card h-55 p-5">
          <div class="art-card-header">
            <div class="title">
              <ArtSvgIcon
                icon="ri:cpu-line"
                class="mr-0.5 align-middle text-xl" />
              <h4 class="inline-block align-middle">CPU</h4>
            </div>
          </div>
          <div class="mt-4">
            <div class="mb-3 flex items-center justify-between">
              <span class="text-sm text-g-600">核心数</span>
              <span class="font-medium text-g-900">
                {{ server.cpu?.cpuNum || '-' }}
              </span>
            </div>
            <div class="mb-3 flex items-center justify-between">
              <span class="text-sm text-g-600">用户使用率</span>
              <span class="font-medium text-g-900">
                {{ server.cpu?.used || '-' }}%
              </span>
            </div>
            <div class="mb-3 flex items-center justify-between">
              <span class="text-sm text-g-600">系统使用率</span>
              <span class="font-medium text-g-900">
                {{ server.cpu?.sys || '-' }}%
              </span>
            </div>
            <div class="flex items-center justify-between">
              <span class="text-sm text-g-600">当前空闲率</span>
              <span class="font-medium text-g-900">
                {{ server.cpu?.free || '-' }}%
              </span>
            </div>
          </div>
        </div>
      </ElCol>

      <ElCol :span="12">
        <div class="art-card h-55 p-5">
          <div class="art-card-header">
            <div class="title">
              <ArtSvgIcon
                icon="ri:file-text-line"
                class="mr-0.5 align-middle text-xl" />
              <h4 class="inline-block align-middle">内存</h4>
            </div>
          </div>
          <div class="mt-4">
            <div class="mb-4">
              <div class="mb-1 flex items-center justify-between">
                <span class="text-sm text-g-600">系统内存</span>
                <span class="font-medium text-g-900">
                  {{ server.mem?.total || '-' }}G
                </span>
              </div>
              <ElProgress
                :percentage="server.mem?.usage || 0"
                :color="
                  server.mem?.usage > 80
                    ? 'var(--art-error)'
                    : 'var(--art-primary)'
                "
                :stroke-width="8"
                :show-text="false" />
              <div class="mt-1 flex items-center justify-between">
                <span class="text-xs text-g-500">
                  已用 {{ server.mem?.used || '-' }}G
                </span>
                <span class="text-xs text-g-500">
                  剩余 {{ server.mem?.free || '-' }}G
                </span>
              </div>
            </div>
            <div>
              <div class="mb-1 flex items-center justify-between">
                <span class="text-sm text-g-600">JVM内存</span>
                <span class="font-medium text-g-900">
                  {{ server.jvm?.total || '-' }}M
                </span>
              </div>
              <ElProgress
                :percentage="server.jvm?.usage || 0"
                :color="
                  server.jvm?.usage > 80
                    ? 'var(--art-error)'
                    : 'var(--art-secondary)'
                "
                :stroke-width="8"
                :show-text="false" />
              <div class="mt-1 flex items-center justify-between">
                <span class="text-xs text-g-500">
                  已用 {{ server.jvm?.used || '-' }}M
                </span>
                <span class="text-xs text-g-500">
                  剩余 {{ server.jvm?.free || '-' }}M
                </span>
              </div>
            </div>
          </div>
        </div>
      </ElCol>
    </ElRow>

    <ElRow :gutter="16" class="mb-4">
      <ElCol :span="24">
        <div class="art-card p-5">
          <div class="art-card-header">
            <div class="title">
              <ArtSvgIcon
                icon="ri:computer-line"
                class="mr-0.5 align-middle text-xl" />
              <h4 class="inline-block align-middle">服务器信息</h4>
            </div>
          </div>
          <div class="mt-4">
            <ElDescriptions :column="2" border>
              <ElDescriptionsItem label="服务器名称">
                {{ server.sys?.computerName || '-' }}
              </ElDescriptionsItem>
              <ElDescriptionsItem label="操作系统">
                {{ server.sys?.osName || '-' }}
              </ElDescriptionsItem>
              <ElDescriptionsItem label="服务器IP">
                {{ server.sys?.computerIp || '-' }}
              </ElDescriptionsItem>
              <ElDescriptionsItem label="系统架构">
                {{ server.sys?.osArch || '-' }}
              </ElDescriptionsItem>
            </ElDescriptions>
          </div>
        </div>
      </ElCol>
    </ElRow>

    <ElRow :gutter="16" class="mb-4">
      <ElCol :span="24">
        <div class="art-card p-5">
          <div class="art-card-header">
            <div class="title">
              <ArtSvgIcon
                icon="ri:java-line"
                class="mr-0.5 align-middle text-xl" />
              <h4 class="inline-block align-middle">Java虚拟机信息</h4>
            </div>
          </div>
          <div class="mt-4">
            <ElDescriptions :column="2" border>
              <ElDescriptionsItem label="Java名称">
                {{ server.jvm?.name || '-' }}
              </ElDescriptionsItem>
              <ElDescriptionsItem label="Java版本">
                {{ server.jvm?.version || '-' }}
              </ElDescriptionsItem>
              <ElDescriptionsItem label="启动时间">
                {{ server.jvm?.startTime || '-' }}
              </ElDescriptionsItem>
              <ElDescriptionsItem label="运行时长">
                {{ server.jvm?.runTime || '-' }}
              </ElDescriptionsItem>
              <ElDescriptionsItem label="安装路径" :span="2">
                {{ server.jvm?.home || '-' }}
              </ElDescriptionsItem>
              <ElDescriptionsItem label="项目路径" :span="2">
                {{ server.sys?.userDir || '-' }}
              </ElDescriptionsItem>
              <ElDescriptionsItem label="运行参数" :span="2">
                {{ server.jvm?.inputArgs || '-' }}
              </ElDescriptionsItem>
            </ElDescriptions>
          </div>
        </div>
      </ElCol>
    </ElRow>

    <ElRow :gutter="16">
      <ElCol :span="24">
        <div class="art-card p-5">
          <div class="art-card-header">
            <div class="title">
              <ArtSvgIcon
                icon="ri:u-disk-line"
                class="mr-0.5 align-middle text-xl" />
              <h4 class="inline-block align-middle">磁盘状态</h4>
            </div>
          </div>
          <div class="mt-4">
            <ElTable
              :data="server.sysFiles || []"
              size="large"
              :stripe="false"
              :border="false">
              <ElTableColumn prop="dirName" label="盘符路径" min-width="120" />
              <ElTableColumn
                prop="sysTypeName"
                label="文件系统"
                min-width="100" />
              <ElTableColumn prop="typeName" label="盘符类型" min-width="100" />
              <ElTableColumn prop="total" label="总大小" min-width="100" />
              <ElTableColumn prop="free" label="可用大小" min-width="100" />
              <ElTableColumn prop="used" label="已用大小" min-width="100" />
              <ElTableColumn prop="usage" label="已用百分比" min-width="140">
                <template #default="{ row }">
                  <div class="flex items-center gap-2">
                    <ElProgress
                      :percentage="row.usage"
                      :color="
                        row.usage > 80
                          ? 'var(--art-error)'
                          : 'var(--art-primary)'
                      "
                      :stroke-width="6"
                      :show-text="false"
                      class="flex-1" />
                    <span :class="row.usage > 80 ? 'text-error' : 'text-g-600'">
                      {{ row.usage }}%
                    </span>
                  </div>
                </template>
              </ElTableColumn>
            </ElTable>
          </div>
        </div>
      </ElCol>
    </ElRow>
  </div>
</template>

<script setup lang="ts">
import { loadingService } from '@/utils/ui/loading'

import { getServer } from './api'

const server = ref<any>([])

async function getList() {
  loadingService.showLoading('正在加载服务监控数据，请稍候！')
  try {
    const res = await getServer()
    server.value = res
    loadingService.hideLoading()
  } catch (error) {
    loadingService.hideLoading()
  }
}

getList()
</script>
