<script setup>
import BaseForm from '@/BaseComponent/BaseForm'
import emitter from '@/utils/hsj/bus'
import businessStore from '@/store/business/businessStore'

const props = defineProps({
  searchConfig: {
    type: Object,
    default: () => {},
  },
  // 初始化form的搜索的值
  initSearch: {
    type: Object,
    default: () => {},
  },
  // 页面名称与PageContent和PageDialog的一致，每个页面必须唯一
  pageName: {
    type: String,
    required: true,
  },
  // 除form表单外的其他搜索条件
  otherRequestOption: {
    type: Object,
    default: () => {},
  },
  // 是否显示检索和重置按钮
  showSearch: {
    type: Boolean,
    default: true,
  },
  // 自定义重置函数
  reset: {
    type: Function,
  },
  // 当页面会存在打开多个的时候会出现缓存问题，可以用这个区分缓存
  cacheKey: {
    type: [String, Number],
    default: '',
  },
})
const store = businessStore()
const pageSearchRef = ref(null)
const baseFormRef = ref(null)
const searchLoading = ref(false)
const formItem = props.searchConfig?.formItems ?? []

let formData = ref({})
const search = (isReset = false) => {
  searchLoading.value = true
  emitter.emit(`search${props.pageName}Info`, {
    ...formData.value,
    ...props.otherRequestOption,
    searchLoading,
    resetPaginationInfo: typeof isReset === 'boolean' ? isReset : false,
  })
}
const reset = (isReset) => {
  // 判断是否传入自定义的重置函数
  if (props.reset) {
    props.reset()
  } else {
    baseFormRef.value?.elFormRef?.resetFields()
    search(isReset)
  }
}

const keyUpEnter = () => {
  search()
}

const setFormData = (key, value) => {
  formData.value[key] = value
}
// 监听dom尺寸变化
const resizeObserver = new ResizeObserver((entries) => {
  for (let entry of entries) {
    // 获取目标元素的新尺寸
    const newHeight = entry.target.getBoundingClientRect().height
    emitter.emit(`change${props.pageName}Size`, newHeight)
  }
})
// 是否显示组件
const showPageSearch = computed(() => {
  return store.pageSearchControl[`${props.pageName}${props.cacheKey}SearchShow`]
})
onMounted(() => {
  for (const item of formItem) {
    formData.value[item.field] = item.default ?? void 0
  }
  for (const key in props.initSearch) {
    formData.value[key] = props.initSearch[key]
  }
  resizeObserver.observe(pageSearchRef.value)
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
        v-model:data="formData"
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
              @click="reset(true)"
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
