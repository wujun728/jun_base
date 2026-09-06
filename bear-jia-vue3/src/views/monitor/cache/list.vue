<template>
  <page-header-wrapper>
    <div class="cache-container">
      <a-row :gutter="16" class="cache-row">
        <a-col :xs="24" :sm="24" :md="8" :lg="8" :xl="8" class="cache-col">
          <a-card 
            :loading="loading" 
            title="缓存列表" 
            :bordered="false"
            class="modern-card full-height-card"
            :bodyStyle="{ padding: '12px', height: 'calc(100vh - 200px)', overflowY: 'auto' }">
            <template #extra>
              <a-popconfirm
                ok-text="是"
                cancel-text="否"
                @confirm="handleClearCacheAll"
              >
                <template #title>确认<b>清除全部缓存</b>吗?</template>
                <a-button type="link" danger size="small" class="action-btn">
                  <delete-outlined />
                </a-button>
              </a-popconfirm>
              <a-divider type="vertical" />
              <a-button type="link" size="small" @click="refreshCacheNames" class="action-btn">
                <sync-outlined :spin="loading" />
              </a-button>
            </template>
            <a-table
              :loading="loading"
              size="middle"
              rowKey="cacheName"
              :columns="cacheColumns"
              :data-source="cacheNames"
              :row-selection="{ 
                type: 'radio', 
                selectedRowKeys: selectedCacheKeys,
                onChange: onCacheSelectionChange
              }"
              :pagination="false"
              :bordered="false"
              class="modern-table">
              <template #cacheName="{ record }">
                {{ nameFormatter(record) }}
              </template>
              <template #operation="{ record }">
                <a-popconfirm
                  ok-text="是"
                  cancel-text="否"
                  @confirm="handleClearCacheName(record)"
                >
                  <template #title>确认<b>删除</b>缓存: {{ record.cacheName }} 吗?</template>
                  <a-button type="link" danger size="small" class="action-btn">
                    <delete-outlined />
                  </a-button>
                </a-popconfirm>
              </template>
            </a-table>
          </a-card>
        </a-col>
        <a-col :xs="24" :sm="24" :md="8" :lg="8" :xl="8" class="cache-col">
          <a-card 
            :loading="loading" 
            title="键名列表" 
            :bordered="false"
            class="modern-card full-height-card"
            :bodyStyle="{ padding: '12px', height: 'calc(100vh - 200px)', overflowY: 'auto' }">
            <template #extra>
              <a-button type="link" size="small" @click="refreshCacheKeys" class="action-btn">
                <sync-outlined :spin="subLoading" />
              </a-button>
            </template>
            <a-list :data-source="cacheKeys" class="modern-list">
              <template #header>
                <div class="cache-name-header">{{ nowCacheName || '请选择缓存' }}</div>
              </template>
              <template #renderItem="item">
                <a-list-item>
                  <template #actions>
                    <a-popconfirm
                      ok-text="是"
                      cancel-text="否"
                      @confirm="handleClearCacheKey(item)"
                    >
                      <template #title>确认<b>删除</b>缓存: {{ item }} 吗?</template>
                      <a-button type="link" danger size="small" class="action-btn">
                        <delete-outlined />
                      </a-button>
                    </a-popconfirm>
                  </template>
                  <a-list-item-meta 
                    @click="handleCacheValue(item)" 
                    :class="['clickable-item', { 'selected-item': isSelectedKey(item) }]">
                    <template #description>
                      <ellipsis :length="35">
                        {{ keyFormatter(item) }}
                      </ellipsis>
                    </template>
                  </a-list-item-meta>
                </a-list-item>
              </template>
            </a-list>
          </a-card>
        </a-col>
        <a-col :xs="24" :sm="24" :md="8" :lg="8" :xl="8" class="cache-col">
          <a-card 
            :loading="loading" 
            title="缓存内容" 
            :bordered="false"
            class="modern-card full-height-card"
            :bodyStyle="{ padding: '16px', height: 'calc(100vh - 200px)', overflowY: 'auto' }">
            <div class="cache-details-container">
              <a-descriptions size="small" style="word-break: break-all;" layout="vertical" :column="3" bordered class="cache-info-descriptions">
                <a-descriptions-item label="缓存名称" :span="3">
                  <div class="info-content">{{ cacheForm.cacheName || '-' }}</div>
                </a-descriptions-item>
                <a-descriptions-item label="缓存键名" :span="3">
                  <div class="info-content">{{ cacheForm.cacheKey || '-' }}</div>
                </a-descriptions-item>
              </a-descriptions>
              
              <div class="cache-content-section">
                <div class="cache-content-header">
                  <span class="cache-content-title">缓存内容</span>
                  <div v-if="cacheForm.cacheValue" class="cache-content-actions">
                    <span class="cache-value-type">{{ getCacheValueType(cacheForm.cacheValue) }}</span>
                    <a-button type="text" size="small" @click="copyToClipboard(cacheForm.cacheValue)" class="copy-btn">
                      <copy-outlined /> 复制
                    </a-button>
                  </div>
                </div>
                <div class="cache-content-body">
                  <pre v-if="cacheForm.cacheValue" class="cache-value-pre">{{ formatCacheValue(cacheForm.cacheValue) }}</pre>
                  <div v-else class="empty-content">暂无数据</div>
                </div>
              </div>
            </div>
          </a-card>
        </a-col>
      </a-row>
    </div>
  </page-header-wrapper>
