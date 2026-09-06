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
          v-auth="'system:post:edit'">
          修改
        </el-button>
        <el-button
          type="danger"
          plain
          :icon="Delete"
          :disabled="multiple"
          @click="() => handleDelete()"
          v-auth="'system:post:remove'">
          删除
        </el-button>
        <el-button
          type="warning"
          plain
          :icon="Download"
          @click="handleExport"
          v-auth="'system:post:export'">
          导出
        </el-button>
      </template>

      <template #col-status="{ row }">
        <dict-tag :options="sys_normal_disable" :value="row.status" />
      </template>

      <template #col-createTime="{ row }">
        <span>{{ parseTime(row.createTime) }}</span>
      </template>
    </ArtCrud>
  </div>
</template>

<script setup lang="ts" name="Post">
import type { ArtCrudConfig } from '@/components/business/art-crud/index.vue'
import type { PostQueryParams, SysPost } from '@/types/api/system/post'

import { Delete, Download, Edit } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

import ArtCrud from '@/components/business/art-crud/index.vue'
import { useAuth } from '@/hooks/core/useAuth'
import { useDict } from '@/hooks/core/useDict'
import download from '@/utils/http/download'
import { parseTime } from '@/utils/sys/ruoyi'

import { addPost, delPost, getPost, listPost, updatePost } from './api'

const { hasAuth } = useAuth()
const { sys_normal_disable } = useDict('sys_normal_disable')

// 表格实例，用于刷新数据和打开新增/编辑弹窗
const crudRef = useTemplateRef<InstanceType<typeof ArtCrud>>('crudRef')

// 多选岗位 ID
const ids = ref<number[]>([])
// 是否只选中一条，用于控制顶部修改按钮
const single = ref<boolean>(true)
// 是否未选中数据，用于控制顶部删除按钮
const multiple = ref<boolean>(true)

// 查询条件
const queryParams = ref<PostQueryParams>({
  postCode: undefined,
  postName: undefined,
  status: undefined
})

// 新增/编辑表单
const form = ref<SysPost>({
  postId: undefined,
  postCode: undefined,
  postName: undefined,
  postSort: 0,
  status: '0',
  remark: undefined
})

// 重置新增/编辑表单默认值
function resetForm() {
  form.value = {
    postId: undefined,
    postCode: undefined,
    postName: undefined,
    postSort: 0,
    status: '0',
    remark: undefined
  }
}

// 新增岗位
function handleAdd() {
  resetForm()
}

// 修改岗位，支持行内修改和顶部单选修改
async function handleUpdate(row?: SysPost) {
  resetForm()
  const postId = row?.postId || ids.value[0]
  if (!postId) return
  form.value = await getPost(postId)
  crudRef.value?.openEdit(form.value)
}

// 记录多选数据，并联动顶部按钮禁用状态
function handleSelectionChange(selection: SysPost[]) {
  ids.value = selection.map((item) => item.postId!)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

// 删除岗位，支持行内删除和顶部批量删除
function handleDelete(row?: SysPost) {
  const postIds = row?.postId || ids.value
  ElMessageBox.confirm(`是否确认删除岗位编号为"${postIds}"的数据项？`)
    .then(() => delPost(postIds))
    .then(() => {
      refreshTable()
      ElMessage.success('删除成功')
    })
    .catch(() => {})
}

// 导出岗位列表
function handleExport() {
  download(
    'system/post/export',
    {
      ...queryParams.value
    },
    `post_${new Date().getTime()}.xlsx`
  )
}

// 刷新表格数据
function refreshTable() {
  crudRef.value?.handleRefresh()
}

// art-crud 配置：搜索项、表格列、新增/编辑表单和权限按钮
const crudConfig: any = computed<ArtCrudConfig<SysPost>>(() => ({
  api: {
    list: (params) =>
      listPost({
        ...queryParams.value,
        pageNum: params.pageNum,
        pageSize: params.pageSize
      }),
    add: addPost,
    update: updatePost,
    delete: (row: SysPost) => delPost(row.postId!)
  },
  searchConfig: {
    items: [
      {
        label: '岗位编码',
        key: 'postCode',
        type: 'input',
        placeholder: '请输入岗位编码'
      },
      {
        label: '岗位名称',
        key: 'postName',
        type: 'input',
        placeholder: '请输入岗位名称'
      },
      {
        label: '状态',
        key: 'status',
        type: 'select',
        placeholder: '岗位状态',
        options: sys_normal_disable.value
      }
    ]
  },
  columns: [
    { type: 'selection', label: '选择', width: 55, align: 'center' },
    { label: '岗位编号', prop: 'postId', align: 'center' },
    { label: '岗位编码', prop: 'postCode', align: 'center' },
    { label: '岗位名称', prop: 'postName', align: 'center' },
    { label: '岗位排序', prop: 'postSort', align: 'center' },
    {
      label: '状态',
      prop: 'status',
      align: 'center',
      useSlot: true,
      slotName: 'status'
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
      width: 180,
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
        label: '岗位名称',
        key: 'postName',
        type: 'input',
        props: { placeholder: '请输入岗位名称' }
      },
      {
        label: '岗位编码',
        key: 'postCode',
        type: 'input',
        props: { placeholder: '请输入编码名称' }
      },
      {
        label: '岗位顺序',
        key: 'postSort',
        type: 'number',
        props: { controlsPosition: 'right', min: 0 }
      },
      {
        label: '岗位状态',
        key: 'status',
        type: 'radiogroup',
        options: sys_normal_disable.value
      },
      {
        label: '备注',
        key: 'remark',
        type: 'textarea',
        props: { placeholder: '请输入内容' }
      }
    ],
    rules: {
      postName: [
        { required: true, message: '岗位名称不能为空', trigger: 'blur' }
      ],
      postCode: [
        { required: true, message: '岗位编码不能为空', trigger: 'blur' }
      ],
      postSort: [
        { required: true, message: '岗位顺序不能为空', trigger: 'blur' }
      ]
    }
  },
  btnConfig: {
    showAddOperation: () => hasAuth('system:post:add'),
    showEditOperation: () => hasAuth('system:post:edit'),
    showDeleteOperation: () => hasAuth('system:post:remove')
  }
}))
</script>
