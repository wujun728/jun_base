<template>
  <div class="art-full-height">
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
            <ElButton v-if="exportUrl && hasAuth(`${permission}:export`)" @click="exportData"
              >导出</ElButton
            >
            <ElButton v-if="cleanFn && hasAuth(`${permission}:remove`)" type="danger" @click="clean"
              >清空</ElButton
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
        @pagination:size-change="handleSizeChange"
        @pagination:current-change="handleCurrentChange"
      />
    </ElCard>
    <ElDrawer v-model="detailVisible" title="查看详情" size="680px">
      <ElDescriptions :column="1" border>
        <ElDescriptionsItem v-for="field in detailFields" :key="field.prop" :label="field.label">
          <DictTag v-if="field.dict" :options="field.dict" :value="detail[field.prop]" />
          <span v-else class="whitespace-pre-wrap break-all">{{
            field.formatter?.(detail) ?? detail[field.prop] ?? '-'
          }}</span>
        </ElDescriptionsItem>
      </ElDescriptions>
    </ElDrawer>
  </div>
</template>
<script setup lang="ts">
  import { h } from 'vue'
  import { ElMessageBox } from 'element-plus'
  import ArtButtonTable from '@/components/core/forms/art-button-table/index.vue'
  import ArtButtonMore from '@/components/core/forms/art-button-more/index.vue'
  import DictTag from '@/components/business/dict-tag/index.vue'
  import { download, type DictData } from '@/api/common'
  import { useAuth } from '@/hooks/core/useAuth'
  import { useTable } from '@/hooks/core/useTable'
  import type { ColumnOption } from '@/types/component'
  import type { Entity, PageResult, Query } from '@/api/system/types'
  export interface MonitorField {
    prop: string
    label: string
    width?: number
    minWidth?: number
    table?: boolean
    detail?: boolean
    search?:
      | boolean
      | {
          type?: string
          props?: Record<string, unknown>
        }
    dict?: DictData[]
    formatter?: (row: Entity) => unknown
  }
  const props = withDefaults(
    defineProps<{
      permission: string
      idKey: string
      fields: MonitorField[]
      listFn: (params: Query) => Promise<PageResult>
      removeFn?: (id: string | number) => Promise<unknown>
      cleanFn?: () => Promise<unknown>
      exportUrl?: string
      transformParams?: (params: Entity) => Query
      actions?: Array<{
        key: string
        label: string
        auth?: string
        icon?: string
        handler: (row: Entity) => void
      }>
    }>(),
    { actions: () => [] }
  )
  const { hasAuth } = useAuth(),
    filters = reactive<Entity>({})
  const showSearchBar = ref(true)
  const detailVisible = ref(false)
  const detail = reactive<Entity>({})
  const detailFields = computed(() => props.fields.filter((field) => field.detail !== false))
  const searchItems = computed(() =>
    props.fields
      .filter((field) => field.search)
      .map((field) => ({
        key: field.prop,
        label: field.label,
        ...(typeof field.search === 'object' ? field.search : {})
      }))
  )
  const toColumn = (field: MonitorField): ColumnOption => ({
    ...field,
    showOverflowTooltip: true,
    formatter:
      field.formatter ||
      (field.dict
        ? (row: Entity) => h(DictTag, { options: field.dict!, value: row[field.prop] })
        : undefined)
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
    handleCurrentChange,
    updateColumn
  } = useTable({
    core: {
      apiFn: props.listFn,
      apiParams: { pageNum: 1, pageSize: 10 },
      columnsFactory: () => {
        const result: ColumnOption[] = props.fields
          .filter((field) => field.table !== false)
          .map(toColumn)
        if (hasAuth(`${props.permission}:query`) || props.removeFn || props.actions.length)
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
                    ? h(ArtButtonTable, { type: 'view', onClick: () => viewRow(row) })
                    : null,
                  props.removeFn && hasAuth(`${props.permission}:remove`)
                    ? h(ArtButtonTable, { type: 'delete', onClick: () => removeRow(row) })
                    : null,
                  props.actions.length
                    ? h(ArtButtonMore, {
                        list: props.actions,
                        onClick: (item: { key: string | number }) =>
                          props.actions.find((action) => action.key === item.key)?.handler(row)
                      })
                    : null
                ]
              )
          })
        return result
      }
    }
  })
  watch(
    () => props.fields,
    (fields) => {
      updateColumn?.(
        fields
          .filter((field) => field.table !== false)
          .map((field) => ({ prop: field.prop, updates: toColumn(field) }))
      )
    },
    { deep: true }
  )
  const buildParams = () => props.transformParams?.({ ...filters }) || { ...filters }
  function search() {
    replaceSearchParams(buildParams())
    getData()
  }
  function reset() {
    Object.keys(filters).forEach((key) => delete filters[key])
    resetSearchParams()
    getData()
  }
  function exportData() {
    download(props.exportUrl!, buildParams(), `export_${Date.now()}.xlsx`)
  }
  async function clean() {
    await ElMessageBox.confirm('确定清空全部记录吗？', '提示', { type: 'warning' })
    await props.cleanFn?.()
    refreshData()
  }
  async function removeRow(row: Entity) {
    await ElMessageBox.confirm('确定删除该记录吗？', '提示', { type: 'warning' })
    await props.removeFn?.(row[props.idKey])
    refreshData()
  }
  function viewRow(row: Entity) {
    Object.assign(detail, row)
    detailVisible.value = true
  }
  defineExpose({ refreshData })
</script>
