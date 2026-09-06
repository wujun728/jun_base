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
            <ElButton v-auth="'tool:gen:import'" type="primary" @click="openImport"
              >导入表</ElButton
            >
            <ElButton v-auth="'tool:gen:code'" :disabled="!selection.length" @click="batchDownload"
              >批量生成</ElButton
            >
          </ElSpace>
        </template>
      </ArtTableHeader>
      <ArtTable
        :loading="loading"
        :data="data"
        :columns="columns"
        :pagination="pagination"
        @selection-change="selection = $event"
        @pagination:size-change="handleSizeChange"
        @pagination:current-change="handleCurrentChange"
      />
    </ElCard>

    <ElDialog v-model="importVisible" title="导入数据库表" width="860px">
      <ElForm inline>
        <ElFormItem label="表名称"><ElInput v-model="dbFilters.tableName" clearable /></ElFormItem>
        <ElFormItem label="表描述"
          ><ElInput v-model="dbFilters.tableComment" clearable
        /></ElFormItem>
        <ElFormItem label="前端模板">
          <ElSelect v-model="tplWebType" style="width: 220px">
            <ElOption label="Art Design Pro TypeScript" value="art-design-pro" />
            <ElOption label="Element Plus TypeScript" value="element-plus-ts" />
            <ElOption label="Element Plus" value="element-plus" />
            <ElOption label="Element UI" value="element-ui" />
          </ElSelect>
        </ElFormItem>
        <ElButton type="primary" @click="loadDbTables">查询</ElButton>
      </ElForm>
      <ElTable :data="dbTables" @selection-change="dbSelection = $event">
        <ElTableColumn type="selection" width="48" />
        <ElTableColumn prop="tableName" label="表名称" />
        <ElTableColumn prop="tableComment" label="表描述" />
        <ElTableColumn prop="createTime" label="创建时间" width="170" />
      </ElTable>
      <template #footer>
        <ElButton @click="importVisible = false">取消</ElButton>
        <ElButton type="primary" :disabled="!dbSelection.length" @click="submitImport"
          >导入</ElButton
        >
      </template>
    </ElDialog>

    <ElDrawer v-model="editVisible" title="修改代码生成配置" size="88%">
      <ArtForm v-model="form" :items="formItems" :show-submit="false" :show-reset="false" />
      <ElDivider content-position="left">字段配置</ElDivider>
      <ElTable :data="form.columns" border>
        <ElTableColumn prop="columnName" label="字段列名" min-width="130" />
        <ElTableColumn label="字段描述" min-width="140"
          ><template #default="{ row }"><ElInput v-model="row.columnComment" /></template
        ></ElTableColumn>
        <ElTableColumn label="Java 属性" min-width="130"
          ><template #default="{ row }"><ElInput v-model="row.javaField" /></template
        ></ElTableColumn>
        <ElTableColumn label="Java 类型" width="130"
          ><template #default="{ row }"
            ><ElSelect v-model="row.javaType"
              ><ElOption
                v-for="item in javaTypes"
                :key="item"
                :label="item"
                :value="item" /></ElSelect></template
        ></ElTableColumn>
        <ElTableColumn label="控件" width="130"
          ><template #default="{ row }"
            ><ElSelect v-model="row.htmlType"
              ><ElOption
                v-for="item in htmlTypes"
                :key="item"
                :label="item"
                :value="item" /></ElSelect></template
        ></ElTableColumn>
        <ElTableColumn label="字典类型" min-width="130"
          ><template #default="{ row }"><ElInput v-model="row.dictType" /></template
        ></ElTableColumn>
        <ElTableColumn
          v-for="item in flagColumns"
          :key="item.prop"
          :label="item.label"
          width="64"
          align="center"
          ><template #default="{ row }"
            ><ElCheckbox v-model="row[item.prop]" true-value="1" false-value="0" /></template
        ></ElTableColumn>
      </ElTable>
      <template #footer
        ><ElButton @click="editVisible = false">取消</ElButton
        ><ElButton type="primary" @click="save">保存</ElButton></template
      >
    </ElDrawer>

    <ElDrawer v-model="previewVisible" title="代码预览" size="88%">
      <ElTabs v-model="previewTab">
        <ElTabPane v-for="(content, file) in preview" :key="file" :label="file" :name="file">
          <pre class="code-preview max-h-[66vh] overflow-auto rounded p-4 text-xs">{{
            content
          }}</pre>
        </ElTabPane>
      </ElTabs>
    </ElDrawer>
  </div>
</template>

