<template>
  <div>
    <ArtCrud
      ref="crudRef"
      :config="crudConfig"
      v-model:search="queryParams"
      @selection-change="handleSelectionChange">
      <template #toolbar>
        <el-button
          type="primary"
          plain
          :icon="Download"
          :disabled="multiple"
          @click="handleGenTable()"
          v-auth="'tool:gen:code'">
          生成
        </el-button>
        <el-button
          type="primary"
          plain
          :icon="Plus"
          @click="openCreateTable"
          v-roles="['admin']">
          创建
        </el-button>
        <el-button
          type="info"
          plain
          :icon="Upload"
          @click="openImportTable"
          v-auth="'tool:gen:import'">
          导入
        </el-button>
        <el-button
          type="success"
          plain
          :icon="Edit"
          :disabled="single"
          @click="handleEditTable()"
          v-auth="'tool:gen:edit'">
          修改
        </el-button>
        <el-button
          type="danger"
          plain
          :icon="Delete"
          :disabled="multiple"
          @click="handleDelete()"
          v-auth="'tool:gen:remove'">
          删除
        </el-button>
      </template>

      <template #col-createTime="{ row }">
        <span>{{ parseTime(row.createTime) }}</span>
      </template>

      <template #col-updateTime="{ row }">
        <span>{{ parseTime(row.updateTime) }}</span>
      </template>

      <template #col-operationTemplate="{ row }">
        <ArtButtonTable
          v-auth="'tool:gen:preview'"
          type="view"
          tooltip="预览"
          @click="handlePreview(row)" />
        <ArtButtonTable
          v-auth="'tool:gen:edit'"
          type="edit"
          tooltip="编辑"
          @click="handleEditTable(row)" />
        <ArtButtonTable
          v-auth="'tool:gen:remove'"
          type="delete"
          tooltip="删除"
          @click="handleDelete(row)" />
        <ArtButtonTable
          v-auth="'tool:gen:edit'"
          icon="ri:refresh-line"
          iconClass="bg-warning/12 text-warning"
          tooltip="同步"
          @click="handleSynchDb(row)" />
        <ArtButtonTable
          v-auth="'tool:gen:code'"
          icon="ri:download-2-line"
          iconClass="bg-info/12 text-info"
          tooltip="生成代码"
          @click="handleGenTable(row)" />
      </template>
    </ArtCrud>

    <el-dialog
      :title="preview.title"
      v-model="preview.open"
      width="80%"
      top="5vh"
      append-to-body
      class="scrollbar">
      <el-tabs v-model="preview.activeName">
        <el-tab-pane
          v-for="(value, key) in preview.data"
          :key="key"
          :label="getTabLabel(String(key))"
          :name="getTabLabel(String(key))">
          <el-link
            underline="never"
            icon="DocumentCopy"
            v-copyText="value"
            v-copyText:callback="copyTextSuccess"
            style="float: right">
            &nbsp;复制
          </el-link>
          <pre>{{ value }}</pre>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>

    <ImportTable ref="importRef" @ok="refreshTable" />
    <CreateTable ref="createRef" @ok="refreshTable" />
  </div>
</template>

<script setup lang="ts">
import type { ArtCrudConfig } from '@/components/business/art-crud/index.vue'
import type { GenQueryParams, GenTable } from '@/types/api/tool/gen'

import { Delete, Download, Edit, Plus, Upload } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { useRoute } from 'vue-router'

import ArtCrud from '@/components/business/art-crud/index.vue'
import { useAuth } from '@/hooks/core/useAuth'
import downloadTools from '@/utils/http/download-tools.js'
import { addDateRange, parseTime } from '@/utils/sys/ruoyi'

import { delTable, genCode, listTable, previewTable, synchDb } from './api'
import CreateTable from './components/createTable.vue'
import ImportTable from './components/importTable.vue'

defineOptions({ name: 'Gen' })

const router = useRouter()
const route = useRoute()
const { hasAuth } = useAuth()

const crudRef = useTemplateRef<InstanceType<typeof ArtCrud>>('crudRef')
const importRef = useTemplateRef<InstanceType<typeof ImportTable>>('importRef')
const createRef = useTemplateRef<InstanceType<typeof CreateTable>>('createRef')

const ids = ref<number[]>([])
const tableNames = ref<string[]>([])
const single = ref(true)
const multiple = ref(true)
const uniqueId = ref<string>('')

const queryParams = ref<GenQueryParams & { createTime?: string[] }>({
  tableName: undefined,
  tableComment: undefined,
  createTime: []
})

const preview = reactive({
  open: false,
  title: '代码预览',
  data: {} as Record<string, string>,
  activeName: 'domain.java'
})

function getTabLabel(key: string) {
  return key.substring(key.lastIndexOf('/') + 1, key.indexOf('.vm'))
}

/** 排序触发事件 */
function handleSortChange(column: any) {
  queryParams.value.orderByColumn = column.prop
  queryParams.value.isAsc = column.order
  refreshTable()
}

