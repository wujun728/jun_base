<script setup name="Data">
import {
  optionselect as getDictOptionselect,
  getType,
} from '@/api/system/dict/type'
import { nextTick } from 'vue'
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
const pageSearchRef = ref(null)
const pageContentRef = ref(null)
const descConfig = ref({})
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
    dialogRef.value.setFormData('dictType', dictInfo.value.dictType)
  })
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

const beforeSend = (queryInfo) => {}

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
    'system/dict/data/export',
    {
      ...searchData.value,
    },
    `dict_data_${new Date().getTime()}.xlsx`
  )
}
/** 查询字典类型详细 */
const dictInfo = ref({})
const getTypes = async (dictId) => {
  const [res] = await to(getType(dictId))
  if (res) {
    const data = res.data
    dictInfo.value = data
    pageSearchRef.value.setFormData('dictType', data.dictType)
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
  getTypes(route.params && route.params.dictId)
}

const handleClose = () => {
  const obj = { path: '/system/dict' }
  proxy.$tab.closeOpenPage(obj)
}
const reset = () => {
  for (let key of Object.keys(pageSearchRef.value.formData)) {
    if (key !== 'dictType') {
      pageSearchRef.value.setFormData(key, void 0)
    } else {
      pageSearchRef.value.setFormData(key, dictInfo.value.dictType)
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
      :descConfig="descConfig"
      :dictMap="dictMap"
      :tableListener="tableListener"
      :tableSelected="tableSelected"
      :permission="permission"
      :idKey="idKey"
      :autoSend="false"
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
        <el-tag
          v-else
          :type="backData.listClass == 'primary' ? '' : backData.listClass"
          :class="backData.cssClass"
        >
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
