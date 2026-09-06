<script setup>
import { authUserSelectAll } from '@/api/system/role'
import getSearchConfig from '../config/authDialogSearch.js'
import getContentConfig from '../config/authContent.js'
import to from '@/utils/to'
import { systemBaseUrl } from '@/api/config/base.js'
import { authRole } from '@/views/pageName.js'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
})

const emits = defineEmits(['update:modelValue', 'saveSuccess'])

const proxy = inject('proxy')
const { sys_normal_disable } = proxy.useDict('sys_normal_disable')
const route = useRoute()
const pageContentRef = useTemplateRef('pageContentRef')
const pageSearchRef = useTemplateRef('pageSearchRef')
const pageName = authRole
const requestUrl = 'authUser/unallocatedList'
const requestBaseUrl = systemBaseUrl
const roleId = route.params.roleId

const tableHideItems = ref(['todo'])
const searchConfig = getSearchConfig()
const contentConfig = getContentConfig()

const contentConfigComputed = computed(() => {
  contentConfig.hideItems = tableHideItems
  return contentConfig
})

const tableSelected = ref([])
const tableListener = {
  selectionChange: (selected) => {
    tableSelected.value = selected
  },
}

const descConfig = ref({})
const headerButtons = []
const otherRequestOption = ref({
  roleId,
})
const loading = ref(false)

const open = computed({
  get: () => props.modelValue,
  set: (value) => emits('update:modelValue', value),
})

const commitClick = async () => {
  if (tableSelected.value.length === 0) {
    proxy.$modal.msgWarning('请勾选用户')
    return
  }
  loading.value = true
  const uIds = tableSelected.value.map((item) => item.userId)
  const [res] = await to(
    authUserSelectAll({ roleId, userIds: uIds.toString() })
  )
  if (res?.code === 200) {
    proxy.$modal.msgSuccess(res.msg)
    open.value = false
    emits('saveSuccess', res)
  }
  loading.value = false
}

const handleCancel = () => {
  open.value = false
}

const isSmall = window.isSmallScreen
</script>

<template>
  <a-modal
    v-model:open="open"
    title="选择用户"
    :width="getWidth('850px')"
    :mask-closable="false"
    :destroy-on-close="true"
    :wrap-class-name="isSmall ? 'page-dialog-fullscreen' : ''"
    class="auth-user-dialog"
  >
    <PageSearch
      ref="pageSearchRef"
      :pageName="pageName"
      :otherRequestOption="otherRequestOption"
      :searchConfig="searchConfig"
    />
    <PageContent
      ref="pageContentRef"
      :pageName="pageName"
      :contentConfig="contentConfigComputed"
      :descConfig="descConfig"
      :headerButtons="headerButtons"
      :showEdit="false"
      :showDelete="false"
      :requestUrl="requestUrl"
      :otherRequestOption="otherRequestOption"
      :tableListener="tableListener"
      :requestBaseUrl="requestBaseUrl"
    >
      <template #statusSlot="{ backData }">
        <DictTag :options="sys_normal_disable" :value="backData.status" />
      </template>
    </PageContent>
    <template #footer>
      <a-button :loading="loading" @click="handleCancel">取消</a-button>
      <a-button type="primary" :loading="loading" @click="commitClick">
        保存
      </a-button>
    </template>
  </a-modal>
</template>

<style scoped lang="scss">
.auth-user-dialog {
  :deep(.ant-pagination) {
    padding-top: 20px;
  }
}
</style>
