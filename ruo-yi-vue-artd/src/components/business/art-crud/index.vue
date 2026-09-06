<template>
  <div class="art-full-height ejs-grid-container">
    <!-- 搜索栏 -->
    <ArtSearchBar
      v-if="showSearch"
      v-model="searchForm"
      v-bind="searchConfig"
      @search="handleSearch"
      @reset="resetSearchParams">
      <!-- 搜索栏插槽 -->
      <template
        v-for="(slot, name) in searchSlots"
        :key="name"
        #[name]="slotProps">
        <slot :name="`search-${name}`" v-bind="slotProps" />
      </template>
    </ArtSearchBar>

    <ElCard class="art-table-card" shadow="never">
      <ArtTableBar
        v-model:columns="columnChecks"
        v-model:showSearchBar="showSearchBar"
        @refresh="handleRefresh">
        <template #left>
          <ElSpace wrap>
            <ElButton
              v-if="btnConfig.showAddOperation?.()"
              type="primary"
              plain
              :icon="Plus"
              @click="addHandle">
              新增
            </ElButton>
            <slot name="toolbar"></slot>
          </ElSpace>
        </template>
      </ArtTableBar>
      <!-- 表格组件 -->
      <ArtTable
        ref="tableRef"
        :loading="loading"
        :data="data"
        :columns="columns"
        table-layout="fixed"
        v-bind="props.config.crudConfig"
        :pagination="pagination"
        @selection-change="handleSelectionChange"
        @pagination:size-change="handleSizeChange"
        @pagination:current-change="handleCurrentChange">
        <template v-for="slot in tableSlotArr" :key="slot" #[slot]="slotProps">
          <slot
            :name="`col-${slot}`"
            v-bind="{ ...slotProps, $pagination: pagination }" />
        </template>

        <template #operationTemplate="scope">
          <div class="w-full">
            <ArtButtonTable
              v-if="btnConfig?.showEditOperation?.(scope.row)"
              type="edit"
              @click="editRow(scope.row)">
              编辑
            </ArtButtonTable>
            <ArtButtonTable
              v-if="btnConfig?.showDeleteOperation?.(scope.row)"
              type="delete"
              @click="deleteRow(scope.row)">
              删除
            </ArtButtonTable>
            <div class="inline-flex align-bottom">
              <slot :name="'col-operationTemplate'" v-bind="scope"></slot>
            </div>
          </div>
        </template>
      </ArtTable>
    </ElCard>
    <ElDialog
      v-if="formItems.length"
      :title="editType === 'add' ? '新增' : '编辑'"
      v-model="editVisible"
      width="680px"
      @close="cancelVisible">
      <ArtForm
        v-model="editForm"
        :span="editConfig.span || 8"
        :labelWidth="editConfig.labelWidth"
        :showReset="false"
        :showSubmit="false"
        :items="formItems"
        :rules="formRules"
        ref="formRef">
        <template v-for="item in formSlot" :key="item.key" #[item.key]="scope">
          <slot :name="`form-${item.slotName}`" v-bind="scope"></slot>
        </template>
      </ArtForm>
      <template #footer>
        <ElButton @click="cancelVisible">取消</ElButton>
        <ElButton type="primary" @click="submitAdd">确定</ElButton>
      </template>
    </ElDialog>
  </div>
</template>
<script setup lang="ts">
import type { ColumnOption } from '../../../types'
import type { FormItem, FormProps } from '../../core/forms/art-form/index.vue'
import type { SearchBarProps } from '@/components/core/forms/art-search-bar/index.vue'
import type { TableProps } from 'element-plus'
import type { FormRules } from 'element-plus'
import type { Ref } from 'vue'

import { Plus } from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'

import { useTableColumns } from '../../../hooks'
import ArtForm from '../../core/forms/art-form/index.vue'
import ArtSearchBar from '../../core/forms/art-search-bar/index.vue'
import ArtTableBar from '../../core/tables/art-table-header/index.vue'
import ArtTable from '../../core/tables/art-table/index.vue'

defineOptions({ name: 'ArtGrid' })

type EditConfig = FormProps & {
  rules: FormRules
}