</template>

<script setup name="CacheList">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { listCacheName, listCacheKey, getCacheValue, clearCacheName, clearCacheKey, clearCacheAll } from '@/api/monitor/cache'
import { Ellipsis } from '@/components/Ellipsis'
import { DeleteOutlined, SyncOutlined, CopyOutlined } from '@ant-design/icons-vue'

const loading = ref(true)
const subLoading = ref(false)
const nowCacheName = ref('')
const selectedCacheKeys = ref([])
const selectedCacheKey = ref('')

const cacheColumns = [
  {
    title: '缓存名称',
    dataIndex: 'cacheName',
    slots: { customRender: 'cacheName' },
    ellipsis: true,
    align: 'center'
  },
  {
    title: '备注',
    dataIndex: 'remark',
    ellipsis: true,
    align: 'center'
  },
  {
    title: '操作',
    dataIndex: 'operation',
    width: '20%',
    slots: { customRender: 'operation' },
    align: 'center'
  }
]

const cacheSubColumns = [
  {
    title: '缓存键名',
    dataIndex: 'cacheKey',
    slots: { customRender: 'cacheKey' },
    ellipsis: true,
    align: 'center'
  },
  {
    title: '操作',
    dataIndex: 'operation',
    slots: { customRender: 'operation' },
    align: 'center'
  }
]

const cacheNames = ref([])
const cacheKeys = ref([])
const cacheForm = reactive({})

/** 查询缓存名称列表 */
const getCacheNames = () => {
  loading.value = true
  listCacheName().then(response => {
    cacheNames.value = response.data
    loading.value = false
    // 清空选择状态
    selectedCacheKeys.value = []
    cacheKeys.value = []
    nowCacheName.value = ''
    Object.keys(cacheForm).forEach(key => delete cacheForm[key])
  }).catch(error => {
    loading.value = false
    message.error('获取缓存名称失败: ' + error.message)
  })
}

/** 缓存选择变化处理 */
const onCacheSelectionChange = (selectedRowKeys, selectedRows) => {
  selectedCacheKeys.value = selectedRowKeys
  selectedCacheKey.value = '' // 清空选中的键名
  if (selectedRows.length > 0) {
    getCacheKeys(selectedRows[0])
  } else {
    // 清空键名列表和缓存内容
    cacheKeys.value = []
    Object.keys(cacheForm).forEach(key => delete cacheForm[key])
  }
}

/** 刷新缓存名称列表 */
const refreshCacheNames = () => {
  getCacheNames()
  message.success('刷新缓存列表成功', 3)
}

/** 清理指定名称缓存 */
const handleClearCacheName = (row) => {
  clearCacheName(row.cacheName).then(response => {
    message.success('清理缓存名称[' + row.cacheName + ']成功', 3)
    // 刷新缓存名称列表
    getCacheNames()
  }).catch(error => {
    message.error('清理缓存失败: ' + error.message)
  })
}

/** 查询缓存键名列表 */
const getCacheKeys = (row) => {
  if (!row && !nowCacheName.value) {
    message.warning('请先选择缓存名称')
    return
  }
  
  const cacheName = row ? row.cacheName : nowCacheName.value
  subLoading.value = true
  listCacheKey(cacheName).then(response => {
    cacheKeys.value = response.data
    subLoading.value = false
    nowCacheName.value = cacheName
  }).catch(error => {
    subLoading.value = false
    message.error('获取缓存键名失败: ' + error.message)
  })
}

