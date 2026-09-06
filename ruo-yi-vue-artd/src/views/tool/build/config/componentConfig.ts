/**
 * 表单构建器 - 组件元数据配置
 * 配置驱动架构的核心。新增组件只需在此文件添加一条配置，
 * 属性面板、渲染器、代码生成器自动适配。
 */
import type { ComponentConfig } from '../types/form'

export const basicComponents: ComponentConfig[] = [
  {
    type: 'input',
    label: '单行文本',
    icon: 'Edit',
    category: 'basic',
    renderTag: 'el-input',
    defaultProps: {
      placeholder: '请输入',
      maxlength: undefined,
      showWordLimit: false,
      clearable: true,
      prefix: '',
      suffix: ''
    },
    propsConfig: [
      { name: 'placeholder', label: '占位提示', type: 'input' },
      { name: 'maxlength', label: '最大长度', type: 'number' },
      { name: 'showWordLimit', label: '显示字数统计', type: 'switch' },
      { name: 'clearable', label: '可清空', type: 'switch' },
      {
        name: 'prefix',
        label: '前缀图标',
        type: 'input',
        placeholder: 'el-icon 名称'
      },
      {
        name: 'suffix',
        label: '后缀图标',
        type: 'input',
        placeholder: 'el-icon 名称'
      }
    ]
  },
  {
    type: 'textarea',
    label: '多行文本',
    icon: 'Document',
    category: 'basic',
    renderTag: 'el-input',
    defaultProps: {
      placeholder: '请输入',
      maxlength: undefined,
      showWordLimit: false,
      rows: 3,
      type: 'textarea'
    },
    propsConfig: [
      { name: 'placeholder', label: '占位提示', type: 'input' },
      { name: 'maxlength', label: '最大长度', type: 'number' },
      { name: 'showWordLimit', label: '显示字数统计', type: 'switch' },
      { name: 'rows', label: '行数', type: 'number' }
    ]
  },
  {
    type: 'input-number',
    label: '数字输入',
    icon: 'Sort',
    category: 'basic',
    renderTag: 'el-input-number',
    defaultProps: {
      placeholder: '请输入数字',
      min: undefined,
      max: undefined,
      step: 1,
      precision: undefined,
      controls: true
    },
    propsConfig: [
      { name: 'placeholder', label: '占位提示', type: 'input' },
      { name: 'min', label: '最小值', type: 'number' },
      { name: 'max', label: '最大值', type: 'number' },
      { name: 'step', label: '步长', type: 'number' },
      {
        name: 'precision',
        label: '精度',
        type: 'number',
        placeholder: '小数位数'
      },
      { name: 'controls', label: '显示控制按钮', type: 'switch' }
    ]
  },
  {
    type: 'password',
    label: '密码框',
    icon: 'Lock',
    category: 'basic',
    renderTag: 'el-input',
    defaultProps: {
      placeholder: '请输入密码',
      showPassword: true,
      type: 'password'
    },
    propsConfig: [
      { name: 'placeholder', label: '占位提示', type: 'input' },
      { name: 'showPassword', label: '显示切换按钮', type: 'switch' }
    ]
  },
  {
    type: 'select',
    label: '下拉选择',
    icon: 'ArrowDown',
    category: 'basic',
    renderTag: 'el-select',
    defaultProps: {
      placeholder: '请选择',
      multiple: false,
      filterable: false,
      clearable: true,
      collapseTags: false,
      allowCreate: false,
      options: [
        { label: '选项一', value: 'option1' },
        { label: '选项二', value: 'option2' }
      ]
    },
    propsConfig: [
      { name: 'placeholder', label: '占位提示', type: 'input' },
      { name: 'multiple', label: '多选', type: 'switch' },
      { name: 'filterable', label: '可搜索', type: 'switch' },
      { name: 'clearable', label: '可清空', type: 'switch' },
      { name: 'collapseTags', label: '折叠标签', type: 'switch' },
      { name: 'allowCreate', label: '允许创建', type: 'switch' },
      { name: 'options', label: '选项', type: 'option-editor' }
    ]
  },
  {
    type: 'multi-select',
    label: '多选 Select',
    icon: 'List',
    category: 'basic',
    renderTag: 'el-select',
    defaultProps: {
      placeholder: '请选择',
      multiple: true,
      filterable: false,
      clearable: true,
      collapseTags: false,
      options: [
        { label: '选项一', value: 'option1' },
        { label: '选项二', value: 'option2' }
      ]
    },
    propsConfig: [
      { name: 'placeholder', label: '占位提示', type: 'input' },
      { name: 'filterable', label: '可搜索', type: 'switch' },
      { name: 'clearable', label: '可清空', type: 'switch' },
      { name: 'collapseTags', label: '折叠标签', type: 'switch' },
      { name: 'options', label: '选项', type: 'option-editor' }
    ]
  },
  {
    type: 'radio',
    label: '单选框组',
    icon: 'CircleCheck',
    category: 'basic',
    renderTag: 'el-radio-group',
    defaultProps: {
      options: [
        { label: '选项一', value: 'option1' },
        { label: '选项二', value: 'option2' }
      ]
    },
    propsConfig: [{ name: 'options', label: '选项', type: 'option-editor' }]
  },
  {
    type: 'checkbox',
    label: '多选框组',
    icon: 'Select',
    category: 'basic',
    renderTag: 'el-checkbox-group',
    defaultProps: {
      options: [
        { label: '选项一', value: 'option1' },
        { label: '选项二', value: 'option2' }
      ]
    },
    propsConfig: [{ name: 'options', label: '选项', type: 'option-editor' }]
  },
  {
    type: 'switch',
    label: '开关',
    icon: 'Switch',
    category: 'basic',
    renderTag: 'el-switch',
    defaultProps: {
      activeText: '',
      inactiveText: '',
      activeValue: true,
      inactiveValue: false
    },
    propsConfig: [
      { name: 'activeText', label: '开启文字', type: 'input' },
      { name: 'inactiveText', label: '关闭文字', type: 'input' }
    ]
  },
  {
    type: 'date',
    label: '日期选择',
    icon: 'Calendar',
    category: 'basic',
    renderTag: 'el-date-picker',
    defaultProps: {
      placeholder: '选择日期',
      type: 'date',
      format: 'YYYY-MM-DD',
      valueFormat: 'YYYY-MM-DD'
    },
    propsConfig: [
      { name: 'placeholder', label: '占位提示', type: 'input' },
      {
        name: 'format',
        label: '显示格式',
        type: 'input',
        placeholder: 'YYYY-MM-DD'
      },
      {
        name: 'valueFormat',
        label: '值格式',
        type: 'input',
        placeholder: 'YYYY-MM-DD'
      }
    ]
  },
  {
    type: 'daterange',
    label: '日期范围',
    icon: 'Calendar',
    category: 'basic',
    renderTag: 'el-date-picker',
    defaultProps: {
      placeholder: '选择日期范围',
      type: 'daterange',
      format: 'YYYY-MM-DD',
      valueFormat: 'YYYY-MM-DD',
      startPlaceholder: '开始日期',
      endPlaceholder: '结束日期',
      rangeSeparator: '至'
    },
    propsConfig: [
      { name: 'startPlaceholder', label: '开始占位', type: 'input' },
      { name: 'endPlaceholder', label: '结束占位', type: 'input' },
      {
        name: 'rangeSeparator',
        label: '分隔符',
        type: 'input',
        placeholder: '至'
      },
      {
        name: 'format',
        label: '显示格式',
        type: 'input',
        placeholder: 'YYYY-MM-DD'
      },
      {
        name: 'valueFormat',
        label: '值格式',
        type: 'input',
        placeholder: 'YYYY-MM-DD'
      }
    ]
  },
  {
    type: 'time',
    label: '时间选择',
    icon: 'Clock',
    category: 'basic',
    renderTag: 'el-time-picker',
    defaultProps: {
      placeholder: '选择时间',
      format: 'HH:mm:ss',
      valueFormat: 'HH:mm:ss'
    },
    propsConfig: [
      { name: 'placeholder', label: '占位提示', type: 'input' },
      {
        name: 'format',
        label: '显示格式',
        type: 'input',
        placeholder: 'HH:mm:ss'
      },
      {
        name: 'valueFormat',
        label: '值格式',
        type: 'input',
        placeholder: 'HH:mm:ss'
      }
    ]
  },
  {
    type: 'datetime',
    label: '日期时间',
    icon: 'Timer',
    category: 'basic',
    renderTag: 'el-date-picker',
    defaultProps: {
      placeholder: '选择日期时间',
      type: 'datetime',
      format: 'YYYY-MM-DD HH:mm:ss',
      valueFormat: 'YYYY-MM-DD HH:mm:ss'
    },
    propsConfig: [
      { name: 'placeholder', label: '占位提示', type: 'input' },
      {
        name: 'format',
        label: '显示格式',
        type: 'input',
        placeholder: 'YYYY-MM-DD HH:mm:ss'
      },
      {
        name: 'valueFormat',
        label: '值格式',
        type: 'input',
        placeholder: 'YYYY-MM-DD HH:mm:ss'
      }
    ]
  },
  {
    type: 'cascader',
    label: '级联选择',
    icon: 'Connection',
    category: 'basic',
    renderTag: 'el-cascader',
    defaultProps: {
      placeholder: '请选择',
      clearable: true,
      filterable: false,
      options: [
        {
          label: '选项一',
          value: 'option1',
          children: [
            { label: '子项一', value: 'child1' },
            { label: '子项二', value: 'child2' }
          ]
        },
        {
          label: '选项二',
          value: 'option2',
          children: [{ label: '子项三', value: 'child3' }]
        }
      ]
    },
    propsConfig: [
      { name: 'placeholder', label: '占位提示', type: 'input' },
      { name: 'clearable', label: '可清空', type: 'switch' },
      { name: 'filterable', label: '可搜索', type: 'switch' },
      { name: 'options', label: '选项树', type: 'option-editor' }
    ]
  },
  {
    type: 'tree-select',
    label: '树选择',
    icon: 'Share',
    category: 'basic',
    renderTag: 'el-tree-select',
    defaultProps: {
      placeholder: '请选择',
      clearable: true,
      filterable: false,
      data: [
        {
          label: '节点一',
          value: 'node1',
          children: [{ label: '子节点一', value: 'child1' }]
        },
        { label: '节点二', value: 'node2' }
      ]
    },
    propsConfig: [
      { name: 'placeholder', label: '占位提示', type: 'input' },
      { name: 'clearable', label: '可清空', type: 'switch' },
      { name: 'filterable', label: '可搜索', type: 'switch' }
    ]
  },
  {
    type: 'slider',
    label: '滑块',
    icon: 'Rank',
    category: 'basic',
    renderTag: 'el-slider',
    defaultProps: {
      min: 0,
      max: 100,
      step: 1,
      showInput: false,
      showStops: false,
      range: false
    },
    propsConfig: [
      { name: 'min', label: '最小值', type: 'number' },
      { name: 'max', label: '最大值', type: 'number' },
      { name: 'step', label: '步长', type: 'number' },
      { name: 'showInput', label: '显示输入框', type: 'switch' },
      { name: 'showStops', label: '显示间断点', type: 'switch' },
      { name: 'range', label: '范围选择', type: 'switch' }
    ]
  },
  {
    type: 'rate',
    label: '评分',
    icon: 'Star',
    category: 'basic',
    renderTag: 'el-rate',
    defaultProps: {
      max: 5,
      allowHalf: false,
      showText: false,
      showScore: false
    },
    propsConfig: [
      { name: 'max', label: '最大分值', type: 'number' },
      { name: 'allowHalf', label: '允许半选', type: 'switch' },
      { name: 'showText', label: '显示辅助文字', type: 'switch' },
      { name: 'showScore', label: '显示分数', type: 'switch' }
    ]
  },
  {
    type: 'color-picker',
    label: '颜色选择',
    icon: 'Brush',
    category: 'basic',
    renderTag: 'el-color-picker',
    defaultProps: {
      showAlpha: false,
      colorFormat: 'hex'
    },
    propsConfig: [
      { name: 'showAlpha', label: '支持透明度', type: 'switch' },
      {
        name: 'colorFormat',
        label: '颜色格式',
        type: 'select',
        options: [
          { label: 'hex', value: 'hex' },
          { label: 'rgb', value: 'rgb' },
          { label: 'hsl', value: 'hsl' }
        ]
      }
    ]
  },
  {
    type: 'upload',
    label: '上传',
    icon: 'Upload',
    category: 'basic',
    renderTag: 'el-upload',
    defaultProps: {
      action: '',
      multiple: false,
      limit: undefined,
      accept: '',
      listType: 'text',
      autoUpload: true
    },
    propsConfig: [
      { name: 'action', label: '上传地址', type: 'input' },
      { name: 'multiple', label: '多文件上传', type: 'switch' },
      { name: 'limit', label: '最大上传数', type: 'number' },
      {
        name: 'accept',
        label: '接受文件类型',
        type: 'input',
        placeholder: '如: .jpg,.png'
      },
      {
        name: 'listType',
        label: '文件列表类型',
        type: 'select',
        options: [
          { label: 'text', value: '文本' },
          { label: 'picture', value: '图片' },
          { label: 'picture-card', value: '卡片' }
        ]
      }
    ]
  }
]

