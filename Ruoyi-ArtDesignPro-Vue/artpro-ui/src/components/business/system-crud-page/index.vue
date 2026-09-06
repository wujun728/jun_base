<template>
  <div class="system-crud-page art-full-height">
    <ArtSearchBar
      v-show="showSearchBar"
      v-model="filters"
      :items="searchItems"
      :show-expand="false"
      @search="search"
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
            <ElButton v-if="canAdd" type="primary" @click="openDialog()">新增</ElButton>
            <ElButton
              v-if="exportUrl && hasAuth(`${permission}:${exportPermission}`)"
              @click="handleExport"
              >导出</ElButton
            >
            <slot name="toolbar" :refresh="refreshData" />
          </ElSpace>
        </template>
      </ArtTableHeader>
      <ArtTable
        :loading="loading"
        :data="data"
        :columns="columns"
        :pagination="pagination"
        @row-click="rowClick"
        @pagination:size-change="handleSizeChange"
        @pagination:current-change="handleCurrentChange"
      />
    </ElCard>
    <ElDrawer v-model="dialogVisible" :title="drawerTitle" :size="drawerSize">
      <ArtForm
        ref="formRef"
        v-model="form"
        :items="drawerFormItems"
        :rules="rules"
        :show-submit="false"
        :show-reset="false"
      />
      <template #footer>
        <ElButton @click="dialogVisible = false">取消</ElButton>
        <ElButton v-if="mode !== 'view'" type="primary" :loading="saving" @click="submit"
          >保存</ElButton
        >
      </template>
    </ElDrawer>
  </div>
</template>

