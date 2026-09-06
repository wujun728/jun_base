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
const cacheKey = route.params.roleId
const pageName = authUserRole
const requestUrl = 'authUser/allocatedList'
const requestBaseUrl = systemBaseUrl
const otherRequestOption = ref({
  roleId,
})

const pageSearchRef = useTemplateRef('pageSearchRef')
const pageContentRef = useTemplateRef('pageContentRef')
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

const headerButtons = ['refresh', 'columnDisplay', 'comSearch']
const dialogVisible = ref(false)

const search = () => {
  pageSearchRef.value?.search()
}

const onChangeShowColumn = (filterArr) => {
  tableHideItems.value = filterArr
}

const deleteRow = async () => {
  const uIds = tableSelected.value.map((item) => item.userId)
  const [res] = await to(
    authUserCancelAll({ roleId, userIds: uIds.toString() })
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
    .confirm(`确认要取消该用户"${row.userName}"角色吗？`)
    .then(() => authUserCancel({ userId: row.userId, roleId }))
    .then(() => {
      search()
      proxy.$modal.msgSuccess('取消授权成功')
    })
    .catch(() => {})
}

const handleClose = () => {
  proxy.$tab.closeOpenPage({ path: '/system/role' })
}

const saveSuccess = () => {
  search()
}
</script>

<template>
  <div class="default-main page">
    <PageSearch
      ref="pageSearchRef"
      :pageName="pageName"
      :otherRequestOption="otherRequestOption"
      :searchConfig="searchConfigComputed"
      :cacheKey="cacheKey"
    />
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
      :cacheKey="cacheKey"
      :tableHideItems="tableHideItems"
      @onChangeShowColumn="onChangeShowColumn"
    >
      <template #handleLeft>
        <a-button
          type="primary"
          v-hasPermi="['system:role:add']"
          @click="addClickUser"
        >
          <template #icon>
            <SvgIcon :size="14" iconClass="plus" />
          </template>
          <span class="ml6">添加用户</span>
        </a-button>

        <a-popconfirm
          title="确定批量取消授权选中记录？"
          ok-text="确认"
          cancel-text="取消"
          @confirm="deleteRow"
        >
          <a-button
            danger
            v-hasPermi="['system:role:remove']"
            :disabled="tableSelected.length === 0"
          >
            <template #icon>
              <SvgIcon size="14" iconClass="close" />
            </template>
            <span class="ml6">批量取消授权</span>
          </a-button>
        </a-popconfirm>

        <a-button type="dashed" @click="handleClose">
          <template #icon>
            <SvgIcon size="14" iconClass="close" />
          </template>
          <span class="ml6">关闭</span>
        </a-button>
      </template>
      <template #todoSlot="{ backData }">
        <a-button
          class="order11"
          size="small"
          type="primary"
          v-hasPermi="['system:role:edit']"
          @click="handleAuthUser(backData)"
        >
          <template #icon>
            <SvgIcon size="11" iconClass="close" />
          </template>
          <span class="ml6">取消授权</span>
        </a-button>
      </template>
      <template #statusSlot="{ backData }">
        <DictTag :options="sys_normal_disable" :value="backData.status" />
      </template>
    </PageContent>
    <AuthUserDialog v-model="dialogVisible" @saveSuccess="saveSuccess" />
  </div>
</template>

<style scoped lang="scss"></style>