export const layoutComponents: ComponentConfig[] = [
  {
    type: 'row',
    label: '行容器',
    icon: 'Grid',
    category: 'layout',
    renderTag: 'el-row',
    canHaveChildren: true,
    allowedChildren: ['col'],
    defaultProps: {
      gutter: 0,
      justify: 'start',
      align: 'top'
    },
    propsConfig: [
      { name: 'gutter', label: '栅格间隔', type: 'number' },
      {
        name: 'justify',
        label: '水平排列',
        type: 'select',
        options: [
          { label: '左对齐', value: 'start' },
          { label: '居中', value: 'center' },
          { label: '右对齐', value: 'end' },
          { label: '两侧', value: 'space-around' },
          { label: '均分', value: 'space-between' },
          { label: '均匀', value: 'space-evenly' }
        ]
      },
      {
        name: 'align',
        label: '垂直排列',
        type: 'select',
        options: [
          { label: '顶部', value: 'top' },
          { label: '居中', value: 'middle' },
          { label: '底部', value: 'bottom' }
        ]
      }
    ]
  },
  {
    type: 'col',
    label: '列容器',
    icon: 'Grid',
    category: 'layout',
    renderTag: 'el-col',
    canHaveChildren: true,
    allowedChildren: undefined,
    defaultProps: {
      span: 12,
      offset: 0,
      push: 0,
      pull: 0
    },
    propsConfig: [
      { name: 'span', label: '占位列数', type: 'number' },
      { name: 'offset', label: '左侧偏移', type: 'number' },
      { name: 'push', label: '右移列数', type: 'number' },
      { name: 'pull', label: '左移列数', type: 'number' }
    ]
  }
]

