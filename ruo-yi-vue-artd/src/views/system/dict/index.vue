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
          v-auth="'system:dict:edit'">
          修改
        </el-button>
        <el-button
          type="danger"
          plain
          :icon="Delete"
          :disabled="multiple"
          @click="() => handleDelete()"
          v-auth="'system:dict:remove'">
          删除
        </el-button>
        <el-button
          type="warning"
          plain
          :icon="Download"
          @click="handleExport"
          v-auth="'system:dict:export'">
          导出
        </el-button>
        <el-button
          type="danger"
          plain
          :icon="Refresh"
          @click="handleRefreshCache"
          v-auth="'system:dict:remove'">
          刷新缓存
        </el-button>
      </template>

      <template #col-dictType="{ row }">
        <a
          class="link-type"
          style="cursor: pointer"
          @click="handleViewData(row)">
          {{ row.dictType }}
        </a>
      </template>

      <template #col-status="{ row }">
        <dict-tag :options="sys_normal_disable" :value="row.status" />
      </template>

      <template #col-createTime="{ row }">
        <span>{{ parseTime(row.createTime) }}</span>
      </template>

      <template #col-operationTemplate="{ row }">
        <ArtButtonTable
          v-auth="'system:dict:edit'"
          tooltip="列表"
          icon="ri:list-check"
          iconClass="bg-warning/12 text-warning"
          @click="handleDataList(row)">
          列表
        </ArtButtonTable>
      </template>
    </ArtCrud>

    <DictDetailDrawer v-model:visible="detailVisible" :row="detailRow" />
    <DictDataDrawer ref="dataDrawerRef" />
  </div>
</template>

<script setup lang="ts" name="Dict">
import type { ArtCrudConfig } from '@/components/business/art-crud/index.vue'
import type { DictTypeQueryParams, SysDictType } from '@/types/api/system/dict'

import { Delete, Download, Edit, Refresh } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

import ArtCrud from '@/components/business/art-crud/index.vue'
import { useAuth } from '@/hooks/core/useAuth'
import { useDict } from '@/hooks/core/useDict'
import useDictStore from '@/store/modules/dict'
import download from '@/utils/http/download'
import { addDateRange, parseTime } from '@/utils/sys/ruoyi'

import {
  addType,
  delType,
  getType,
  listType,
  refreshCache,
  updateType
} from './api'
import DictDataDrawer from './components/data.vue'
import DictDetailDrawer from './components/detail.vue'

const { hasAuth } = useAuth()
const { sys_normal_disable } = useDict('sys_normal_disable')

// 表格实例，用于刷新数据和打开 art-crud 新增/编辑弹窗
const crudRef = useTemplateRef<InstanceType<typeof ArtCrud>>('crudRef')
// 字典数据管理抽屉
const dataDrawerRef =
  useTemplateRef<InstanceType<typeof DictDataDrawer>>('dataDrawerRef')

// 多选字典类型 ID
const ids = ref<number[]>([])
// 是否只选中一条，用于控制顶部修改按钮
const single = ref(true)
// 是否未选中数据，用于控制顶部删除按钮
const multiple = ref(true)

// 字典数据预览抽屉状态
const detailVisible = ref(false)
const detailRow = ref<SysDictType>({})

// 查询条件
const queryParams = ref<DictTypeQueryParams>({
  dictName: undefined,
  dictType: undefined,
  status: undefined,
  createTime: []
})

// 新增/编辑表单
const form = ref<SysDictType>({
  dictId: undefined,
  dictName: undefined,
  dictType: undefined,
  status: '0',
  remark: undefined
})

function resetForm() {
  form.value = {
    dictId: undefined,
    dictName: undefined,
    dictType: undefined,
    status: '0',
    remark: undefined
  }
}

// 新增字典类型
function handleAdd() {
  resetForm()
}

// 修改字典类型，支持行内修改和顶部单选修改
async function handleUpdate(row?: SysDictType) {
  resetForm()
  const dictId = row?.dictId || ids.value[0]
  if (!dictId) return
  form.value = await getType(dictId)
  crudRef.value?.openEdit(form.value)
}

