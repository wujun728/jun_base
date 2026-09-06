<script setup name="Gen">
import { previewTable, genCode, synchDb } from '@/api/tool/gen'
import ImportTable from './importTable.vue'
import getSearchConfig from './config/searchConfig'
import getContentConfig from './config/contentConfig.js'
import getComputedConfig from '@/hooks/getPageConfig'
import { tool } from '@/api/config/base.js'
import to from '@/utils/to'
import { antiShake, getDialogMaxHeight } from '@/utils/utils'
import { gen } from '@/views/pageName.js'

const router = useRouter()
const proxy = inject('proxy')
const pageName = gen
const idKey = 'tableId'
const requestBaseUrl = tool
const pageSearchRef = useTemplateRef('pageSearchRef')
const pageContentRef = useTemplateRef('pageContentRef')
const importRef = useTemplateRef('importRef')
const headerButtons = ['refresh', 'delete', 'columnDisplay', 'comSearch']
const tableHideItems = ref([])
const dictMap = {}

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

const searchData = computed(() => pageContentRef.value?.finalSearchData)

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
  del: 'tool:gen:remove',
})

const deleteRow = (row) => {
  pageContentRef.value.deleteRow(row)
}

const onChangeShowColumn = (filterArr) => {
  tableHideItems.value = filterArr
}

const preview = ref({
  open: false,
  title: '代码预览',
  data: {},
  activeName: 'domain.java',
})

const previewTabs = computed(() => {
  return Object.entries(preview.value.data).map(([key, value]) => ({
    key: key.substring(key.lastIndexOf('/') + 1, key.indexOf('.vm')),
    label: key.substring(key.lastIndexOf('/') + 1, key.indexOf('.vm')),
    value,
  }))
})

const handlePreview = async (row) => {
  const [res] = await to(previewTable(row.tableId))
  if (res) {
    preview.value.data = res.data
    preview.value.open = true
    preview.value.activeName = 'domain.java'
  }
}

const handleEditTable = (row) => {
  router.push({
    path: '/tool/gen-edit/index/' + row.tableId,
    query: { pageNum: searchData.value?.pageNum },
  })
}

const handleSynchDb = async (row) => {
  const [res] = await to(synchDb(row.tableName))
  if (res) {
    proxy.$modal.msgSuccess('同步成功')
  }
}

const handleGenTable = (row) => {
  const tbNames = row.tableName
  if (!tbNames) {
    proxy.$modal.msgError('请选择要生成的数据')
    return
  }
  if (row.genType === '1') {
    genCode(row.tableName).then(() => {
      proxy.$modal.msgSuccess('成功生成到自定义路径：' + row.genPath)
    })
  } else {
    proxy.$download.zip('/tool/gen/batchGenCode?tables=' + tbNames, 'demo.zip')
  }
}

const openImportTable = () => {
  importRef.value?.show()
}

const copyTextSuccess = () => {
  proxy.$modal.msgSuccess('复制成功')
}

const previewMaxHeight = ref(520)

const getPreviewMaxHeight = () => {
  previewMaxHeight.value = getDialogMaxHeight('.preview-dialog')
}

const getPreviewMaxHeightAntiShake = antiShake(getPreviewMaxHeight, 100)