export const actionComponents: ComponentConfig[] = [
  {
    type: 'button',
    label: '按钮',
    icon: 'Plus',
    category: 'action',
    renderTag: 'el-button',
    noFormItem: true,
    noLabel: true,
    defaultProps: {
      text: '按钮',
      type: 'primary',
      size: 'default',
      icon: '',
      loading: false,
      plain: false,
      round: false,
      circle: false,
      disabled: false,
      clickEvent: 'handleClick'
    },
    propsConfig: [
      { name: 'text', label: '按钮文字', type: 'input' },
      {
        name: 'type',
        label: '按钮类型',
        type: 'select',
        options: [
          { label: '默认', value: '' },
          { label: '主要', value: 'primary' },
          { label: '成功', value: 'success' },
          { label: '警告', value: 'warning' },
          { label: '危险', value: 'danger' },
          { label: '信息', value: 'info' }
        ]
      },
      {
        name: 'size',
        label: '尺寸',
        type: 'select',
        options: [
          { label: '大', value: 'large' },
          { label: '默认', value: 'default' },
          { label: '小', value: 'small' }
        ]
      },
      {
        name: 'icon',
        label: '图标',
        type: 'input',
        placeholder: 'el-icon 名称'
      },
      { name: 'loading', label: '加载中', type: 'switch' },
      { name: 'plain', label: '朴素', type: 'switch' },
      { name: 'round', label: '圆角', type: 'switch' },
      { name: 'circle', label: '圆形', type: 'switch' },
      { name: 'disabled', label: '禁用', type: 'switch' },
      {
        name: 'clickEvent',
        label: '点击事件',
        type: 'input',
        placeholder: 'handleClick'
      }
    ]
  }
]