<script setup lang="ts">
  import { ElMessageBox } from 'element-plus'
  import ArtButtonMore from '@/components/core/forms/art-button-more/index.vue'
  import ArtButtonTable from '@/components/core/forms/art-button-table/index.vue'
  import { useAuth } from '@/hooks/core/useAuth'
  import { useTable } from '@/hooks/core/useTable'
  import type { Entity } from '@/api/system/types'
  import {
    downloadBatchGen,
    downloadGen,
    fetchDbTables,
    fetchGenInfo,
    fetchGenList,
    generateGen,
    importTables,
    previewGen,
    removeGen,
    syncGen,
    updateGen
  } from '@/api/tool/gen'

  defineOptions({ name: 'ToolGen' })
  const { hasAuth } = useAuth()
  const showSearchBar = ref(true)
  const filters = reactive<Entity>({})
  const selection = ref<Entity[]>([])
  const importVisible = ref(false),
    editVisible = ref(false),
    previewVisible = ref(false)
  const dbFilters = reactive<Entity>({}),
    dbTables = ref<Entity[]>([]),
    dbSelection = ref<Entity[]>([])
  const tplWebType = ref('art-design-pro')
  const form = reactive<Entity>({ columns: [] })
  const genTables = ref<Entity[]>([])
  const preview = ref<Record<string, string>>({}),
    previewTab = ref('')
  const searchItems = [
    { key: 'tableName', label: '表名称', type: 'input', props: { clearable: true } },
    { key: 'tableComment', label: '表描述', type: 'input', props: { clearable: true } }
  ]
  const javaTypes = ['Long', 'String', 'Integer', 'Double', 'BigDecimal', 'Date', 'Boolean']
  const htmlTypes = [
    'input',
    'textarea',
    'select',
    'radio',
    'checkbox',
    'datetime',
    'imageUpload',
    'fileUpload',
    'editor'
  ]
  const flagColumns = [
    { prop: 'isInsert', label: '新增' },
    { prop: 'isEdit', label: '编辑' },
    { prop: 'isList', label: '列表' },
    { prop: 'isQuery', label: '查询' },
    { prop: 'isRequired', label: '必填' }
  ]
  const columnOptions = computed(() =>
    (form.columns || []).map((column: Entity) => ({
      label: `${column.columnName}：${column.columnComment}`,
      value: column.columnName
    }))
  )
  const tableOptions = computed(() =>
    genTables.value
      .filter((table) => table.tableName !== form.tableName)
      .map((table) => ({ label: `${table.tableName}：${table.tableComment}`, value: table.tableName }))
  )
  const formItems = computed(() => [
    { key: 'tableName', label: '表名称', type: 'input', span: 8, props: { disabled: true } },
    { key: 'tableComment', label: '表描述', type: 'input', span: 8 },
    { key: 'className', label: '实体类', type: 'input', span: 8 },
    { key: 'packageName', label: '生成包路径', type: 'input', span: 8 },
    { key: 'moduleName', label: '模块名', type: 'input', span: 8 },
    { key: 'businessName', label: '业务名', type: 'input', span: 8 },
    { key: 'functionName', label: '功能名', type: 'input', span: 8 },
    { key: 'functionAuthor', label: '作者', type: 'input', span: 8 },
    {
      key: 'tplCategory',
      label: '模板',
      type: 'select',
      span: 8,
      props: {
        options: [
          { label: '单表', value: 'crud' },
          { label: '树表', value: 'tree' },
          { label: '主子表', value: 'sub' }
        ]
      }
    },
    {
      key: 'tplWebType',
      label: '前端模板',
      type: 'select',
      span: 8,
      props: {
        options: [
          { label: 'Art Design Pro TypeScript', value: 'art-design-pro' },
          { label: 'Element Plus TypeScript', value: 'element-plus-ts' },
          { label: 'Element Plus', value: 'element-plus' },
          { label: 'Element UI', value: 'element-ui' }
        ]
      }
    },
    {
      key: 'formColNum',
      label: '表单列数',
      type: 'select',
      span: 8,
      props: {
        options: [
          { label: '单列', value: 1 },
          { label: '双列', value: 2 },
          { label: '三列', value: 3 }
        ]
      }
    },
    { key: 'parentMenuId', label: '上级菜单 ID', type: 'number', span: 8, props: { min: 0 } },
    { key: 'view', label: '生成详情页', type: 'switch', span: 8 },
    {
      key: 'treeCode',
      label: '树编码字段',
      type: 'select',
      span: 8,
      hidden: form.tplCategory !== 'tree',
      props: { options: columnOptions.value }
    },
    {
      key: 'treeParentCode',
      label: '树父编码字段',
      type: 'select',
      span: 8,
      hidden: form.tplCategory !== 'tree',
      props: { options: columnOptions.value }
    },
    {
      key: 'treeName',
      label: '树名称字段',
      type: 'select',
      span: 8,
      hidden: form.tplCategory !== 'tree',
      props: { options: columnOptions.value }
    },
    {
      key: 'subTableName',
      label: '关联子表',
      type: 'select',
      span: 8,
      hidden: form.tplCategory !== 'sub',
      props: { options: tableOptions.value }
    },
    {
      key: 'subTableFkName',
      label: '子表外键',
      type: 'input',
      span: 8,
      hidden: form.tplCategory !== 'sub'
    },
    {
      key: 'genType',
      label: '生成方式',
      type: 'select',
      span: 8,
      props: {
        options: [
          { label: 'zip 下载', value: '0' },
          { label: '自定义路径', value: '1' }
        ]
      }
    },
    { key: 'genPath', label: '生成路径', type: 'input', span: 16 }
  ])
  const table = useTable({
    core: {
      apiFn: fetchGenList,
      apiParams: { pageNum: 1, pageSize: 10 },
      columnsFactory: () => [
        { type: 'selection', width: 48 },
        { prop: 'tableName', label: '表名称', minWidth: 150 },
        { prop: 'tableComment', label: '表描述', minWidth: 150 },
        { prop: 'className', label: '实体类', minWidth: 120 },
        { prop: 'tplCategory', label: '模板', width: 90 },
        { prop: 'updateTime', label: '更新时间', width: 170 },
        { prop: 'operation', label: '操作', align: 'right', fixed: 'right', minWidth: 220, formatter: actions }
      ]
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
  function actions(row: Entity) {
    const more = [
      { key: 'sync', label: '同步数据库' },
      { key: 'download', label: '下载代码' },
      { key: 'generate', label: '生成到路径' }
    ]
    return h('div', { style: 'display:flex;align-items:center;justify-content:flex-end;gap:10px;white-space:nowrap' }, [
      hasAuth('tool:gen:preview')
        ? h(ArtButtonTable, { type: 'view', onClick: () => openPreview(row) })
        : null,
      hasAuth('tool:gen:edit')
        ? h(ArtButtonTable, { type: 'edit', onClick: () => openEdit(row) })
        : null,
      hasAuth('tool:gen:remove')
        ? h(ArtButtonTable, { type: 'delete', onClick: () => remove(row) })
        : null,
      h(ArtButtonMore, { list: more, onClick: (item: Entity) => runMore(item.key, row) })
    ])
  }
  function search() {
    replaceSearchParams({ ...filters })
    getData()
  }
  function reset() {
    Object.keys(filters).forEach((key) => delete filters[key])
    resetSearchParams()
    getData()
  }
  async function openImport() {
    importVisible.value = true
    await loadDbTables()
  }
  async function loadDbTables() {
    dbTables.value = (await fetchDbTables({ ...dbFilters, pageNum: 1, pageSize: 100 })).rows
  }
  async function submitImport() {
    await importTables(dbSelection.value.map((item) => item.tableName).join(','), tplWebType.value)
    importVisible.value = false
    refreshData()
  }
  async function openEdit(row: Entity) {
    const result = await fetchGenInfo(row.tableId)
    genTables.value = result.tables
    Object.keys(form).forEach((key) => delete form[key])
    Object.assign(form, result.info, { columns: result.rows })
    editVisible.value = true
  }
  async function save() {
    await updateGen({
      ...form,
      params: {
        treeCode: form.treeCode,
        treeParentCode: form.treeParentCode,
        treeName: form.treeName,
        parentMenuId: form.parentMenuId,
        parentMenuName: form.parentMenuName,
        genView: form.view
      }
    })
    editVisible.value = false
    refreshData()
  }
  async function openPreview(row: Entity) {
    preview.value = await previewGen(row.tableId)
    previewTab.value = Object.keys(preview.value)[0] || ''
    previewVisible.value = true
  }
  async function remove(row: Entity) {
    await ElMessageBox.confirm(`确定删除“${row.tableName}”吗？`, '提示', { type: 'warning' })
    await removeGen(row.tableId)
    refreshData()
  }
  async function runMore(key: string, row: Entity) {
    if (key === 'sync') {
      await ElMessageBox.confirm(`确定同步“${row.tableName}”吗？`, '提示', { type: 'warning' })
      await syncGen(row.tableName)
      refreshData()
    }
    if (key === 'download') await downloadGen(row.tableName)
    if (key === 'generate') await generateGen(row.tableName)
  }
  function batchDownload() {
    downloadBatchGen(selection.value.map((item) => item.tableName).join(','))
  }
</script>

<style scoped>
  .code-preview {
    color: var(--art-gray-900);
    border: 1px solid var(--default-border);
    background: var(--default-bg-color);
  }
</style>
