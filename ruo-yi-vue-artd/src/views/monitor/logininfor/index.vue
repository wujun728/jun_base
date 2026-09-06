<template>
  <ArtCrud
    ref="crudRef"
    :config="crudConfig"
    v-model:search="queryParams"
    @selection-change="handleSelectionChange">
    <template #toolbar>
      <el-button
        type="danger"
        plain
        :icon="Delete"
        :disabled="multiple"
        @click="handleDelete()"
        v-auth="'monitor:logininfor:remove'">
        删除
      </el-button>
      <el-button
        type="danger"
        plain
        :icon="Delete"
        @click="handleClean"
        v-auth="'monitor:logininfor:remove'">
        清空
      </el-button>
      <el-button
        type="primary"
        plain
        :icon="Unlock"
        :disabled="single"
        @click="handleUnlock"
        v-auth="'monitor:logininfor:unlock'">
        解锁
      </el-button>
      <el-button
        type="warning"
        plain
        :icon="Download"
        @click="handleExport"
        v-auth="'monitor:logininfor:export'">
        导出
      </el-button>
    </template>

    <template #col-status="{ row }">
      <dict-tag :options="sys_common_status" :value="row.status" />
    </template>

    <template #col-loginTime="{ row }">
      <span>{{ parseTime(row.loginTime) }}</span>
    </template>
  </ArtCrud>
</template>

<script setup lang="ts" name="Logininfor">
import type { ArtCrudConfig } from '@/components/business/art-crud/index.vue'
import type { SysLogininfor } from '@/types/api/monitor/logininfor'

import { Delete, Download, Unlock } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

import ArtCrud from '@/components/business/art-crud/index.vue'
import { useDict } from '@/hooks/core/useDict'
import download from '@/utils/http/download'
import { parseTime } from '@/utils/sys/ruoyi'

import {
  cleanLogininfor,
  delLogininfor,
  listLogininfor,
  unlockLogininfor
} from './api'

const { sys_common_status } = useDict('sys_common_status')

const crudRef = useTemplateRef<InstanceType<typeof ArtCrud>>('crudRef')

/** 多选编号数组 */
const ids = ref<number[]>([])
/** 是否只选中一条 */
const single = ref<boolean>(true)
/** 是否未选中数据 */
const multiple = ref<boolean>(true)
/** 选中用户名称列表 */
const selectName = ref<string[]>([])

/** 查询参数 */
const queryParams = ref<Record<string, any>>({
  ipaddr: undefined,
  userName: undefined,
  status: undefined,
  orderByColumn: 'loginTime',
  isAsc: 'descending',
  dateRange: undefined
})

/** 多选框选中数据 */
function handleSelectionChange(selection: SysLogininfor[]) {
  ids.value = selection.map((item) => item.infoId!)
  multiple.value = !selection.length
  single.value = selection.length !== 1
  selectName.value = selection.map((item) => item.userName!)
}

/** 删除按钮操作 */
function handleDelete(row?: SysLogininfor) {
  const infoIds = row?.infoId || ids.value
  ElMessageBox.confirm(`是否确认删除访问编号为"${infoIds}"的数据项?`)
    .then(() => delLogininfor(infoIds))
    .then(() => {
      crudRef.value?.handleRefresh()
      ElMessage.success('删除成功')
    })
    .catch(() => {})
}

/** 清空按钮操作 */
function handleClean() {
  ElMessageBox.confirm('是否确认清空所有登录日志数据项?')
    .then(() => cleanLogininfor())
    .then(() => {
      crudRef.value?.handleRefresh()
      ElMessage.success('清空成功')
    })
    .catch(() => {})
}

/** 解锁按钮操作 */
function handleUnlock() {
  const username = selectName.value[0]
  ElMessageBox.confirm(`是否确认解锁用户"${username}"数据项?`)
    .then(() => unlockLogininfor(username))
    .then(() => {
      ElMessage.success(`用户${username}解锁成功`)
    })
    .catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  download(
    'monitor/logininfor/export',
    { ...queryParams.value },
    `logininfor_${new Date().getTime()}.xlsx`
  )
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

/** art-crud 配置 */
const crudConfig = computed<ArtCrudConfig<SysLogininfor>>(() => ({
  api: {
    list: (params) =>
      listLogininfor({
        ...queryParams.value,
        pageNum: params.pageNum,
        pageSize: params.pageSize
      })
  },
  crudConfig: {
    defaultSort: { prop: 'loginTime', order: 'descending' },
    onSortChange: handleSortChange
  },
  searchConfig: {
    items: [
      {
        label: '登录地址',
        key: 'ipaddr',
        type: 'input',
        placeholder: '请输入登录地址'
      },
      {
        label: '用户名称',
        key: 'userName',
        type: 'input',
        placeholder: '请输入用户名称'
      },
      {
        label: '状态',
        key: 'status',
        type: 'select',
        placeholder: '登录状态',
        options: sys_common_status.value
      },
      {
        label: '登录时间',
        key: 'dateRange',
        type: 'daterange',
        props: {
          type: 'daterange',
          valueFormat: 'YYYY-MM-DD HH:mm:ss',
          rangeSeparator: '-',
          startPlaceholder: '开始日期',
          endPlaceholder: '结束日期',
          defaultTime: [
            new Date(2000, 1, 1, 0, 0, 0),
            new Date(2000, 1, 1, 23, 59, 59)
          ]
        }
      }
    ]
  },
  columns: [
    { type: 'selection', width: 55, align: 'center' },
    { label: '访问编号', prop: 'infoId', align: 'center' },
    {
      label: '用户名称',
      prop: 'userName',
      align: 'center',
      showOverflowTooltip: true,
      width: 160,
      sortable: 'custom',
      sortOrders: ['descending', 'ascending']
    },
    {
      label: '登录地址',
      prop: 'ipaddr',
      align: 'center',
      showOverflowTooltip: true
    },
    {
      label: '登录地点',
      prop: 'loginLocation',
      align: 'center',
      showOverflowTooltip: true
    },
    {
      label: '操作系统',
      prop: 'os',
      align: 'center',
      showOverflowTooltip: true
    },
    {
      label: '浏览器',
      prop: 'browser',
      align: 'center',
      showOverflowTooltip: true
    },
    {
      label: '登录状态',
      prop: 'status',
      align: 'center',
      useSlot: true,
      slotName: 'status'
    },
    { label: '描述', prop: 'msg', align: 'center', showOverflowTooltip: true },
    {
      label: '访问时间',
      prop: 'loginTime',
      width: 180,
      align: 'center',
      sortable: 'custom',
      sortOrders: ['descending', 'ascending'],
      useSlot: true,
      slotName: 'loginTime'
    }
  ],
  btnConfig: {
    showAddOperation: () => false,
    showEditOperation: () => false,
    showDeleteOperation: () => false
  }
}))
</script>
