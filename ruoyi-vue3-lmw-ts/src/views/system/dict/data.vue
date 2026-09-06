<script setup name="Data" lang="ts">
import {
  optionselect as getDictOptionselect,
  getType,
} from '@/api/system/dict/type'
import getSearchConfig from './config/dataSearchConfig'
import getContentConfig from './config/dataContentConfig'
import getDialogConfig from './config/dataDialogConfig'
import useDialog from '@/hooks/useDialog'
import getComputedConfig from '@/hooks/getPageConfig'
import to from '@/utils/to'
import { systemBaseUrl } from '@/api/config/base.ts'
import { dictData } from '@/views/pageName.ts'
import { proxy } from '@/utils/provide'

const { sys_normal_disable } = proxy.useDict('sys_normal_disable')
const dictTypeList = ref([])
const route = useRoute()
const dictId = route.params.dictId as string
const pageName = dictData
const idKey = 'dictCode'
const sendIdKey = 'dictCode'
const requestBaseUrl = systemBaseUrl
const pageSearchRef = useTemplateRef('pageSearchRef')
const pageContentRef = useTemplateRef('pageContentRef')
const dialogHideItems = ref<string[]>([])
const tableHideItems = ref<string[]>([])
const dictMap = {
  status: sys_normal_disable,
  dictType: dictTypeList,
}
const searchConfig = getSearchConfig()
const searchConfigComputed = computed(() => {
  return getComputedConfig(searchConfig, dictMap)
})
const tableSelected = ref<DictEntry[]>([])
const tableListener = {
  selectionChange: (selected: DictEntry[]) => {
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
  nextTick(() => {
    dialogRef.value.setFormData('dictType', dictInfo.value.dictType)
  })
}
const editCallBack = (_item: DictEntry, type: any) => {
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

const editNext = (data: DictEntry) => {
  pageContentRef.value?.editClick(data, true)
}

const { dialogRef, infoInit, addClick, editBtnClick } = useDialog(
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

const permission = ref({
  add: 'system:dict:add',
  edit: 'system:dict:edit',
  del: 'system:dict:remove',
})

const onChangeShowColumn = (filterArr: string[]) => {
  tableHideItems.value = filterArr
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy.download(
    'system/dict/data/export',
    {
      ...searchData.value,
    },
    `dict_data_${new Date().getTime()}.xlsx`
  )
}
/** 查询字典类型详细 */
const dictInfo = ref<anyObj>({})
const getTypes = async (dictId: string) => {
  const [res] = await to(getType(dictId))
  if (res) {
    const data = res.data
    dictInfo.value = data
    pageSearchRef.value?.setFormData('dictType', data.dictType)
    nextTick(() => {
      search()
    })
  }
}
const getDictTypeList = async () => {
  const [res] = await to(getDictOptionselect())
  if (res) {
    dictTypeList.value = res.data
  }
}
const init = () => {
  getDictTypeList()
  const params = route.params
  if (params.dictId) {
    getTypes(params.dictId as string)
  }
}

const handleClose = () => {
  const obj = { path: '/system/dict' }
  proxy.$tab.closeOpenPage(obj)
}
const reset = () => {
  for (let key of Object.keys(pageSearchRef.value?.formData!)) {
    if (key !== 'dictType') {
      pageSearchRef.value?.setFormData(key, void 0)
    } else {
      pageSearchRef.value?.setFormData(key, dictInfo.value.dictType)
    }
  }
  search()
}
init()
</script>
<template>
  <div class="default-main page">
    <PageSearch
      ref="pageSearchRef"
      :pageName="pageName"
      :searchConfig="searchConfigComputed"
      :reset="reset"
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
      :autoSend="false"
      :requestBaseUrl="requestBaseUrl"
      :cacheKey="dictId"
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
        <el-button class="order18 ml12" type="warning" @click="handleClose">
          <SvgIcon size="14" iconClass="times" />
          <span class="ml6">关闭</span>
        </el-button>
      </template>
      <template #statusSlot="{ backData }">
        <el-tag :type="backData.status == 0 ? 'success' : 'danger'">
          {{ backData.status == 0 ? '启用' : '禁用' }}
        </el-tag>
      </template>
      <template #dictLabelSlot="{ backData }">
        <span
          v-if="
            (backData.listClass == '' || backData.listClass == 'default') &&
            (backData.cssClass == '' || backData.cssClass == null)
          "
        >
          {{ backData.dictLabel }}
        </span>
        <el-tag v-else :type="backData.listClass" :class="backData.cssClass">
          {{ backData.dictLabel }}
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
