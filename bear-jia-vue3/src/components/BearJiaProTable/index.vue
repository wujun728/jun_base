<template>
  <div ref="tableContainerRef" class="bearjia-pro-table-container">
    <PageContainer>
      <!-- 1. 查询表单区 -->
      <template #search>
        <SearchForm
            v-if="props.searchFields && props.searchFields.length > 0"
            :model-value="searchFormData"
            :fields="props.searchFields"
            @update:modelValue="handleSearchFormUpdate"
            @reset="handleReset"
            @search="handleSearch"
        />
      </template>

      <!-- 2. 操作按钮区 -->
      <template #actions>
        <div class="table-actions-wrapper" v-if="props.showActions" >
          <div class="actions-left">
            <slot
              name="actions"
              :selectedRowKeys="tableState.selectedRowKeys"
              :selectedRows="tableState.selectedRows || []"
              :delete="handleDelete"
              :refresh="enhancedQueryTableData"
              :export="handleExport"
              :loading="tableState.loading"
              :error="errorState"
            ></slot>
          </div>

          <div class="actions-right">
            <TableToolbar
              :columns="props.columns"
              :selected-columns="selectedColumnKeys"
              :loading="tableState.loading"
              :toolbar-config="props.toolbarConfig"
              :export-config="props.exportConfig"
              :import-config="props.importConfig"
              @refresh="enhancedQueryTableData"
              @columnSettingsChange="handleColumnSettingsChange"
              @fullscreen="handleFullscreen"
              @export="handleExport"
              @import="handleImport"
              @reset="handleTableReset"
            />
          </div>
        </div>
      </template>

      <!-- 3. 表格区 -->
      <a-table
          ref="virtualScrollState.containerRef"
          :bordered="tableConfigStore.bordered"
          :class="[tableConfigStore.stripe ? 'bearjia-table-striped' : '']"
          :columns="displayColumns"
          :data-source="finalDataSource"
          :loading="tableState.loading"
          :pagination="pagination"
          :row-selection="finalRowSelectionConfig"
          :rowKey="props.rowKey"
          :scroll="computedScrollConfig"
          :size="tableConfigStore.size"
          :show-header="tableConfigStore.showHeader"
          :expandable="props.expandable"
          :default-expand-all-rows="props.isTreeTable"
          :indent-size="props.isTreeTable ? 20 : undefined"
          v-bind="$attrs"
          @change="handleTableChange"
          @resizeColumn="handleResizeColumn"
      >
        <template #bodyCell="{ index, column, record }">
          <slot :column="column" :index="index" :record="record" name="bodyCell">
            <!-- 提供默认渲染，支持过长内容的 tooltip -->
            <a-tooltip
              v-if="record[column.dataIndex]"
              :title="String(record[column.dataIndex])"
              placement="topLeft"
            >
              <div class="table-cell-content">
                {{ record[column.dataIndex] }}
              </div>
            </a-tooltip>
            <span v-else>{{ record[column.dataIndex] }}</span>
          </slot>
        </template>

        <!-- 支持可展开行的插槽 -->
        <template v-if="props.expandable && props.expandable.expandedRowRender" #expandedRowRender="{ record }">
          <component :is="props.expandable.expandedRowRender(record)" />
        </template>

        <!-- 增强的空状态处理 -->
        <template #emptyText>
          <a-empty
            v-if="!errorState"
            description="暂无数据"
          />
          <a-result
            v-else
            status="error"
            title="加载失败"
            :sub-title="errorState"
          >
            <template #extra>
              <a-button type="primary" @click="handleRetry">点击重试</a-button>
            </template>
          </a-result>
        </template>
      </a-table>
    </PageContainer>
  </div>
</template>

<script setup>
import {computed, ref, onMounted, watch} from 'vue';
import {useRoute} from 'vue-router';
import {useTable} from '@/composables/useTable';
import {useTableConfigStore} from '@/stores/tableConfig';
import {useResizeObserver} from '@vueuse/core';
import {useVirtualScroll, getItemHeightBySize} from '@/composables/useVirtualScroll';

import PageContainer from '@/components/PageContainer/index.vue';
import SearchForm from '@/components/SearchForm/index.vue';
import TableToolbar from './TableToolbar.vue';

