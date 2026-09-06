<template>
  <div ref="splitRef" class="dict-page art-full-height">
    <section class="dict-column dict-type-column" :style="{ width: leftWidth }">
      <ArtSearchBar
        v-model="typeFilters"
        :items="typeSearchItems"
        :span="15"
        :show-expand="false"
        :is-expand="true"
        @search="searchTypes"
      />
      <ElCard class="dict-card">
        <div class="dict-toolbar">
          <ElButton v-if="hasAuth('system:dict:add')" type="primary" @click="openTypeDrawer()">
            新增字典
          </ElButton>
          <ElButton v-if="hasAuth('system:dict:remove')" @click="clearCache">刷新缓存</ElButton>
        </div>
        <ArtTable
          :loading="typeLoading"
          :data="typeRows"
          :columns="typeColumns"
          :pagination="typePagination"
          :row-class-name="typeRowClassName"
          @row-click="selectType"
          @pagination:size-change="typeSizeChange"
          @pagination:current-change="typeCurrentChange"
        />
      </ElCard>
    </section>

    <div class="dict-resizer" @mousedown="startResize">
      <button type="button" title="收起左侧" @mousedown.stop @click="collapseLeft">
        <ArtSvgIcon icon="ri:arrow-left-s-line" />
      </button>
      <button type="button" title="展开左侧" @mousedown.stop @click="expandLeft">
        <ArtSvgIcon icon="ri:arrow-right-s-line" />
      </button>
    </div>

    <section class="dict-column dict-data-column">
      <ArtSearchBar
        v-model="dataFilters"
        :items="dataSearchItems"
        :span="8"
        :show-expand="false"
        @search="searchData"
        @reset="resetDataSearch"
      />
      <ElCard class="dict-card">
        <div class="dict-data-title">
          <b
            >字典数据<span v-if="current.dictName"> · {{ current.dictName }}</span></b
          >
          <ElTag v-if="current.dictType" type="success">{{ current.dictType }}</ElTag>
        </div>
        <template v-if="current.dictId">
          <ArtTableHeader
            v-model:columns="dataColumnChecks"
            :loading="dataLoading"
            layout="refresh,size,fullscreen,columns,settings"
            @refresh="refreshData"
          >
            <template #left>
              <ElButton v-if="hasAuth('system:dict:add')" @click="openDataDrawer()">
                新增数据
              </ElButton>
              <ElButton v-if="hasAuth('system:dict:export')" @click="exportData">导出</ElButton>
            </template>
          </ArtTableHeader>
          <ArtTable
            :loading="dataLoading"
            :data="dataRows"
            :columns="dataColumns"
            :pagination="dataPagination"
            @pagination:size-change="dataSizeChange"
            @pagination:current-change="dataCurrentChange"
          />
        </template>
        <ElEmpty v-else class="dict-empty" description="请选择左侧字典类型" />
      </ElCard>
    </section>

    <ElDrawer
      v-model="typeDrawerVisible"
      :title="`${typeEditing ? '修改' : '新增'}字典`"
      size="560px"
    >
      <ArtForm
        ref="typeFormRef"
        v-model="typeForm"
        :items="typeFormItems"
        :rules="typeRules"
        :show-submit="false"
        :show-reset="false"
      />
      <template #footer>
        <ElButton @click="typeDrawerVisible = false">取消</ElButton>
        <ElButton type="primary" :loading="saving" @click="saveType">保存</ElButton>
      </template>
    </ElDrawer>

    <ElDrawer
      v-model="dataDrawerVisible"
      :title="`${dataEditing ? '修改' : '新增'}字典数据`"
      size="620px"
    >
      <ArtForm
        ref="dataFormRef"
        v-model="dataForm"
        :items="dataFormItems"
        :rules="dataRules"
        :show-submit="false"
        :show-reset="false"
      />
      <template #footer>
        <ElButton @click="dataDrawerVisible = false">取消</ElButton>
        <ElButton type="primary" :loading="saving" @click="saveData">保存</ElButton>
      </template>
    </ElDrawer>
  </div>
</template>