watch(
  () => preview.value.open,
  (open) => {
    if (open) {
      nextTick(() => {
        getPreviewMaxHeight()
      })
      window.addEventListener('resize', getPreviewMaxHeightAntiShake)
    } else {
      window.removeEventListener('resize', getPreviewMaxHeightAntiShake)
    }
  }
)
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
      :idKey="idKey"
      :contentConfig="contentConfigComputed"
      :autoDesc="false"
      :dictMap="dictMap"
      :tableListener="tableListener"
      :tableSelected="tableSelected"
      :permission="permission"
      :requestBaseUrl="requestBaseUrl"
      :headerButtons="headerButtons"
      :tableHideItems="tableHideItems"
      @beforeSend="beforeSend"
      @onChangeShowColumn="onChangeShowColumn"
    >
      <template #handleLeft>
        <a-button
          class="order16"
          type="dashed"
          v-hasPermi="['tool:gen:import']"
          @click="openImportTable"
        >
          <template #icon>
            <SvgIcon size="14" iconClass="upload" />
          </template>
          <span class="ml6">导入</span>
        </a-button>
      </template>

      <template #doSth="{ backData }">
        <div class="do-sth">
          <a-tooltip v-if="hasPermi('tool:gen:preview')" title="预览">
            <a-button
              type="primary"
              size="small"
              @click="handlePreview(backData)"
            >
              <template #icon>
                <SvgIcon size="12" iconClass="eye-open" />
              </template>
            </a-button>
          </a-tooltip>

          <a-tooltip v-if="hasPermi('tool:gen:edit')" title="编辑">
            <a-button
              class="ml6"
              type="primary"
              size="small"
              @click="handleEditTable(backData)"
            >
              <template #icon>
                <SvgIcon size="12" iconClass="pencil" />
              </template>
            </a-button>
          </a-tooltip>

          <a-popconfirm
            v-if="hasPermi('tool:gen:edit')"
            :title="`确认要强制同步${backData.tableName}表结构吗？`"
            @confirm="handleSynchDb(backData)"
          >
            <a-tooltip title="同步">
              <a-button class="ml6" type="primary" size="small">
                <template #icon>
                  <SvgIcon size="12" iconClass="sync-alt" />
                </template>
              </a-button>
            </a-tooltip>
          </a-popconfirm>

          <a-tooltip v-if="hasPermi('tool:gen:code')" title="生成代码">
            <a-button
              class="ml6"
              type="primary"
              size="small"
              @click="handleGenTable(backData)"
            >
              <template #icon>
                <SvgIcon size="12" iconClass="export" />
              </template>
            </a-button>
          </a-tooltip>

          <a-popconfirm
            v-if="hasPermi('tool:gen:remove')"
            title="确定删除选中记录？"
            ok-text="确认"
            cancel-text="取消"
            @confirm="deleteRow(backData)"
          >
            <a-tooltip title="删除">
              <a-button class="ml6" type="primary" danger size="small">
                <template #icon>
                  <SvgIcon size="12" iconClass="trash" />
                </template>
              </a-button>
            </a-tooltip>
          </a-popconfirm>
        </div>
      </template>
    </PageContent>

    <a-modal
      v-model:open="preview.open"
      class="preview-dialog"
      :title="preview.title"
      :width="getWidth('80%')"
      :style="{ top: '25px' }"
      destroy-on-close
      :footer="null"
    >
      <div
        class="preview-body"
        :style="{ height: `${previewMaxHeight}px` }"
      >
        <a-tabs v-model:activeKey="preview.activeName" class="preview-tabs">
          <a-tab-pane
            v-for="tab in previewTabs"
            :key="tab.key"
            :tab="tab.label"
          >
            <div class="preview-pane-inner">
              <a-button
                v-copyText="tab.value"
                v-copyText:callback="copyTextSuccess"
                type="link"
                class="copy-btn"
              >
                复制
              </a-button>
              <pre class="preview-code">{{ tab.value }}</pre>
            </div>
          </a-tab-pane>
        </a-tabs>
      </div>
    </a-modal>

    <ImportTable ref="importRef" @ok="search" />
  </div>
</template>

<style scoped lang="scss">
.do-sth {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
  gap: 0;
}

.preview-body {
  overflow: hidden;
}

.preview-tabs {
  height: 100%;
  display: flex;
  flex-direction: column;

  :deep(.ant-tabs-nav) {
    flex-shrink: 0;
    margin-bottom: 0;
  }

  :deep(.ant-tabs-content-holder) {
    flex: 1;
    min-height: 0;
    overflow: hidden;
  }

  :deep(.ant-tabs-content),
  :deep(.ant-tabs-tabpane) {
    height: 100%;
  }

  :deep(.ant-tabs-tabpane) {
    overflow: hidden;
  }
}

.preview-pane-inner {
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.copy-btn {
  flex-shrink: 0;
  align-self: flex-end;
  margin-bottom: 8px;
}

.preview-code {
  flex: 1;
  min-height: 0;
  margin: 0;
  padding: 12px;
  background: var(--ba-bg-color);
  border-radius: 4px;
  overflow: auto;
  white-space: pre-wrap;
  word-break: break-all;
}
</style>