/** 刷新缓存键名列表 */
const refreshCacheKeys = () => {
  getCacheKeys()
  message.success('刷新键名列表成功', 3)
}

/** 清理指定键名缓存 */
const handleClearCacheKey = (cacheKey) => {
  // 提取实际的键名
  let keyString = cacheKey
  if (typeof cacheKey === 'object' && cacheKey !== null) {
    keyString = cacheKey.key || cacheKey.name || cacheKey.cacheKey || String(cacheKey)
  } else {
    keyString = String(cacheKey)
  }
  
  clearCacheKey(nowCacheName.value, keyString).then(response => {
    message.success('清理缓存键名[' + keyString + ']成功', 3)
    getCacheKeys()
    // 如果删除的是当前选中的键，清空选中状态
    if (selectedCacheKey.value === cacheKey) {
      selectedCacheKey.value = ''
      Object.keys(cacheForm).forEach(key => delete cacheForm[key])
    }
  }).catch(error => {
    message.error('清理缓存失败: ' + error.message)
  })
}

/** 列表前缀去除 */
const nameFormatter = (row) => {
  if (row && row.cacheName && typeof row.cacheName === 'string') {
    return row.cacheName.replace(':', '')
  }
  return row?.cacheName || ''
}

/** 键名前缀去除 */
const keyFormatter = (cacheKey) => {
  console.log('keyFormatter', cacheKey)
  // 如果是对象，可能有 key 或 name 属性
  if (typeof cacheKey === 'object' && cacheKey !== null) {
    const keyString = cacheKey.item || cacheKey.name || cacheKey.cacheKey || String(cacheKey)
    if (typeof keyString === 'string' && nowCacheName.value) {
      return keyString.replace(nowCacheName.value + ':', '')
    }
    return keyString
  }
  
  // 如果是字符串
  if (typeof cacheKey === 'string') {
    if (nowCacheName.value) {
      return cacheKey.replace(nowCacheName.value + ':', '')
    }
    return cacheKey
  }
  
  return String(cacheKey)
}

/** 判断是否为选中的键 */
const isSelectedKey = (item) => {
  if (!selectedCacheKey.value) return false
  
  // 如果两者都是对象，比较关键属性
  if (typeof item === 'object' && typeof selectedCacheKey.value === 'object') {
    const itemKey = item.key || item.name || item.cacheKey || String(item)
    const selectedKey = selectedCacheKey.value.key || selectedCacheKey.value.name || selectedCacheKey.value.cacheKey || String(selectedCacheKey.value)
    return itemKey === selectedKey
  }
  
  // 其他情况直接比较
  return item === selectedCacheKey.value
}

/** 格式化缓存值显示 */
const formatCacheValue = (value) => {
  if (!value) return ''
  
  // 如果是字符串，尝试解析为JSON并格式化
  if (typeof value === 'string') {
    // 先清理可能的转义字符
    let cleanValue = value.trim()
    
    // 尝试解析JSON
    try {
      const parsed = JSON.parse(cleanValue)
      return JSON.stringify(parsed, null, 2)
    } catch {
      // 如果不是标准JSON，尝试处理可能的对象字符串
      if (cleanValue.startsWith('[{') || cleanValue.startsWith('{')) {
        try {
          // 尝试修复常见的JSON格式问题
          const fixedJson = cleanValue
            .replace(/'/g, '"')  // 单引号替换为双引号
            .replace(/(\w+):/g, '"$1":')  // 属性名加双引号
          const parsed = JSON.parse(fixedJson)
          return JSON.stringify(parsed, null, 2)
        } catch {
          // 如果还是失败，返回原字符串但添加换行
          return cleanValue.replace(/,/g, ',\n').replace(/{/g, '{\n  ').replace(/}/g, '\n}')
        }
      }
      
      // 普通字符串，尝试美化显示
      return cleanValue
    }
  }
  
  // 如果是对象，格式化为JSON
  if (typeof value === 'object') {
    return JSON.stringify(value, null, 2)
  }
  
  return String(value)
}

/** 获取缓存值类型 */
const getCacheValueType = (value) => {
  if (!value) return 'Empty'
  
  if (typeof value === 'string') {
    try {
      JSON.parse(value)
      return 'JSON'
    } catch {
      return 'String'
    }
  }
  
  if (typeof value === 'object') {
    return Array.isArray(value) ? 'Array' : 'Object'
  }
  
  return typeof value
}

/** 复制到剪贴板 */
const copyToClipboard = async (value) => {
  try {
    const textToCopy = formatCacheValue(value)
    await navigator.clipboard.writeText(textToCopy)
    message.success('已复制到剪贴板')
  } catch (error) {
    console.error('复制失败:', error)
    message.error('复制失败')
  }
}

/** 查询缓存内容详细 */
const handleCacheValue = (cacheKey) => {
  // 提取实际的键名
  const keyString = cacheKey.item
  
  selectedCacheKey.value = cacheKey // 保存原始对象用于比较
  getCacheValue(nowCacheName.value, keyString).then(response => {
    Object.assign(cacheForm, response.data)
    // 缓存值格式化现在由 formatCacheValue 函数处理
  }).catch(error => {
    console.error('获取缓存值失败:', error)
    message.error('获取缓存值失败')
    selectedCacheKey.value = ''
  })
}

/** 清理全部缓存 */
const handleClearCacheAll = () => {
  clearCacheAll().then(response => {
    message.success('清理全部缓存成功', 3)
    // 清理成功后刷新缓存列表
    getCacheNames()
  }).catch(error => {
    console.error('清理全部缓存失败:', error)
    message.error('清理全部缓存失败')
  })
}

// 初始化加载
onMounted(() => {
  getCacheNames()
})
</script>

<style scoped>
* {
  box-sizing: border-box;
}

.cache-container {
  max-width: 100vw;
  overflow-x: hidden;
}

.modern-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
  transition: all 0.3s;
  margin-bottom: 16px;
}

