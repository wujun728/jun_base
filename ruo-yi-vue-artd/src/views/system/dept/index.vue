<template>
  <div>
    <ArtCrud
      ref="crudRef"
      :config="crudConfig"
      v-model:search="queryParams"
      v-model:edit-form="form"
      @add-row="handleAdd"
      @edit-row="handleUpdate">
      <template #toolbar>
        <el-button
          type="warning"
          plain
          :icon="Check"
          @click="handleSaveSort"
          v-auth="'system:dept:edit'">
          保存排序
        </el-button>
        <el-button type="info" plain :icon="Sort" @click="toggleExpandAll">
          展开/折叠
        </el-button>
      </template>

      <template #col-orderNum="{ row }">
        <el-input-number
          v-model="row.orderNum"
          controls-position="right"
          :min="0"
          style="width: 88px" />
      </template>

      <template #col-status="{ row }">
        <dict-tag :options="sys_normal_disable" :value="row.status" />
      </template>

      <template #col-createTime="{ row }">
        <span>{{ parseTime(row.createTime) }}</span>
      </template>

      <template #col-operationTemplate="{ row }">
        <ArtButtonTable
          v-if="hasAuth('system:dept:add')"
          type="add"
          tooltip="新增"
          @click="handleAdd(row)">
          新增
        </ArtButtonTable>
      </template>
    </ArtCrud>
  </div>
</template>

<script setup lang="ts" name="Dept">
import type { ArtCrudConfig } from '@/components/business/art-crud/index.vue'
import type { DeptQueryParams, SysDept } from '@/types/api'
import type { TreeSelect } from '@/types/api/system/common'

import { Check, Sort } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

import ArtCrud from '@/components/business/art-crud/index.vue'
import { useAuth } from '@/hooks'
import { useDict } from '@/hooks/core/useDict'
import { handleTree, parseTime } from '@/utils/sys/ruoyi'

import {
  addDept,
  delDept,
  getDept,
  listDept,
  listDeptExcludeChild,
  updateDept,
  updateDeptSort
} from './api'

const { hasAuth } = useAuth()
const { sys_normal_disable } = useDict('sys_normal_disable')

// 表格实例，用于刷新数据和调用新增/编辑弹窗
const crudRef = useTemplateRef<InstanceType<typeof ArtCrud>>('crudRef')

// 展开的部门行 key，接口加载完成后默认填充全部节点
const expandRowKeys = ref<string[]>([])
// 记录原始排序值，用于保存排序时只提交变更项
const originalOrders = ref<Record<number, number>>({})
// 部门树表格数据
const deptList = ref<SysDept[]>([])
// 新增/编辑弹窗中的上级部门树
const deptOptions = ref<TreeSelect[]>([])

// 查询条件
const queryParams = ref<DeptQueryParams>({
  deptName: undefined,
  status: undefined
})

// 新增/编辑表单
const form = ref<SysDept>({
  deptId: undefined,
  parentId: undefined,
  deptName: undefined,
  orderNum: 0,
  leader: undefined,
  phone: undefined,
  email: undefined,
  status: '0'
})

// 新增部门，点击行内新增时默认带入父级部门
async function handleAdd(row?: SysDept) {
  resetForm()
  const res = await listDept()
  deptOptions.value = handleTree(res, 'deptId')
  if (row?.deptId) {
    form.value.parentId = row.deptId
    crudRef.value?.openAdd()
  }
}

// 编辑部门，加载详情并排除自身及子级作为上级部门选项
async function handleUpdate(row: SysDept) {
  resetForm()
  if (row.deptId) {
    const deptTree = await listDeptExcludeChild(row.deptId)
    deptOptions.value = handleTree(deptTree, 'deptId')
    form.value = await getDept(row.deptId)
    crudRef.value?.openEdit(form.value)
  }
}

// 重置新增/编辑表单默认值
function resetForm() {
  form.value = {
    deptId: undefined,
    parentId: undefined,
    deptName: undefined,
    orderNum: 0,
    leader: undefined,
    phone: undefined,
    email: undefined,
    status: '0'
  }
}

// 展开/折叠部门树表格
function toggleExpandAll() {
  expandRowKeys.value = expandRowKeys.value.length ? [] : expandAll()
}

// 获取所有部门节点 key，用于默认展开和手动展开
function expandAll(): string[] {
  const keys: string[] = []
  const dfs = (list: SysDept[]) => {
    list.forEach((item) => {
      keys.push(String(item.deptId!))
      if (item.children?.length) {
        dfs(item.children)
      }
    })
  }
  dfs(deptList.value)
  return keys
}

// 递归记录原始排序
function recordOriginalOrders(list: SysDept[]) {
  list.forEach((item) => {
    originalOrders.value[item.deptId!] = item.orderNum!
    if (item.children?.length) {
      recordOriginalOrders(item.children)
    }
  })
}

