import { useTableConfigStore } from '@/stores/tableConfig';
import { h, computed } from 'vue';
import { Table } from 'ant-design-vue';

export default {
  install: (app) => {
    console.log('Table plugin installing...');

    // 创建新的表格组件，继承Ant Design Vue的Table组件
    app.component('ATable', {
      name: 'ATable',
      setup(props, { slots, attrs }) {
        const store = useTableConfigStore();
        console.log('Table component setup, store:', store);

        // 处理列宽调整
        const handleResizeColumn = (width, column) => {
          if (column && typeof column === 'object') {
            column.width = width;
          }
          // 如果父组件传入了resizeColumn事件处理器，也要调用
          if (props.onResizeColumn) {
            props.onResizeColumn(width, column);
          }
        };

        // 处理columns配置，添加resizable支持
        const processColumns = (columns) => {
          if (!Array.isArray(columns) || !store.resizable) {
            return columns;
          }

          return columns.map((col) => ({
            ...col,
            resizable: col.resizable !== false, // 默认启用，除非明确设置为false
          }));
        };

        // 使用computed来获取响应式的配置
        const mergedProps = computed(() => {
          const globalPaginationProps = store.getPaginationProps
            ? store.getPaginationProps()
            : store.pagination;

          const config = {
            size: props.size ?? store.size,
            bordered: props.bordered ?? store.bordered,
            loading: props.loading !== undefined ? props.loading : store.loading,
            scroll: props.scroll ?? (store.fixHeader ? { y: store.tableHeight } : undefined),
            showHeader: props.showHeader ?? store.showHeader,
            // 行选择配置
            rowSelection:
              props.rowSelection !== undefined
                ? props.rowSelection
                : store.rowSelection
                  ? {}
                  : undefined,
            // 分页配置
            pagination: props.pagination !== undefined ? props.pagination : globalPaginationProps,
            // 处理columns，添加resizable支持和表头样式
            columns: processColumns(props.columns),
            // 添加列宽调整事件处理
            onResizeColumn: handleResizeColumn,
            // 添加表格样式类
            class: [
              attrs.class,
              store.stripe ? 'bearjia-table-striped' : '',
              `bearjia-table-${store.size}`,
            ]
              .filter(Boolean)
              .join(' '),
            ...attrs,
            ...props,
          };

          return config;
        });

        return () => h(Table, mergedProps.value, slots);
      },
    });

    console.log('Table plugin installed successfully');
  },
};
