<template>
  <a-card :body-style="{ padding: '12px 16px' }">
    <a-form :model="modelValue" class="search-form">
      <a-row :gutter="[16, 16]">
        <!-- 动态渲染表单项 -->
        <a-col v-for="field in visibleFields" :key="field.name" :span="field.span || 8">
          <a-form-item :label="field.label">
            <a-input
                v-if="field.type === 'input'"
                v-model:value="modelValue[field.name]"
                :placeholder="field.placeholder || `请输入${field.label}`"
                :style="getFieldStyle(field)"
                v-bind="field.props"
                allow-clear
            />
            <a-select
                v-else-if="field.type === 'select'"
                v-model:value="modelValue[field.name]"
                :options="field.options"
                :placeholder="field.placeholder || `请选择${field.label}`"
                :style="getFieldStyle(field)"
                v-bind="field.props"
                allow-clear
            />
            <a-date-picker
                v-else-if="field.type === 'date'"
                v-model:value="modelValue[field.name]"
                :placeholder="field.placeholder || '请选择日期'"
                :style="getFieldStyle(field)"
                v-bind="field.props"
                allow-clear
            />
            <a-range-picker
                v-else-if="field.type === 'daterange'"
                v-model:value="modelValue[field.name]"
                :style="getFieldStyle(field)"
                v-bind="field.props"
                allow-clear
            />
          </a-form-item>
        </a-col>

        <!-- 关键改动(2): 按钮区域使用动态计算的 span，而不是 flex: 1 -->
        <a-col :span="buttonColSpan" class="search-actions">
          <a-form-item>
            <a-space>
              <a-button type="primary" @click="handleSearch">
                <BearJiaIcon icon="SearchOutlined"/>
                查询
              </a-button>
              <a-button @click="handleReset">
                <BearJiaIcon icon="ReloadOutlined"/>
                重置
              </a-button>
              <a v-if="fields.length > defaultVisibleCount" class="expand-link" @click="expand = !expand">
                {{ expand ? '收起' : '展开' }}
                <component :is="expand ? UpOutlined : DownOutlined"/>
              </a>
            </a-space>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-card>
</template>

<script lang="ts" setup>
import type {PropType} from 'vue'
import {computed, ref} from 'vue'
// 确保您的图标组件和 antd 图标都已正确引入
import {BearJiaIcon} from '@/utils/BearJiaIcon.js';
import {DownOutlined, UpOutlined} from '@ant-design/icons-vue'

// --- 类型定义 ---
interface FormItemOption {
  label: string
  value: string | number
}

interface FormField {
  name: string
  label: string
  type: 'input' | 'select' | 'date' | 'daterange'
  placeholder?: string
  options?: FormItemOption[]
  span?: number
  width?: number | string
  props?: Record<string, any> // 支持传入任意原生组件属性
}

// --- Props 和 Emits (保持不变) ---
const props = defineProps({
  modelValue: {
    type: Object as PropType<Record<string, any>>,
    required: true,
  },
  fields: {
    type: Array as PropType<FormField[]>,
    required: true,
  },
  defaultVisibleCount: {
    type: Number,
    default: 3,
  },
})

const emit = defineEmits(['update:modelValue', 'search', 'reset'])

// --- 响应式状态 (保持不变) ---
const expand = ref(false)

// --- 计算属性 ---
const visibleFields = computed(() => {
  if (expand.value || props.fields.length <= props.defaultVisibleCount) {
    return props.fields
  }
  return props.fields.slice(0, props.defaultVisibleCount)
})

// 让按钮与字段在同一行
const buttonColSpan = computed(() => {
  const occupiedSpan = visibleFields.value.reduce((acc, field) => acc + (field.span || 8), 0)
  const remainingSpan = 24 - (occupiedSpan % 24)
  // 如果剩余空间少于8个span，就使用剩余空间；否则使用8个span
  return remainingSpan === 24 ? 8 : Math.max(remainingSpan, 8)
})


// --- 方法 (保持不变) ---
const getFieldStyle = (field: FormField) => {
  const widthValue = field.width
      ? typeof field.width === 'number'
          ? `${field.width}px`
          : field.width
      : '100%' // 默认使用100%宽度
  return {
    width: widthValue,
    minWidth: '180px' // 确保最小宽度，防止下拉框选择后变小
  }
}

const handleSearch = () => {
  emit('search')
}

const handleReset = () => {
  const clearedObject = Object.fromEntries(Object.keys(props.modelValue).map(k => [k, undefined]))
  emit('update:modelValue', clearedObject)
  emit('reset')
}
</script>

<style scoped>
.search-form {
  width: 100%;
}

:deep(.ant-form-item) {
  display: flex;
  flex-direction: row;
  width: 100%;
  margin-bottom: 0;
}

:deep(.ant-form-item-control-wrapper),
:deep(.ant-form-item-control) {
  flex: 1 1 0;
  min-width: 180px;
}

/* 确保表单控件保持合适的宽度 */
:deep(.ant-select),
:deep(.ant-input),
:deep(.ant-picker) {
  min-width: 180px; /* 统一最小宽度 */
  width: 100% !important; /* 强制宽度为100% */
  box-sizing: border-box; /* 确保宽度计算包含边框和内边距 */
}

/* 特别针对下拉框的样式优化 - 修复宽度问题 */
:deep(.ant-select-selector) {
  min-width: 180px !important;
  width: 100% !important;
  box-sizing: border-box;
}

/* 确保下拉框选择后保持宽度 */
:deep(.ant-select-single.ant-select-show-arrow .ant-select-selection-item) {
  padding-right: 18px; /* 为箭头留出空间 */
}

/* 修复下拉框选择后的宽度问题 */
:deep(.ant-select-single) {
  width: 100% !important;
}

:deep(.ant-select-single .ant-select-selector) {
  width: 100% !important;
}

.search-actions {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  height: 100%;
}

.search-actions :deep(.ant-form-item) {
  margin-bottom: 0;
}

/* 修复展开/收起按钮换行问题 */
.expand-link {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  cursor: pointer;
  white-space: nowrap; /* 防止文字换行 */
  flex-shrink: 0; /* 防止按钮被压缩 */
}

/* 确保按钮区域有足够空间 */
:deep(.ant-space) {
  flex-wrap: nowrap; /* 防止按钮换行 */
}

:deep(.ant-space-item) {
  flex-shrink: 0; /* 防止按钮被压缩 */
}
</style>
