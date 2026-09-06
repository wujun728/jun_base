<script setup name="Data">
import {
  optionselect as getDictOptionselect,
  getType,
} from '@/api/system/dict/type'
import getSearchConfig from './config/dataSearchConfig'
import getContentConfig from './config/dataContentConfig.js'
import getDialogConfig from './config/dataDialogConfig.js'
import useDialog from '@/hooks/useDialog'
import getComputedConfig from '@/hooks/getPageConfig'
import to from '@/utils/to'
import { systemBaseUrl } from '@/api/config/base.js'
import { dictData } from '@/views/pageName.js'

const proxy = inject('proxy')
const { sys_normal_disable } = proxy.useDict('sys_normal_disable')
const dictTypeList = ref([])
const route = useRoute()
const pageName = dictData
const idKey = 'dictCode'
const sendIdKey = 'dictCode'
const requestBaseUrl = systemBaseUrl
const pageSearchRef = useTemplateRef('pageSearchRef')
const pageContentRef = useTemplateRef('pageContentRef')
const dialogHideItems = ref([])
const tableHideItems = ref([])

const dictMap = {
  status: sys_normal_disable,
  dictType: dictTypeList,
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
  nextTick(() => {
    dialogRef.value?.setFormData('dictType', dictInfo.value.dictType)
  })
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

const beforeSend = () => {}

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
    'system/dict/data/export',
    {
      ...searchData.value,
    },
    `dict_data_${new Date().getTime()}.xlsx`
  )
}

const dictInfo = ref({})
const getTypes = async (dictId) => {
  const [res] = await to(getType(dictId))
  if (res) {
    dictInfo.value = res.data
    pageSearchRef.value.setFormData('dictType', res.data.dictType)
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

const handleClose = () => {
  proxy.$tab.closeOpenPage({ path: '/system/dict' })
}

const reset = () => {
  const formData = pageSearchRef.value.getFormData()
  for (const key of Object.keys(formData)) {
    if (key !== 'dictType') {
      pageSearchRef.value.setFormData(key, undefined)
    } else {
      pageSearchRef.value.setFormData(key, dictInfo.value.dictType)
    }
  }
  search()
}

const listClassColorMap = {
  primary: 'blue',
  success: 'success',
  info: 'default',
  warning: 'warning',
  danger: 'error',
}

const getListClassColor = (listClass) => listClassColorMap[listClass]

const init = () => {
  getDictTypeList()
  getTypes(route.params?.dictId)
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
    />
    <PageContent
      ref="pageContentRef"
      :pageName="pageName"
      :contentConfig="contentConfigComputed"
      :dictMap="dictMap"
      :tableListener="tableListener"
      :tableSelected="tableSelected"
      :permission="permission"
      :idKey="idKey"
      :autoSend="false"
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
        <a-button class="order17" type="dashed" @click="handleClose">
          <template #icon>
            <SvgIcon size="14" iconClass="close" />
          </template>
          <span class="ml6">关闭</span>
        </a-button>
      </template>
      <template #statusSlot="{ backData }">
        <DictTag :options="sys_normal_disable" :value="backData.status" />
      </template>
      <template #dictLabelSlot="{ backData }">
        <span
          v-if="
            (backData.listClass === 'default' || !backData.listClass) &&
            (!backData.cssClass || backData.cssClass === '')
          "
        >
          {{ backData.dictLabel }}
        </span>
        <a-tag
          v-else
          :color="getListClassColor(backData.listClass)"
          :class="backData.cssClass"
        >
          {{ backData.dictLabel }}
        </a-tag>
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
