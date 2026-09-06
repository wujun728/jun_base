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
          v-auth="'system:notice:edit'">
          修改
        </el-button>
        <el-button
          type="danger"
          plain
          :icon="Delete"
          :disabled="multiple"
          @click="() => handleDelete()"
          v-auth="'system:notice:remove'">
          删除
        </el-button>
      </template>

      <template #col-noticeTitle="{ row }">
        <div
          class="text-primary! hover:text-primary!"
          style="cursor: pointer"
          @click="handleViewData(row)">
          {{ row.noticeTitle }}
        </div>
      </template>

      <template #col-noticeType="{ row }">
        <dict-tag :options="sys_notice_type" :value="row.noticeType" />
      </template>

      <template #col-status="{ row }">
        <dict-tag :options="sys_notice_status" :value="row.status" />
      </template>

      <template #col-createTime="{ row }">
        <span>{{ parseTime(row.createTime, '{y}-{m}-{d}') }}</span>
      </template>

      <template #col-operationTemplate="{ row }">
        <ArtButtonTable
          v-auth="'system:notice:list'"
          tooltip="阅读用户"
          icon="ri:team-line"
          iconClass="bg-primary/12 text-primary"
          @click="handleReadUsers(row)">
          阅读用户
        </ArtButtonTable>
      </template>

      <template #form-noticeContent="{ modelValue }">
        <ArtWangEditor v-model="modelValue.noticeContent" :height="'300px'" />
      </template>
    </ArtCrud>

    <NoticeDetailView ref="noticeViewRef" />
    <ReadUsers ref="readUsersRef" />
  </div>
</template>

<script setup lang="ts" name="Notice">
import type { ArtCrudConfig } from '@/components/business/art-crud/index.vue'
import type { NoticeQueryParams, SysNotice } from '@/types/api/system/notice'

import { Delete, Edit } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

import ArtCrud from '@/components/business/art-crud/index.vue'
import { useAuth } from '@/hooks/core/useAuth'
import { useDict } from '@/hooks/core/useDict'
import { parseTime } from '@/utils/sys/ruoyi'

import {
  addNotice,
  delNotice,
  getNotice,
  listNotice,
  updateNotice
} from './api'
import NoticeDetailView from './components/notice-detail-view.vue'
import ReadUsers from './components/read-users.vue'

const { hasAuth } = useAuth()
const { sys_notice_status, sys_notice_type } = useDict(
  'sys_notice_status',
  'sys_notice_type'
)

// 表格实例
const crudRef = useTemplateRef<InstanceType<typeof ArtCrud>>('crudRef')
// 子组件引用
const noticeViewRef =
  useTemplateRef<InstanceType<typeof NoticeDetailView>>('noticeViewRef')
const readUsersRef =
  useTemplateRef<InstanceType<typeof ReadUsers>>('readUsersRef')

// 多选公告 ID
const ids = ref<number[]>([])
// 是否只选中一条，用于控制顶部修改按钮
const single = ref(true)
// 是否未选中数据，用于控制顶部删除按钮
const multiple = ref(true)

// 查询条件
const queryParams = ref<NoticeQueryParams>({
  noticeTitle: undefined,
  createBy: undefined,
  noticeType: undefined,
  status: undefined
})

// 新增/编辑表单
const form = ref<SysNotice>({
  noticeId: undefined,
  noticeTitle: undefined,
  noticeType: undefined,
  noticeContent: '',
  status: '0'
})

function resetForm() {
  form.value = {
    noticeId: undefined,
    noticeTitle: undefined,
    noticeType: undefined,
    noticeContent: '',
    status: '0'
  }
}

// 新增公告
function handleAdd() {
  resetForm()
}

// 修改公告，支持行内修改和顶部单选修改
async function handleUpdate(row?: SysNotice) {
  resetForm()
  const noticeId = row?.noticeId || ids.value[0]
  if (!noticeId) return
  form.value = await getNotice(noticeId)
  crudRef.value?.openEdit(form.value)
}