// 记录多选数据，并联动顶部按钮禁用状态
function handleSelectionChange(selection: SysDictType[]) {
  ids.value = selection.map((item) => item.dictId!)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

// 预览字典数据
function handleViewData(row: SysDictType) {
  detailRow.value = row
  detailVisible.value = true
}

// 打开字典数据管理抽屉
function handleDataList(row: SysDictType) {
  dataDrawerRef.value?.open(row)
}

// 删除字典类型，支持行内删除和顶部批量删除
function handleDelete(row?: SysDictType) {
  const dictIds = row?.dictId || ids.value
  ElMessageBox.confirm(`是否确认删除字典编号为"${dictIds}"的数据项？`)
    .then(() => delType(dictIds))
    .then(() => {
      refreshTable()
      ElMessage.success('删除成功')
    })
    .catch(() => {})
}

// 导出字典类型列表
function handleExport() {
  download(
    'system/dict/type/export',
    {
      ...queryParams.value
    },
    `dict_${new Date().getTime()}.xlsx`
  )
}

// 刷新字典缓存
function handleRefreshCache() {
  refreshCache().then(() => {
    ElMessage.success('刷新成功')
    useDictStore().cleanDict()
  })
}

// 刷新表格数据
function refreshTable() {
  crudRef.value?.handleRefresh()
}

// art-crud 配置：搜索项、表格列、新增/编辑表单和权限按钮
const crudConfig: any = computed<ArtCrudConfig<SysDictType>>(() => ({
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
      return listType(query)
    },
    add: addType,
    update: updateType,
    delete: async (row?: SysDictType) => delType(row?.dictId as number)
  },
  searchConfig: {
    items: [
      {
        label: '字典名称',
        key: 'dictName',
        type: 'input',
        placeholder: '请输入字典名称'
      },
      {
        label: '字典类型',
        key: 'dictType',
        type: 'input',
        placeholder: '请输入字典类型'
      },
      {
        label: '状态',
        key: 'status',
        type: 'select',
        placeholder: '字典状态',
        options: sys_normal_disable.value
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
    { label: '字典编号', prop: 'dictId', align: 'center' },
    {
      label: '字典名称',
      prop: 'dictName',
      align: 'center',
      showOverflowTooltip: true
    },
    {
      label: '字典类型',
      prop: 'dictType',
      align: 'center',
      width: '140',
      showOverflowTooltip: true,
      useSlot: true,
      slotName: 'dictType'
    },
    {
      label: '状态',
      prop: 'status',
      align: 'center',
      useSlot: true,
      slotName: 'status'
    },
    {
      label: '备注',
      prop: 'remark',
      align: 'center',
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
      width: 280,
      align: 'center',
      useSlot: true,
      slotName: 'operationTemplate'
    }
  ],
  editConfig: {
    span: 24,
    labelWidth: 100,
    items: [
      {
        label: '字典名称',
        key: 'dictName',
        type: 'input',
        props: { placeholder: '请输入字典名称' }
      },
      {
        label: '字典类型',
        tooltip: '数据存储中的Key值，如：sys_user_sex',
        key: 'dictType',
        type: 'input',
        props: { placeholder: '请输入字典类型' }
      },
      {
        label: '状态',
        key: 'status',
        type: 'radiogroup',
        props: { options: sys_normal_disable.value }
      },
      {
        label: '备注',
        key: 'remark',
        type: 'textarea',
        props: { placeholder: '请输入内容' }
      }
    ],
    rules: {
      dictName: [
        { required: true, message: '字典名称不能为空', trigger: 'blur' }
      ],
      dictType: [
        { required: true, message: '字典类型不能为空', trigger: 'blur' }
      ]
    }
  },
  btnConfig: {
    showAddOperation: () => hasAuth('system:dict:add'),
    showEditOperation: () => hasAuth('system:dict:edit'),
    showDeleteOperation: () => hasAuth('system:dict:remove')
  }
}))
</script>