<script setup lang="ts">
  import { h } from 'vue'
  import { ElMessageBox, ElSwitch } from 'element-plus'
  import ArtButtonTable from '@/components/core/forms/art-button-table/index.vue'
  import ArtButtonMore from '@/components/core/forms/art-button-more/index.vue'
  import DictTag from '@/components/business/dict-tag/index.vue'
  import { download, fetchDictData } from '@/api/common'
  import { useAuth } from '@/hooks/core/useAuth'
  import { useTable } from '@/hooks/core/useTable'
  import type { ColumnOption } from '@/types/component'
  import type { Entity, PageResult, Query } from '@/api/system/types'
  import type { DictData } from '@/api/common'

  export interface CrudField {
    prop: string
    label: string
    type?: string
    width?: number
    minWidth?: number
    required?: boolean
    hidden?: boolean
    table?: boolean
    form?: boolean
    search?: boolean
    span?: number
    options?: Array<Record<string, any>>
    dict?: Array<Record<string, any>>
    dictType?: string
    props?: Record<string, any>
    render?: any
  }

  const props = withDefaults(
    defineProps<{
      title: string
      permission: string
      idKey: string
      fields: CrudField[]
      listFn: (params: Query) => Promise<PageResult>
      getFn?: (id: number) => Promise<Entity>
      addFn: (data: Entity) => Promise<unknown>
      updateFn: (data: Entity) => Promise<unknown>
      removeFn: (id: number | string) => Promise<unknown>
      removePermission?: string
      exportPermission?: string
      statusFn?: (data: Entity) => Promise<unknown>
      exportUrl?: string
      defaults?: Entity
      actions?: Array<{
        key: string
        label: string
        auth?: string
        icon?: string
        handler: (row: Entity) => void
      }>
      drawerSize?: string
      rowClick?: (row: Entity) => void
    }>(),
    {
      defaults: () => ({}),
      drawerSize: '680px',
      rowClick: () => undefined,
      removePermission: 'remove',
      exportPermission: 'export'
    }
  )

  const { hasAuth } = useAuth()
  const dialogVisible = ref(false)
  const showSearchBar = ref(true)
  const mode = ref<'add' | 'edit' | 'view'>('add')
  const saving = ref(false)
  const dictMap = reactive<Record<string, DictData[]>>({})
  const formRef = ref<{ validate: () => Promise<boolean> }>()
  const form = reactive<Entity>({})
  const filters = reactive<Entity>({})
  const rules = computed(() =>
    Object.fromEntries(
      props.fields
        .filter((field) => field.required)
        .map((field) => [
          field.prop,
          [{ required: true, message: `请输入${field.label}`, trigger: 'blur' }]
        ])
    )
  )
  const formItems = computed(() =>
    props.fields
      .filter((field) => !field.hidden && field.form !== false)
      .map((field) => ({
        key: field.prop,
        label: field.label,
        type: field.type || 'input',
        render: field.render,
        span: field.span ?? 12,
        props: { clearable: true, ...field.props, options: getSelectOptions(field) }
      }))
  )
  const drawerFormItems = computed(() =>
    formItems.value.map((item) => ({
      ...item,
      props: { ...item.props, disabled: mode.value === 'view' }
    }))
  )
  const drawerTitle = computed(() => {
    const action = mode.value === 'view' ? '查看' : mode.value === 'edit' ? '修改' : '新增'
    return `${action}${props.title}`
  })
  const searchItems = computed(() =>
    props.fields
      .filter((field) => field.search)
      .map((field) => ({
        key: field.prop,
        label: field.label,
        type: field.type || 'input',
        props: { clearable: true, ...field.props, options: getSelectOptions(field) }
      }))
  )
  const canAdd = computed(() => hasAuth(`${props.permission}:add`))

  const table = useTable({
    core: {
      apiFn: props.listFn,
      apiParams: { pageNum: 1, pageSize: 10 },
      columnsFactory: () => {
        const result: ColumnOption[] = props.fields
          .filter((field) => field.table !== false && !field.hidden)
          .map((field) => ({
            prop: field.prop,
            label: field.label,
            width: field.width,
            minWidth: field.minWidth,
            showOverflowTooltip: true,
            formatter:
              field.prop === 'status' && props.statusFn
                ? (row: Entity) =>
                    h(ElSwitch, {
                      modelValue: row.status,
                      activeValue: '0',
                      inactiveValue: '1',
                      disabled: !hasAuth(`${props.permission}:edit`),
                      'onUpdate:modelValue': (status: string | number | boolean) =>
                        changeStatus(row, String(status))
                    })
                : field.dict || field.dictType
                  ? (row: Entity) => {
                      const options = getDictOptions(field)
                      return options?.length
                        ? h(DictTag, {
                            options,
                            value: getValue(row, field.prop) as string | number | undefined
                          })
                        : getValue(row, field.prop)
                    }
                  : undefined
          }))
        result.push({
          prop: 'operation',
          label: '操作',
          align: 'right',
          fixed: 'right',
          minWidth: 220,
          formatter: (row: Entity) =>
            h(
              'div',
              {
                style:
                  'display:flex;align-items:center;justify-content:flex-end;gap:10px;white-space:nowrap'
              },
              [
                hasAuth(`${props.permission}:query`)
                  ? h(ArtButtonTable, { type: 'view', onClick: () => openDialog(row, 'view') })
                  : null,
                hasAuth(`${props.permission}:edit`)
                  ? h(ArtButtonTable, { type: 'edit', onClick: () => openDialog(row, 'edit') })
                  : null,
                hasAuth(`${props.permission}:${props.removePermission}`)
                  ? h(ArtButtonTable, { type: 'delete', onClick: () => handleRemove(row) })
                  : null,
                props.actions?.length
                  ? h(ArtButtonMore, {
                      list: props.actions,
                      onClick: (item: { key: string | number }) =>
                        props.actions?.find((action) => action.key === item.key)?.handler(row)
                    })
                  : null
              ]
            )
        })
        return result
      }
    }
  })
  const {
    data,
    columns,
    columnChecks,
    loading,
    pagination,
    getData,
    replaceSearchParams,
    resetSearchParams,
    refreshData,
    handleSizeChange,
    handleCurrentChange
  } = table

  onMounted(loadDicts)

  function search() {
    replaceSearchParams({ ...filters })
    getData()
  }
  function getValue(row: Entity, path: string): unknown {
    return path
      .split('.')
      .reduce<unknown>((value, key) => (value as Record<string, unknown> | undefined)?.[key], row)
  }
  function normalizeOptions(options?: Array<Record<string, any>>) {
    return options?.map((option) => ({
      ...option,
      label: option.label ?? option.dictLabel,
      value: option.value ?? option.dictValue
    }))
  }
  function getDictOptions(field: CrudField) {
    return (field.dict || (field.dictType ? dictMap[field.dictType] : undefined)) as
      | DictData[]
      | undefined
  }
  function getSelectOptions(field: CrudField) {
    return normalizeOptions(field.options || getDictOptions(field))
  }
  async function loadDicts() {
    const types = Array.from(
      new Set(props.fields.map((field) => field.dictType).filter(Boolean) as string[])
    )
    await Promise.all(
      types.map(async (type) => {
        if (!dictMap[type]) dictMap[type] = await fetchDictData(type)
      })
    )
  }
  function reset() {
    Object.keys(filters).forEach((key) => delete filters[key])
    resetSearchParams()
    getData()
  }
  function handleExport() {
    download(props.exportUrl!, filters, `${props.title}_${Date.now()}.xlsx`)
  }
  async function openDialog(
    row?: Entity,
    nextMode: 'add' | 'edit' | 'view' = row ? 'edit' : 'add'
  ) {
    Object.keys(form).forEach((key) => delete form[key])
    Object.assign(form, props.defaults)
    if (row) Object.assign(form, props.getFn ? await props.getFn(row[props.idKey]) : row)
    mode.value = nextMode
    dialogVisible.value = true
  }
  async function submit() {
    await formRef.value?.validate()
    saving.value = true
    try {
      await (mode.value === 'edit' ? props.updateFn(form) : props.addFn(form))
      dialogVisible.value = false
      refreshData()
    } finally {
      saving.value = false
    }
  }
  async function handleRemove(row: Entity) {
    await ElMessageBox.confirm(
      `确定删除${props.title}“${row[props.fields[0].prop] ?? row[props.idKey]}”吗？`,
      '提示',
      { type: 'warning' }
    )
    await props.removeFn(row[props.idKey])
    refreshData()
  }
  async function changeStatus(row: Entity, status: string) {
    const previous = row.status
    row.status = status
    try {
      await props.statusFn?.({ [props.idKey]: row[props.idKey], status })
    } catch {
      row.status = previous
    }
  }
  defineExpose({ refreshData, openDialog })
</script>

<style scoped>
  .system-crud-page {
    display: flex;
    min-height: 0;
    flex-direction: column;
  }

  .system-crud-page :deep(.art-table-card) {
    min-height: 0;
    flex: 1;
  }

  .system-crud-page :deep(.art-table-card .el-card__body) {
    height: 100%;
    min-height: 0;
  }
</style>