export interface ArtCrudConfig<T = Record<string, any>> {
  data?: T[]
  // 表格配置项
  crudConfig?: Partial<TableProps<Record<string, any>>> & {
    showPagination?: boolean /** 是否显示分页 */
    infiniteScrollFn?: (isClear: boolean) => Promise<any>
    infiniteScrollDisabled?: boolean
  }
  // 排除的参数,例如不需要分页时
  excludeParams?: string[]
  // API 相关
  api: {
    list?: (params: any) => Promise<any>
    add?: (data: any) => Promise<any>
    update?: (data: any) => Promise<any>
    delete?: (data: any) => Promise<any>
    count?: () => Promise<any>
    batchDelete?: (ids: (string | number)[]) => Promise<any>
  }

  // 搜索表单
  searchForm?: Record<string, any>
  // 搜索配置
  searchConfig?: SearchBarProps

  // 表格列配置
  columns: ColumnOption<T>[]
  // 汇总列配置
  summaryColumns?: ColumnOption<T>[]

  // editConfig 编辑表单配置
  editConfig?: EditConfig

  // 按钮操作配置
  btnConfig?: {
    // 是否显示导出当前页按钮
    showExportCurrent?: boolean | Ref<boolean>
    // 是否显示导出所有数据按钮
    showExportAll?: boolean | Ref<boolean>
    // 新增操作
    showAddOperation?: () => boolean | Ref<boolean>
    // 删除操作
    showDeleteOperation?: (data: any) => boolean | Ref<boolean>
    // 编辑操作
    showEditOperation?: (data: any) => boolean | Ref<boolean>
  }
}
export interface CrudProps<T = Record<string, any>> {
  config: ArtCrudConfig<T>
}

const props = withDefaults(defineProps<CrudProps>(), {
  config: () => ({
    data: [],
    url: '',
    crudConfig: {
      showPagination: true
    },
    api: {},
    searchForm: {},
    searchConfig: {
      items: []
    },
    columns: [],
    editConfig: {
      items: [],
      rules: {}
    },
    summaryColumns: [],
    btnConfig: {
      showExportCurrent: false,
      showExportAll: false,
      showAddOperation: () => true,
      showDeleteOperation: () => true,
      showEditOperation: () => true
    }
  })
})

const formRef = useTemplateRef<typeof ArtForm>('formRef')
const tableRef = useTemplateRef<typeof ArtTable>('tableRef')

const searchForm = defineModel<Record<string, any>>('search', {
  default: () => ({})
})
const editForm = defineModel<Record<string, any>>('editForm', {
  default: () => ({})
})

const editConfig = computed<EditConfig>(
  () => props.config.editConfig || { items: [], rules: {} }
)
const formItems = computed(() => editConfig.value?.items || [])
const formRules = computed(() => editConfig.value?.rules || {})
const api = computed(() => props.config.api || {})
const btnConfig = computed(() =>
  Object.assign(
    {
      showExportCurrent: false,
      showExportAll: false,
      showAddOperation: true,
      showDeleteOperation: () => true,
      showEditOperation: () => true
    },
    props.config.btnConfig || {}
  )
)
// ------------编辑相关-----------------
type FormSlot = { key: string; slotName: string }[]
const formSlot = computed(() => {
  const arr: FormSlot = []
  formItems.value?.forEach((item) => {
    if (item.useSlot && item.slotName) {
      arr.push({ key: item.key, slotName: item.slotName })
    }
  })
  return arr
})

// ------------search相关--------------
const showSearch = computed(
  () =>
    props.config.searchConfig &&
    props.config.searchConfig?.items.length > 0 &&
    showSearchBar.value
)
const showSearchBar = ref<boolean>(
  !!(props.config.searchConfig && props.config.searchConfig?.items.length > 0)
)
const searchConfig = computed(() => props.config.searchConfig || { items: [] })
// 插槽处理
const searchSlots = computed(() => {
  if (!showSearch.value) return {}
  return (
    searchConfig.value?.items?.reduce((slots: Record<string, any>, item) => {
      if (item.slots) {
        Object.assign(slots, item.slots)
      }
      return slots
    }, {}) || {}
  )
})

// 根据搜索条件进行搜索
const handleSearch = () => {
  pagination.value.current = 1
  emit('search')
  if (props.config.crudConfig?.infiniteScrollFn) {
    props.config.crudConfig.infiniteScrollFn(true)
  } else {
    getData()
  }
}
// 清空数据
const resetSearchParams = async () => {
  pagination.value.current = 1
  emit('reset-search')
  await nextTick()
  if (props.config.crudConfig?.infiniteScrollFn) {
    props.config.crudConfig.infiniteScrollFn(true)
  } else {
    getData()
  }
}

