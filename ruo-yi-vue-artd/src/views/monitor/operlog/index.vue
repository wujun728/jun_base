<template>
  <div>
    <ArtCrud
      ref="crudRef"
      :config="crudConfig"
      v-model:search="queryParams"
      @selection-change="handleSelectionChange">
      <template #toolbar>
        <el-button
          v-auth="'monitor:operlog:remove'"
          type="danger"
          plain
          :icon="Delete"
          :disabled="multiple"
          @click="handleDelete">
          删除
        </el-button>
        <el-button
          v-auth="'monitor:operlog:remove'"
          type="danger"
          plain
          :icon="Delete"
          @click="handleClean">
          清空
        </el-button>
        <el-button
          v-auth="'monitor:operlog:export'"
          type="warning"
          plain
          :icon="Download"
          @click="handleExport">
          导出
        </el-button>
      </template>

      <template #col-businessType="{ row }">
        <dict-tag :options="sys_oper_type" :value="row.businessType" />
      </template>

      <template #col-status="{ row }">
        <dict-tag :options="sys_common_status" :value="row.status" />
      </template>

      <template #col-operTime="{ row }">
        <span>{{ parseTime(row.operTime) }}</span>
      </template>

      <template #col-costTime="{ row }">
        <span>{{ row.costTime }} 毫秒</span>
      </template>

      <template #col-operationTemplate="{ row }">
        <ArtButtonTable
          v-auth="'monitor:operlog:query'"
          type="view"
          @click="handleDetail(row)">
          详细
        </ArtButtonTable>
      </template>
    </ArtCrud>

    <OperlogDetail v-model:visible="detailVisible" :row="detailRow" />
  </div>
</template>

<script setup lang="ts" name="Operlog">
import type { ArtCrudConfig } from '@/components/business/art-crud/index.vue'
import type {
  OperlogQueryParams,
  SysOperLog
} from '@/types/api/monitor/operlog'

import { Delete, Download } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

import ArtCrud from '@/components/business/art-crud/index.vue'
import { useDict } from '@/hooks/core/useDict'
import download from '@/utils/http/download'
import { addDateRange, parseTime } from '@/utils/sys/ruoyi'

import { cleanOperlog, delOperlog, list } from './api'
import OperlogDetail from './detail.vue'

const { sys_oper_type, sys_common_status } = useDict(
  'sys_oper_type',
  'sys_common_status'
)

const crudRef = useTemplateRef<InstanceType<typeof ArtCrud>>('crudRef')

// 多选选中的日志编号
const ids = ref<number[]>([])
// 是否未选中数据，用于控制顶部删除按钮
const multiple = ref<boolean>(true)

// 详情弹窗
const detailVisible = ref<boolean>(false)
const detailRow = ref<SysOperLog>({})

// 查询条件
const queryParams = ref<OperlogQueryParams & { operTime?: string[] }>({
  operIp: undefined,
  title: undefined,
  operName: undefined,
  businessType: undefined,
  status: undefined,
  operTime: []
})

// 记录多选数据，联动顶部按钮禁用状态
function handleSelectionChange(selection: SysOperLog[]) {
  ids.value = selection.map((item) => item.operId!)
  multiple.value = !selection.length
}

// 详情
function handleDetail(row: SysOperLog) {
  detailRow.value = row
  detailVisible.value = true
}

// 删除（支持顶部批量删除）
function handleDelete() {
  if (!ids.value.length) return
  const operIds = ids.value
  ElMessageBox.confirm(`是否确认删除日志编号为"${operIds}"的数据项？`)
    .then(() => delOperlog(operIds))
    .then(() => {
      refreshTable()
      ElMessage.success('删除成功')
    })
    .catch(() => {})
}

// 清空
function handleClean() {
  ElMessageBox.confirm('是否确认清空所有操作日志数据项？')
    .then(() => cleanOperlog())
    .then(() => {
      refreshTable()
      ElMessage.success('清空成功')
    })
    .catch(() => {})
}

// 导出
function handleExport() {
  download(
    'monitor/operlog/export',
    {
      ...queryParams.value
    },
    `operlog_${new Date().getTime()}.xlsx`
  )
}

// 刷新表格
function refreshTable() {
  crudRef.value?.handleRefresh()
}

/** 排序触发事件 */
function handleSortChange(column: any) {
  queryParams.value.orderByColumn = column.prop
  queryParams.value.isAsc = column.order
  refreshTable()
}

// art-crud 配置：搜索项、表格列和权限控制
const crudConfig: any = computed<ArtCrudConfig<SysOperLog>>(() => ({
  crudConfig: {
    defaultSort: { prop: 'operTime', order: 'descending' },
    onSortChange: handleSortChange
  },
  api: {
    list: (params) => {
      const query = addDateRange(
        {
          ...queryParams.value,
          pageNum: params.pageNum,
          pageSize: params.pageSize
        },
        queryParams.value.operTime as string[]
      )
      delete query.operTime
      return list(query)
    },
    delete: (row: SysOperLog) => delOperlog(row.operId!)
  },
  searchConfig: {
    items: [
      {
        label: '操作地址',
        key: 'operIp',
        type: 'input',
        placeholder: '请输入操作地址'
      },
      {
        label: '系统模块',
        key: 'title',
        type: 'input',
        placeholder: '请输入系统模块'
      },
      {
        label: '操作人员',
        key: 'operName',
        type: 'input',
        placeholder: '请输入操作人员'
      },
      {
        label: '类型',
        key: 'businessType',
        type: 'select',
        placeholder: '操作类型',
        options: sys_oper_type.value
      },
      {
        label: '状态',
        key: 'status',
        type: 'select',
        placeholder: '操作状态',
        options: sys_common_status.value
      },
      {
        label: '操作时间',
        key: 'operTime',
        type: 'daterange',
        props: {
          type: 'daterange',
          valueFormat: 'YYYY-MM-DD HH:mm:ss',
          rangeSeparator: '-',
          startPlaceholder: '开始日期',
          endPlaceholder: '结束日期'
        }
      }
    ]
  },
  columns: [
    { type: 'selection', label: '选择', width: 55, align: 'center' },
    { label: '日志编号', prop: 'operId', align: 'center' },
    {
      label: '系统模块',
      prop: 'title',
      align: 'center',
      showOverflowTooltip: true
    },
    {
      label: '操作类型',
      prop: 'businessType',
      align: 'center',
      useSlot: true,
      slotName: 'businessType'
    },
    {
      label: '操作人员',
      prop: 'operName',
      width: 110,
      align: 'center',
      showOverflowTooltip: true,
      sortable: true
    },
    {
      label: '操作地址',
      prop: 'operIp',
      width: 130,
      align: 'center',
      showOverflowTooltip: true
    },
    {
      label: '操作状态',
      prop: 'status',
      align: 'center',
      useSlot: true,
      slotName: 'status'
    },
    {
      label: '操作日期',
      prop: 'operTime',
      width: 180,
      align: 'center',
      sortable: true,
      useSlot: true,
      slotName: 'operTime'
    },
    {
      label: '消耗时间',
      prop: 'costTime',
      width: 110,
      align: 'center',
      showOverflowTooltip: true,
      sortable: true,
      useSlot: true,
      slotName: 'costTime'
    },
    {
      label: '操作',
      prop: 'operation',
      width: 100,
      align: 'center',
      fixed: 'right',
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

<style scoped lang="scss"></style>
