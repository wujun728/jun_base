<template>
  <div>
    <ArtCrud
      ref="crudRef"
      :config="crudConfig"
      v-model:search="queryParams"
      v-model:edit-form="form"
      @add-row="handleAdd"
      @edit-row="handleUpdate"
      @selection-change="handleSelectionChange">
      <template #toolbar>
        <el-button
          type="success"
          plain
          :icon="Edit"
          :disabled="single"
          @click="() => handleUpdate()"
          v-auth="'system:config:edit'">
          修改
        </el-button>
        <el-button
          type="danger"
          plain
          :icon="Delete"
          :disabled="multiple"
          @click="() => handleDelete()"
          v-auth="'system:config:remove'">
          删除
        </el-button>
        <el-button
          type="warning"
          plain
          :icon="Download"
          @click="handleExport"
          v-auth="'system:config:export'">
          导出
        </el-button>
        <el-button
          type="danger"
          plain
          :icon="Refresh"
          @click="handleRefreshCache"
          v-auth="'system:config:remove'">
          刷新缓存
        </el-button>
      </template>

      <template #col-configType="{ row }">
        <dict-tag :options="sys_yes_no" :value="row.configType" />
      </template>

      <template #col-createTime="{ row }">
        <span>{{ parseTime(row.createTime) }}</span>
      </template>
    </ArtCrud>
  </div>
</template>

<script setup lang="ts" name="Config">
import type { ArtCrudConfig } from '@/components/business/art-crud/index.vue'
import type { ConfigQueryParams, SysConfig } from '@/types/api/system/config'

import { Delete, Download, Edit, Refresh } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

import ArtCrud from '@/components/business/art-crud/index.vue'
import { useAuth } from '@/hooks/core/useAuth'
import { useDict } from '@/hooks/core/useDict'
import download from '@/utils/http/download'
import { addDateRange, parseTime } from '@/utils/sys/ruoyi'

import {
  addConfig,
  delConfig,
  getConfig,
  listConfig,
  refreshCache,
  updateConfig
} from './api'

const { hasAuth } = useAuth()
const { sys_yes_no } = useDict('sys_yes_no')

// 表格实例，用于刷新数据和打开 art-crud 新增/编辑弹窗
const crudRef = useTemplateRef<InstanceType<typeof ArtCrud>>('crudRef')

// 多选参数 ID
const ids = ref<number[]>([])
// 是否只选中一条，用于控制顶部修改按钮
const single = ref(true)
// 是否未选中数据，用于控制顶部删除按钮
const multiple = ref(true)

// 查询条件
const queryParams = ref<ConfigQueryParams>({
  configName: undefined,
  configKey: undefined,
  configType: undefined,
  createTime: []
})

// 新增/编辑表单
const form = ref<SysConfig>({
  configId: undefined,
  configName: undefined,
  configKey: undefined,
  configValue: undefined,
  configType: 'Y',
  remark: undefined
})

function resetForm() {
  form.value = {
    configId: undefined,
    configName: undefined,
    configKey: undefined,
    configValue: undefined,
    configType: 'Y',
    remark: undefined
  }
}

// 新增参数配置
function handleAdd() {
  resetForm()
}

// 修改参数配置，支持行内修改和顶部单选修改
async function handleUpdate(row?: SysConfig) {
  resetForm()
  const configId = row?.configId || ids.value[0]
  if (!configId) return
  form.value = await getConfig(configId)
  crudRef.value?.openEdit(form.value)
}

