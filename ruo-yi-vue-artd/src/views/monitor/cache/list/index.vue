<template>
  <div class="app-container">
    <!-- <el-row :gutter="10">
      <el-col :span="8"></el-col>

      <el-col :span="8"></el-col>

      <el-col :span="8"></el-col>
    </el-row> -->

    <el-splitter>
      <el-splitter-panel size="33%" :min="250">
        <div class="art-card mr-4 p-5">
          <div class="art-card-header">
            <div class="title">
              <ArtSvgIcon
                icon="ri:list-check"
                class="mr-1.5! align-middle text-xl" />
              <span class="align-middle">缓存列表</span>
              <span class="align-middle" @click="refreshCacheNames">
                <ArtSvgIcon
                  icon="ri:refresh-line"
                  class="ml-2 cursor-pointer align-middle text-xl text-primary!" />
              </span>
            </div>
          </div>
          <div class="mt-3">
            <art-table
              ref="cacheNameTableRef"
              v-loading="loading"
              :data="cacheNames"
              :height="tableHeight"
              highlight-current-row
              @row-click="getCacheKeys">
              <el-table-column
                label="序号"
                width="60"
                type="index"
                align="center" />
              <el-table-column
                label="缓存名称"
                align="center"
                prop="cacheName"
                :show-overflow-tooltip="true"
                :formatter="nameFormatter" />
              <el-table-column
                label="备注"
                align="center"
                prop="remark"
                :show-overflow-tooltip="true" />
              <el-table-column
                label="操作"
                width="60"
                align="center"
                class-name="small-padding fixed-width">
                <template #default="{ row }">
                  <el-button
                    link
                    type="primary"
                    :icon="Delete"
                    @click="handleClearCacheName(row)" />
                </template>
              </el-table-column>
            </art-table>
          </div>
        </div>
      </el-splitter-panel>
      <el-splitter-panel size="33%" :min="250">
        <div class="art-card mx-4 p-5">
          <div class="art-card-header">
            <div class="title">
              <ArtSvgIcon
                icon="ri:key-line"
                class="mr-1.5! align-middle text-xl" />
              <span class="align-middle">键名列表</span>
              <span class="align-middle" @click="refreshCacheKeys">
                <ArtSvgIcon
                  icon="ri:refresh-line"
                  class="ml-2 cursor-pointer align-middle text-xl text-primary!" />
              </span>
            </div>
          </div>
          <div class="mt-3">
            <art-table
              ref="cacheKeyTableRef"
              v-loading="subLoading"
              :empty-height="tableHeight + 'px'"
              :data="cacheKeys"
              :height="tableHeight"
              highlight-current-row
              showTableHeader
              @row-click="handleCacheValue">
              <el-table-column
                label="序号"
                width="60"
                type="index"
                align="center" />
              <el-table-column
                label="缓存键名"
                align="center"
                :show-overflow-tooltip="true"
                :formatter="keyFormatter" />
              <el-table-column
                label="操作"
                width="60"
                align="center"
                class-name="small-padding fixed-width">
                <template #default="{ row }">
                  <el-button
                    link
                    type="primary"
                    :icon="Delete"
                    @click="handleClearCacheKey(row as CacheKey)" />
                </template>
              </el-table-column>
            </art-table>
          </div>
        </div>
      </el-splitter-panel>
      <el-splitter-panel size="33%" :min="250">
        <div class="art-card ml-4 p-5">
          <div class="art-card-header">
            <div class="title">
              <ArtSvgIcon
                icon="ri:file-text-line"
                class="mr-1.5! align-middle text-xl" />
              <span class="align-middle">缓存内容</span>
              <span class="align-middle" @click="handleClearCacheAll">
                <ArtSvgIcon
                  icon="ri:refresh-line"
                  class="ml-2 cursor-pointer align-middle text-xl text-primary!" />
              </span>
            </div>
          </div>
          <div class="mt-3">
            <el-form :model="cacheForm" :style="{ height: tableHeight + 'px' }">
              <el-row :gutter="32">
                <el-col :offset="1" :span="22">
                  <el-form-item label="缓存名称:" prop="cacheName">
                    <el-input v-model="cacheForm.cacheName" readonly />
                  </el-form-item>
                </el-col>
                <el-col :offset="1" :span="22">
                  <el-form-item label="缓存键名:" prop="cacheKey">
                    <el-input v-model="cacheForm.cacheKey" readonly />
                  </el-form-item>
                </el-col>
                <el-col :offset="1" :span="22">
                  <el-form-item label="缓存内容:" prop="cacheValue">
                    <el-input
                      v-model="cacheForm.cacheValue"
                      type="textarea"
                      :rows="8"
                      readonly />
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </div>
        </div>
      </el-splitter-panel>
    </el-splitter>
  </div>
