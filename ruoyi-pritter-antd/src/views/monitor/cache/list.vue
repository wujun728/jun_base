<script setup name="CacheList">
import {
  listCacheName,
  listCacheKey,
  getCacheValue,
  clearCacheName,
  clearCacheKey,
  clearCacheAll,
} from '@/api/monitor/cache'
import {
  DatabaseOutlined,
  KeyOutlined,
  FileTextOutlined,
  ReloadOutlined,
  DeleteOutlined,
} from '@ant-design/icons-vue'

const proxy = inject('proxy')

const pageRef = useTemplateRef('pageRef')
const pageStyle = ref({})
const cardStyle = ref({})
const tableHeight = ref(400)

const cacheNames = ref([])
const cacheKeys = ref([])
const cacheForm = ref({})
const loading = ref(true)
const subLoading = ref(false)
const nowCacheName = ref('')
const selectedNameKey = ref('')
const selectedKey = ref('')

const CARD_EXTRA = 120

const layout = {
  xl: 8,
  lg: 8,
  md: 12,
  sm: 12,
  xs: 24,
}

const nameColumns = [
  { title: '序号', width: 60, customRender: ({ index }) => index + 1 },
  {
    title: '缓存名称',
    dataIndex: 'cacheName',
    align: 'center',
    ellipsis: true,
    customRender: ({ record }) => nameFormatter(record),
  },
  { title: '备注', dataIndex: 'remark', align: 'center', ellipsis: true },
  { title: '操作', width: 60, align: 'center', key: 'action' },
]

const keyColumns = [
  { title: '序号', width: 60, customRender: ({ index }) => index + 1 },
  {
    title: '缓存键名',
    dataIndex: 'cacheKey',
    align: 'center',
    ellipsis: true,
    customRender: ({ record }) => keyFormatter(record.cacheKey),
  },
  { title: '操作', width: 60, align: 'center', key: 'action' },
]

const cacheKeyRows = computed(() =>
  cacheKeys.value.map((cacheKey) => ({ cacheKey }))
)

const getCacheNames = () => {
  loading.value = true
  listCacheName().then((response) => {
    cacheNames.value = response.data
    loading.value = false
  })
}

const refreshCacheNames = () => {
  getCacheNames()
  proxy.$modal.msgSuccess('刷新缓存列表成功')
}

const handleClearCacheName = (row) => {
  clearCacheName(row.cacheName).then(() => {
    proxy.$modal.msgSuccess(`清理缓存名称[${row.cacheName}]成功`)
    getCacheKeys()
  })
}

const getCacheKeys = (row) => {
  const cacheName = row?.cacheName ?? nowCacheName.value
  if (!cacheName) {
    return
  }
  subLoading.value = true
  listCacheKey(cacheName).then((response) => {
    cacheKeys.value = response.data
    subLoading.value = false
    nowCacheName.value = cacheName
  })
}

const refreshCacheKeys = () => {
  getCacheKeys()
  proxy.$modal.msgSuccess('刷新键名列表成功')
}

const handleClearCacheKey = (cacheKey) => {
  clearCacheKey(cacheKey).then(() => {
    proxy.$modal.msgSuccess(`清理缓存键名[${cacheKey}]成功`)
    getCacheKeys()
  })
}

const nameFormatter = (row) => row.cacheName.replace(':', '')

const keyFormatter = (cacheKey) => cacheKey.replace(nowCacheName.value, '')

const handleCacheValue = (cacheKey) => {
  getCacheValue(nowCacheName.value, cacheKey).then((response) => {
    cacheForm.value = response.data
  })
}

const handleClearCacheAll = () => {
  clearCacheAll().then(() => {
    proxy.$modal.msgSuccess('清理全部缓存成功')
  })
}

const onNameRowClick = (record) => {
  selectedNameKey.value = record.cacheName
  getCacheKeys(record)
}

const onKeyRowClick = (record) => {
  selectedKey.value = record.cacheKey
  handleCacheValue(record.cacheKey)
}

