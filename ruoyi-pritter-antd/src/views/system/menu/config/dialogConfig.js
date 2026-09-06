export default (listeners = {}) => {
  return {
    rules: {
      menuName: [
        { required: true, message: '菜单名称不能为空', trigger: 'blur' },
      ],
      orderNum: [
        { required: true, message: '菜单顺序不能为空', trigger: 'blur' },
      ],
      path: [{ required: true, message: '路由地址不能为空', trigger: 'blur' }],
    },
    formItems: [
      {
        field: 'parentId',
        type: 'treeSelect',
        options: ref([]),
        label: '上级菜单',
        config: {
          fieldNames: {
            label: 'menuName',
            value: 'menuId',
            children: 'children',
          },
          allowClear: false,
          treeDefaultExpandAll: true,
          showSearch: true,
          treeNodeFilterProp: 'menuName',
        },
        layout: {
          xl: 24,
          lg: 24,
          md: 24,
          sm: 24,
          xs: 24,
        },
      },
      {
        field: 'menuType',
        type: 'radio',
        label: '菜单类型',
        isGroup: true,
        options: [
          { label: '目录', value: 'M' },
          { label: '菜单', value: 'C' },
          { label: '按钮', value: 'F' },
        ],
        config: {
          optionType: 'button',
          buttonStyle: 'solid',
        },
        eventFunction: {
          change: listeners.menuTypeChange,
        },
        layout: {
          xl: 24,
          lg: 24,
          md: 24,
          sm: 24,
          xs: 24,
        },
      },
      {
        label: '菜单图标',
        field: 'icon',
        type: 'custom',
        layout: {
          xl: 24,
          lg: 24,
          md: 24,
          sm: 24,
          xs: 24,
        },
      },
      {
        field: 'menuName',
        type: 'input',
        label: '菜单名称',
        config: {
          allowClear: false,
          maxlength: 50,
        },
      },
      {
        field: 'orderNum',
        type: 'inputNumber',
        label: '显示排序',
        config: {
          min: 0,
        },
      },
      {
        field: 'isFrame',
        type: 'radio',
        label: '是否外链',
        isGroup: true,
        tip: '选择是外链则路由地址需要以`http(s)://`开头',
        options: [
          { label: '是', value: '0' },
          { label: '否', value: '1' },
        ],
        config: {
          optionType: 'button',
          buttonStyle: 'solid',
        },
      },
      {
        field: 'path',
        type: 'input',
        label: '路由地址',
        tip: '访问的路由地址，如：`user`，如外网地址需内链访问则以`http(s)://`开头',
        config: {
          maxlength: 200,
        },
      },
      {
        field: 'component',
        type: 'input',
        label: '组件路径',
        tip: '访问的组件路径，如：`system/user/index`，默认在`views`目录下',
        config: {
          maxlength: 200,
        },
      },
      {
        field: 'perms',
        type: 'input',
        label: '权限字符',
        tip: "控制器中定义的权限字符，如：@PreAuthorize(`@ss.hasPermi('system:user:list')`)",
        config: {
          maxlength: 100,
        },
      },
      {
        field: 'query',
        type: 'input',
        label: '路由参数',
        tip: '访问路由的默认传递参数，如：{"id": 1}',
        config: {
          maxlength: 255,
        },
      },
      {
        field: 'isCache',
        type: 'radio',
        label: '是否缓存',
        isGroup: true,
        options: [
          { label: '缓存', value: '0' },
          { label: '不缓存', value: '1' },
        ],
        config: {
          optionType: 'button',
          buttonStyle: 'solid',
        },
        tip: '选择是则会被`keep-alive`缓存，需要匹配组件的`name`和地址保持一致',
      },
      {
        field: 'visible',
        type: 'radio',
        label: '显示状态',
        isGroup: true,
        options: [],
        tip: '选择隐藏则路由将不会出现在侧边栏，但仍然可以访问',
        config: {
          optionType: 'button',
          buttonStyle: 'solid',
        },
      },
      {
        field: 'status',
        type: 'radio',
        label: '菜单状态',
        isGroup: true,
        options: [],
        tip: '选择停用则路由将不会出现在侧边栏，也不能被访问',
        config: {
          optionType: 'button',
          buttonStyle: 'solid',
        },
      },
    ],
    colLayout: {
      xl: 12,
      lg: 12,
      md: 12,
      sm: 24,
      xs: 24,
    },
    itemStyle: {
      padding: '0px 8px 0px 8px',
    },
    formConfig: {
      layout: 'horizontal',
      labelCol: { style: { width: '100px' } },
      wrapperCol: { flex: 1 },
    },
  }
}