<script setup lang="ts">
  import { h } from 'vue'
  import { ElMessageBox, ElTag } from 'element-plus'
  import ArtButtonTable from '@/components/core/forms/art-button-table/index.vue'
  import DictTag from '@/components/business/dict-tag/index.vue'
  import { download } from '@/api/common'
  import {
    addDictDatum,
    addDictType,
    fetchDictDataList,
    fetchDictDatum,
    fetchDictType,
    fetchDictTypeList,
    refreshDictCache,
    removeDictDatum,
    removeDictType,
    updateDictDatum,
    updateDictType
  } from '@/api/system/dict'
  import { clearDictCache, useDict } from '@/hooks/core/useDict'
  import { useAuth } from '@/hooks/core/useAuth'
  import { useTable } from '@/hooks/core/useTable'
  import type { ColumnOption } from '@/types/component'
  import type { Entity, Query } from '@/api/system/types'

  defineOptions({ name: 'SystemDict' })

  const { hasAuth } = useAuth()
  const { dict } = useDict('sys_normal_disable')
  const splitRef = ref<HTMLElement>()
  const leftWidth = ref('45%')
  const previousLeftWidth = ref('45%')
  const typeDrawerVisible = ref(false)
  const dataDrawerVisible = ref(false)
  const typeEditing = ref(false)
  const dataEditing = ref(false)
  const saving = ref(false)
  const current = reactive<Entity>({})
  const typeFilters = reactive<Entity>({})
  const dataFilters = reactive<Entity>({})
  const typeForm = reactive<Entity>({})
  const dataForm = reactive<Entity>({})
  const typeFormRef = ref<{ validate: () => Promise<boolean> }>()
  const dataFormRef = ref<{ validate: () => Promise<boolean> }>()

  const statusOptions = computed(() => dict.sys_normal_disable)
  const yesNoOptions = [
    { label: '是', value: 'Y' },
    { label: '否', value: 'N' }
  ]
  const typeSearchItems = [{ key: 'dictName', label: '字典名称', props: { placeholder: '请输入字典名称' } }]
  const dataSearchItems = [
    { key: 'dictLabel', label: '数据标签', props: { placeholder: '请输入数据标签' } },
    { key: 'dictValue', label: '数据键值', props: { placeholder: '请输入数据键值' } }
  ]
  const typeFormItems = computed(() => [
    { key: 'dictName', label: '字典名称', type: 'input', span: 24 },
    { key: 'dictType', label: '字典编码', type: 'input', span: 24 },
    {
      key: 'status',
      label: '状态',
      type: 'select',
      span: 24,
      props: { options: statusOptions.value }
    },
    { key: 'remark', label: '备注', type: 'input', span: 24, props: { type: 'textarea', rows: 3 } }
  ])
  const dataFormItems = computed(() => [
    { key: 'dictType', label: '字典编码', type: 'input', span: 24, props: { disabled: true } },
    { key: 'dictLabel', label: '数据标签', type: 'input', span: 24 },
    { key: 'dictValue', label: '数据键值', type: 'input', span: 24 },
    { key: 'dictSort', label: '排序', type: 'number', span: 24 },
    { key: 'listClass', label: '标签类型', type: 'input', span: 24 },
    { key: 'cssClass', label: '样式属性', type: 'input', span: 24 },
    { key: 'isDefault', label: '默认', type: 'select', span: 24, props: { options: yesNoOptions } },
    {
      key: 'status',
      label: '状态',
      type: 'select',
      span: 24,
      props: { options: statusOptions.value }
    },
    { key: 'remark', label: '备注', type: 'input', span: 24, props: { type: 'textarea', rows: 3 } }
  ])
  const typeRules = {
    dictName: [{ required: true, message: '请输入字典名称' }],
    dictType: [{ required: true, message: '请输入字典编码' }]
  }
  const dataRules = {
    dictLabel: [{ required: true, message: '请输入数据标签' }],
    dictValue: [{ required: true, message: '请输入数据键值' }],
    dictSort: [{ required: true, message: '请输入排序' }]
  }

  const renderStatus = (row: Entity) =>
    h(DictTag, { options: statusOptions.value, value: row.status })
  const renderTypeActions = (row: Entity) =>
    h('div', [
      hasAuth('system:dict:edit')
        ? h(ArtButtonTable, { type: 'edit', onClick: () => openTypeDrawer(row) })
        : null,
      hasAuth('system:dict:remove')
        ? h(ArtButtonTable, { type: 'delete', onClick: () => deleteType(row) })
        : null
    ])
  const renderDataActions = (row: Entity) =>
    h('div', [
      hasAuth('system:dict:edit')
        ? h(ArtButtonTable, { type: 'edit', onClick: () => openDataDrawer(row) })
        : null,
      hasAuth('system:dict:remove')
        ? h(ArtButtonTable, { type: 'delete', onClick: () => deleteData(row) })
        : null
    ])

  const typeTable = useTable({
    core: {
      apiFn: fetchDictTypeList,
      apiParams: { pageNum: 1, pageSize: 10 },
      columnsFactory: () =>
        [
          // { type: 'globalIndex', label: '序号', width: 62 },
          { prop: 'dictName', label: '字典名称', minWidth: 104 },
          { prop: 'dictType', label: '字典编码', minWidth: 126 },
          { prop: 'status', label: '状态', width: 74, formatter: renderStatus },
          {
            prop: 'operation',
            label: '操作',
            width: 120,
            fixed: 'right',
            formatter: renderTypeActions
          }
        ] as ColumnOption[]
    },
    hooks: {
      onSuccess: (rows) => {
        const selected = rows.find((row: Entity) => row.dictId === current.dictId) || rows[0]
        if (selected) selectType(selected)
        else clearCurrent()
      }
    }
  })
  const listData = (params: Query) => fetchDictDataList({ ...params, dictType: current.dictType })
  const dataTable = useTable({
    core: {
      apiFn: listData,
      apiParams: { pageNum: 1, pageSize: 10 },
      immediate: false,
      columnsFactory: () =>
        [
          { prop: 'dictValue', label: '数据键值', minWidth: 110 },
          { prop: 'listClass', label: '标签类型', minWidth: 110 },
          { prop: 'dictSort', label: '排序', width: 80 },
          { prop: 'status', label: '状态', width: 86, formatter: renderStatus },
          { prop: 'updateTime', label: '更新时间', minWidth: 150 },
          {
            prop: 'operation',
            label: '操作',
            width: 120,
            fixed: 'right',
            formatter: renderDataActions
          }
        ] as ColumnOption[]
    }
  })

  const {
    data: typeRows,
    columns: typeColumns,
    loading: typeLoading,
    pagination: typePagination,
    getData: refreshTypes,
    replaceSearchParams: replaceTypeSearch,
    handleSizeChange: typeSizeChange,
    handleCurrentChange: typeCurrentChange
  } = typeTable
  const {
    data: dataRows,
    columns: dataColumns,
    columnChecks: dataColumnChecks,
    loading: dataLoading,
    pagination: dataPagination,
    getData: refreshData,
    replaceSearchParams: replaceDataSearch,
    resetSearchParams,
    handleSizeChange: dataSizeChange,
    handleCurrentChange: dataCurrentChange
  } = dataTable

  function selectType(row: Entity) {
    if (current.dictId === row.dictId) return
    Object.assign(current, row)
    Object.keys(dataFilters).forEach((key) => delete dataFilters[key])
    replaceDataSearch({})
    nextTick(() => refreshData())
  }
  function clearCurrent() {
    Object.keys(current).forEach((key) => delete current[key])
    dataRows.value = []
  }
  function typeRowClassName({ row }: { row: Entity }) {
    return row.dictId === current.dictId ? 'is-current-dict' : ''
  }
  function searchTypes() {
    replaceTypeSearch({ ...typeFilters })
    refreshTypes()
  }
  function searchData() {
    replaceDataSearch({ ...dataFilters })
    refreshData()
  }
  async function resetDataSearch() {
    Object.keys(dataFilters).forEach((key) => delete dataFilters[key])
    await resetSearchParams()
  }
  async function openTypeDrawer(row?: Entity) {
    Object.keys(typeForm).forEach((key) => delete typeForm[key])
    Object.assign(typeForm, { status: '0' }, row ? await fetchDictType(row.dictId) : {})
    typeEditing.value = Boolean(row)
    typeDrawerVisible.value = true
  }
  async function openDataDrawer(row?: Entity) {
    Object.keys(dataForm).forEach((key) => delete dataForm[key])
    Object.assign(
      dataForm,
      { dictType: current.dictType, status: '0', isDefault: 'N', dictSort: 0 },
      row ? await fetchDictDatum(row.dictCode) : {}
    )
    dataEditing.value = Boolean(row)
    dataDrawerVisible.value = true
  }
  async function saveType() {
    await typeFormRef.value?.validate()
    saving.value = true
    try {
      await (typeEditing.value ? updateDictType(typeForm) : addDictType(typeForm))
      typeDrawerVisible.value = false
      await refreshTypes()
    } finally {
      saving.value = false
    }
  }
  async function saveData() {
    await dataFormRef.value?.validate()
    saving.value = true
    try {
      const payload = { ...dataForm, dictType: current.dictType }
      await (dataEditing.value ? updateDictDatum(payload) : addDictDatum(payload))
      dataDrawerVisible.value = false
      await refreshData()
    } finally {
      saving.value = false
    }
  }
  async function deleteType(row: Entity) {
    await ElMessageBox.confirm(`确定删除字典“${row.dictName}”吗？`, '提示', { type: 'warning' })
    await removeDictType(row.dictId)
    if (current.dictId === row.dictId) clearCurrent()
    await refreshTypes()
  }
  async function deleteData(row: Entity) {
    await ElMessageBox.confirm(`确定删除数据“${row.dictLabel}”吗？`, '提示', { type: 'warning' })
    await removeDictDatum(row.dictCode)
    await refreshData()
  }
  async function clearCache() {
    await ElMessageBox.confirm('确定刷新字典缓存吗？', '提示', { type: 'warning' })
    await refreshDictCache()
    clearDictCache()
    await refreshTypes()
    if (current.dictId) await refreshData()
  }
  function exportData() {
    download(
      '/system/dict/data/export',
      { ...dataFilters, dictType: current.dictType },
      '字典数据.xlsx'
    )
  }
  function startResize(event: MouseEvent) {
    event.preventDefault()
    const move = (moveEvent: MouseEvent) => {
      const rect = splitRef.value?.getBoundingClientRect()
      if (!rect) return
      leftWidth.value = `${Math.min(Math.max(moveEvent.clientX - rect.left, 320), rect.width - 420)}px`
    }
    const stop = () => {
      window.removeEventListener('mousemove', move)
      window.removeEventListener('mouseup', stop)
    }
    window.addEventListener('mousemove', move)
    window.addEventListener('mouseup', stop)
  }
  function collapseLeft() {
    previousLeftWidth.value = leftWidth.value
    leftWidth.value = '320px'
  }
  function expandLeft() {
    const rect = splitRef.value?.getBoundingClientRect()
    if (!rect) return
    const previous = previousLeftWidth.value.endsWith('%')
      ? (rect.width * Number.parseFloat(previousLeftWidth.value)) / 100
      : Number.parseFloat(previousLeftWidth.value)
    leftWidth.value = `${Math.min(Math.max(previous, 320), rect.width - 420)}px`
  }
