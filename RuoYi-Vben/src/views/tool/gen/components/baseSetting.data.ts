import { FormSchema } from '@/components/Form';

export const formSchemas: FormSchema[] = [
  {
    label: '基本信息',
    field: 'Divider1',
    component: 'Divider',
    colProps: { span: 24 },
  },
  {
    label: '表名称',
    field: 'tableName',
    component: 'Input',
    required: true,
  },
  {
    label: '表描述',
    field: 'tableComment',
    component: 'Input',
    required: true,
  },
  {
    label: '实体类名称',
    field: 'className',
    component: 'Input',
    required: true,
  },
  {
    label: '作者',
    field: 'functionAuthor',
    component: 'Input',
    required: true,
  },
  /**
   * field重复会导致在updateSchema时不显示
   */
  {
    label: '生成信息',
    field: 'Divider2',
    component: 'Divider',
    colProps: { span: 24 },
  },
  {
    label: '模板类型',
    field: 'tplCategory',
    component: 'Select',
    defaultValue: 'crud',
    componentProps: {
      allowClear: false,
      options: [
        { label: '单表(增删改查)', value: 'crud' },
        { label: '树表(增删改查)', value: 'tree' },
      ],
    },
    required: true,
  },
  {
    label: '树编码字段',
    helpMessage: '树节点显示的编码字段名， 如: dept_id (相当于id)',
    field: 'treeCode',
    component: 'Select',
    componentProps: {
      allowClear: false,
    },
    required: true,
    ifShow({ values }) {
      return values.tplCategory === 'tree';
    },
  },
  {
    label: '树父编码字段',
    helpMessage: '树节点显示的父编码字段名， 如: parent_Id (相当于parentId)',
    field: 'treeParentCode',
    component: 'Select',
    componentProps: {
      allowClear: false,
    },
    required: true,
    ifShow({ model }) {
      return model.tplCategory === 'tree';
    },
  },
  {
    label: '树名称字段',
    helpMessage: '树节点的显示名称字段名， 如: dept_name (相当于label)',
    field: 'treeName',
    component: 'Select',
    componentProps: {
      allowClear: false,
    },
    required: true,
    ifShow({ model }) {
      return model.tplCategory === 'tree';
    },
  },
  {
    label: '生成包路径',
    field: 'packageName',
    helpMessage: '生成在哪个java包下, 例如 com.ruoyi.system',
    component: 'Input',
  },
  {
    label: '生成模块名',
    field: 'moduleName',
    helpMessage: '可理解为子系统名，例如 system',
    component: 'Input',
    required: true,
  },
  {
    label: '生成业务名',
    field: 'businessName',
    helpMessage: '可理解为功能英文名，例如 user',
    component: 'Input',
    required: true,
  },
  {
    label: '生成功能名',
    field: 'functionName',
    helpMessage: '用作类描述，例如 用户',
    component: 'Input',
    required: true,
  },
  {
    label: '上级菜单',
    field: 'parentMenuId',
    defaultValue: 0,
    component: 'TreeSelect',
    componentProps: {
      allowClear: false,
    },
  },
  {
    label: '生成代码方式',
    field: 'genType',
    helpMessage: '默认为zip压缩包下载, 也可以自定义生成路径',
    defaultValue: '0',
    component: 'RadioButtonGroup',
    componentProps: {
      options: [
        { label: 'zip压缩包', value: '0' },
        { label: '自定义路径', value: '1' },
      ],
    },
  },
  {
    label: '代码生成路径',
    field: 'genPath',
    component: 'Input',
    defaultValue: '/',
    helpMessage: '输入绝对路径, 不支持"./"相对路径',
    ifShow({ model }) {
      return model.genType === '1';
    },
    rules: [
      {
        required: true,
        message: '请输入合法的路径',
        pattern: /^(?:[a-zA-Z]:)?(?:\/|(?:\\|\/)[^\\\\/:*?"<>|\r\n]+)*(?:\\|\/)?$/,
        trigger: 'blur',
      },
    ],
  },
  {
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
    colProps: { span: 24 },
  },
];
