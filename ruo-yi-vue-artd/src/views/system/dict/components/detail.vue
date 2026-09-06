<template>
  <el-drawer
    :model-value="visible"
    direction="rtl"
    size="700px"
    append-to-body
    @update:model-value="emit('update:visible', $event)">
    <template #header>
      <div class="drawer-head">
        <el-icon class="drawer-head-icon"><List /></el-icon>
        <span class="drawer-head-name">{{ row?.dictName }}</span>
        <span class="drawer-head-type">{{ row?.dictType }}</span>
      </div>
    </template>

    <div class="drawer-wrap">
      <div v-if="loading" class="drawer-loading">
        <el-icon class="is-loading"><Loading /></el-icon>
        <span>加载中...</span>
      </div>

      <div v-else-if="!dataList.length" class="drawer-empty">
        <el-icon><Document /></el-icon>
        <div>暂无字典数据</div>
      </div>

      <template v-else>
        <el-row :gutter="12" class="stat-row">
          <el-col :span="disabledCount > 0 ? 8 : 12">
            <div class="stat-card">
              <div class="stat-num">{{ dataList.length }}</div>
              <div class="stat-label">共计条目</div>
            </div>
          </el-col>
          <el-col :span="disabledCount > 0 ? 8 : 12">
            <div class="stat-card">
              <div class="stat-num success">{{ normalCount }}</div>
              <div class="stat-label">正常</div>
            </div>
          </el-col>
          <el-col v-if="disabledCount > 0" :span="8">
            <div class="stat-card">
              <div class="stat-num danger">{{ disabledCount }}</div>
              <div class="stat-label">停用</div>
            </div>
          </el-col>
        </el-row>

        <div v-for="item in dataList" :key="item.dictCode" class="dict-item">
          <div class="dict-cell">
            <div class="dict-cell-key">标签</div>
            <div class="dict-cell-val">
              <el-tag
                v-if="item.listClass && item.listClass !== 'default'"
                :type="getTagType(item.listClass)"
                size="small">
                {{ item.dictLabel }}
              </el-tag>
              <span v-else>{{ item.dictLabel }}</span>
            </div>
          </div>
          <div class="dict-cell">
            <div class="dict-cell-key">键值</div>
            <div class="dict-cell-val">{{ item.dictValue }}</div>
          </div>
          <div class="dict-cell">
            <div class="dict-cell-key">状态</div>
            <div class="dict-cell-val">
              <el-tag
                :type="item.status === '0' ? 'success' : 'danger'"
                size="small">
                {{ item.status === '0' ? '正常' : '停用' }}
              </el-tag>
            </div>
          </div>
        </div>
      </template>
    </div>
  </el-drawer>
</template>

<script setup lang="ts">
import type { SysDictData, SysDictType } from '@/types/api/system/dict'

import { Document, List, Loading } from '@element-plus/icons-vue'

import { listData } from '../api'

const props = withDefaults(
  defineProps<{
    visible: boolean
    row?: SysDictType
  }>(),
  {
    visible: false,
    row: () => ({})
  }
)

const emit = defineEmits<{
  (event: 'update:visible', value: boolean): void
}>()

const loading = ref(false)
const dataList = ref<SysDictData[]>([])

const normalCount = computed(
  () => dataList.value.filter((item) => item.status === '0').length
)
const disabledCount = computed(
  () => dataList.value.filter((item) => item.status !== '0').length
)

function getTagType(listClass?: string) {
  if (!listClass || listClass === 'primary') return undefined
  if (['success', 'warning', 'info', 'danger'].includes(listClass)) {
    return listClass as 'success' | 'warning' | 'info' | 'danger'
  }
}

watch(
  () => props.visible,
  (visible) => {
    if (visible) {
      loadData()
    } else {
      dataList.value = []
    }
  }
)

function loadData() {
  if (!props.row?.dictType) return
  loading.value = true
  dataList.value = []
  listData({ dictType: props.row.dictType, pageSize: 100, pageNum: 1 })
    .then((response) => {
      dataList.value = response.rows || []
    })
    .catch(() => {})
    .finally(() => {
      loading.value = false
    })
}
</script>

<style scoped lang="scss">
.drawer-head {
  display: flex;
  align-items: center;
}

.drawer-head-icon {
  margin-right: 8px;
  color: #5b9bd5;
}

.drawer-head-name {
  margin-right: 8px;
  color: #2c3e50;
  font-size: 16px;
  font-weight: 600;
}

.drawer-head-type {
  color: #95a5a6;
  font-family: monospace;
  font-size: 14px;
}

.drawer-wrap {
  padding: 0 20px 20px;
}

.drawer-loading {
  display: flex;
  gap: 8px;
  align-items: center;
  justify-content: center;
  height: 120px;
  color: #aaa;
  font-size: 13px;
}

.drawer-empty {
  padding: 60px 0;
  color: #bbb;
  font-size: 13px;
  text-align: center;

  .el-icon {
    display: block;
    margin: 0 auto 8px;
    font-size: 36px;
  }
}

.stat-row {
  margin-bottom: 16px;
}

.stat-card {
  padding: 10px 14px;
  text-align: center;
  background: #f7f9fb;
  border: 1px solid #e8ecf0;
  border-radius: 6px;
}

.stat-num {
  color: #2c3e50;
  font-size: 22px;
  font-weight: 700;

  &.success {
    color: #27ae60;
  }

  &.danger {
    color: #e74c3c;
  }
}

.stat-label {
  margin-top: 4px;
  color: #95a5a6;
  font-size: 11px;
}

.dict-item {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  margin-bottom: 8px;
  overflow: hidden;
  border: 1px solid #e8ecf0;
  border-radius: 6px;
}

.dict-cell {
  display: grid;
  grid-template-columns: 70px 1fr;
  border-right: 1px solid #f0f4f8;

  &:last-child {
    border-right: 0;
  }
}

.dict-cell-key {
  padding: 9px 14px;
  color: #888;
  font-size: 12px;
  background: #f7f9fb;
  border-right: 1px solid #f0f4f8;
}

.dict-cell-val {
  display: flex;
  align-items: center;
  padding: 9px 14px;
  color: #2c3e50;
  font-size: 13px;
  word-break: break-all;
}
</style>
