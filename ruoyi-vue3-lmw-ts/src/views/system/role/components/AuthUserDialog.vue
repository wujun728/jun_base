<script setup lang="ts">
import { authUserSelectAll } from '@/api/system/role'
import getSearchConfig from '../config/authDialogSearch.ts'
import getContentConfig from '../config/authContent.ts'
import to from '@/utils/to'
import { systemBaseUrl } from '@/api/config/base'
import { authRole } from '@/views/pageName'
import { proxy } from '@/utils/provide'

const modelValue = defineModel<boolean>('modelValue')
const emits = defineEmits(['saveSuccess'])
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

const tableSelected = ref<anyObj[]>([])
const tableListener = {
  selectionChange: (selected: anyObj[]) => {
    tableSelected.value = selected
  },
}

const headerButtons: ButtonsType[] = []

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
    modelValue.value = false
    emits('saveSuccess', res)
  }
  loading.value = false
}

const handleCancel = () => {
  modelValue.value = false
}
</script>
<template>
  <div class="authUserDialog">
    <el-dialog
      v-model="modelValue"
      :width="getWidth('850px')"
      title="选择用户"
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
        :autoDesc="false"
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
