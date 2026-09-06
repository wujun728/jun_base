<template>
  <div class="art-icon-picker">
    <ElInput
      :model-value="displayValue"
      :placeholder="placeholder"
      clearable
      @click="visible = true"
      @clear="handleClear">
      <template #prefix>
        <ArtSvgIcon
          v-if="displayValue"
          :icon="`ri:${displayValue}`"
          class="text-base" />
        <el-icon v-else><Search /></el-icon>
      </template>
    </ElInput>

    <ElDialog
      :model-value="visible"
      :title="title"
      width="760px"
      top="5vh"
      :close-on-click-modal="false"
      @update:model-value="visible = $event"
      @closed="handleClosed">
      <div class="flex flex-col gap-4">
        <ElInput
          v-model="searchText"
          placeholder="搜索图标名称"
          :clearable="!!displayValue"
          :prefix-icon="Search" />

        <div class="art-icon-picker__categories">
          <div class="art-icon-picker__category-list">
            <ElTag
              :type="activeCategory === '' ? 'primary' : 'info'"
              effect="plain"
              class="shrink-0 cursor-pointer!"
              @click="activeCategory = ''">
              全部 ({{ allTotal }})
            </ElTag>
            <ElTag
              v-for="cat in categories"
              :key="cat.name"
              :type="activeCategory === cat.name ? 'primary' : 'info'"
              effect="plain"
              class="shrink-0 cursor-pointer!"
              @click="activeCategory = cat.name">
              {{ cat.label }} ({{ cat.count }})
            </ElTag>
          </div>
        </div>

        <div class="art-icon-picker__grid">
          <button
            v-for="icon in pageIcons"
            :key="icon"
            type="button"
            class="art-icon-picker__item"
            :class="{ 'is-active': icon === selectedIcon }"
            :title="icon"
            @click="selectIcon(icon)">
            <ArtSvgIcon :icon="`ri:${icon}`" class="art-icon-picker__icon" />
          </button>
          <div
            v-if="pageIcons.length === 0"
            class="col-span-full py-12 text-center text-gray-400">
            未找到匹配的图标
          </div>
        </div>

        <ElPagination
          v-if="totalPages > 1"
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="filteredIcons.length"
          layout="prev, pager, next"
          small
          background
          class="justify-center!" />
      </div>

      <template #footer>
        <ElButton @click="visible = false">取消</ElButton>
        <ElButton type="primary" @click="handleConfirm">确定</ElButton>
      </template>
    </ElDialog>
  </div>
</template>

<script setup lang="ts">
import type { IconCategory } from './types'

import { Search } from '@element-plus/icons-vue'
import riMeta from '@iconify-json/ri/metadata.json'

defineOptions({ name: 'ArtIconPicker' })

interface Props {
  modelValue?: string
  placeholder?: string
  title?: string
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: '',
  placeholder: '请选择图标',
  title: '选择图标'
})

const emit = defineEmits<{
  'update:modelValue': [value: string]
}>()

const visible = ref(false)
const searchText = ref('')
const activeCategory = ref('')
const selectedIcon = ref('')
const currentPage = ref(1)
const pageSize = 48

const categoryLabels: Record<string, string> = {
  Arrows: '箭头',
  Buildings: '建筑',
  Business: '商务',
  Communication: '通信',
  Design: '设计',
  Development: '开发',
  Device: '设备',
  Document: '文档',
  Editor: '编辑器',
  Finance: '金融',
  Food: '食物',
  'Game & Sports': '游戏运动',
  'Health & Medical': '健康医疗',
  Logos: '品牌',
  Map: '地图',
  Media: '媒体',
  Others: '其他',
  System: '系统',
  'User & Faces': '用户表情',
  Weather: '天气'
}

const normalizeIconName = (icon: string) => icon.replace(/^ri:/, '')

const categories = computed<IconCategory[]>(() => {
  const cats = riMeta.categories || {}
  return Object.entries(cats).map(([name, icons]) => ({
    name,
    label: categoryLabels[name] || name,
    count: icons.length,
    icons
  }))
})

const allIcons = computed<string[]>(() => {
  if (activeCategory.value) {
    const cat = categories.value.find((c) => c.name === activeCategory.value)
    return cat?.icons || []
  }
  return categories.value.flatMap((c) => c.icons)
})

const filteredIcons = computed(() => {
  const keyword = searchText.value.trim().toLowerCase()
  if (!keyword) return allIcons.value
  return allIcons.value.filter((name) => name.includes(keyword))
})

const allTotal = computed(() =>
  categories.value.reduce((total, cat) => total + cat.count, 0)
)

const totalCount = computed(() => filteredIcons.value.length)

const totalPages = computed(() =>
  Math.ceil(filteredIcons.value.length / pageSize)
)

const pageIcons = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return filteredIcons.value.slice(start, start + pageSize)
})

const displayValue = computed(() => normalizeIconName(props.modelValue))

watch(visible, (val) => {
  if (val) {
    selectedIcon.value = normalizeIconName(props.modelValue)
    searchText.value = ''
    activeCategory.value = ''
    currentPage.value = 1
  }
})

watch(searchText, () => {
  currentPage.value = 1
})

watch(activeCategory, () => {
  currentPage.value = 1
})

function selectIcon(icon: string) {
  selectedIcon.value = icon
}

function handleConfirm() {
  emit('update:modelValue', selectedIcon.value)
  visible.value = false
}

function handleClear() {
  emit('update:modelValue', '')
}

function handleClosed() {
  selectedIcon.value = ''
  searchText.value = ''
  activeCategory.value = ''
  currentPage.value = 1
}
</script>

<style scoped lang="scss">
.art-icon-picker {
  :deep(.el-input) {
    cursor: pointer;
  }

  &__categories {
    padding: 8px;
    background: var(--el-fill-color-lighter);
    border: 1px solid var(--el-border-color-lighter);
    border-radius: 8px;
  }

  &__category-list {
    display: flex;
    flex-wrap: wrap;
    gap: 6px;
  }

  &__grid {
    display: grid;
    grid-template-columns: repeat(8, 1fr);
    gap: 0;
    overflow: hidden;
    border-top: 1px solid var(--el-border-color-lighter);
    border-left: 1px solid var(--el-border-color-lighter);
    // border-radius: 8px;
  }

  &__item {
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    aspect-ratio: 1;
    min-height: 58px;
    color: var(--el-text-color-regular);
    background: var(--el-bg-color);
    border: 0;
    border-right: 1px solid var(--el-border-color-lighter);
    border-bottom: 1px solid var(--el-border-color-lighter);
    outline: none;
    cursor: pointer;
    transition: all 0.18s ease;

    &:hover {
      z-index: 1;
      color: var(--el-color-primary);
      background: var(--el-color-primary-light-9);
      box-shadow: inset 0 0 0 1px var(--el-color-primary-light-5);
    }

    &.is-active {
      z-index: 2;
      color: var(--el-color-primary);
      background: var(--el-color-primary-light-9);
      box-shadow: inset 0 0 0 2px var(--el-color-primary);
    }

    // &.is-active::after {
    //   position: absolute;
    //   right: 6px;
    //   bottom: 6px;
    //   width: 8px;
    //   height: 8px;
    //   content: '';
    //   background: var(--el-color-primary);
    //   border-radius: 50%;
    // }
  }

  &__icon {
    font-size: 24px;
  }
}
</style>