// 记录多选数据，并联动顶部按钮禁用状态
function handleSelectionChange(selection: SysNotice[]) {
  ids.value = selection.map((item) => item.noticeId!)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

// 删除公告，支持行内删除和顶部批量删除
function handleDelete(row?: SysNotice) {
  const noticeIds = row?.noticeId || ids.value
  ElMessageBox.confirm(`是否确认删除公告编号为"${noticeIds}"的数据项？`)
    .then(() => delNotice(noticeIds))
    .then(() => {
      refreshTable()
      ElMessage.success('删除成功')
    })
    .catch(() => {})
}

// 查看公告详情
function handleViewData(row: SysNotice) {
  noticeViewRef.value?.open(row)
}

// 查看已读用户
function handleReadUsers(row: SysNotice) {
  readUsersRef.value?.open(row)
}

// 刷新表格数据
function refreshTable() {
  crudRef.value?.handleRefresh()
}

// art-crud 配置：搜索项、表格列、新增/编辑表单和权限按钮
const crudConfig: any = computed<ArtCrudConfig<SysNotice>>(() => ({
  api: {
    list: (params) =>
      listNotice({
        ...queryParams.value,
        pageNum: params.pageNum,
        pageSize: params.pageSize
      }),
    add: addNotice,
    update: updateNotice,
    delete: async (row: SysNotice) => delNotice(row.noticeId!)
  },
  searchConfig: {
    items: [
      {
        label: '公告标题',
        key: 'noticeTitle',
        type: 'input',
        placeholder: '请输入公告标题'
      },
      {
        label: '操作人员',
        key: 'createBy',
        type: 'input',
        placeholder: '请输入操作人员'
      },
      {
        label: '类型',
        key: 'noticeType',
        type: 'select',
        placeholder: '公告类型',
        options: sys_notice_type.value
      }
    ]
  },
  columns: [
    { type: 'selection', label: '选择', width: 55, align: 'center' },
    { label: '序号', prop: 'noticeId', width: 100, align: 'center' },
    {
      label: '公告标题',
      prop: 'noticeTitle',
      align: 'center',
      minWidth: 200,
      showOverflowTooltip: true,
      useSlot: true,
      slotName: 'noticeTitle'
    },
    {
      label: '公告类型',
      prop: 'noticeType',
      width: 100,
      align: 'center',
      useSlot: true,
      slotName: 'noticeType'
    },
    {
      label: '状态',
      prop: 'status',
      width: 100,
      align: 'center',
      useSlot: true,
      slotName: 'status'
    },
    { label: '创建者', prop: 'createBy', width: 100, align: 'center' },
    {
      label: '创建时间',
      prop: 'createTime',
      width: 170,
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
    labelWidth: 80,
    items: [
      {
        label: '公告标题',
        key: 'noticeTitle',
        type: 'input',
        props: { placeholder: '请输入公告标题' }
      },
      {
        label: '公告类型',
        key: 'noticeType',
        type: 'select',
        props: {
          placeholder: '请选择',
          options: sys_notice_type.value
        }
      },
      {
        label: '状态',
        key: 'status',
        type: 'radiogroup',
        props: { options: sys_notice_status.value }
      },
      {
        label: '内容',
        key: 'noticeContent',
        type: 'input',
        useSlot: true,
        slotName: 'noticeContent'
      }
    ],
    rules: {
      noticeTitle: [
        { required: true, message: '公告标题不能为空', trigger: 'blur' }
      ],
      noticeType: [
        { required: true, message: '公告类型不能为空', trigger: 'change' }
      ]
    }
  },
  btnConfig: {
    showAddOperation: () => hasAuth('system:notice:add'),
    showEditOperation: () => hasAuth('system:notice:edit'),
    showDeleteOperation: () => hasAuth('system:notice:remove')
  }
}))
</script>