// 记录多选数据，并联动顶部按钮禁用状态
function handleSelectionChange(selection: SysConfig[]) {
  ids.value = selection.map((item) => item.configId!)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

// 删除参数配置，支持行内删除和顶部批量删除
function handleDelete(row?: SysConfig) {
  const configIds = row?.configId || ids.value
  ElMessageBox.confirm(`是否确认删除参数编号为"${configIds}"的数据项？`)
    .then(() => delConfig(configIds))
    .then(() => {
      refreshTable()
      ElMessage.success('删除成功')
    })
    .catch(() => {})
}

// 导出参数列表
function handleExport() {
  download(
    'system/config/export',
    {
      ...queryParams.value
    },
    `config_${new Date().getTime()}.xlsx`
  )
}

// 刷新缓存
function handleRefreshCache() {
  refreshCache().then(() => {
    ElMessage.success('刷新缓存成功')
  })
}

// 刷新表格数据
function refreshTable() {
  crudRef.value?.handleRefresh()
}

// art-crud 配置
const crudConfig: any = computed<ArtCrudConfig<SysConfig>>(() => ({
  api: {
    list: (params) => {
      const query = addDateRange(
        {
          ...queryParams.value,
          pageNum: params.pageNum,
          pageSize: params.pageSize
        },
        queryParams.value.createTime as string[]
      )
      delete query.createTime
      return listConfig(query)
    },
    add: addConfig,
    update: updateConfig,
    delete: async (row?: SysConfig) => delConfig(row?.configId as number)
  },
  searchConfig: {
    items: [
      {
        label: '参数名称',
        key: 'configName',
        type: 'input',
        placeholder: '请输入参数名称'
      },
      {
        label: '参数键名',
        key: 'configKey',
        type: 'input',
        placeholder: '请输入参数键名'
      },
      {
        label: '系统内置',
        key: 'configType',
        type: 'select',
        placeholder: '系统内置',
        options: sys_yes_no.value
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
  columns: [
    { type: 'selection', label: '选择', width: 55, align: 'center' },
    { label: '参数主键', prop: 'configId', align: 'center' },
    {
      label: '参数名称',
      prop: 'configName',
      width: 160,
      align: 'center',
      showOverflowTooltip: true
    },
    {
      label: '参数键名',
      prop: 'configKey',
      width: 140,
      align: 'center',
      showOverflowTooltip: true
    },
    {
      label: '参数键值',
      prop: 'configValue',
      align: 'center',
      width: 140,
      showOverflowTooltip: true
    },
    {
      label: '系统内置',
      prop: 'configType',
      align: 'center',
      useSlot: true,
      slotName: 'configType'
    },
    {
      label: '备注',
      prop: 'remark',
      align: 'center',
      width: 140,
      showOverflowTooltip: true
    },
    {
      label: '创建时间',
      prop: 'createTime',
      width: 180,
      align: 'center',
      useSlot: true,
      slotName: 'createTime'
    },
    {
      label: '操作',
      prop: 'operation',
      width: 150,
      align: 'center',
      fixed: 'right',
      useSlot: true,
      slotName: 'operationTemplate'
    }
  ],
  editConfig: {
    span: 24,
    labelWidth: 80,
    items: [
      {
        label: '参数名称',
        key: 'configName',
        type: 'input',
        props: { placeholder: '请输入参数名称' }
      },
      {
        label: '参数键名',
        key: 'configKey',
        type: 'input',
        props: { placeholder: '请输入参数键名' }
      },
      {
        label: '参数键值',
        key: 'configValue',
        type: 'textarea',
        props: { placeholder: '请输入参数键值' }
      },
      {
        label: '系统内置',
        key: 'configType',
        type: 'radiogroup',
        props: { options: sys_yes_no.value }
      },
      {
        label: '备注',
        key: 'remark',
        type: 'textarea',
        props: { placeholder: '请输入内容' }
      }
    ],
    rules: {
      configName: [
        { required: true, message: '参数名称不能为空', trigger: 'blur' }
      ],
      configKey: [
        { required: true, message: '参数键名不能为空', trigger: 'blur' }
      ],
      configValue: [
        { required: true, message: '参数键值不能为空', trigger: 'blur' }
      ]
    }
  },
  btnConfig: {
    showAddOperation: () => hasAuth('system:config:add'),
    showEditOperation: () => hasAuth('system:config:edit'),
    showDeleteOperation: () => hasAuth('system:config:remove')
  }
}))
</script>