// 定义组件的 Props
const props = defineProps({
  // API 配置 - 应包含 list, delete 等方法
  api: {type: Object, required: true},
  // 表格列配置
  columns: {type: Array, required: true},
  // 行键
  rowKey: {type: String, required: true},
  // 搜索字段配置
  searchFields: {type: Array, default: () => []},
  // 初始搜索参数
  initialSearchParams: {type: Object, default: () => ({})},
  // 导出配置 { url: '...', fileName: '...' }
  exportConfig: {type: Object, default: null},
  // 是否显示行选择
  showSelection: {type: Boolean, default: true},
  // 是否显示操作区域
  showActions: {type: Boolean, default: true},
  // 树形表格支持
  isTreeTable: {type: Boolean, default: false},
  // 可展开行支持
  expandable: {type: Object, default: null},
  // 自定义行选择配置
  rowSelection: {type: Object, default: null},
  // 排序配置
  sortable: {type: Boolean, default: true},
  // 自动横向滚动配置
  autoHorizontalScroll: {type: Boolean, default: false},
  // 是否启用虚拟滚动
  virtualScroll: {type: Boolean, default: false},
  // 虚拟滚动配置
  virtualScrollConfig: {
    type: Object,
    default: () => ({
      threshold: 100,  // 数据量超过100条启用
      buffer: 5,       // 缓冲区行数
    })
  },
  // 是否显示列设置
  showColumnSettings: {type: Boolean, default: true},
  // 工具栏配置
  toolbarConfig: {
    type: Object,
    default: () => ({
      refresh: true,        // 刷新按钮
      density: true,        // 密度设置
      fullscreen: true,     // 全屏按钮
      columnSettings: true, // 列设置
      settings: true        // 更多设置
    })
  },
  // 导入配置
  importConfig: {
    type: Object,
    default: () => ({
      enabled: false,
      url: '',
      accept: '.xlsx,.xls,.csv'
    })
  },
  // 表格唯一标识，用于持久化列设置和列宽（不传则自动使用路由路径）
  persistenceId: { type: String, default: '' },
});

const route = useRoute();
const tableConfigStore = useTableConfigStore();

