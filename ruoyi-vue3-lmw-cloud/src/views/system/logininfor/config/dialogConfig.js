export default (listeners = {}) => {
  return {
    formItems: [
      {
        field: 'title',
        type: 'custom',
        label: '操作模块：',
      },
      {
        field: 'loginInfo',
        type: 'custom',
        label: '登录信息：',
      },
      {
        field: 'operUrl',
        type: 'custom',
        label: '请求地址：',
      },
      {
        field: 'method',
        type: 'custom',
        label: '操作方法：',
        layout: {
          xl: 24,
          lg: 24,
          md: 24,
          sm: 24,
          xs: 24,
        },
      },
      {
        field: 'operParam',
        type: 'custom',
        label: '请求参数：',
        layout: {
          xl: 24,
          lg: 24,
          md: 24,
          sm: 24,
          xs: 24,
        },
      },
      {
        field: 'jsonResult',
        type: 'custom',
        label: '返回参数：',
        layout: {
          xl: 24,
          lg: 24,
          md: 24,
          sm: 24,
          xs: 24,
        },
      },
      {
        field: 'operTime',
        type: 'custom',
        label: '操作时间：',
        layout: {
          xl: 24,
          lg: 24,
          md: 24,
          sm: 24,
          xs: 24,
        },
      },
      {
        field: 'status',
        type: 'custom',
        label: '操作状态：',
        layout: {
          xl: 8,
          lg: 8,
          md: 8,
          sm: 24,
          xs: 24,
        },
      },
      {
        field: 'costTime',
        type: 'custom',
        label: '消耗时间：',
        layout: {
          xl: 8,
          lg: 8,
          md: 8,
          sm: 24,
          xs: 24,
        },
      },
      {
        field: 'errorMsg',
        type: 'custom',
        hideLabel: true,
        layout: {
          xl: 24,
          lg: 24,
          md: 24,
          sm: 24,
          xs: 24,
        },
      },
    ],
    colLayout: {
      xl: 12,
      lg: 12,
      md: 12,
      sm: 12,
      xs: 24,
    },
    itemStyle: {
      padding: '0px 8px 0px 8px',
    },
    elFormConfig: {
      labelWidth: '100px',
    },
    hideItems: ref([]),
  }
}
