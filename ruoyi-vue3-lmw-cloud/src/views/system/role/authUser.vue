<script setup name="AuthUser">
import getSearchConfig from './config/authSearch.js'
import getContentConfig from './config/authContent.js'
import getComputedConfig from '@/hooks/getPageConfig'
import { authUserCancel, authUserCancelAll } from '@/api/system/role'
import AuthUserDialog from './components/AuthUserDialog.vue'
import to from '@/utils/to'
import { systemBaseUrl } from '@/api/config/base.js'
import { authUserRole } from '@/views/pageName'

const route = useRoute()
const roleId = route.params.roleId
const proxy = inject('proxy')
const { sys_normal_disable } = proxy.useDict('sys_normal_disable')
const pageId = useId()
const pageName = authUserRole + pageId
const requestUrl = 'authUser/allocatedList'
const requestBaseUrl = systemBaseUrl
const otherRequestOption = ref({
  roleId: roleId,
})
const pageSearchRef = ref(null)
const pageContentRef = ref(null)
const descConfig = ref({})

const tableHideItems = ref([])
const dictMap = {
  status: sys_normal_disable,
}
const searchConfig = getSearchConfig()
const searchConfigComputed = computed(() => {
  return getComputedConfig(searchConfig, dictMap)
})
const tableSelected = ref([])
const tableListener = {
  selectionChange: (selected) => {
    tableSelected.value = selected
  },
}
const contentConfig = getContentConfig()
const contentConfigComputed = computed(() => {
  contentConfig.hideItems = tableHideItems
  return contentConfig
})

const search = () => {
  pageSearchRef.value?.search()
}

const headerButtons = ['refresh', 'columnDisplay', 'comSearch']

const onChangeShowColumn = (filterArr) => {
  tableHideItems.value = filterArr
}

const deleteRow = async () => {
  let uIds = tableSelected.value.map((item) => {
    return item.userId
  })
  const [res] = await to(
    authUserCancelAll({ roleId: roleId, userIds: uIds.toString() })
  )
  if (res) {
    search()
  }
}

const addClickUser = () => {
  dialogVisible.value = true
}

const handleAuthUser = (row) => {
  proxy.$modal
    .confirm('确认要取消该用户"' + row.userName + '"角色吗？')
    .then(function () {
      return authUserCancel({ userId: row.userId, roleId: roleId })
    })
    .then(() => {
      search()
      proxy.$modal.notifySuccess('取消授权成功')
    })
    .catch(() => {})
}

const handleClose = () => {
  const obj = { path: '/system/role' }
  proxy.$tab.closeOpenPage(obj)
}

const dialogVisible = ref(false)

const saveSuccess = () => {
  search()
}

const init = () => {}

init()
</script>
<template>
  <div class="default-main page">
    <PageSearch
      ref="pageSearchRef"
      :pageName="pageName"
      :otherRequestOption="otherRequestOption"
      :searchConfig="searchConfigComputed"
    ></PageSearch>
    <PageContent
      ref="pageContentRef"
      :pageName="pageName"
      :contentConfig="contentConfigComputed"
      :descConfig="descConfig"
      :dictMap="dictMap"
      :tableListener="tableListener"
      :tableSelected="tableSelected"
      :headerButtons="headerButtons"
      :showEdit="false"
      :showDelete="false"
      :requestUrl="requestUrl"
      :requestBaseUrl="requestBaseUrl"
      :otherRequestOption="otherRequestOption"
      @onChangeShowColumn="onChangeShowColumn"
    >
      <template #handleLeft>
        <el-button
          class="ml12"
          type="primary"
          v-hasPermi="['system:role:add']"
          @click="addClickUser"
        >
          <SvgIcon :size="14" iconClass="plus"></SvgIcon>
          <span class="ml6">添加用户</span>
        </el-button>

        <div v-hasPermi="['system:role:remove']" class="ml12">
          <el-popconfirm
            title="确定批量取消授权选中记录？"
            confirm-button-text="确认"
            cancel-button-text="取消"
            confirmButtonType="danger"
            :hide-after="0"
            @confirm="deleteRow"
          >
            <template #reference>
              <el-button type="danger" :disabled="tableSelected.length === 0">
                <SvgIcon size="14" iconClass="times"></SvgIcon>
                <span class="ml6">批量取消授权</span>
              </el-button>
            </template>
          </el-popconfirm>
        </div>
        <el-button class="ml12" @click="handleClose" type="warning">
          <SvgIcon size="14" iconClass="times" />
          <span class="ml6">关闭</span>
        </el-button>
      </template>
      <template #todoSlot="{ backData }">
        <el-button
          class="mt6 order11"
          size="small"
          type="primary"
          @click="handleAuthUser(backData)"
          v-hasPermi="['system:role:edit']"
        >
          <SvgIcon size="11" iconClass="times" />
          <span class="ml6">取消授权</span>
        </el-button>
      </template>
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
    <AuthUserDialog
      v-model="dialogVisible"
      @saveSuccess="saveSuccess"
    ></AuthUserDialog>
  </div>
</template>

<style scoped lang="scss">
.page {
  :deep(.statusClass .el-radio-group) {
    width: 100%;
  }
}
</style>