// 计算实际使用的 persistenceId：优先使用传入的值，否则使用当前路由路径
const effectivePersistenceId = computed(() => {
  if (props.persistenceId) {
    return props.persistenceId;
  }
  // 使用路由路径作为默认标识符，将 / 替换为 - 并去掉开头的 -
  return route.path.replace(/\//g, '-').replace(/^-/, '') || 'default';
});

// 容器引用和宽度监听
const tableContainerRef = ref(null);
const containerWidth = ref(0);

// 错误状态
const errorState = ref(null);

// 列设置相关状态
const columnSettingsVisible = ref(false);
const selectedColumnKeys = ref([]);

// 监听容器宽度变化
onMounted(() => {
  if (tableContainerRef.value) {
    useResizeObserver(tableContainerRef.value, (entries) => {
      const entry = entries[0];
      containerWidth.value = entry.contentRect.width;
    });
  }

  // 组件挂载后查询一次数据
  enhancedQueryTableData();
});

// 使用 useTable Hook
const {
  searchFormData,
  tableState,
  pagination,
  rowSelection,
  queryTableData,
  handleSearch: baseHandleSearch,
  handleReset: baseHandleReset,
  handleTableChange: baseHandleTableChange,
  handleDelete,
  handleExport,
} = useTable({
  api: {
    list: props.api.list,
    delete: props.api.delete,
    exportUrl: props.exportConfig?.url,
    processListData: props.api.processListData,
  },
  columns: props.columns,
  initialSearchParams: props.initialSearchParams,
  rowKey: props.rowKey,
  exportFileName: props.exportConfig?.fileName,
  isTreeTable: props.isTreeTable,
  sortable: props.sortable,
});

// 虚拟滚动集成
const virtualScrollState = useVirtualScroll({
  dataSource: computed(() => tableState.dataSource),
  itemHeight: computed(() => getItemHeightBySize(tableConfigStore.size)),
  buffer: props.virtualScrollConfig.buffer,
  containerHeight: computed(() => tableConfigStore.tableHeight || 600),
  enabled: props.virtualScroll,
  threshold: props.virtualScrollConfig.threshold,
});

// 使用虚拟滚动时的数据源
const finalDataSource = computed(() => {
  if (props.virtualScroll && virtualScrollState.isVirtualScrollEnabled.value) {
    return virtualScrollState.visibleData.value;
  }
  return tableState.dataSource;
});

const getTableErrorMessage = (error) => {
  return error?.message || '数据加载失败，请重试';
};

const runTableAction = async (action, errorPrefix = '表格操作失败') => {
  try {
    errorState.value = null;
    return await action();
  } catch (error) {
    console.error(`${errorPrefix}:`, error);
    errorState.value = getTableErrorMessage(error);
    return undefined;
  }
};

const enhancedQueryTableData = () => runTableAction(queryTableData, '表格数据加载失败');

const handleSearch = () => runTableAction(baseHandleSearch, '表格搜索失败');

const handleReset = () => runTableAction(baseHandleReset, '表格重置失败');

const handleTableChange = (...args) =>
  runTableAction(() => baseHandleTableChange(...args), '表格分页或排序失败');

const handleSearchFormUpdate = (nextValue) => {
  Object.keys(searchFormData).forEach((key) => {
    delete searchFormData[key];
  });
  Object.assign(searchFormData, nextValue || {});
};

// 重试方法
const handleRetry = () => {
  enhancedQueryTableData();
};

// 列宽状态管理
const columnWidths = ref({});

// 从 localStorage 恢复列宽
const restoreColumnWidths = () => {
  const key = `table-${effectivePersistenceId.value}-column-widths`;
  const saved = localStorage.getItem(key);
  if (saved) {
    try {
      columnWidths.value = JSON.parse(saved);
    } catch (error) {
      console.warn('恢复列宽设置失败:', error);
    }
  }
};

// 防抖保存函数
let saveTimer = null;
const debouncedSave = (widths) => {
  if (saveTimer) {
    clearTimeout(saveTimer);
  }
  saveTimer = setTimeout(() => {
    const key = `table-${effectivePersistenceId.value}-column-widths`;
    localStorage.setItem(key, JSON.stringify(widths));
  }, 300); // 300ms 防抖延迟
};

// 处理列宽调整 - 优化版本
const handleResizeColumn = (width, column) => {
  // 立即更新状态以保持UI响应
  columnWidths.value = {
    ...columnWidths.value,
    [column.dataIndex]: width
  };

  // 防抖保存到 localStorage
  debouncedSave(columnWidths.value);
};

// 计算显示的列（支持列设置和列宽调整）
const displayColumns = computed(() => {
  let columns = tableState.columns;

  // 根据列设置过滤列
  if (props.showColumnSettings) {
    columns = columns.filter(column =>
      selectedColumnKeys.value.includes(column.dataIndex || column.key)
    );
  }

  // 应用保存的列宽和 resizable 属性
  columns = columns.map(column => {
    const newColumn = { ...column };

    // 应用保存的列宽
    if (columnWidths.value[column.dataIndex]) {
      newColumn.width = columnWidths.value[column.dataIndex];
    }

    // 根据全局配置添加 resizable 属性
    if (tableConfigStore.resizable) {
      newColumn.resizable = column.resizable !== false; // 允许单独列禁用 resizable
      // 当启用 resizable 时，确保列有 width 值（Ant Design Vue 要求）
      // 操作列（dataIndex 为 'action' 或没有 dataIndex）通常不需要 resizable
      if (newColumn.resizable && newColumn.width === undefined) {
        newColumn.width = 120; // 默认列宽 120px
      }
    }

    return newColumn;
  });

  return columns;
});

// 计算滚动配置
const computedScrollConfig = computed(() => {
  const scrollConfig = {};

  // 垂直滚动配置
  if (tableConfigStore.fixHeader) {
    scrollConfig.y = tableConfigStore.tableHeight;
  }

  // 虚拟滚动配置
  if (props.virtualScroll) {
    if (scrollConfig.y) {
      scrollConfig.scrollToFirstRowOnChange = true;
    }
    // 虚拟滚动需要固定的容器高度
    if (virtualScrollState.isVirtualScrollEnabled.value) {
      scrollConfig.y = scrollConfig.y || 600;
    }
  }

  // 检查是否有固定列
  const hasFixedColumn = displayColumns.value.some(col => col.fixed === 'left' || col.fixed === 'right');

  // 水平滚动配置 - 智能计算是否需要横向滚动
  // 默认启用横向滚动计算，以确保列宽超出时能正常显示滚动条
  const totalWidth = displayColumns.value.reduce((sum, col) => {
    return sum + (Number(col.width) || 120); // 默认列宽120px
  }, 0);

  // 使用实际容器宽度进行比较，如果容器宽度未获取到则使用默认值
  const compareWidth = containerWidth.value || 1200;

  // 强制设置 scroll.x 为 'max-content' 或计算出的总宽度
  // 这样 Ant Design Table 内部会使用 table-layout: fixed 并允许横向滚动
  // 强制启用横向滚动，使用 'max-content' 让表格根据内容伸展并触发滚动条
  // 这是解决 Ant Design Table 在 Flex 容器中不触发滚动的最稳妥方案
  scrollConfig.x = totalWidth > compareWidth ? totalWidth : 'max-content';

  return scrollConfig;
});

// 计算最终的 rowSelection 配置
const finalRowSelectionConfig = computed(() => {
  // 如果传入了自定义的 rowSelection 配置，优先使用
  if (props.rowSelection) {
    return {
      ...rowSelection.value,
      ...props.rowSelection,
    };
  }

  // 否则使用默认配置
  if (!props.showSelection || !tableConfigStore.rowSelection) return undefined;
  return rowSelection.value;
});

const showColumnSettings = props.showColumnSettings;

// 从 localStorage 恢复列设置
const restoreColumnSettings = () => {
  const key = `table-${effectivePersistenceId.value}-column-settings`;
  const saved = localStorage.getItem(key);
  if (saved) {
    try {
      const savedKeys = JSON.parse(saved);
      // 确保保存的列键在当前列中存在（使用 dataIndex 或 key 作为标识符）
      const validKeys = savedKeys.filter(savedKey =>
        props.columns.some(col => (col.dataIndex || col.key) === savedKey)
      );
      if (validKeys.length > 0) {
        selectedColumnKeys.value = validKeys;
      }
    } catch (error) {
      console.warn('恢复列设置失败:', error);
    }
  }
};

// 工具栏事件处理
const handleColumnSettingsChange = (selectedKeys) => {
  selectedColumnKeys.value = selectedKeys;
  const key = `table-${effectivePersistenceId.value}-column-settings`;
  localStorage.setItem(key, JSON.stringify(selectedKeys));
};

const handleFullscreen = (isFullscreen) => {
  // 可以在这里处理全屏逻辑
  console.log('全屏状态:', isFullscreen);
};

const handleImport = () => {
  // 处理导入逻辑
  console.log('导入数据');
};

const handleTableReset = () => {
  // 重置表格状态（使用 dataIndex 或 key 作为标识符）
  selectedColumnKeys.value = props.columns.map(col => col.dataIndex || col.key);
  const settingsKey = `table-${effectivePersistenceId.value}-column-settings`;
  localStorage.removeItem(settingsKey);
  // 重置列宽
  columnWidths.value = {};
  const widthsKey = `table-${effectivePersistenceId.value}-column-widths`;
  localStorage.removeItem(widthsKey);
  enhancedQueryTableData();
};

// 监听 columns 变化，重新初始化列设置
watch(() => props.columns, () => {
  // 使用 dataIndex 或 key 作为列的唯一标识符，确保操作列等没有 dataIndex 的列也能被正确处理
  selectedColumnKeys.value = props.columns.map(col => col.dataIndex || col.key);
  restoreColumnSettings();
  restoreColumnWidths();
}, { immediate: true });

// 使用增强的查询方法替换原始方法
// 移除 immediate: true,避免重复调用(已在 onMounted 中调用)
watch(() => [props.api, props.initialSearchParams], () => {
  enhancedQueryTableData();
});

// 暴露方法给父组件
defineExpose({
  refresh: enhancedQueryTableData,
  delete: handleDelete,
  export: handleExport,
  searchFormData: searchFormData,
  tableState: tableState,
  showColumnSettings: showColumnSettings,
  retry: handleRetry,
  handleFullscreen,
  handleImport,
  handleTableReset,
  // 虚拟滚动相关方法
  virtualScroll: {
    scrollToIndex: virtualScrollState.scrollToIndex,
    scrollToTop: virtualScrollState.scrollToTop,
    scrollToBottom: virtualScrollState.scrollToBottom,
    stats: virtualScrollState.performanceStats,
  },
});
</script>

<style scoped lang="less">
.table-actions-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: transparent;
  background: transparent;
}

