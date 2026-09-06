<script setup name="Dict">
import { nextTick } from 'vue'
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
const pageSearchRef = ref(null)
const pageContentRef = ref(null)
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
const editCallBack = (item, type, res) => {
  isEditMore.value = type
}
const isEditMore = ref(false)
const editMoreClick = () => {
  if (tableSelected.value.length > 0) {
    const data = tableSelected.value[0]
    pageContentRef.value?.editClick(data, true)
    nextTick(() => {
      const newArray = tableSelected.value.slice(1)
      dialogRef.value?.changeSelected(newArray)
    })
  }
}

const editNext = (data) => {
  pageContentRef.value?.editClick(data, true)
}

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

/** 导出按钮操作 */
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
  router.push({
    path: '/system/dict-data/index/' + row.dictId,
  })
}
const refreshLoading = ref(false)
const handleRefreshCache = async () => {
  refreshLoading.value = true
  const [res] = await to(refreshCache())
  if (res) {
    proxy.$modal.notifySuccess('刷新成功')
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
    ></PageSearch>
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
      @beforeSend="beforeSend"
      @addClick="addClick"
      @editBtnClick="editBtnClick"
      @onChangeShowColumn="onChangeShowColumn"
      @editMoreClick="editMoreClick"
    >
      <template #handleLeft>
        <el-button
          class="order17 ml12"
          type="warning"
          v-hasPermi="['system:post:export']"
          @click="handleExport"
        >
          <SvgIcon size="14" iconClass="download" />
          <span class="ml6">导出</span>
        </el-button>
        <el-button
          class="order18 ml12"
          type="success"
          v-hasPermi="['system:dict:remove']"
          @click="handleRefreshCache"
          :loading="refreshLoading"
        >
          <SvgIcon size="14" iconClass="refresh" />
          <span class="ml6">刷新缓存</span>
        </el-button>
      </template>
      <template #dictTypeSlot="{ backData }">
        <el-button link type="primary" @click="handleDictType(backData)">
          {{ backData.dictType }}
        </el-button>
      </template>
      <template #statusSlot="{ backData }">
        <el-tag :type="backData.status == 0 ? 'success' : 'danger'">
          {{ backData.status == 0 ? '启用' : '禁用' }}
        </el-tag>
      </template>
    </PageContent>
    <PageDialog
      ref="dialogRef"
      :width="getWidth(dialogWidth)"
      :pageName="pageName"
      :dialogConfig="dialogConfigComputed"
      :infoInit="infoInit"
      :search="search"
      :isEditMore="isEditMore"
      :idKey="idKey"
      :sendIdKey="sendIdKey"
      :requestBaseUrl="requestBaseUrl"
      @editNext="editNext"
    >
    </PageDialog>
  </div>
</template>

<style scoped lang="scss">
.page {
  :deep(.statusClass .el-radio-group) {
    width: 100%;
    .el-radio {
      margin-right: 16px;
    }
  }
}
</style>
