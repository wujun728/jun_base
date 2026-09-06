<template>
  <div class="art-full-height">
    <ArtSearchBar
      v-show="showSearchBar"
      v-model="filters"
      :items="searchItems"
      :show-expand="false"
      @search="load"
      @reset="reset"
    />
    <ElCard class="art-table-card">
      <ArtTableHeader
        v-model:columns="columnChecks"
        v-model:show-search-bar="showSearchBar"
        :loading="loading"
        @refresh="load"
      >
        <template #left>
          <ElSpace wrap>
            <ElButton v-if="hasAuth('system:dept:add')" type="primary" @click="openDialog()"
              >新增部门</ElButton
            >
            <ElButton @click="toggleExpand">{{ expanded ? '收起' : '展开' }}</ElButton>
            <ElButton v-if="hasAuth('system:dept:edit')" @click="saveSort">保存排序</ElButton>
          </ElSpace>
        </template>
      </ArtTableHeader>
      <ArtTable
        ref="tableRef"
        row-key="deptId"
        :loading="loading"
        :data="treeData"
        :columns="columns"
        :pagination="undefined"
      />
    </ElCard>
    <ElDrawer v-model="visible" :title="drawerTitle" size="680px">
      <ArtForm
        ref="formRef"
        v-model="form"
        :items="drawerFormItems"
        :rules="rules"
        :show-submit="false"
        :show-reset="false"
      />
      <template #footer
        ><ElButton @click="visible = false">取消</ElButton
        ><ElButton v-if="mode !== 'view'" type="primary" @click="submit">保存</ElButton></template
      >
    </ElDrawer>
  </div>
</template>