// ---------表格----------------
const { columns = [] as ColumnOption[], columnChecks } = useTableColumns<
  Record<string, any>
>(() => props.config.columns)
const data = ref<Row[]>([])
const loading = ref(false)
const pagination = ref({ total: 0, size: 10, current: 1 })
// 获取数据
const getData = async () => {
  loading.value = true

  if (props.config.api?.list) {
    const response = await props.config.api.list({
      ...searchForm.value,
      pageNum: pagination.value.current,
      pageSize: pagination.value.size
    })
    if (Array.isArray(response)) {
      data.value = response
    } else {
      data.value = response.rows || []
      pagination.value.total = response.total
    }
  } else {
    data.value = props.config.data || []
    pagination.value.total = Array.isArray(props.config.data)
      ? props.config.data.length
      : 0
  }

  loading.value = false
}

onMounted(() => {
  getData()
})

const handleSizeChange = (size: number) => {
  pagination.value.size = size
  getData()
}

const handleCurrentChange = (current: number) => {
  pagination.value.current = current
  getData()
}

// ---------表格操作栏----------------

const tableSlotArr = computed(() => {
  const arr: string[] = []
  props.config.columns.forEach((item) => {
    if (item.prop !== 'operation') {
      if (item.useSlot && item.slotName) {
        arr.push(item.slotName)
      }
      if (item.useHeaderSlot && item.headerSlotName) {
        arr.push(item.headerSlotName)
      }
    }
  })
  return arr
})

const editVisible = ref<boolean>(false)
const editType = ref<'add' | 'edit'>('add')
// 新增操作
function addHandle() {
  if (formItems.value) {
    editVisible.value = true
    editType.value = 'add'
  }
  emit('add-row')
}

// 关闭弹窗
function cancelVisible() {
  // 关闭弹窗
  editVisible.value = false
  // 重置类型
  editType.value = 'add'
  // 重置表单数据
  editForm.value = {}
  // 重置表单验证
  formRef.value?.reset()

  emit('close-edit')
}

// 提交表单
function submitAdd() {
  if (formRef.value) {
    formRef.value.validate().then(async (valid: boolean) => {
      if (valid) {
        if (editType.value === 'add') {
          await api.value?.add?.(editForm.value)
        } else {
          await api.value?.update?.(editForm.value)
        }
        ElMessage({
          type: 'success',
          message: `${editType.value === 'add' ? '新增' : '修改'}成功！`
        })
        cancelVisible()
        getData()
      }
    })
  }
}
function openAdd(data?: Record<string, any>) {
  editVisible.value = true
  editType.value = 'add'
}

function openEdit(data?: Record<string, any>) {
  editVisible.value = true
  editType.value = 'edit'
  editForm.value = { ...data }
}

function editRow(data: Record<string, any>) {
  openEdit(data)
  emit('edit-row', data)
}
function deleteRow(data: Record<string, any>) {
  console.log('删除行:', data)
  ElMessageBox.confirm('确定要删除这条数据吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await api.value?.delete?.(data)
    ElMessage({
      type: 'success',
      message: '删除成功！'
    })
    getData()
  })
}
// ---------表格----------------

// 使用泛型CrudProps来获取正确的类型
type Row = typeof props extends ArtCrudConfig<infer U> ? U : Record<string, any>

const getTableRef = () => {
  return {
    artTable: tableRef.value
  }
}
const handleRefresh = () => {
  getData()
}

const selectedRows = ref<Row>([])

const getSelectionData = () => {
  return selectedRows.value
}
const handleSelectionChange = (selection: Row): void => {
  selectedRows.value = selection
  console.log('选中行数据:', selectedRows.value)
  emit('selection-change', selectedRows.value)
}

const setLoadTableData = (dataList: Row[]) => {
  data.value = dataList
}

defineExpose({
  openAdd,
  openEdit,
  getTableRef,
  handleRefresh,
  getSelectionData,
  setLoadTableData,
  addHandle,
  editType,
  data
})
const emit = defineEmits([
  'edit-row',
  'add-row',
  'close-edit',
  'reset-search',
  'selection-change',
  'search'
])
</script>

<style scoped lang="scss"></style>
