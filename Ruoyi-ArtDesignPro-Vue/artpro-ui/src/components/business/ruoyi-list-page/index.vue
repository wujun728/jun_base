<template>
  <div class="art-full-height">
    <ArtSearchBar
      v-show="showSearchBar"
      v-model="filters"
      :items="searchItems"
      :show-expand="false"
      @search="handleSearch"
      @reset="reset"
    />
    <ElCard class="art-table-card">
      <ArtTableHeader
        v-model:columns="columnChecks"
        v-model:show-search-bar="showSearchBar"
        :loading="loading"
        @refresh="refreshData"
      >
        <template #left>
          <ElSpace wrap>
            <ElButton
              v-if="editable && hasAuth(`${permission}:add`)"
              type="primary"
              @click="openDialog()"
              >新增</ElButton
            >
            <ElButton v-if="exportable" v-auth="`${permission}:export`" @click="handleExport"
              >导出</ElButton
            >
            <ElButton
              v-if="clearable"
              v-auth="`${permission}:remove`"
              type="danger"
              @click="handleClear"
              >清空</ElButton
            >
          </ElSpace>
        </template>
      </ArtTableHeader>
      <ArtTable
        :loading="loading"
        :data="data"
        :columns="columns"
        :pagination="pagination"
        @pagination:size-change="handleSizeChange"
        @pagination:current-change="handleCurrentChange"
      />
    </ElCard>
    <ElDrawer v-model="dialogVisible" :title="drawerTitle" size="680px">
      <ElForm ref="formRef" :model="form" label-width="100px">
        <ElFormItem
          v-for="field in formFields"
          :key="field.prop"
          :label="field.label"
          :prop="field.prop"
          :rules="[{ required: true, message: `请输入${field.label}` }]"
        >
          <ElInput v-model="form[field.prop]" :disabled="mode === 'view'" />
        </ElFormItem>
      </ElForm>
      <template #footer
        ><ElButton @click="dialogVisible = false">取消</ElButton
        ><ElButton v-if="mode !== 'view'" type="primary" @click="submit">保存</ElButton></template
      >
    </ElDrawer>
  </div>
</template>

<script setup lang="ts">
  import { useTable } from '@/hooks/core/useTable'
  import { add, list, remove, update, type Entity } from '@/api/ruoyi'
  import { download } from '@/api/common'
  import { useAuth } from '@/hooks/core/useAuth'
  import ArtButtonTable from '@/components/core/forms/art-button-table/index.vue'
  import { ElMessageBox, type FormInstance } from 'element-plus'

  interface Column {
    prop: string
    label: string
    width?: number
  }
  const props = withDefaults(
    defineProps<{
      endpoint: string
      permission: string
      columns: Column[]
      search?: Column[]
      exportable?: boolean
      clearable?: boolean
      editable?: boolean
      removable?: boolean
      removePermission?: string
      idKey?: string
    }>(),
    {
      search: () => [],
      exportable: false,
      clearable: false,
      editable: false,
      removable: false,
      removePermission: 'remove',
      idKey: 'id'
    }
  )
  const { hasAuth } = useAuth()
  const dialogVisible = ref(false)
  const showSearchBar = ref(true)
  const mode = ref<'add' | 'edit' | 'view'>('add')
  const drawerTitle = computed(() =>
    mode.value === 'view' ? '查看' : mode.value === 'edit' ? '修改' : '新增'
  )
  const formRef = ref<FormInstance>()
  const form = reactive<Entity>({})
  const formFields = computed(() =>
    props.columns
      .filter((item) => item.prop !== props.idKey && !/time|date|status/i.test(item.prop))
      .slice(0, 8)
  )
  const filters = reactive<Record<string, string>>({})
  const searchItems = computed(() =>
    props.search.map((item) => ({
      label: item.label,
      key: item.prop,
      type: 'input',
      props: { clearable: true }
    }))
  )
  const apiFn = (params: Record<string, unknown>) => list(`${props.endpoint}/list`, params)
  const {
    columns,
    columnChecks,
    data,
    loading,
    pagination,
    getData,
    replaceSearchParams,
    resetSearchParams,
    refreshData,
    handleSizeChange,
    handleCurrentChange
  } = useTable({
    core: {
      apiFn,
      apiParams: { pageNum: 1, pageSize: 10 },
      columnsFactory: () => {
        const result: any[] = props.columns.map((column) => ({
          ...column,
          showOverflowTooltip: true
        }))
        if (props.editable || props.removable)
          result.push({
            prop: 'operation',
            label: '操作',
            width: 140,
            fixed: 'right',
            formatter: (row: Entity) =>
              h('div', [
                hasAuth(`${props.permission}:query`)
                  ? h(ArtButtonTable, { type: 'view', onClick: () => openDialog(row, 'view') })
                  : null,
                props.editable && hasAuth(`${props.permission}:edit`)
                  ? h(ArtButtonTable, { type: 'edit', onClick: () => openDialog(row, 'edit') })
                  : null,
                props.removable && hasAuth(`${props.permission}:${props.removePermission}`)
                  ? h(ArtButtonTable, { type: 'delete', onClick: () => handleRemove(row) })
                  : null
              ])
          })
        return result
      }
    }
  })
  function handleSearch() {
    replaceSearchParams({ ...filters })
    getData()
  }
  function reset() {
    Object.keys(filters).forEach((key) => delete filters[key])
    resetSearchParams()
    getData()
  }
  function handleExport() {
    download(`${props.endpoint}/export`, filters, `export_${Date.now()}.xlsx`)
  }
  async function handleClear() {
    await ElMessageBox.confirm('确定清空全部记录吗？', '提示', { type: 'warning' })
    await remove(`${props.endpoint}/clean`)
    refreshData()
  }
  function openDialog(row?: Entity, nextMode: 'add' | 'edit' | 'view' = row ? 'edit' : 'add') {
    Object.keys(form).forEach((key) => delete form[key])
    Object.assign(form, row || {})
    mode.value = nextMode
    dialogVisible.value = true
  }
  async function submit() {
    await formRef.value?.validate()
    await (mode.value === 'edit' ? update(props.endpoint, form) : add(props.endpoint, form))
    dialogVisible.value = false
    refreshData()
  }
  async function handleRemove(row: Entity) {
    await ElMessageBox.confirm('确定删除该记录吗？', '提示', { type: 'warning' })
    await remove(`${props.endpoint}/${row[props.idKey]}`)
    refreshData()
  }
</script>