.actions-left {
  display: flex;
  align-items: center;
  gap: 8px;
  background-color: transparent;
  background: transparent;
}

.actions-right {
  display: flex;
  align-items: center;
  background-color: transparent;
  background: transparent;
}

.column-settings {
  max-height: 400px;
  overflow-y: auto;
}

.column-list {
  width: 100%;
}

.column-item {
  display: block;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}

.column-item:last-child {
  border-bottom: none;
}

/* 表格容器样式 */
.bearjia-pro-table-container {
  width: 100%;
  max-width: 100%;
  overflow: hidden;
}

.ant-table-wrapper {
  position: relative;
  width: 100%;
  max-width: 100%;
}

/* 确保表格内容区域支持横向滚动 */
:deep(.ant-table) {
  width: 100%;
}

:deep(.ant-table-container) {
  width: 100%;
}

/* 应用全局表头样式设置 */
:deep(.ant-table-thead > tr > th) {
  background-color: v-bind('tableConfigStore.headerBgColor') !important;
  font-weight: v-bind('tableConfigStore.headerBold ? "bold" : "normal"') !important;
}

/* 斑马条纹样式 */
.bearjia-table-striped :deep(.ant-table-tbody > tr.ant-table-row:nth-child(2n) td) {
  background-color: #fafafa !important;
}

