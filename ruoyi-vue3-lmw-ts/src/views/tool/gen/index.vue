<script setup name="Gen" lang="ts">
import { previewTable, genCode, synchDb } from '@/api/tool/gen'
import ImportTable from './importTable.vue'
import getSearchConfig from './config/searchConfig'
import getContentConfig from './config/contentConfig'
import getComputedConfig from '@/hooks/getPageConfig'
import { tool } from '@/api/config/base'
import to from '@/utils/to'
import { gen } from '@/views/pageName'
import { proxy } from '@/utils/provide'

const router = useRouter()
const pageName = gen
const idKey = 'tableId'
const requestBaseUrl = tool
const pageSearchRef = useTemplateRef('pageSearchRef')
const pageContentRef = useTemplateRef('pageContentRef')
const headerButtons: ButtonsType[] = [
  'refresh',
  'delete',
  'columnDisplay',
  'comSearch',
]
const tableHideItems = ref<string[]>([])
const dictMap = {}
const searchConfig = getSearchConfig()
const searchConfigComputed = computed(() => {
  return getComputedConfig(searchConfig, dictMap)
})
const tableSelected = ref<GenInfo[]>([])
const tableListener = {
  selectionChange: (selected: GenInfo[]) => {
    tableSelected.value = selected
  },
}
const contentConfig = getContentConfig()
const contentConfigComputed = computed(() => {
  contentConfig.hideItems = tableHideItems
  return contentConfig
})

const searchData = computed(() => {
  return pageContentRef.value?.finalSearchData
})

const search = () => {
  pageSearchRef.value?.search()
}

const beforeSend = (queryInfo: anyObj) => {
  if (queryInfo.dateRange && Array.isArray(queryInfo.dateRange)) {
    const dateRange = queryInfo.dateRange
    queryInfo['params[beginTime]'] = dateRange[0]
    queryInfo['params[endTime]'] = dateRange[1]
    delete queryInfo.dateRange
  }
}

const permission = ref({
  del: 'tool:gen:remove',
})

const deleteRow = (row: GenInfo) => {
  pageContentRef.value?.deleteRow(row)
}

const onChangeShowColumn = (filterArr: string[]) => {
  tableHideItems.value = filterArr
}

