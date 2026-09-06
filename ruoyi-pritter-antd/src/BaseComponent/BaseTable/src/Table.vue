<template>
  <div class="baseTable">
    <div
      v-if="hasSlot($slots, ['handleLeft', 'handleRight', 'header'])"
      ref="headerRef"
      class="header"
    >
      <slot name="header">
        <div class="handle">
          <slot name="handleLeft" />
        </div>
        <div class="handleRight">
          <slot name="handleRight" />
        </div>
      </slot>
    </div>

    <a-table
      ref="tableRef"
      class="antTable"
      :columns="tableColumns"
      :data-source="dataList"
      :bordered="border"
      :pagination="false"
      :row-selection="rowSelection"
      :scroll="scrollConfig"
      :row-class-name="rowClassName"
      v-bind="mergedTableConfig"
      @change="onTableChange"
      @resizeColumn="handleResizeColumn"
    >
      <template #headerCell="{ column }">
        <slot
          v-if="column.slotName"
          :name="`${column.slotName}Header`"
          :backData="{ column: column.columnItem || column }"
        >
          {{ column.title }}
        </slot>
        <template v-else>
          {{ column.title }}
        </template>
      </template>

      <template #bodyCell="{ column, record, index }">
        <template v-if="column.key === '__index'">
          {{
            (paginationInfo.pageNum - 1) * paginationInfo.pageSize + index + 1
          }}
        </template>
        <slot
          v-else-if="column.slotName"
          :name="column.slotName"
          :backData="record"
          :currentItem="column.columnItem || column"
        >
          {{ record[column.dataIndex] }}
        </slot>
        <template v-else>
          {{ record[column.dataIndex] }}
        </template>
      </template>

      <template v-if="showExpand" #expandedRowRender="{ record }">
        <slot name="expand" :backData="record" />
      </template>
    </a-table>

    <div
      v-if="pagination"
      ref="footerRef"
      class="footer pagination-footer"
      :class="{ isSmall }"
    >
      <slot name="footer">
        <a-pagination
          :current="paginationInfo.pageNum"
          :page-size="paginationInfo.pageSize"
          :total="listCount"
          :page-size-options="['20', '50', '100', '200', '300']"
          :show-total="paginationOptions.showTotal"
          :show-size-changer="paginationOptions.showSizeChanger"
          :show-quick-jumper="paginationOptions.showQuickJumper"
          @change="handlePageChange"
          @show-size-change="handleSizeChange"
        />
      </slot>
    </div>
  </div>
</template>

<script setup>
import {
  applyColumnWidths,
  buildTableColumns,
  collectExpandableKeys,
  getColumnKey,
  getTableScrollX,
  hasSlot,
} from './utils/index.js'

const props = defineProps({
  border: {
    type: Boolean,
    default: false,
  },
  dataList: {
    type: Array,
    default: () => [],
  },
  tableItem: {
    type: Array,
    default: () => [],
  },
  tableListener: {
    type: Object,
    default: () => ({}),
  },
  showChoose: {
    type: Boolean,
    default: false,
  },
  showIndex: {
    type: Boolean,
    default: false,
  },
  pagination: {
    type: Boolean,
    default: true,
  },
  listCount: {
    type: Number,
    default: 0,
  },
  paginationInfo: {
    type: Object,
    default: () => ({ pageNum: 1, pageSize: 50 }),
  },
  tableConfig: {
    type: Object,
    default: () => ({}),
  },
  showExpand: {
    type: Boolean,
    default: false,
  },
  align: {
    type: String,
    default: 'left',
  },
  paginationLayout: {
    type: String,
    default: 'total, sizes, prev, pager, next, jumper',
  },
  hideItems: {
    type: [Array, Object],
    default: () => [],
  },
  maxTableHeight: {
    type: Number,
  },
  selectionConfig: {
    type: Object,
    default: () => ({}),
  },
})

const emit = defineEmits(['update:paginationInfo', 'sortChange'])
const headerRef = useTemplateRef('headerRef')
const tableRef = useTemplateRef('tableRef')
const footerRef = useTemplateRef('footerRef')
const expandedRowKeys = ref([])
const isSmall = window.isSmallScreen

let expandAll = false
const columnWidthMap = ref({})

const tableColumns = computed(() => {
  const columns = buildTableColumns(
    props.tableItem,
    props.hideItems,
    props.align,
    props.showIndex
  )
  return applyColumnWidths(columns, columnWidthMap.value)
})

const rowKey = computed(() => props.tableConfig.rowKey || 'id')

const hasTreeData = computed(() =>
  props.dataList.some(
    (row) => Array.isArray(row.children) && row.children.length
  )
)

const syncTreeExpandedKeys = () => {
  if (!hasTreeData.value) {
    return
  }
  expandedRowKeys.value = expandAll
    ? collectExpandableKeys(props.dataList, rowKey.value)
    : []
}

watch(
  () => props.dataList,
  () => {
    if (hasTreeData.value && props.tableConfig.defaultExpandAllRows) {
      expandAll = true
      syncTreeExpandedKeys()
    }
  },
  { immediate: true, deep: true }
)

