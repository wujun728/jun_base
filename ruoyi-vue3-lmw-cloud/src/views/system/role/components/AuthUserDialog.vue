<script setup>
import { authUserSelectAll } from '@/api/system/role'
import getSearchConfig from '../config/authDialogSearch.js'
import getContentConfig from '../config/authContent.js'
import to from '@/utils/to'
import { systemBaseUrl } from '@/api/config/base.js'
import { authRole } from '@/views/pageName'
const props = defineProps({
  modelValue: {
    type: Boolean,
  },
})
const emits = defineEmits(['update:modelValue', 'saveSuccess'])

const proxy = inject('proxy')
const route = useRoute()
const pageContentRef = ref(null)
const pageSearchRef = ref(null)
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
const search = () => {
  pageSearchRef.value?.search()
}

const descConfig = ref({})

const headerButtons = []

const otherRequestOption = ref({
  roleId: roleId,
})

const loading = ref(false)

const commitClick = async () => {
  if (tableSelected.value.length === 0) {
    proxy.$modal.msgWarning('请勾选用户')
    return
  }
  loading.value = true
  const uIds = tableSelected.value.map((item) => item.userId)
  const [res] = await to(
    authUserSelectAll({ roleId: roleId, userIds: uIds.toString() })
  )
  if (res?.code === 200) {
    proxy.$modal.notifySuccess(res.msg)
    handleValueChange(false)
    // search()
    emits('saveSuccess', res)
  }
  loading.value = false
}

const handleCancel = () => {
  handleValueChange(false)
}

const handleValueChange = (value) => {
  emits('update:modelValue', value)
}
</script>
<template>
  <div class="authUserDialog">
    <el-dialog
      :width="getWidth('850px')"
      title="选择用户"
      :model-value="modelValue"
      @update:modelValue="handleValueChange($event)"
      draggable
      destroy-on-close
    >
      <PageSearch
        ref="pageSearchRef"
        :pageName="pageName"
        :otherRequestOption="otherRequestOption"
        :searchConfig="searchConfig"
      ></PageSearch>
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
          <el-tag
            :type="backData.status == '0' ? 'success' : 'danger'"
            class="mx-1"
            effect="light"
          >
            {{ backData.status == '0' ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </PageContent>
      <template #footer>
        <el-button :loading="loading" @click="handleCancel"> 取消 </el-button>
        <el-button type="primary" @click="commitClick" :loading="loading">
          保存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.authUserDialog {
  :deep(.el-pagination) {
    padding-top: 20px;
  }
}
</style>
