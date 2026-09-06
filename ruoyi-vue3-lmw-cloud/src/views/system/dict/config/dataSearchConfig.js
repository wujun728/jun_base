export default (otherConfig = {}) => {
  return {
    formItems: [
      {
        label: '字典名称',
        field: 'dictType',
        type: 'select',
        options: ref([]),
        setLabel: 'dictName',
        setValue: 'dictType',
        config: {
          clearable: false,
        },
      },
      {
        label: '字典标签',
        field: 'dictLabel',
        type: 'input',
      },
      {
        label: '状态',
        field: 'status',
        type: 'select',
        options: [],
      },
    ],
    colLayout: {
      xl: 4,
      lg: 4,
      md: 6,
      sm: 12,
      xs: 24,
    },
  }
}
