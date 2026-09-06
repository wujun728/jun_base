<template>
  <el-drawer v-model="visible" :title="drawerTitle" size="85%" append-to-body>
    <ArtCrud
      :key="queryParams.dictType"
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
      </template>

      <template #col-dictLabel="{ row }">
        <span v-if="isDefaultTag(row)">{{ row.dictLabel }}</span>
        <el-tag
          v-else
          :type="row.listClass === 'primary' ? '' : row.listClass"
          :class="row.cssClass">
          {{ row.dictLabel }}
        </el-tag>
      </template>

      <template #col-status="{ row }">
        <dict-tag :options="sys_normal_disable" :value="row.status" />
      </template>

      <template #col-createTime="{ row }">
        <span>{{ parseTime(row.createTime) }}</span>
      </template>
    </ArtCrud>
  </el-drawer>
</template>

<script setup lang="ts">
import type { ArtCrudConfig } from '@/components/business/art-crud/index.vue'
import type {
  DictDataQueryParams,
  SysDictData,
  SysDictType
} from '@/types/api/system/dict'

import { Delete, Download, Edit } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

import ArtCrud from '@/components/business/art-crud/index.vue'
import { useAuth } from '@/hooks/core/useAuth'
import { useDict } from '@/hooks/core/useDict'
import useDictStore from '@/store/modules/dict'
import download from '@/utils/http/download'
import { parseTime } from '@/utils/sys/ruoyi'

import { addData, delData, getData, listData, updateData } from '../api'

const { hasAuth } = useAuth()
const { sys_normal_disable } = useDict('sys_normal_disable')

const visible = ref(false)
const currentType = ref<SysDictType>({})
const crudRef = useTemplateRef<InstanceType<typeof ArtCrud>>('crudRef')

const ids = ref<number[]>([])
const single = ref(true)
const multiple = ref(true)

const queryParams = ref<DictDataQueryParams>({
  dictType: undefined,
  dictLabel: undefined,
  status: undefined
})

const form = ref<SysDictData>({
  dictCode: undefined,
  dictLabel: undefined,
  dictValue: undefined,
  cssClass: undefined,
  listClass: 'default',
  dictSort: 0,
  status: '0',
  remark: undefined,
  dictType: undefined
})

const listClassOptions = [
  { value: 'default', label: '默认' },
  { value: 'primary', label: '主要' },
  { value: 'success', label: '成功' },
  { value: 'info', label: '信息' },
  { value: 'warning', label: '警告' },
  { value: 'danger', label: '危险' }
]

const drawerTitle = computed(() =>
  currentType.value.dictName
    ? `字典数据 - ${currentType.value.dictName}`
    : '字典数据'
)

function open(row: SysDictType) {
  currentType.value = row
  queryParams.value = {
    dictType: row.dictType,
    dictLabel: undefined,
    status: undefined
  }
  ids.value = []
  single.value = true
  multiple.value = true
  visible.value = true
}

function resetForm() {
  form.value = {
    dictCode: undefined,
    dictLabel: undefined,
    dictValue: undefined,
    cssClass: undefined,
    listClass: 'default',
    dictSort: 0,
    status: '0',
    remark: undefined,
    dictType: queryParams.value.dictType
  }
}

function handleAdd() {
  resetForm()
}

async function handleUpdate(row?: SysDictData) {
  resetForm()
  const dictCode = row?.dictCode || ids.value[0]
  if (!dictCode) return
  form.value = await getData(dictCode)
  crudRef.value?.openEdit(form.value)
}

function handleSelectionChange(selection: SysDictData[]) {
  ids.value = selection.map((item) => item.dictCode!)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

function handleDelete(row?: SysDictData) {
  const dictCodes = row?.dictCode || ids.value
  ElMessageBox.confirm(`是否确认删除字典编码为"${dictCodes}"的数据项？`)
    .then(() => delData(dictCodes))
    .then(() => {
      refreshTable()
      ElMessage.success('删除成功')
      useDictStore().removeDict(queryParams.value.dictType!)
    })
    .catch(() => {})
}

function handleExport() {
  download(
    'system/dict/data/export',
    {
      ...queryParams.value
    },
    `dict_data_${new Date().getTime()}.xlsx`
  )
}

function refreshTable() {
  crudRef.value?.handleRefresh()
}

function isDefaultTag(row: SysDictData) {
  return (
    (!row.listClass || row.listClass === 'default') &&
    (!row.cssClass || row.cssClass === '')
  )
}

const crudConfig: any = computed<ArtCrudConfig<SysDictData>>(() => ({
  api: {
    list: (params) =>
      listData({
        ...queryParams.value,
        pageNum: params.pageNum,
        pageSize: params.pageSize
      }),
    add: async (data: SysDictData) => {
      await addData(data)
      useDictStore().removeDict(queryParams.value.dictType!)
    },
    update: async (data: SysDictData) => {
      await updateData(data)
      useDictStore().removeDict(queryParams.value.dictType!)
    },
    delete: async (row?: SysDictData) => {
      await delData(row?.dictCode || ids.value)
      useDictStore().removeDict(queryParams.value.dictType!)
    }
  },
  searchConfig: {
    items: [
      {
        label: '字典标签',
        key: 'dictLabel',
        type: 'input',
        placeholder: '请输入字典标签'
      },
      {
        label: '状态',
        key: 'status',
        type: 'select',
        placeholder: '数据状态',
        options: sys_normal_disable.value
      }
    ]
  },
  columns: [
    { type: 'selection', label: '选择', width: 55, align: 'center' },
    { label: '字典编码', prop: 'dictCode', align: 'center' },
    {
      label: '字典标签',
      prop: 'dictLabel',
      align: 'center',
      useSlot: true,
      slotName: 'dictLabel'
    },
    { label: '字典键值', prop: 'dictValue', align: 'center' },
    { label: '字典排序', prop: 'dictSort', align: 'center' },
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
      width: 160,
      align: 'center',
      useSlot: true,
      slotName: 'operationTemplate'
    }
  ],
  editConfig: {
    span: 24,
    labelWidth: 80,
    items: [
      {
        label: '字典类型',
        key: 'dictType',
        type: 'input',
        props: { disabled: true }
      },
      {
        label: '数据标签',
        key: 'dictLabel',
        type: 'input',
        props: { placeholder: '请输入数据标签' }
      },
      {
        label: '数据键值',
        key: 'dictValue',
        type: 'input',
        props: { placeholder: '请输入数据键值' }
      },
      {
        label: '样式属性',
        key: 'cssClass',
        type: 'input',
        props: { placeholder: '请输入样式属性' }
      },
      {
        label: '显示排序',
        key: 'dictSort',
        type: 'number',
        props: { controlsPosition: 'right', min: 0 }
      },
      {
        label: '回显样式',
        key: 'listClass',
        type: 'select',
        props: { options: listClassOptions }
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
      dictLabel: [
        { required: true, message: '数据标签不能为空', trigger: 'blur' }
      ],
      dictValue: [
        { required: true, message: '数据键值不能为空', trigger: 'blur' }
      ],
      dictSort: [
        { required: true, message: '数据顺序不能为空', trigger: 'blur' }
      ]
    }
  },
  btnConfig: {
    showAddOperation: () => hasAuth('system:dict:add'),
    showEditOperation: () => hasAuth('system:dict:edit'),
    showDeleteOperation: () => hasAuth('system:dict:remove')
  }
}))

defineExpose({ open })
</script>