/* 暗色主题下的斑马条纹 */
:global(.dark-theme) .bearjia-table-striped :deep(.ant-table-tbody > tr.ant-table-row:nth-child(2n) td) {
  background-color: rgba(255, 255, 255, 0.02) !important;
}

/* 错误状态样式 */
.ant-result {
  padding: 12px 0;
}

.table-actions-wrapper {
  margin-bottom: 12px;
}

/* 优化列宽调整体验 - 使拖动更丝滑 */
:deep(.ant-table) {
  /* 禁用列的过渡动画，避免拖动时的延迟感 */
  .ant-table-thead > tr > th,
  .ant-table-tbody > tr > td {
    transition: none !important;
  }

  /* 优化表头单元格样式 */
  .ant-table-thead > tr > th {
    position: relative;
    user-select: none;
  }

  /* 调整手柄的样式优化 - 移除 z-index 避免穿透固定列 */
  .ant-table-thead > tr > th .ant-table-column-sorter {
    margin-left: 4px;
    position: relative;
    /* 不设置 z-index，避免穿透固定列 */
  }

  /* 扩大调整手柄的感应区域 - 关键优化 */
  .ant-table-thead > tr > th.ant-table-cell::before {
    content: '';
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    width: 4px; /* 进一步缩小感应区域，仅保留边缘 */
    cursor: col-resize;
    z-index: 1; /* 降低层级，确保不遮挡文字和排序图标 */
  }

  /* 最后一列不显示调整手柄 */
  .ant-table-thead > tr > th:last-child.ant-table-cell::before {
    display: none;
  }
}

/* 优化调整手柄的视觉效果 */
:deep(.ant-table-thead > tr > th::after) {
  /* Ant Design Vue 使用伪元素作为调整手柄 */
  transition: background-color 0.2s ease;
  width: 1px; /* 视觉线条 */
}

/* 悬停时的视觉反馈 */
:deep(.ant-table-thead > tr > th:hover::before) {
  background: linear-gradient(90deg,
    transparent 0%,
    rgba(24, 144, 255, 0.1) 25%,
    rgba(24, 144, 255, 0.2) 50%,
    rgba(24, 144, 255, 0.1) 75%,
    transparent 100%);
}

:deep(.ant-table-thead > tr > th:hover::after) {
  background-color: rgba(24, 144, 255, 0.5) !important;
  width: 2px; /* 悬停时加粗 */
}

/* 拖动时的视觉反馈 */
:deep(.ant-table-thead > tr > th.ant-table-column-has-sorters) {
  cursor: pointer;
}

/* 非固定列的表头单元格需要 overflow: hidden 来裁剪内容，防止穿透到固定列 */
:deep(.ant-table-thead > tr > th:not(.ant-table-cell-fix-left):not(.ant-table-cell-fix-right)) {
  overflow: hidden;
}

/* 非固定列的表体单元格也需要 overflow: hidden */
:deep(.ant-table-tbody > tr > td:not(.ant-table-cell-fix-left):not(.ant-table-cell-fix-right)) {
  overflow: hidden;
}

/* 优化固定列的 z-index，确保固定列能遮挡其他列的内容（包括排序图标） */
:deep(.ant-table-thead > tr > th.ant-table-cell-fix-left,
       .ant-table-thead > tr > th.ant-table-cell-fix-right) {
  z-index: 10 !important;
  border-left: none !important;
  /* 创建新的层叠上下文，确保内部元素不会被其他列的内容穿透 */
  isolation: isolate;
}

