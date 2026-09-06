import { computed, reactive, watch } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useTableConfigStore } from '@/stores/tableConfig';
// 假设您有这样的工具库
import BearJiaUtil from '@/utils/BearJiaUtil';
import dayjs from 'dayjs';

/**
 * 通用表格页面逻辑 Hook
 * @param {object} options 配置项
 * @param {object} options.api API接口对象，必须包含 list, delete(可选), exportUrl(可选)
 * @param {Array} options.columns - 表格的列定义
 * @param {object} options.initialSearchParams - 初始的查询表单参数
 * @param {string} [options.rowKey='id'] - 表格行的 Key
 * @param {string} [options.exportFileName='导出数据'] - 默认的导出文件名
 */
export function useTable(options) {
  const {
    api,
    columns,
    initialSearchParams,
    exportFileName = '导出数据',
    isTreeTable = false,
    sortable = true,
    showError = true,
  } = options;

  // 实例化 Pinia Store
  const tableConfigStore = useTableConfigStore();

  // 搜索表单数据
  const searchFormData = reactive({ ...initialSearchParams });

  // 查询参数（包含分页和排序）
  const queryParams = reactive({
    pageNum: 1,
    pageSize: tableConfigStore.pageSize, // 从 Store 获取初始 pageSize
    orderByColumn: null, // 排序字段
    isAsc: null, // 排序方向：asc/desc
    ...initialSearchParams,
  });

  // 处理列配置，添加排序功能
  const processedColumns = computed(() => {
    if (!sortable) return columns;

    return columns.map((col) => {
      // 如果列已经有sorter配置，保持不变
      if (col.sorter !== undefined) return col;

      // 跳过操作列和特殊列
      if (
        col.dataIndex === 'action' ||
        col.key === 'action' ||
        col.dataIndex === 'operation' ||
        col.key === 'operation' ||
        !col.dataIndex
      ) {
        return col;
      }

      // 为其他列添加排序功能
      return {
        ...col,
        sorter: true,
        sortDirections: ['ascend', 'descend'],
      };
    });
  });

  // 表格状态
  const tableState = reactive({
    dataSource: [],
    total: 0,
    loading: false,
    selectedRowKeys: [],
    columns: processedColumns,
  });

  // --- 方法 ---

  let latestRequestId = 0;

  const queryTableData = async () => {
    const requestId = latestRequestId + 1;
    latestRequestId = requestId;
    tableState.loading = true;
    try {
      // 对于树形数据，不需要分页参数
      let requestParams = isTreeTable
        ? { ...searchFormData }
        : { ...queryParams, ...searchFormData };

      // 过滤掉 null 和 undefined 的参数
      requestParams = Object.fromEntries(
        Object.entries(requestParams).filter(
          ([_key, value]) => value !== null && value !== undefined && value !== ''
        )
      );

      // 调用API获取数据
      const response = await api.list(requestParams);

      if (requestId !== latestRequestId) {
        return undefined;
      }

      // 处理不同的响应格式
      let rows, total;
      if (response.rows !== undefined) {
        // 标准分页格式 { rows, total }
        rows = response.rows;
        total = response.total;
      } else if (Array.isArray(response)) {
        // 直接返回数组（如树形数据）
        rows = response;
        total = response.length;
      } else if (response.data) {
        // 其他格式
        rows = response.data;
        total = response.total || rows.length;
      } else {
        rows = [];
        total = 0;
      }

      // 如果有自定义数据处理函数，则使用它
      if (api.processListData && typeof api.processListData === 'function') {
        rows = api.processListData(rows);
        // 树形数据处理后重新计算总数
        if (isTreeTable) {
          total = rows.length;
        }
      }

      tableState.dataSource = rows;
      tableState.total = total;
      return { rows, total };
    } catch (error) {
      if (requestId !== latestRequestId) {
        return undefined;
      }

      console.error('Failed to fetch table data:', error);
      if (showError) {
        message.error('数据加载失败');
      }
      throw error;
    } finally {
      if (requestId === latestRequestId) {
        tableState.loading = false;
      }
    }
  };

  // 监听 store 中的 pageSize 变化
  watch(
    () => tableConfigStore.pageSize,
    (newSize) => {
      if (newSize && newSize !== queryParams.pageSize) {
        queryParams.pageSize = parseInt(newSize);
        queryParams.pageNum = 1;
        queryTableData().catch(() => undefined);
      }
    }
  );

  const handleSearch = () => {
    queryParams.pageNum = 1;
    return queryTableData();
  };

  const handleReset = () => {
    Object.keys(searchFormData).forEach((key) => {
      delete searchFormData[key];
    });
    Object.assign(searchFormData, initialSearchParams);
    return handleSearch();
  };

  const handleTableChange = (pagination, filters, sorter) => {
    // 处理分页变化
    if (pagination) {
      queryParams.pageNum = pagination.current;
      queryParams.pageSize = pagination.pageSize;
    }

    // 处理排序变化
    if (sorter && sorter.field) {
      queryParams.orderByColumn = sorter.field;
      queryParams.isAsc = sorter.order === 'ascend' ? 'asc' : 'desc';
    } else {
      // 清除排序 - 删除属性而不是设置为null
      delete queryParams.orderByColumn;
      delete queryParams.isAsc;
    }

    return queryTableData();
  };

  const onSelectionChange = (keys) => {
    tableState.selectedRowKeys = keys;
  };

  const handleDelete = (ids) => {
    if (!api.delete) {
      console.error("API 'delete' function is not provided.");
      return;
    }
    const targetIds = ids || tableState.selectedRowKeys;
    if (!targetIds || targetIds.length === 0) {
      message.warning('请至少选择一条数据');
      return;
    }
    Modal.confirm({
      title: '确认删除',
      content: `您确定要删除选中的 ${targetIds.length} 条数据吗？`,
      okText: '确认',
      cancelText: '取消',
      onOk: async () => {
        await api.delete(targetIds);
        message.success('删除成功');
        tableState.selectedRowKeys = [];
        // 为了更好的用户体验，删除后留在当前页刷新
        queryTableData().catch(() => undefined);
      },
    });
  };

  const handleExport = () => {
    if (!api.exportUrl) {
      console.error("API 'exportUrl' is not provided.");
      return;
    }
    const finalExportFileName = `${exportFileName}_${dayjs().format('YYYY-MM-DD_HH-mm-ss')}.xlsx`;
    BearJiaUtil.download(api.exportUrl, queryParams, finalExportFileName);
  };

  // --- 计算属性 ---

  const pagination = computed(() => {
    // 如果是树形数据或者总数小于等于分页大小，则不显示分页
    if (isTreeTable || tableState.total <= queryParams.pageSize) {
      return false;
    }

    return {
      total: tableState.total,
      current: queryParams.pageNum,
      pageSize: queryParams.pageSize,
      showSizeChanger: tableConfigStore.showSizeChanger,
      showQuickJumper: tableConfigStore.showQuickJumper,
      pageSizeOptions: tableConfigStore.pageSizeOptions,
      showTotal: tableConfigStore.showTotal ? (total) => `共 ${total} 条` : undefined,
    };
  });

  const rowSelection = computed(() => ({
    selectedRowKeys: tableState.selectedRowKeys,
    onChange: onSelectionChange,
  }));

  return {
    searchFormData,
    tableState,
    pagination,
    rowSelection,
    queryTableData,
    handleSearch,
    handleReset,
    handleTableChange,
    handleDelete,
    handleExport,
  };
}
