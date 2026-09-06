<script setup>
import BaseForm from '@/BaseComponent/BaseForm'
import emitter from '@/utils/bus'
import businessStore from '@/store/business/businessStore'

const props = defineProps({
  searchConfig: {
    type: Object,
    required: true,
  },
  pageName: {
    type: String,
    required: true,
  },
  initSearch: {
    type: Object,
    default: () => ({}),
  },
  otherRequestOption: {
    type: Object,
    default: () => ({}),
  },
  showSearch: {
    type: Boolean,
    default: true,
  },
  reset: {
    type: Function,
    default: null,
  },
  cacheKey: {
    type: String,
    default: '',
  },
})

const store = businessStore()
const pageSearchRef = useTemplateRef('pageSearchRef')
const baseFormRef = useTemplateRef('baseFormRef')
const searchLoading = ref(false)
const formItem = computed(() => props.searchConfig?.formItems ?? [])
const formData = ref({})

const search = (isReset = false) => {
  searchLoading.value = true
  emitter.emit(`search${props.pageName}Info`, {
    ...formData.value,
    ...props.otherRequestOption,
    searchLoading,
    resetPaginationInfo: typeof isReset === 'boolean' ? isReset : false,
  })
}

const resetForm = (isReset) => {
  if (props.reset) {
    props.reset()
  } else {
    baseFormRef.value?.formRef?.resetFields()
    search(isReset)
  }
}

const keyUpEnter = () => {
  search()
}

const setFormData = (key, value) => {
  formData.value[key] = value
}

const resizeObserver = new ResizeObserver((entries) => {
  for (const entry of entries) {
    const newHeight = entry.target.getBoundingClientRect().height
    emitter.emit(`change${props.pageName}Size`, newHeight)
  }
})

const showPageSearch = computed(() => {
  const key = `${props.pageName}${props.cacheKey}SearchShow`
  if (typeof store.pageSearchControl[key] !== 'boolean') {
    return true
  }
  return store.pageSearchControl[key]
})

onMounted(() => {
  for (const item of formItem.value) {
    formData.value[item.field] = item.default ?? undefined
  }
  for (const key in props.initSearch) {
    formData.value[key] = props.initSearch[key]
  }
  if (pageSearchRef.value) {
    resizeObserver.observe(pageSearchRef.value)
  }
})

onBeforeUnmount(() => {
  resizeObserver.disconnect()
})

defineExpose({
  formData,
  search,
  setFormData,
})
</script>

<template>
  <div v-show="showPageSearch" ref="pageSearchRef" class="page-search">
    <div class="search">
      <BaseForm
        ref="baseFormRef"
        v-bind="searchConfig"
        :data="formData"
        @key-up-enter="keyUpEnter"
      >
        <template #footer>
          <div v-if="showSearch" class="footer">
            <a-button
              type="primary"
              :loading="searchLoading"
              @click="search(false)"
            >
              <template #icon>
                <SvgIcon icon-class="search" :size="14" />
              </template>
              <span class="ml6">检索</span>
            </a-button>
            <a-button :loading="searchLoading" @click="resetForm(true)">
              <template #icon>
                <SvgIcon icon-class="ant-icon-SyncOutlined" :size="14" />
              </template>
              <span class="ml6">重置</span>
            </a-button>
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
  padding-bottom: 20px;
  background-color: var(--ba-bg-color-overlay);
  border-bottom: 1px solid var(--ba-border-color);
  font-size: 14px;
}

.footer {
  text-align: right;
  display: inline-flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
}
</style>