</template>

<script setup lang="ts" name="CacheList">
import type { SysCache } from '@/types/api/monitor/cache'

import {
  Collection,
  Delete,
  Document,
  Key,
  Refresh
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

import ArtTable from '@/components/core/tables/art-table/index.vue'
import { useTableHeight } from '@/hooks/core/useTableHeight'

import {
  listCacheName,
  listCacheKey,
  getCacheValue,
  clearCacheName,
  clearCacheKey,
  clearCacheAll
} from './api'

interface CacheName {
  cacheName: string
  [key: string]: any
}

interface CacheForm {
  cacheName?: string
  cacheKey?: string
  cacheValue?: string
}
interface CacheKey {
  key: string
  [key: string]: any
}

const cacheNameTableRef = ref<InstanceType<typeof ArtTable>>()
const cacheKeyTableRef = ref<InstanceType<typeof ArtTable>>()

const cacheNames = ref<SysCache[]>([])
const cacheKeys = ref<CacheKey[]>([])
const cacheForm = ref<CacheForm>({})
const loading = ref<boolean>(true)
const subLoading = ref<boolean>(false)
const nowCacheName = ref<string>('')
const tableHeight = ref<number>(window.innerHeight - 200)

const showTableHeader = ref(true)
const paginationHeight = ref(40)
const tableHeaderHeight = ref(40)
const paginationSpacing = ref(6)
const { containerHeight } = useTableHeight({
  showTableHeader: showTableHeader,
  paginationHeight: paginationHeight,
  tableHeaderHeight: tableHeaderHeight,
  paginationSpacing: paginationSpacing
})

/** 查询缓存名称列表 */
async function getCacheNames() {
  loading.value = true
  const res = await listCacheName()
  cacheNames.value = res || []
  loading.value = false
}

function refreshCacheNames() {
  console.log('刷新缓存列表')
  getCacheNames()
  ElMessage.success('刷新缓存列表成功')
}

async function handleClearCacheName(row: SysCache) {
  await clearCacheName(row.cacheName as string)
  ElMessage.success(`清理缓存名称[${row.cacheName}]成功`)
  getCacheKeys()
}

/** 查询缓存键名列表 */
async function getCacheKeys(row?: SysCache) {
  const cacheName = row !== undefined ? row.cacheName : nowCacheName.value
  if (!cacheName) return

  subLoading.value = true
  const keys = await listCacheKey(cacheName)
  cacheKeys.value = keys.map((key) => ({ key }))
  subLoading.value = false
  nowCacheName.value = cacheName
}

/** 刷新缓存键名列表 */
async function refreshCacheKeys() {
  await getCacheKeys()
  ElMessage.success('刷新键名列表成功')
}

/** 清理指定键名缓存 */
async function handleClearCacheKey(cacheKey: CacheKey) {
  await clearCacheKey(cacheKey.key)
  ElMessage.success(`清理缓存键名[${cacheKey.key}]成功`)
  getCacheKeys()
}

/** 列表前缀去除 */
function nameFormatter(row: SysCache): string {
  if (!row?.cacheName) return ''
  return row.cacheName.replace(':', '')
}

/** 键名前缀去除 */
function keyFormatter({ key }: { key: string }): string {
  return key.replace(nowCacheName.value, '')
}

/** 查询缓存内容详细 */
async function handleCacheValue(cacheKey: { key: string }) {
  const res = await getCacheValue(nowCacheName.value, cacheKey.key)
  cacheForm.value = res
}

/** 清理全部缓存 */
async function handleClearCacheAll() {
  await clearCacheAll()
  ElMessage.success('清理全部缓存成功')
}

onMounted(() => {
  getCacheNames()
})
</script>