/** 刷新表格 */
function refreshTable() {
  if (crudRef.value) {
    crudRef.value.handleRefresh()
  }
}
// 多选框选中数据
function handleSelectionChange(selection: GenTable[]) {
  ids.value = selection.map((item) => item.tableId!)
  tableNames.value = selection.map((item) => item.tableName!)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

/** 生成代码操作 */
async function handleGenTable(row?: GenTable) {
  const tbNames = row?.tableName || tableNames.value
  if (tbNames === '') {
    ElMessage.error('请选择要生成的数据')
    return
  }
  if (row?.genType === '1') {
    await genCode(row.tableName!)
    ElMessage.success('成功生成到自定义路径：' + row.genPath)
  } else {
    const zipName = Array.isArray(tbNames) ? 'ruoyi.zip' : tbNames + '.zip'
    downloadTools.zip('/tool/gen/batchGenCode?tables=' + tbNames, zipName)
  }
}

/** 同步数据库操作 */
function handleSynchDb(row: GenTable) {
  const tableName = row.tableName
  ElMessageBox.confirm('确认要强制同步"' + tableName + '"表结构吗？')
    .then(() => synchDb(tableName!))
    .then(() => {
      ElMessage.success('同步成功')
    })
    .catch(() => {})
}

/** 打开导入表弹窗 */
function openImportTable() {
  importRef.value?.show()
}

/** 打开创建表弹窗 */
function openCreateTable() {
  createRef.value?.show()
}
/** 预览按钮 */
async function handlePreview(row: GenTable) {
  const response = await previewTable(row.tableId!)
  preview.data = response
  preview.open = true
  preview.activeName = 'domain.java'
}

function copyTextSuccess() {
  ElMessage.success('复制成功')
}

/** 修改按钮操作 */
function handleEditTable(row?: GenTable) {
  const tableId = row?.tableId || ids.value[0]
  const tableName = row?.tableName || tableNames.value[0]
  router.push('/tool/gen-edit/index/' + tableId)
}

/** 删除表操作 */
async function handleDelete(row?: GenTable) {
  const tableIds = row?.tableId || ids.value
  ElMessageBox.confirm('是否确认删除表编号为"' + tableIds + '"的数据项？')
    .then(() => delTable(tableIds))
    .then(() => {
      crudRef.value?.handleRefresh()
      ElMessage.success('删除成功')
    })
    .catch(() => {})
}

onActivated(() => {
  const time = route.query.t as string
  if (time != null && time != uniqueId.value) {
    uniqueId.value = time
    refreshTable()
  }
})

const crudConfig: any = computed<ArtCrudConfig<GenTable>>(() => ({
  api: {
    list: (params) => {
      let query = addDateRange(
        {
          ...queryParams.value,
          pageNum: params.pageNum,
          pageSize: params.pageSize
        },
        params.createTime
      )
      delete query.createTime
      return listTable(query)
    },
    delete: async (row?: GenTable) => delTable(row?.tableId as number)
  },
  searchConfig: {
    items: [
      {
        label: '表名称',
        key: 'tableName',
        type: 'input',
        placeholder: '请输入表名称'
      },
      {
        label: '表描述',
        key: 'tableComment',
        type: 'input',
        placeholder: '请输入表描述'
      },
      {
        label: '创建时间',
        key: 'createTime',
        type: 'daterange',
        props: {
          type: 'daterange',
          valueFormat: 'YYYY-MM-DD',
          rangeSeparator: '-',
          startPlaceholder: '开始日期',
          endPlaceholder: '结束日期'
        }
      }
    ]
  },
  crudConfig: {
    defaultSort: { prop: 'createTime', order: 'descending' },
    onSortChange: handleSortChange
  },
  columns: [
    { type: 'selection', label: '选择', width: 65, align: 'center' },
    { label: '序号', type: 'index', width: 60, align: 'center' },
    {
      label: '表名称',
      prop: 'tableName',
      align: 'center',
      width: 150,
      showOverflowTooltip: true
    },
    {
      label: '表描述',
      prop: 'tableComment',
      align: 'center',
      width: 150,
      showOverflowTooltip: true
    },
    {
      label: '实体',
      prop: 'className',
      align: 'center',
      minWidth: 150,
      showOverflowTooltip: true
    },
    {
      label: '创建时间',
      prop: 'createTime',
      width: 170,
      align: 'center',
      sortable: 'custom',
      useSlot: true,
      slotName: 'createTime'
    },
    {
      label: '更新时间',
      prop: 'updateTime',
      width: 170,
      align: 'center',
      sortable: 'custom',
      useSlot: true,
      slotName: 'updateTime'
    },
    {
      label: '操作',
      prop: 'operation',
      width: 300,
      fixed: 'right',
      align: 'center',
      useSlot: true,
      slotName: 'operationTemplate'
    }
  ],
  btnConfig: {
    showAddOperation: () => false,
    showEditOperation: () => false,
    showDeleteOperation: () => false
  }
}))
</script>
