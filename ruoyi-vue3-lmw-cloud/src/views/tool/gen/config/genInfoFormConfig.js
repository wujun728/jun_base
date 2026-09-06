export default (otherConfig = {}) => {
  return {
    rules: {
      tplCategory: [
        { required: true, message: '请选择生成模板', trigger: 'blur' },
      ],
      packageName: [
        { required: true, message: '请输入生成包路径', trigger: 'blur' },
      ],
      moduleName: [
        { required: true, message: '请输入生成模块名', trigger: 'blur' },
      ],
      businessName: [
        { required: true, message: '请输入生成业务名', trigger: 'blur' },
      ],
      functionName: [
        { required: true, message: '请输入生成功能名', trigger: 'blur' },
      ],
    },
    itemStyle: { padding: '0px 20px 0px 0px' },
    formItems: [
      {
        label: '生成模板',
        field: 'tplCategory',
        type: 'select',
        options: [
          {
            label: '单表（增删改查）',
            value: 'crud',
          },
          {
            label: '树表（增删改查）',
            value: 'tree',
          },
          {
            label: '主子表（增删改查）',
            value: 'sub',
          },
        ],
      },
      {
        label: '生成包路径',
        field: 'packageName',
        type: 'input',
        tip: '生成在哪个java包下，例如 com.ruoyi.system',
      },
      {
        label: '生成模块名',
        field: 'moduleName',
        type: 'input',
        tip: '可理解为子系统名，例如 system',
      },
      {
        label: '生成业务名',
        field: 'businessName',
        type: 'input',
        tip: '可理解为功能英文名，例如 user',
      },
      {
        label: '生成功能名',
        field: 'functionName',
        type: 'input',
        tip: '用作类描述，例如 用户',
      },
      {
        label: '上级菜单',
        field: 'parentMenuId',
        type: 'treeSelect',
        config: {
          props: { value: 'menuId', label: 'menuName', children: 'children' },
          valueKey: 'menuId',
          checkStrictly: true,
          filterable: true,
          filterNodeMethod: otherConfig.filterNodeMethod,
          clearable: true,
        },
        tip: '分配到指定菜单下，例如 系统管理',
      },
      {
        label: '生成代码方式',
        field: 'genType',
        type: 'radio',
        isGroup: true,
        optionConfig: {
          border: true,
        },
        options: [
          {
            label: 'zip压缩包下载',
            value: '0',
          },
          {
            label: '自定义路径',
            value: '1',
          },
        ],
        tip: '默认为zip压缩包下载，也可以自定义生成路径',
      },
      {
        label: '自定义路径',
        field: 'genPath',
        type: 'input',
        tip: '填写磁盘绝对路径，若不填写，则生成到当前Web项目下',
      },
      {
        label: '树编码字段',
        field: 'treeCode',
        type: 'select',
        options: [],
        tip: '树显示的编码字段名， 如：dept_id',
      },
      {
        label: '树父编码字段',
        field: 'treeParentCode',
        type: 'select',
        options: [],
        tip: '树显示的父编码字段名， 如：parent_Id',
      },
      {
        label: '树名称字段',
        field: 'treeName',
        type: 'select',
        options: [],
        tip: '树节点的显示名称字段名， 如：dept_name',
      },
      {
        label: '关联子表的表名',
        field: 'subTableName',
        type: 'select',
        options: [],
        tip: '关联子表的表名， 如：sys_user',
      },
      {
        label: '子表关联的外键名',
        field: 'subTableFkName',
        type: 'select',
        options: [],
        tip: '子表关联的外键名， 如：user_id',
      },
    ],
    colLayout: {
      xl: 12,
      lg: 12,
      md: 12,
      sm: 12,
      xs: 24,
    },
    elFormConfig: {
      // labelWidth: '140px',
      labelPosition: 'top',
    },
  }
}
