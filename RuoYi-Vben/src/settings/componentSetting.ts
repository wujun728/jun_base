// Used to configure the general configuration of some components without modifying the components

import type { SorterResult } from '../components/Table';

export default {
  // basic-table setting
  table: {
    // 表格接口返回参数
    // support xxx.xxx.xxx
    fetchSetting: {
      pageField: 'pageNum',
      sizeField: 'pageSize',
      listField: 'rows',
      totalField: 'total',
    },
    // 页码数设置
    pageSizeOptions: ['10', '20', '30', '40'],
    // 默认每页条数
    defaultPageSize: 10,
    // 默认大小
    defaultSize: 'middle',
    // 默认排序
    defaultSortFn: (sortInfo: SorterResult) => {
      const { field, order } = sortInfo;
      if (field && order) {
        return {
          // 根据哪个字段排序
          orderByColumn: field,
          // 排序方式
          isAsc: 'ascend' === order ? 'asc' : 'desc',
        };
      } else {
        return {};
      }
    },
    // Custom general filter function
    defaultFilterFn: (data: Partial<Recordable<string[]>>) => {
      return data;
    },
    formConfig: {
      labelWidth: 100,
      autoSubmitOnEnter: true,
    },
  },
  vxeTable: {
    table: {
      border: true,
      stripe: true,
      columnConfig: {
        resizable: true,
        isCurrent: true,
        isHover: true,
      },
      rowConfig: {
        isCurrent: true,
        isHover: true,
      },
      emptyRender: {
        name: 'AEmpty',
      },
      printConfig: {},
      exportConfig: {},
      customConfig: {
        storage: true,
      },
    },
    grid: {
      toolbarConfig: {
        enabled: true,
        export: true,
        zoom: true,
        print: true,
        refresh: true,
        custom: true,
      },
      pagerConfig: {
        pageSizes: [20, 50, 100, 500],
        pageSize: 20,
        autoHidden: true,
      },
      proxyConfig: {
        form: true,
        props: {
          result: 'items',
          total: 'total',
        },
      },
      zoomConfig: {},
    },
  },
  // scrollbar setting
  scrollbar: {
    // Whether to use native scroll bar
    // After opening, the menu, modal, drawer will change the pop-up scroll bar to native
    native: false,
  },
};