.modern-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}

.modern-table {
  border-radius: 8px;
}

.modern-list {
  border-radius: 8px;
}

.action-btn {
  padding: 4px 8px;
  border-radius: 4px;
  transition: all 0.3s;
}

.action-btn:hover {
  background-color: rgba(0, 0, 0, 0.04);
}

.clickable-item {
  cursor: pointer;
  transition: all 0.3s;
  padding: 4px;
  border-radius: 4px;
}

.clickable-item:hover {
  background-color: rgba(0, 0, 0, 0.04);
}

.selected-item {
  background-color: #e6f7ff !important;
  border: 1px solid #91d5ff;
}

.selected-item:hover {
  background-color: #bae7ff !important;
}

.cache-name-header {
  font-weight: 500;
  color: #1890ff;
  padding: 8px 0;
}

.modern-descriptions :deep(.ant-descriptions-item-label) {
  background-color: #fafafa;
  font-weight: 500;
}

.description-content {
  padding: 8px 0;
  min-height: 32px;
  max-height: 200px;
  overflow-y: auto;
}

.cache-value-type {
  font-size: 12px;
  font-weight: 500;
  color: #586069;
  background-color: #fff;
  padding: 2px 6px;
  border-radius: 3px;
  border: 1px solid #d1d5da;
}

.copy-btn {
  padding: 4px 8px;
  font-size: 12px;
}

.cache-value-pre {
  background-color: #fff;
  border: none;
  padding: 16px;
  margin: 0;
  font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace;
  font-size: 13px;
  line-height: 1.5;
  white-space: pre-wrap;
  word-wrap: break-word;
  word-break: break-all;
  overflow-wrap: break-word;
  height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  color: #24292e;
}

.empty-content {
  color: #6a737d;
  font-style: italic;
  padding: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.cache-container {
  width: 100%;
  height: calc(100vh - 140px);
  overflow: hidden;
}

.cache-row {
  height: 100%;
  margin: 0 !important;
}

.cache-col {
  height: 100%;
  padding: 0 8px !important;
}

.full-height-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.full-height-card :deep(.ant-card-body) {
  flex: 1;
  overflow: hidden;
  padding: 12px !important;
}

.modern-table {
  height: 100%;
}

.modern-table :deep(.ant-table-tbody) {
  max-height: calc(100vh - 250px);
  overflow-y: auto;
}

.modern-list {
  height: 100%;
  overflow-y: auto;
}

.cache-details-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.cache-info-descriptions {
  flex-shrink: 0;
  margin-bottom: 16px;
}

.info-content {
  padding: 8px 0;
  min-height: 24px;
  word-break: break-all;
}

.cache-content-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  overflow: hidden;
}

.cache-content-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background-color: #fafafa;
  border-bottom: 1px solid #d9d9d9;
  flex-shrink: 0;
}

.cache-content-title {
  font-weight: 500;
  color: rgba(0, 0, 0, 0.85);
}

.cache-content-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.cache-content-body {
  flex: 1;
  overflow: hidden;
  background-color: #fff;
}
</style>