const updatePageLayout = () => {
  const el = pageRef.value
  if (!el) return
  const { top } = el.getBoundingClientRect()
  const bottomSpace =
    Number.parseFloat(
      getComputedStyle(document.documentElement).getPropertyValue(
        '--ba-main-space'
      )
    ) || 16
  const pageMaxHeight = window.innerHeight - top - bottomSpace

  pageStyle.value = {
    maxHeight: `${pageMaxHeight}px`,
  }

  if (window.innerWidth >= 992) {
    cardStyle.value = { height: `${pageMaxHeight}px` }
    tableHeight.value = Math.max(pageMaxHeight - CARD_EXTRA, 200)
  } else {
    cardStyle.value = { minHeight: '320px' }
    tableHeight.value = Math.min(360, Math.max(pageMaxHeight - CARD_EXTRA, 200))
  }
}

onMounted(() => {
  getCacheNames()
  window.addEventListener('resize', updatePageLayout)
  nextTick(updatePageLayout)
})

onActivated(() => {
  nextTick(updatePageLayout)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', updatePageLayout)
})
</script>

<template>
  <div ref="pageRef" class="cache-list default-main" :style="pageStyle">
    <a-row :gutter="10">
      <a-col v-bind="layout">
        <a-card :style="cardStyle">
          <template #title>
            <DatabaseOutlined />
            <span class="card-title">缓存列表</span>
          </template>
          <template #extra>
            <a-button type="link" @click="refreshCacheNames">
              <ReloadOutlined />
            </a-button>
          </template>
          <a-table
            row-key="cacheName"
            :loading="loading"
            :columns="nameColumns"
            :data-source="cacheNames"
            :pagination="false"
            :scroll="{ y: tableHeight }"
            :custom-row="
              (record) => ({
                onClick: () => onNameRowClick(record),
                class: selectedNameKey === record.cacheName ? 'row-active' : '',
              })
            "
            size="small"
          >
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'action'">
                <a-popconfirm
                  title="确认清理该缓存名称？"
                  @confirm="handleClearCacheName(record)"
                >
                  <a-button type="link" danger @click.stop>
                    <DeleteOutlined />
                  </a-button>
                </a-popconfirm>
              </template>
            </template>
          </a-table>
        </a-card>
      </a-col>

      <a-col v-bind="layout">
        <a-card :style="cardStyle">
          <template #title>
            <KeyOutlined />
            <span class="card-title">键名列表</span>
          </template>
          <template #extra>
            <a-button type="link" @click="refreshCacheKeys">
              <ReloadOutlined />
            </a-button>
          </template>
          <a-table
            :loading="subLoading"
            :columns="keyColumns"
            :data-source="cacheKeyRows"
            :pagination="false"
            :scroll="{ y: tableHeight }"
            :custom-row="
              (record) => ({
                onClick: () => onKeyRowClick(record),
                class: selectedKey === record.cacheKey ? 'row-active' : '',
              })
            "
            size="small"
            row-key="cacheKey"
          >
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'action'">
                <a-popconfirm
                  title="确认清理该缓存键？"
                  @confirm="handleClearCacheKey(record.cacheKey)"
                >
                  <a-button type="link" danger @click.stop>
                    <DeleteOutlined />
                  </a-button>
                </a-popconfirm>
              </template>
            </template>
          </a-table>
        </a-card>
      </a-col>

      <a-col v-bind="layout">
        <a-card :style="cardStyle">
          <template #title>
            <FileTextOutlined />
            <span class="card-title">缓存内容</span>
          </template>
          <template #extra>
            <a-popconfirm
              title="确认清理全部缓存？"
              @confirm="handleClearCacheAll"
            >
              <a-button type="link" danger>清理全部</a-button>
            </a-popconfirm>
          </template>
          <a-form :model="cacheForm" layout="vertical">
            <a-form-item label="缓存名称">
              <a-input v-model:value="cacheForm.cacheName" readonly />
            </a-form-item>
            <a-form-item label="缓存键名">
              <a-input v-model:value="cacheForm.cacheKey" readonly />
            </a-form-item>
            <a-form-item label="缓存内容">
              <a-textarea
                v-model:value="cacheForm.cacheValue"
                :rows="8"
                readonly
              />
            </a-form-item>
          </a-form>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<style scoped lang="scss">
.cache-list {
  overflow-x: hidden;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
}

.card-title {
  margin-left: 6px;
}

:deep(.row-active) {
  td {
    background: rgba(22, 119, 255, 0.08) !important;
  }
}
</style>