</script>

<style scoped>
  .dict-page.art-full-height {
    display: flex;
    min-height: 0;
    flex-direction: row;
    gap: 10px;
  }

  .dict-column {
    display: flex;
    min-width: 0;
    min-height: 0;
    flex-direction: column;
    gap: 10px;
  }

  .dict-type-column {
    min-width: 320px;
    flex: 0 0 auto;
  }

  .dict-data-column {
    min-width: 0;
    flex: 1;
  }

  .dict-card {
    min-height: 0;
    flex: 1;
  }

  .dict-card :deep(.el-card__body) {
    display: flex;
    height: 100%;
    min-height: 0;
    flex-direction: column;
  }

  .dict-toolbar,
  .dict-data-title {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 10px;
    padding-bottom: 16px;
  }

  .dict-data-title {
    margin: -2px -20px 16px;
    padding: 0 20px 16px;
    border-bottom: 1px solid var(--el-border-color-light);
  }

  .dict-resizer {
    position: relative;
    display: flex;
    width: 1px;
    flex: 0 0 1px;
    align-items: center;
    justify-content: center;
    cursor: col-resize;
    background: var(--el-border-color-light);
  }

  .dict-resizer button {
    position: absolute;
    z-index: 2;
    display: none;
    width: 20px;
    height: 32px;
    align-items: center;
    justify-content: center;
    border: 1px solid var(--el-border-color-light);
    border-radius: 4px;
    background: var(--el-bg-color);
    color: var(--el-text-color-secondary);
    cursor: pointer;
  }

  .dict-resizer:hover button {
    display: flex;
  }

  .dict-resizer button:first-child {
    right: 2px;
  }

  .dict-resizer button:last-child {
    left: 2px;
  }

  .dict-empty {
    flex: 1;
  }

  :deep(.is-current-dict > td.el-table__cell) {
    background: var(--el-color-primary-light-9) !important;
  }

  @media (max-width: 800px) {
    .dict-page.art-full-height {
      display: block;
      overflow: auto;
    }

    .dict-type-column,
    .dict-data-column {
      width: 100% !important;
      min-width: 0;
      min-height: 520px;
    }

    .dict-data-column {
      margin-top: 12px;
    }

    .dict-resizer {
      display: none;
    }
  }
</style>
