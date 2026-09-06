<script setup lang="ts">
import BaseForm from '@/BaseComponent/BaseForm'
import emitter from '@/utils/hsj/bus'
import businessStore from '@/store/business/businessStore'
import type { SearchProps } from './types/types'
const {
  searchConfig,
  // 初始化form的搜索的值
  initSearch = {},
  // 页面名称与PageContent和PageDialog的一致，每个页面必须唯一
  pageName,
  // 除form表单外的其他搜索条件
  otherRequestOption = {},
  // 是否显示检索和重置按钮
  showSearch = true,
  // 自定义重置函数
  reset,
  // 当页面会存在打开多个的时候会出现缓存问题，可以用这个区分缓存
  cacheKey = '',
} = defineProps<SearchProps>()

const store = businessStore()
const pageSearchRef = useTemplateRef('pageSearchRef')
const baseFormRef = useTemplateRef('baseFormRef')
const searchLoading = ref(false)
const formItem = searchConfig?.formItems ?? []

let formData = ref<anyObj>({})
const search = (isReset = false) => {
  searchLoading.value = true
  emitter.emit(`search${pageName}Info`, {
    ...formData.value,
    ...otherRequestOption,
    searchLoading,
    resetPaginationInfo: typeof isReset === 'boolean' ? isReset : false,
  })
}
const resetForm = (isReset: boolean) => {
  // 判断是否传入自定义的重置函数
  if (reset) {
    reset()
  } else {
    baseFormRef.value?.elFormRef?.resetFields()
    search(isReset)
  }
}

const keyUpEnter = () => {
  search()
}

const setFormData = (key: string, value: any) => {
  formData.value[key] = value
}
// 监听dom尺寸变化
const resizeObserver = new ResizeObserver((entries) => {
  for (let entry of entries) {
    // 获取目标元素的新尺寸
    const newHeight = entry.target.getBoundingClientRect().height
    emitter.emit(`change${pageName}Size`, newHeight)
  }
})
// 是否显示组件
const showPageSearch = computed(() => {
  const key = `${pageName}${cacheKey}SearchShow`
  if (typeof store.pageSearchControl[key] !== 'boolean') {
    {
      return true
    }
  }
  return store.pageSearchControl[key]
})
onMounted(() => {
  for (const item of formItem) {
    formData.value[item.field] = item.default ?? void 0
  }
  for (const key in initSearch) {
    formData.value[key] = initSearch[key]
  }
  resizeObserver.observe(pageSearchRef.value!)
})
defineExpose({
  formData,
  search,
  setFormData,
})
</script>

<template>
  <div class="page-search" ref="pageSearchRef" v-show="showPageSearch">
    <div class="search">
      <BaseForm
        ref="baseFormRef"
        v-bind="searchConfig"
        :data="formData"
        @keyUpEnter="keyUpEnter"
      >
        <template #footer>
          <div class="footer" v-if="showSearch">
            <el-button
              type="primary"
              icon="Search"
              @click="search(false)"
              :loading="searchLoading"
              >检索</el-button
            >
            <el-button
              @click="resetForm(true)"
              icon="Refresh"
              :loading="searchLoading"
            >
              重置
            </el-button>
          </div>
        </template>
      </BaseForm>
    </div>
  </div>
</template>

<style scoped lang="scss">
.page-search {
  box-sizing: border-box;
  width: 100%;
  max-width: 100%;
  background-color: var(--ba-bg-color-overlay);
  border: 1px solid var(--ba-border-color);
  border-bottom: none;
  padding: 13px 15px;
  font-size: 14px;
}
.footer {
  text-align: right;
  display: inline-flex;
  justify-content: center;
  align-items: center;
}
</style>
