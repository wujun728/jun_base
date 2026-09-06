<script setup name="Dict">
import getSearchConfig from './config/searchConfig'
import getContentConfig from './config/contentConfig.js'
import getDialogConfig from './config/dialogConfig.js'
import useDialog from '@/hooks/useDialog'
import getComputedConfig from '@/hooks/getPageConfig'
import { refreshCache } from '@/api/system/dict/type'
import useDictStore from '@/store/modules/dict'
import to from '@/utils/to'
import { systemBaseUrl } from '@/api/config/base.js'
import { dictType } from '@/views/pageName.js'

const proxy = inject('proxy')
const { sys_normal_disable } = proxy.useDict('sys_normal_disable')
const router = useRouter()
const pageName = dictType
const idKey = 'dictId'
const sendIdKey = 'dictId'
const requestBaseUrl = systemBaseUrl
const pageSearchRef = useTemplateRef('pageSearchRef')
const pageContentRef = useTemplateRef('pageContentRef')
const dialogHideItems = ref([])
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

const dialogConfig = getDialogConfig()
const dialogConfigComputed = computed(() => {
  dialogConfig.hideItems = dialogHideItems
  return getComputedConfig(dialogConfig, dictMap)
})

const addCallBack = () => {
  dialogHideItems.value.length = 0
}

const editCallBack = () => {}

const [dialogRef, infoInit, addClick, editBtnClick] = useDialog(
  addCallBack,
  editCallBack,
  '添加'
)

const dialogWidth = ref('600px')
const searchData = computed(() => {
  return pageContentRef.value?.finalSearchData
})

const search = () => {
  pageSearchRef.value?.search()
}

const beforeSend = (queryInfo) => {
  if (queryInfo.dateRange && Array.isArray(queryInfo.dateRange)) {
    const dateRange = queryInfo.dateRange
    queryInfo['params[beginTime]'] = dateRange[0]
    queryInfo['params[endTime]'] = dateRange[1]
    delete queryInfo.dateRange
  }
}

const permission = ref({
  add: 'system:dict:add',
  edit: 'system:dict:edit',
  del: 'system:dict:remove',
})

const onChangeShowColumn = (filterArr) => {
  tableHideItems.value = filterArr
}

const handleExport = () => {
  proxy.download(
    'system/dict/type/export',
    {
      ...searchData.value,
    },
    `dict_${new Date().getTime()}.xlsx`
  )
}

const handleDictType = (row) => {
  router.push(`/system/dict-data/index/${row.dictId}`)
}

const refreshLoading = ref(false)
const handleRefreshCache = async () => {
  refreshLoading.value = true
  const [res] = await to(refreshCache())
  if (res) {
    proxy.$modal.msgSuccess('刷新成功')
    useDictStore().cleanDict()
  }
  refreshLoading.value = false
}
</script>

<template>
  <div class="default-main page">
    <PageSearch
      ref="pageSearchRef"
      :pageName="pageName"
      :searchConfig="searchConfigComputed"
    />
    <PageContent
      ref="pageContentRef"
      :pageName="pageName"
      :contentConfig="contentConfigComputed"
      :autoDesc="false"
      :dictMap="dictMap"
      :tableListener="tableListener"
      :tableSelected="tableSelected"
      :permission="permission"
      :idKey="idKey"
      :requestBaseUrl="requestBaseUrl"
      :tableHideItems="tableHideItems"
      @beforeSend="beforeSend"
      @addClick="addClick"
      @editBtnClick="editBtnClick"
      @onChangeShowColumn="onChangeShowColumn"
    >
      <template #handleLeft>
        <a-button
          class="order16"
          type="dashed"
          v-hasPermi="['system:dict:export']"
          @click="handleExport"
        >
          <template #icon>
            <SvgIcon size="14" iconClass="export" />
          </template>
          <span class="ml6">导出</span>
        </a-button>
        <a-button
          class="order17"
          type="primary"
          ghost
          v-hasPermi="['system:dict:remove']"
          :loading="refreshLoading"
          @click="handleRefreshCache"
        >
          <template #icon>
            <SvgIcon size="14" iconClass="sync-alt" />
          </template>
          <span class="ml6">刷新缓存</span>
        </a-button>
      </template>
      <template #dictTypeSlot="{ backData }">
        <a-button type="link" size="small" @click="handleDictType(backData)">
          {{ backData.dictType }}
        </a-button>
      </template>
      <template #statusSlot="{ backData }">
        <DictTag :options="sys_normal_disable" :value="backData.status" />
      </template>
    </PageContent>
    <PageDialog
      ref="dialogRef"
      :width="getWidth(dialogWidth)"
      :pageName="pageName"
      :dialogConfig="dialogConfigComputed"
      :infoInit="infoInit"
      :search="search"
      :idKey="idKey"
      :sendIdKey="sendIdKey"
      :requestBaseUrl="requestBaseUrl"
    />
  </div>
</template>

<style scoped lang="scss"></style>
