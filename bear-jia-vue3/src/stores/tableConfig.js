import { defineStore } from 'pinia';

export const useTableConfigStore = defineStore('tableConfig', {
  state: () => ({
    // 表格配置
    size: 'large',
    bordered: true,
    loading: false,
    tableHeight: 400,
    showHeader: true,
    headerBold: true,
    stripe: false,
    headerBgColor: '#f5f7fa',
    fixHeader: false,
    resizable: false,
    rowSelection: true,
    // 分页配置
    pageSize: 10,
    pageSizeOptions: ['10', '20', '50', '100'],
    showTotal: true,
    showSizeChanger: true,
    showQuickJumper: true,
    pagination: {
      current: 1,
      pageSize: 10,
      total: 0,
      showSizeChanger: true,
      showQuickJumper: true,
      showTotal: true,
    },
  }),

  actions: {
    // 更新整个配置
    updateConfig(config) {
      Object.assign(this, config);
    },

    // 设置表格大小
    setSize(size) {
      this.size = size;
    },

    // 设置表格边框
    setBordered(bordered) {
      this.bordered = bordered;
    },

    // 设置表格高度
    setTableHeight(height) {
      this.tableHeight = height;
    },

    // 设置表格头部显示
    setShowHeader(show) {
      this.showHeader = show;
    },

    // 设置行选择
    setRowSelection(enable) {
      this.rowSelection = enable;
    },

    // 更新分页配置
    updatePagination(config) {
      this.pagination = {
        ...this.pagination,
        ...config,
      };
    },

    // 重置配置
    resetConfig() {
      const defaultConfig = {
        size: 'middle',
        bordered: true,
        loading: false,
        tableHeight: 400,
        showHeader: true,
        headerBold: true,
        stripe: false,
        headerBgColor: '#f5f7fa',
        fixHeader: false,
        resizable: false,
        rowSelection: true,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '50', '100'],
        showTotal: true,
        showSizeChanger: true,
        showQuickJumper: true,
        pagination: {
          current: 1,
          pageSize: 10,
          total: 0,
          showSizeChanger: true,
          showQuickJumper: true,
          showTotal: true,
        },
      };

      Object.assign(this, defaultConfig);

      // 清除持久化存储
      localStorage.removeItem('table-config');
    },

    // 从localStorage加载配置
    loadConfig() {
      const savedConfig = localStorage.getItem('table-config');
      if (savedConfig) {
        try {
          const config = JSON.parse(savedConfig);
          Object.assign(this, config);
        } catch (error) {
          console.warn('Failed to load table config from localStorage:', error);
        }
      }
    },

    // 保存配置到localStorage
    saveConfig() {
      localStorage.setItem('table-config', JSON.stringify(this.$state));
    },

    // 获取表格样式
    getTableProps() {
      return {
        size: this.size,
        bordered: this.bordered,
        loading: this.loading,
        showHeader: this.showHeader,
        scroll: this.fixHeader ? { y: this.tableHeight } : undefined,
        rowSelection: this.rowSelection
          ? {
              type: 'checkbox',
              showSelectAll: true,
            }
          : undefined,
      };
    },

    // 获取分页配置
    getPaginationProps() {
      return {
        current: this.pagination.current,
        pageSize: this.pagination.pageSize,
        total: this.pagination.total,
        showSizeChanger: this.showSizeChanger,
        showQuickJumper: this.showQuickJumper,
        showTotal: this.showTotal
          ? (total, range) => `第 ${range[0]}-${range[1]} 条，共 ${total} 条`
          : false,
        pageSizeOptions: this.pageSizeOptions,
      };
    },
  },
});