// 保存排序，仅提交排序值发生变化的部门
function handleSaveSort() {
  const changedDeptIds: number[] = []
  const changedOrderNums: number[] = []
  const dataList = crudRef.value?.data || []
  const collectChanged = (list: SysDept[]) => {
    list.forEach((item) => {
      if (
        String(originalOrders.value[item.deptId!]) !== String(item.orderNum)
      ) {
        changedDeptIds.push(item.deptId!)
        changedOrderNums.push(item.orderNum!)
      }
      if (item.children?.length) {
        collectChanged(item.children)
      }
    })
  }
  collectChanged(dataList)
  if (changedDeptIds.length === 0) {
    ElMessage.warning('未检测到排序修改')
    return
  }
  updateDeptSort({
    deptIds: changedDeptIds.join(','),
    orderNums: changedOrderNums.join(',')
  }).then(() => {
    ElMessage.success('排序保存成功')
    recordOriginalOrders(dataList)
    refreshTable()
  })
}

// 刷新表格数据
function refreshTable() {
  crudRef.value?.handleRefresh()
}

// art-crud 配置：搜索项、表格列、新增/编辑表单和权限按钮
const crudConfig: any = computed<ArtCrudConfig<SysDept>>(() => ({
  crudConfig: {
    rowKey: 'deptId',
    expandRowKeys: expandRowKeys.value,
    treeProps: { children: 'children', hasChildren: 'hasChildren' }
  },
  api: {
    list: async () => {
      const res = await listDept(queryParams.value)
      deptList.value = handleTree(res, 'deptId')
      expandRowKeys.value = expandAll()
      recordOriginalOrders(deptList.value)
      return deptList.value
    },
    add: addDept,
    update: updateDept,
    delete: delDept
  },
  searchConfig: {
    items: [
      {
        label: '部门名称',
        key: 'deptName',
        type: 'input',
        placeholder: '请输入部门名称'
      },
      {
        label: '状态',
        key: 'status',
        type: 'select',
        placeholder: '部门状态',
        options: sys_normal_disable.value
      }
    ]
  },
  columns: [
    {
      label: '部门名称',
      prop: 'deptName',
      width: 260
    },
    {
      label: '排序',
      prop: 'orderNum',
      width: 200,
      useSlot: true,
      slotName: 'orderNum'
    },
    {
      label: '状态',
      prop: 'status',
      width: 100,
      useSlot: true,
      slotName: 'status'
    },
    {
      label: '创建时间',
      prop: 'createTime',
      width: 200,
      align: 'center',
      useSlot: true,
      slotName: 'createTime'
    },
    {
      label: '操作',
      prop: 'operation',
      align: 'center',
      useSlot: true,
      slotName: 'operationTemplate'
    }
  ],
  editConfig: {
    labelWidth: 80,
    items: [
      {
        label: '上级部门',
        key: 'parentId',
        type: 'treeselect',
        span: 24,
        hidden: form.value.parentId === 0,
        props: {
          data: deptOptions.value,
          props: { value: 'deptId', label: 'deptName', children: 'children' },
          valueKey: 'deptId',
          placeholder: '选择上级部门',
          checkStrictly: true
        }
      },
      {
        label: '部门名称',
        key: 'deptName',
        type: 'input',
        span: 12,
        props: { placeholder: '请输入部门名称' }
      },
      {
        label: '显示排序',
        key: 'orderNum',
        type: 'number',
        span: 12,
        props: { controlsPosition: 'right', min: 0 }
      },
      {
        label: '负责人',
        key: 'leader',
        type: 'input',
        span: 12,
        props: { placeholder: '请输入负责人', maxlength: 20 }
      },
      {
        label: '联系电话',
        key: 'phone',
        type: 'input',
        span: 12,
        props: { placeholder: '请输入联系电话', maxlength: 11 }
      },
      {
        label: '邮箱',
        key: 'email',
        type: 'input',
        span: 12,
        props: { placeholder: '请输入邮箱', maxlength: 50 }
      },
      {
        label: '部门状态',
        key: 'status',
        type: 'radiogroup',
        span: 12,
        props: {
          options: sys_normal_disable.value
        }
      }
    ],
    rules: {
      parentId: [
        { required: true, message: '上级部门不能为空', trigger: 'blur' }
      ],
      deptName: [
        { required: true, message: '部门名称不能为空', trigger: 'blur' }
      ],
      orderNum: [
        { required: true, message: '显示排序不能为空', trigger: 'blur' }
      ],
      email: [
        {
          type: 'email',
          message: '请输入正确的邮箱地址',
          trigger: ['blur', 'change']
        }
      ],
      phone: [
        {
          pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
          message: '请输入正确的手机号码',
          trigger: 'blur'
        }
      ]
    }
  },
  btnConfig: {
    showAddOperation: () => hasAuth('system:dept:add'),
    showEditOperation: () => hasAuth('system:dept:edit'),
    showDeleteOperation: (row) =>
      row.parentId !== 0 && hasAuth('system:dept:remove')
  }
}))
</script>