const mergedTableConfig = computed(() => {
  const { rowClassName, expandable, scroll, ...rest } = props.tableConfig

  const config = {
    rowKey: rowKey.value,
    ...rest,
  }

  if (props.showExpand) {
    config.expandable = {
      ...(expandable || {}),
      expandedRowKeys: expandedRowKeys.value,
      onExpandedRowsChange: (keys) => {
        expandedRowKeys.value = keys
        expandable?.onExpandedRowsChange?.(keys)
      },
    }
  } else if (hasTreeData.value) {
    config.expandedRowKeys = expandedRowKeys.value
    config.onExpandedRowsChange = (keys) => {
      expandedRowKeys.value = keys
    }
  }

  return config
})

const rowSelection = computed(() => {
  if (!props.showChoose) return undefined

  const { onChange, ...rest } = props.selectionConfig

  return {
    ...rest,
    onChange: (selectedRowKeys, selectedRows) => {
      props.tableListener.selectionChange?.(selectedRows)
      onChange?.(selectedRowKeys, selectedRows)
    },
  }
})

const maxHeightComputed = computed(() => {
  let headerHeight = 0
  let footerHeight = 0
  if (footerRef.value?.clientHeight) {
    footerHeight = footerRef.value.clientHeight
  }
  if (headerRef.value) {
    headerHeight = headerRef.value.clientHeight
  }
  const antHeaderHeight =
    document.querySelector('.ant-table-header')?.clientHeight ?? 0
  if (props.maxTableHeight) {
    return props.maxTableHeight - headerHeight - footerHeight - antHeaderHeight
  }
  return (
    window.innerHeight - 260 - headerHeight - footerHeight - antHeaderHeight
  )
})

const scrollConfig = computed(() => {
  const y = maxHeightComputed.value > 300 ? maxHeightComputed.value : 300
  const { scroll = {} } = props.tableConfig
  const x =
    scroll.x ??
    getTableScrollX(props.tableItem, props.showIndex, columnWidthMap.value)
  return {
    ...scroll,
    y,
    ...(x ? { x } : {}),
  }
})

const rowClassName = computed(() => {
  const customRowClassName = props.tableConfig.rowClassName
  return (record, index) => {
    const stripeClass = index % 2 === 1 ? 'table-striped-row' : ''
    if (typeof customRowClassName === 'function') {
      return [customRowClassName(record, index), stripeClass]
        .filter(Boolean)
        .join(' ')
    }
    if (typeof customRowClassName === 'string') {
      return [customRowClassName, stripeClass].filter(Boolean).join(' ')
    }
    return stripeClass
  }
})

const paginationLayoutComputed = computed(() => {
  if (!isSmall) {
    return props.paginationLayout
  }
  return 'total, prev, pager, next'
})

const paginationOptions = computed(() => {
  const layout = paginationLayoutComputed.value
  return {
    showTotal: layout.includes('total')
      ? (total) => `共 ${total} 条`
      : undefined,
    showSizeChanger: layout.includes('sizes'),
    showQuickJumper: layout.includes('jumper'),
  }
})

const scrollToTop = () => {
  const tableEl = tableRef.value?.$el || tableRef.value
  const body = tableEl?.querySelector?.('.ant-table-body')
  body?.scrollTo?.(0, 0)
}

const handlePageChange = (pageNum, pageSize) => {
  scrollToTop()
  emit('update:paginationInfo', {
    ...props.paginationInfo,
    pageNum,
    pageSize,
  })
}

const handleSizeChange = (_, pageSize) => {
  scrollToTop()
  emit('update:paginationInfo', {
    ...props.paginationInfo,
    pageSize,
  })
}

const onTableChange = (_pagination, _filters, sorter) => {
  scrollToTop()
  if (sorter && (sorter.order || Array.isArray(sorter))) {
    emit('sortChange', sorter)
  }
  props.tableListener.change?.(_pagination, _filters, sorter)
}

const unFoldAll = (...arg) => {
  if (arg.length) {
    expandAll = arg[0]
  } else {
    expandAll = !expandAll
  }
  if (hasTreeData.value) {
    syncTreeExpandedKeys()
    return
  }
  expandedRowKeys.value = expandAll
    ? collectExpandableKeys(props.dataList, rowKey.value)
    : []
}

const handleResizeColumn = (w, col) => {
  const key = getColumnKey(col)
  if (!key) return
  columnWidthMap.value = {
    ...columnWidthMap.value,
    [key]: w,
  }
}

defineExpose({
  tableRef,
  unFoldAll,
})
</script>

<style scoped lang="scss">
.header {
  position: relative;
  box-sizing: border-box;
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  max-width: 100%;
  background-color: var(--ba-bg-color-overlay);
  border-bottom: 1px solid var(--ba-border-color);
  font-size: 14px;
  .table-header-operate-text {
    margin-left: 6px;
  }
}

.footer {
  background-color: var(--ba-bg-color-overlay);

  :deep(.ant-pagination) {
    display: flex;
    flex-wrap: wrap;
    flex-direction: row;
    justify-content: flex-end;
    box-sizing: border-box;
    padding: 13px 15px;
  }
}

.isSmall {
  :deep(.ant-pagination) {
    padding: 10px;
  }
}
</style>