const preview = ref({
  open: false,
  title: '代码预览',
  data: {} as GenPreviewInfo,
  activeName: 'domain.java',
})
const handlePreview = async (row: GenInfo) => {
  const [res] = await to(previewTable(row.tableId))
  if (res) {
    preview.value.data = res.data
    preview.value.open = true
    preview.value.activeName = 'domain.java'
  }
}
const handleEditTable = (row: GenInfo) => {
  const tableId = row.tableId
  router.push({
    path: '/tool/gen-edit/index/' + tableId,
    query: { pageNum: searchData.value?.pageNum },
  })
}
const handleSynchDb = async (row: GenInfo) => {
  const tableName = row.tableName
  const [res] = await to(synchDb(tableName))
  if (res) {
    proxy.$modal.notifySuccess('同步成功')
  }
}
const handleGenTable = (row: GenInfo) => {
  const tbNames = row.tableName
  if (tbNames == '') {
    proxy.$modal.msgError('请选择要生成的数据')
    return
  }
  if (row.genType === '1') {
    genCode(row.tableName).then(() => {
      proxy.$modal.notifySuccess('成功生成到自定义路径：' + row.genPath)
    })
  } else {
    proxy.$download.zip('/tool/gen/batchGenCode?tables=' + tbNames, 'demo.zip')
  }
}
const importRef = useTemplateRef('importRef')
const openImportTable = () => {
  importRef.value?.show()
}
const copyTextSuccess = () => {
  proxy.$modal.msgSuccess('复制成功')
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
      :idKey="idKey"
      :contentConfig="contentConfigComputed"
      :autoDesc="false"
      :dictMap="dictMap"
      :tableListener="tableListener"
      :tableSelected="tableSelected"
      :permission="permission"
      :requestBaseUrl="requestBaseUrl"
      :headerButtons="headerButtons"
      @beforeSend="beforeSend"
      @onChangeShowColumn="onChangeShowColumn"
    >
      <template #handleLeft>
        <el-button
          type="warning"
          class="ml12 order16"
          v-hasPermi="['tool:gen:import']"
          @click="openImportTable"
        >
          <SvgIcon size="14" iconClass="upload" />
          <span class="ml6">导入</span>
        </el-button>
      </template>
      <template #doSth="{ backData }">
        <div class="doSth">
          <el-tooltip
            :hide-after="0"
            effect="dark"
            content="预览"
            placement="top"
            v-if="hasPermi('tool:gen:preview')"
          >
            <el-button
              type="primary"
              size="small"
              @click="handlePreview(backData)"
            >
              <SvgIcon size="12" iconClass="eye" />
            </el-button>
          </el-tooltip>

          <el-tooltip
            :hide-after="0"
            effect="dark"
            content="编辑"
            placement="top"
            v-if="hasPermi('tool:gen:edit')"
          >
            <el-button
              class="ml12"
              type="primary"
              size="small"
              @click="handleEditTable(backData)"
            >
              <SvgIcon size="12" iconClass="pencil" />
            </el-button>
          </el-tooltip>

          <el-popconfirm
            v-if="hasPermi('tool:gen:edit')"
            :title="`确认要强制同步${backData.tableName}表结构吗？`"
            @confirm="handleSynchDb(backData)"
          >
            <template #reference>
              <div>
                <el-tooltip
                  :hide-after="0"
                  effect="dark"
                  content="同步"
                  placement="top"
                >
                  <el-button class="ml12 order16" type="primary" size="small">
                    <SvgIcon size="12" iconClass="refresh" />
                  </el-button>
                </el-tooltip>
              </div>
            </template>
          </el-popconfirm>

          <el-tooltip
            v-if="hasPermi('tool:gen:code')"
            :hide-after="0"
            effect="dark"
            content="生成代码"
            placement="top"
          >
            <el-button
              class="order17 ml12"
              type="primary"
              size="small"
              @click="handleGenTable(backData)"
            >
              <SvgIcon size="12" iconClass="download" />
            </el-button>
          </el-tooltip>

          <el-popconfirm
            v-if="hasPermi('tool:gen:remove')"
            title="确定删除选中记录？"
            confirm-button-text="确认"
            cancel-button-text="取消"
            confirmButtonType="danger"
            :hide-after="0"
            @confirm="deleteRow(backData)"
          >
            <template #reference>
              <div>
                <el-tooltip
                  :hide-after="0"
                  effect="dark"
                  content="删除"
                  placement="top"
                >
                  <el-button class="ml12" type="danger" size="small">
                    <SvgIcon :size="12" iconClass="trash"></SvgIcon>
                  </el-button>
                </el-tooltip>
              </div>
            </template>
          </el-popconfirm>
        </div>
      </template>
    </PageContent>
    <div class="previewDialog">
      <el-dialog
        :title="preview.title"
        v-model="preview.open"
        width="80%"
        top="25px"
      >
        <el-scrollbar class="ba-table-form-scrollbar" :max-height="590">
          <el-tabs v-model="preview.activeName">
            <el-tab-pane
              v-for="(value, key) in preview.data"
              :label="
                key.substring(key.lastIndexOf('/') + 1, key.indexOf('.vm'))
              "
              :name="
                key.substring(key.lastIndexOf('/') + 1, key.indexOf('.vm'))
              "
              :key="value"
            >
              <el-link
                class="mr10"
                :underline="false"
                icon="DocumentCopy"
                v-copyText="value"
                v-copyText:callback="copyTextSuccess"
                style="float: right"
                >&nbsp;复制
              </el-link>
              <pre>{{ value }}</pre>
            </el-tab-pane>
          </el-tabs>
        </el-scrollbar>
      </el-dialog>
    </div>

    <ImportTable ref="importRef" @ok="search" />
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
.doSth {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
}
.previewDialog {
  :deep(.el-dialog__body) {
    padding-bottom: 40px;
  }
}
</style>