/* 固定列表体单元格也需要足够高的 z-index */
:deep(.ant-table-tbody > tr > td.ant-table-cell-fix-left,
       .ant-table-tbody > tr > td.ant-table-cell-fix-right) {
  z-index: 10 !important;
  /* 创建新的层叠上下文 */
  isolation: isolate;
}

/* 针对右侧固定列的特殊处理，消除其左侧的阴影或边框线 */
:deep(.ant-table-cell-fix-right-first::after),
:deep(.ant-table-cell-fix-left-last::after) {
  box-shadow: none !important;
  display: none !important;
}

/* 固定列保持正常背景,避免视觉断层 */
:deep(.ant-table-cell-fix-right),
:deep(.ant-table-cell-fix-left) {
  /* 亮色主题下使用白色背景，确保固定列不透明 */
  background: #ffffff !important;
}

/* 表头固定列背景 */
:deep(.ant-table-thead > tr > th.ant-table-cell-fix-right),
:deep(.ant-table-thead > tr > th.ant-table-cell-fix-left) {
  background: #fafafa !important;
}

/* 斑马条纹行的固定列背景 */
.bearjia-table-striped :deep(.ant-table-tbody > tr.ant-table-row:nth-child(2n) > td.ant-table-cell-fix-right),
.bearjia-table-striped :deep(.ant-table-tbody > tr.ant-table-row:nth-child(2n) > td.ant-table-cell-fix-left) {
  background-color: #fafafa !important;
}

/* 暗色主题下的固定列适配 */
:global(.dark-theme) {
  :deep(.ant-table-cell-fix-right),
  :deep(.ant-table-cell-fix-left) {
    background-color: var(--component-background, #141414) !important;
  }

  :deep(.ant-table-thead > tr > th.ant-table-cell-fix-right),
  :deep(.ant-table-thead > tr > th.ant-table-cell-fix-left) {
    background-color: var(--table-header-bg, #1d1d1d) !important;
  }
}

/* 暗色主题下斑马条纹行的固定列背景 */
:global(.dark-theme) .bearjia-table-striped :deep(.ant-table-tbody > tr.ant-table-row:nth-child(2n) > td.ant-table-cell-fix-right),
:global(.dark-theme) .bearjia-table-striped :deep(.ant-table-tbody > tr.ant-table-row:nth-child(2n) > td.ant-table-cell-fix-left) {
  background-color: rgba(255, 255, 255, 0.02) !important;
}

/* 表格拖拽时禁用文本选择 */
:deep(.ant-table.resizing) {
  user-select: none;
  cursor: col-resize !important;

  * {
    cursor: col-resize !important;
  }
}

/* 表格单元格内容样式 - 支持文本溢出显示 */
.table-cell-content {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 100%;
  display: block;
}

/* 优化表格滚动条样式 */
:deep(.ant-table-body)::-webkit-scrollbar,
:deep(.ant-table-header)::-webkit-scrollbar,
:deep(.ant-table-content)::-webkit-scrollbar {
  height: 8px !important;
  width: 8px !important;
}

:deep(.ant-table-body)::-webkit-scrollbar-thumb,
:deep(.ant-table-header)::-webkit-scrollbar-thumb,
:deep(.ant-table-content)::-webkit-scrollbar-thumb {
  background-color: rgba(148, 163, 184, 0.45);
}

:global(.dark-theme) :deep(.ant-table-body)::-webkit-scrollbar-thumb,
:global(.dark-theme) :deep(.ant-table-header)::-webkit-scrollbar-thumb,
:global(.dark-theme) :deep(.ant-table-content)::-webkit-scrollbar-thumb {
  background-color: rgba(148, 163, 184, 0.45);
}

:deep(.ant-table-body)::-webkit-scrollbar-thumb:hover,
:deep(.ant-table-header)::-webkit-scrollbar-thumb:hover,
:deep(.ant-table-content)::-webkit-scrollbar-thumb:hover {
  background-color: rgba(100, 116, 139, 0.58);
}

:global(.dark-theme) :deep(.ant-table-body)::-webkit-scrollbar-thumb:hover,
:global(.dark-theme) :deep(.ant-table-header)::-webkit-scrollbar-thumb:hover,
:global(.dark-theme) :deep(.ant-table-content)::-webkit-scrollbar-thumb:hover {
  background-color: rgba(100, 116, 139, 0.58);
}

:deep(.ant-table-body)::-webkit-scrollbar-track,
:deep(.ant-table-header)::-webkit-scrollbar-track,
:deep(.ant-table-content)::-webkit-scrollbar-track {
  background-color: transparent;
}
</style>