<script setup lang="ts">
  import { h, nextTick } from 'vue'
  import { ElMessageBox, ElTag } from 'element-plus'
  import ArtButtonTable from '@/components/core/forms/art-button-table/index.vue'
  import {
    addDept,
    fetchDept,
    fetchDeptExclude,
    fetchDeptList,
    removeDept,
    updateDept,
    updateDeptSort
  } from '@/api/system/dept'
  import { useAuth } from '@/hooks/core/useAuth'
  import { useTableColumns } from '@/hooks/core/useTableColumns'
  import { buildTree, flattenTree } from '@/utils/system/tree'
  import type { Entity } from '@/api/system/types'

  defineOptions({ name: 'SystemDept' })
  const { hasAuth } = useAuth()
  const loading = ref(false)
  const showSearchBar = ref(true)
  const expanded = ref(false)
  const visible = ref(false)
  const mode = ref<'add' | 'edit' | 'view'>('add')
  const filters = reactive({ deptName: '', deptCode: '', status: '' })
  const rows = ref<Entity[]>([])
  const treeData = computed(() => buildTree(rows.value, 'deptId', 'parentId'))
  const tableRef = ref<any>()
  const formRef = ref<any>()
  const form = reactive<Entity>({})
  const parentOptions = ref<Entity[]>([])
  const statusOptions = [
    { label: '正常', value: '0' },
    { label: '停用', value: '1' }
  ]
  const searchItems = [
    { key: 'deptName', label: '部门名称', type: 'input' },
    { key: 'deptCode', label: '部门编码', type: 'input' },
    { key: 'status', label: '状态', type: 'select', props: { options: statusOptions } }
  ]
  const rules = {
    parentId: [{ required: true, message: '请选择上级部门' }],
    deptName: [{ required: true, message: '请输入部门名称' }],
    deptCode: [{ required: true, message: '请输入部门编码' }],
    orderNum: [{ required: true, message: '请输入排序' }]
  }
  const formItems = computed(() => [
    {
      key: 'parentId',
      label: '上级部门',
      type: 'treeselect',
      span: 12,
      props: {
        data: [
          {
            deptId: 0,
            deptName: '顶级部门',
            children: buildTree(parentOptions.value, 'deptId', 'parentId')
          }
        ],
        nodeKey: 'deptId',
        valueKey: 'deptId',
        props: { value: 'deptId', label: 'deptName', children: 'children' },
        checkStrictly: true
      }
    },
    { key: 'deptName', label: '部门名称', span: 12 },
    { key: 'deptCode', label: '部门编码', span: 12 },
    { key: 'orderNum', label: '排序', type: 'number', span: 12, props: { min: 0 } },
    { key: 'leader', label: '负责人', span: 12 },
    { key: 'phone', label: '联系电话', span: 12 },
    { key: 'email', label: '邮箱', span: 12 },
    { key: 'status', label: '状态', type: 'select', span: 12, props: { options: statusOptions } }
  ])
  const drawerFormItems = computed(() =>
    formItems.value.map((item) => ({
      ...item,
      props: { ...item.props, disabled: mode.value === 'view' }
    }))
  )
  const drawerTitle = computed(() => {
    const action = mode.value === 'view' ? '查看' : mode.value === 'edit' ? '修改' : '新增'
    return `${action}部门`
  })
  const { columns, columnChecks } = useTableColumns(() => [
    { prop: 'deptName', label: '部门名称', minWidth: 260, showOverflowTooltip: true },
    { prop: 'deptCode', label: '部门编码', minWidth: 130 },
    { prop: 'leader', label: '负责人', minWidth: 100 },
    {
      prop: 'orderNum',
      label: '排序',
      width: 100,
      formatter: (row: Entity) =>
        h('input', {
          class: 'w-16 rounded border border-g-300 px-2 py-1',
          type: 'number',
          min: 0,
          value: row.orderNum,
          onInput: (event: Event) =>
            (row.orderNum = Number((event.target as HTMLInputElement).value))
        })
    },
    {
      prop: 'status',
      label: '状态',
      width: 100,
      formatter: (row: Entity) =>
        h(ElTag, { type: row.status === '0' ? 'success' : 'danger' }, () =>
          row.status === '0' ? '启用' : '停用'
        )
    },
    { prop: 'updateTime', label: '更新时间', minWidth: 170 },
    {
      prop: 'operation',
      label: '操作',
      width: 220,
      fixed: 'right',
      formatter: (row: Entity) =>
        h('div', [
          hasAuth('system:dept:query')
            ? h(ArtButtonTable, { type: 'view', onClick: () => openDialog(row, 0, 'view') })
            : null,
          hasAuth('system:dept:add')
            ? h(ArtButtonTable, { type: 'add', onClick: () => openDialog(undefined, row.deptId) })
            : null,
          hasAuth('system:dept:edit')
            ? h(ArtButtonTable, { type: 'edit', onClick: () => openDialog(row) })
            : null,
          hasAuth('system:dept:remove')
            ? h(ArtButtonTable, { type: 'delete', onClick: () => removeRow(row) })
            : null
        ])
    }
  ])

  async function load() {
    loading.value = true
    try {
      rows.value = await fetchDeptList(filters)
    } finally {
      loading.value = false
    }
  }
  function reset() {
    Object.assign(filters, { deptName: '', deptCode: '', status: '' })
    load()
  }
  async function openDialog(
    row?: Entity,
    parentId = 0,
    nextMode: 'add' | 'edit' | 'view' = row ? 'edit' : 'add'
  ) {
    Object.keys(form).forEach((key) => delete form[key])
    Object.assign(form, { parentId, orderNum: 0, status: '0' })
    parentOptions.value = row ? await fetchDeptExclude(row.deptId) : await fetchDeptList()
    if (row) Object.assign(form, await fetchDept(row.deptId))
    mode.value = nextMode
    visible.value = true
  }
  async function submit() {
    await formRef.value?.validate()
    await (form.deptId ? updateDept(form) : addDept(form))
    visible.value = false
    load()
  }
  async function removeRow(row: Entity) {
    await ElMessageBox.confirm(`确定删除部门“${row.deptName}”吗？`, '提示', { type: 'warning' })
    await removeDept(row.deptId)
    load()
  }
  async function saveSort() {
    const list = flattenTree(treeData.value)
    await updateDeptSort(
      list.map((row) => row.deptId),
      list.map((row) => row.orderNum)
    )
    load()
  }
  function toggleExpand() {
    expanded.value = !expanded.value
    nextTick(() =>
      flattenTree(treeData.value).forEach((row) =>
        tableRef.value?.elTableRef?.toggleRowExpansion(row, expanded.value)
      )
    )
  }
  load()
</script>
