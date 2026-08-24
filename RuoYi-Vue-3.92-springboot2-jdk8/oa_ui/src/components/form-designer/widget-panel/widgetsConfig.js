/**
 * 容器组件数组
 * @type {Container[]}
 */
export const containers = [
  {
    name: '栅格', // 组件名称
    type: 'grid', // 组件类型
    category: 'container', // 组件分类
    cols: [], // 列数组
    options: {
      name: '', // 组件名称
      hidden: false, // 是否隐藏
      gutter: 12, // 列间距
      colHeight: null // 列高度
    }
  },

  {
    name: '标签页',
    type: 'tab',
    category: 'container',
    displayType: 'border-card',
    tabs: [],
    options: {
      name: '',
      hidden: false,
      topMargin: 0,
      bottomMargin: 20
      // 上下间距
      // customClass: '' //自定义css类名
    }
  },

  {
    name: '栅格列',
    type: 'grid-col',
    category: 'container',
    internal: false,
    widgetList: [],
    options: {
      name: '',
      hidden: false,
      span: 12,
      offset: 0,
      push: 0,
      pull: 0,
      responsive: false, //是否开启响应式布局
      md: 12,
      sm: 12,
      xs: 12
    }
  },
  {
    name: '选项卡页',
    type: 'tab-pane',
    category: 'container',
    internal: true,
    widgetList: [],
    options: {
      name: '',
      label: '',
      hidden: false,
      active: false,
      disabled: false,
    }
  },
  {
    name: '卡片',
    type: 'card',
    category: 'container',
    widgetList: [],
    options: {
      name: '',
      label: 'card',
      hidden: false,
      folded: false,
      showFold: true,
      cardHeader: true,
      cardWidth: '100%',
      shadow: 'never',
      topMargin: 0,
      bottomMargin: 20
    }
  }
]

/**
 * 基础字段数组
 * @type {BasicField[]}
 */
export const basicFields = [
  {
    // 表单项名称
    name: '文本',
    // 表单项类型
    type: 'input',
    // 是否为表单项
    formItemFlag: true,

    options: {
      name: '', // 表单项名称
      label: '', // 表单项标签
      labelAlign: '', // 标签对齐方式
      type: 'text', // 表单项类型
      defaultValue: '', // 表单项默认值
      placeholder: '', // 表单项提示文本
      columnWidth: '200px', // 表单项列宽度
      size: '', // 表单项尺寸
      labelWidth: null, // 标签宽度
      labelHidden: false, // 是否隐藏标签
      readonly: false, // 是否只读
      disabled: false, // 是否禁用
      hidden: false, // 是否隐藏
      clearable: true, // 是否可清空
      showPassword: false, // 是否显示密码
      required: false, // 是否必填
      requiredHint: '', // 必填提示文本
      validation: '', // 表单项校验规则
      validationHint: '',

      //-------------------
      /**
       * @description 自定义组件属性
       */

      labelIconClass: null, //标签图标类名
      labelIconPosition: 'rear', //标签图标位置
      labelTooltip: null, //标签提示信息
      minLength: null, //最小长度限制
      maxLength: null, //最大长度限制
      showWordLimit: false, //是否显示字数统计
      prefixIcon: '', //前缀图标
      suffixIcon: '', //后缀图标
      appendButton: false, //是否显示追加按钮
      appendButtonDisabled: false, //追加按钮是否禁用
      buttonIcon: 'el-icon-search', //追加按钮图标

      //-------------------
      /**
       * @description 组件创建时触发的事件
       * @type {string}
       */
      onCreated: '',
      /**
       * @description 组件挂载到DOM上后触发的事件
       * @type {string}
       */
      onMounted: '',
      /**
       * @description 当组件接收到新的输入值时触发的事件
       * @type {string}
       */
      onInput: '',
      /**
       * @description 当组件的值发生改变时触发的事件
       * @type {string}
       */
      onChange: '',
      /**
       * @description 当组件获得焦点时触发的事件
       * @type {string}
       */
      onFocus: '',
      /**
       * @description 当组件失去焦点时触发的事件
       * @type {string}
       */
      onBlur: '',
      /**
       * @description 当组件需要验证时触发的事件
       * @type {string}
       */
      onValidate: '',
      /**
       * @description 当组件的按钮被点击时触发的事件
       * @type {string}
       */
      onAppendButtonClick: ''
    }
  },

  {
    name: '长文本',
    type: 'textarea',
    formItemFlag: true,
    options: {
      name: '',
      label: '',
      labelAlign: '',
      rows: 3,
      defaultValue: '',
      placeholder: '',
      columnWidth: '200px',
      size: '',
      labelWidth: null,
      labelHidden: false,
      readonly: false,
      disabled: false,
      hidden: false,
      required: false,
      requiredHint: '',
      validation: '',
      validationHint: '',
      //-------------------

      labelIconClass: null,
      labelIconPosition: 'rear',
      labelTooltip: null,
      minLength: null,
      maxLength: null,
      showWordLimit: false,
      //-------------------
      onCreated: '',
      onMounted: '',
      onInput: '',
      onChange: '',
      onFocus: '',
      onBlur: '',
      onValidate: ''
    }
  },

  {
    name: '富文本',
    type: 'rich-text',
    formItemFlag: true,
    options: {
      name: '',
      label: '',
      labelAlign: '',
      // rows: 3,
      defaultValue: '',
      placeholder: '',
      columnWidth: '200px',
      labelHidden: false,
      disabled: false,
      hidden: false,

      labelIconClass: null,
      labelIconPosition: 'rear',
      labelTooltip: null,
      //-------------------
      onCreated: '',
      onMounted: '',
      onInput: '',
      onChange: '',
      onFocus: '',
      onBlur: '',
      onValidate: ''
    }
  },

  {
    name: '计数器',
    type: 'number',
    formItemFlag: true,
    options: {
      name: '',
      label: '',
      labelAlign: '',
      defaultValue: 0,
      placeholder: '',
      columnWidth: '200px',
      size: '',
      labelWidth: null,
      labelHidden: false,
      disabled: false,
      hidden: false,
      required: false,
      requiredHint: '',
      validation: '',
      validationHint: '',
      //-------------------

      labelIconClass: null,
      labelIconPosition: 'rear',
      labelTooltip: null,
      min: -100000000000,
      max: 100000000000,
      precision: 0,
      step: 1,
      controlsPosition: 'right',
      //-------------------
      onCreated: '',
      onMounted: '',
      onChange: '',
      onFocus: '',
      onBlur: '',
      onValidate: ''
    }
  },

  {
    name: '单选项',
    type: 'radio',
    formItemFlag: true,
    options: {
      name: '',
      label: '',
      labelAlign: '',
      defaultValue: null,
      columnWidth: '200px',
      size: '',
      displayStyle: 'inline',
      buttonStyle: false,
      border: false,
      labelWidth: null,
      labelHidden: false,
      disabled: false,
      hidden: false,
      optionItems: [
        { label: 'radio 1', value: 1 },
        { label: 'radio 2', value: 2 },
        { label: 'radio 3', value: 3 }
      ],
      required: false,
      requiredHint: '',
      validation: '',
      validationHint: '',
      //-------------------

      labelIconClass: null,
      labelIconPosition: 'rear',
      labelTooltip: null,
      //-------------------
      onCreated: '',
      onMounted: '',
      onChange: '',
      onValidate: ''
    }
  },

  {
    name: '多选项',
    type: 'checkbox',
    formItemFlag: true,
    options: {
      name: '',
      label: '',
      labelAlign: '',
      defaultValue: [],
      columnWidth: '200px',
      size: '',
      displayStyle: 'inline',
      buttonStyle: false,
      border: false,
      labelWidth: null,
      labelHidden: false,
      disabled: false,
      hidden: false,
      // optionItems: [],
      required: false,
      requiredHint: '',
      validation: '',
      validationHint: '',
      //-------------------
      // customClass: '', //自定义css类名
      labelIconClass: null,
      labelIconPosition: 'rear',
      labelTooltip: null,
      //-------------------
      onCreated: '',
      onMounted: '',
      onChange: '',
      onValidate: ''
    }
  },

  {
    name: '下拉选项',
    type: 'select',
    formItemFlag: true,
    options: {
      name: '',
      label: '',
      labelAlign: '',
      defaultValue: '',
      placeholder: '',
      columnWidth: '200px',
      size: '',
      labelWidth: null,
      labelHidden: false,
      disabled: false,
      hidden: false,
      clearable: true,
      filterable: false,
      allowCreate: false,
      remote: false,
      automaticDropdown: false, //自动下拉
      multiple: false,
      multipleLimit: 0,
      optionItems: [
        { label: 'select 1', value: 1 },
        { label: 'select 2', value: 2 },
        { label: 'select 3', value: 3 }
      ],
      required: false,
      requiredHint: '',
      validation: '',
      validationHint: '',
      //-------------------

      labelIconClass: null,
      labelIconPosition: 'rear',
      labelTooltip: null,
      //-------------------
      onCreated: '',
      onMounted: '',
      onRemoteQuery: '',
      onChange: '',
      onFocus: '',
      onBlur: '',
      onValidate: ''
    }
  },

  {
    name: '时间',
    type: 'time',
    formItemFlag: true,
    options: {
      name: '',
      label: '',
      labelAlign: '',
      placeholder: '',
      columnWidth: '200px',
      size: '',
      labelWidth: null,
      labelHidden: false,
      readonly: false,
      disabled: false,
      hidden: false,
      clearable: true,
      editable: false,
      format: 'HH:mm:ss', //时间格式
      required: false,
      requiredHint: '',
      validation: '',
      validationHint: '',
      //-------------------

      labelIconClass: null,
      labelIconPosition: 'rear',
      labelTooltip: null,
      //-------------------
      onCreated: '',
      onMounted: '',
      onChange: '',
      onFocus: '',
      onBlur: '',
      onValidate: ''
    }
  },

  {
    name: '时间范围',
    type: 'time-range',
    formItemFlag: true,
    options: {
      name: '',
      label: '',
      labelAlign: '',
      startPlaceholder: '',
      endPlaceholder: '',
      columnWidth: '200px',
      size: '',
      labelWidth: null,
      labelHidden: false,
      readonly: false,
      disabled: false,
      hidden: false,
      clearable: true,
      editable: false,
      format: 'HH:mm:ss', //时间格式
      required: false,
      requiredHint: '',
      validation: '',
      validationHint: '',
      //-------------------

      labelIconClass: null,
      labelIconPosition: 'rear',
      labelTooltip: null,
      //-------------------
      onCreated: '',
      onMounted: '',
      onChange: '',
      onFocus: '',
      onBlur: '',
      onValidate: ''
    }
  },

  {
    name: '日期',
    type: 'date-picker',
    formItemFlag: true,
    options: {
      name: '',
      label: '',
      labelAlign: '',
      placeholder: '',
      columnWidth: '200px',
      size: '',
      labelWidth: null,
      labelHidden: false,
      readonly: false,
      disabled: false,
      hidden: false,
      clearable: true,
      editable: false,
      required: false,
      requiredHint: '',
      validation: '',
      validationHint: '',
      //-------------------

      labelIconClass: null,
      labelIconPosition: 'rear',
      labelTooltip: null,
      //-------------------
      onCreated: '',
      onMounted: '',
      onChange: '',
      onFocus: '',
      onBlur: '',
      onValidate: ''
    }
  },

  {
    name: '日期时间',
    type: 'date-time-picker',
    formItemFlag: true,
    options: {
      name: '',
      label: '',
      labelAlign: '',
      placeholder: '',
      columnWidth: '200px',
      size: '',
      labelWidth: null,
      labelHidden: false,
      readonly: false,
      disabled: false,
      hidden: false,
      clearable: true,
      editable: false,
      format: 'yyyy-MM-dd', //日期显示格式
      valueFormat: 'yyyy-MM-dd', //日期对象格式
      required: false,
      requiredHint: '',
      validation: '',
      validationHint: '',
      //-------------------

      labelIconClass: null,
      labelIconPosition: 'rear',
      labelTooltip: null,
      //-------------------
      onCreated: '',
      onMounted: '',
      onChange: '',
      onFocus: '',
      onBlur: '',
      onValidate: ''
    }
  },

  {
    name: '开关',
    type: 'switch',
    formItemFlag: true,
    options: {
      name: '',
      label: '',
      labelAlign: '',
      defaultValue: null,
      columnWidth: '200px',
      labelWidth: null,
      labelHidden: false,
      disabled: false,
      hidden: false,
      //-------------------

      labelIconClass: null,
      labelIconPosition: 'rear',
      labelTooltip: null,
      switchWidth: 40,
      activeText: '',
      inactiveText: '',
      activeColor: null,
      inactiveColor: null,
      //-------------------
      onCreated: '',
      onMounted: '',
      onChange: '',
      onValidate: ''
    }
  },
  {
    name: '注释文字',
    type: 'static-text',
    formItemFlag: false,
    options: {
      name: '',
      columnWidth: '200px',
      hidden: false,
      textContent: 'static text',
      textAlign: 'left',
      fontSize: '13px',
      preWrap: false, //是否自动换行
      //-------------------

      //-------------------
      onCreated: '',
      onMounted: ''
    }
  },

  {
    name: '一对一引用',
    type: 'input-select',
    icon: 'reference-field',
    formItemFlag: true,
    options: {
      name: '',
      label: '',
      labelAlign: '',
      type: 'text',
      placeholder: '',
      columnWidth: '200px',
      size: '',
      labelWidth: null,
      labelHidden: false,
      readonly: false,
      disabled: false,
      hidden: false,
      clearable: true,
      required: false,
      requiredHint: '',
      validation: '',
      validationHint: '',
      //-------------------
      buttonText: '选择',
      labelIconPosition: 'rear',
      prefixIcon: '',
      appendButtonDisabled: false,
      //-------------------
      onCreated: '',
      onMounted: '',
      onInput: '',
      onChange: '',
      onFocus: '',
      onBlur: '',
      onValidate: '',
      onAppendButtonClick: ''
    }
  },

  {
    name: '分割线',
    type: 'divider',
    formItemFlag: false,
    options: {
      name: '',
      label: '',
      columnWidth: '200px',
      direction: 'horizontal',
      contentPosition: 'center',
      hidden: false,
      //-------------------

      //-------------------
      onCreated: '',
      onMounted: ''
    }
  },
  {
    name: '地区选择',
    type: 'cascader-address',
    formItemFlag: true,
    options: {
      name: '',
      keyNameEnabled: false,
      keyName: '',
      label: '地区选择',
      labelAlign: '',
      defaultValue: '',
      placeholder: '',
      size: '',
      labelWidth: null,
      labelHidden: false,
      columnWidth: '200px',
      disabled: false,
      hidden: false,
      clearable: true,
      filterable: false,
      multiple: false,
      checkStrictly: false,
      dsEnabled: false,
      dsName: '',
      dataSetName: '',
      labelKey: 'label',
      valueKey: 'value',
      childrenKey: 'children',
      areaDataEnabled: true,
      required: true,
      requiredHint: '',
      customRule: '',
      customRuleHint: '',
      // customClass: [],
      labelIconClass: null,
      labelIconPosition: 'rear',
      labelTooltip: null,
      onCreated: '',
      onMounted: '',
      onChange: '',
      onFocus: '',
      onBlur: '',
      onValidate: ''
    }
  },
  {
    name: '整数字段',
    type: 'input-number',
    formItemFlag: true,
    options: {
      name: 'zhengshuziduan',
      keyNameEnabled: false,
      keyName: '',
      label: '整数字段',
      labelAlign: '',
      defaultValue: 0,
      placeholder: '',
      columnWidth: '200px',
      size: '',
      controls: true,
      labelWidth: null,
      labelHidden: false,
      labelWrap: false,
      disabled: false,
      hidden: false,
      required: true,
      requiredHint: '',
      validation: '',
      validationHint: '',
      formulaEnabled: false,
      formula: '',
      // customClass: [],
      labelIconClass: null,
      labelIconPosition: 'rear',
      labelTooltip: null,
      min: -100000000000,
      max: 100000000000,
      precision: 0,
      step: 1,
      controlsPosition: 'right',
      onCreated: '',
      onMounted: '',
      onChange: '',
      onFocus: '',
      onBlur: '',
      onValidate: ''
    }
  },
  {
    name: '精确小数',
    type: 'input-float',
    formItemFlag: true,
    options: {
      name: 'jingquexiaoshu',
      keyNameEnabled: false,
      keyName: '',
      label: '精确小数',
      labelAlign: '',
      defaultValue: 0,
      placeholder: '',
      columnWidth: '200px',
      size: '',
      controls: false,
      labelWidth: null,
      labelHidden: false,
      labelWrap: false,
      disabled: false,
      hidden: false,
      required: true,
      requiredHint: '',
      validation: '',
      validationHint: '',
      formulaEnabled: false,
      formula: '',

      labelIconClass: null,
      labelIconPosition: 'rear',
      labelTooltip: null,
      min: -999999999,
      max: 999999999,
      precision: 2,
      step: 1,
      controlsPosition: 'right',
      onCreated: '',
      onMounted: '',
      onChange: '',
      onFocus: '',
      onBlur: '',
      onValidate: ''
    }
  },
  {
    name: '金额',
    type: 'input-price',
    formItemFlag: true,
    options: {
      name: 'jine',
      keyNameEnabled: false,
      keyName: '',
      label: '金额',
      labelAlign: '',
      defaultValue: 0,
      placeholder: '',
      columnWidth: '200px',
      size: '',
      controls: false,
      labelWidth: null,
      labelHidden: false,
      labelWrap: false,
      disabled: false,
      hidden: false,
      required: true,
      requiredHint: '',
      validation: '',
      validationHint: '',
      formulaEnabled: false,
      formula: '',

      labelIconClass: null,
      labelIconPosition: 'rear',
      labelTooltip: null,
      min: -999999999,
      max: 999999999,
      precision: 2,
      step: 0.01,
      controlsPosition: 'right',
      onCreated: '',
      onMounted: '',
      onChange: '',
      onFocus: '',
      onBlur: '',
      onValidate: ''
    }
  },
  {
    name: '百分比',
    type: 'input-percentage',
    formItemFlag: true,
    options: {
      name: '',
      label: '',
      labelAlign: '',
      columnWidth: '200px',
      showStops: true,
      size: '',
      labelWidth: null,
      labelHidden: false,
      disabled: false,
      hidden: false,
      required: false,
      requiredHint: '',
      validation: '',
      validationHint: '',
      //-------------------

      labelIconClass: null,
      labelIconPosition: 'rear',
      labelTooltip: null,
      min: 0,
      max: 100,
      step: 10,
      range: false,
      //vertical: false,
      height: null,
      //-------------------
      onCreated: '',
      onMounted: '',
      onChange: '',
      onValidate: ''
    }
  },
  {
    name: '标签',
    type: 'tag',
    formItemFlag: true,
    options: {
      name: '',
      label: '',
      labelAlign: '',
      defaultValue: [],
      placeholder: '',
      columnWidth: '200px',
      size: '',
      labelWidth: null,
      labelHidden: false,
      hidden: false,
      requiredHint: '',
      validation: '',
      validationHint: '',
      //-------------------
      labelIconClass: null,
      labelIconPosition: 'rear',
      labelTooltip: null,
      //-------------------
      onCreated: '',
      onMounted: '',
      onRemoteQuery: '',
      onChange: '',
      onFocus: '',
      onBlur: '',
      onValidate: ''
    }
  }

  //
]

/**
 * 高级字段数组
 * @type {AdvancedField[]}
 */
export const advancedFields = [
  {
    name: '图片上传',
    type: 'image',
    formItemFlag: true,
    options: {
      name: '',
      label: '',
      labelAlign: '',
      labelWidth: null,
      labelHidden: false,
      columnWidth: '200px',
      disabled: false,
      hidden: false,
      required: false,
      requiredHint: '',
      customRule: '',
      customRuleHint: '',
      limit: 3,
      //-------------------
      //headers: [],
      //-------------------

      labelIconClass: null,
      labelIconPosition: 'rear',
      labelTooltip: null,
      //-------------------
      onCreated: '',
      onMounted: '',
      onBeforeUpload: '',
      onUploadSuccess: '',
      onUploadError: '',
      onFileRemove: '',
      onValidate: ''
      //onFileChange: '',
    }
  },

  {
    name: '文件上传',
    type: 'file',
    formItemFlag: true,
    options: {
      name: '',
      label: '',
      labelAlign: '',
      labelWidth: null,
      labelHidden: false,
      columnWidth: '200px',
      disabled: false,
      hidden: false,
      required: false,
      requiredHint: '',
      customRule: '',
      customRuleHint: '',
      //-------------------
      // uploadTip: '',
      withCredentials: false,
      // multipleSelect: false, //多文件上传
      showFileList: true,
      limit: 3,
      // fileMaxSize: 5, //MB
      fileTypes: ['doc', 'docx', 'xls', 'xlsx']
    }
  },

  {
    name: '级联多选',
    type: 'cascader',
    formItemFlag: true,
    options: {
      name: '',
      label: '',
      labelAlign: '',
      defaultValue: '',
      placeholder: '',
      size: '',
      labelWidth: null,
      labelHidden: false,
      columnWidth: '200px',
      disabled: false,
      hidden: false,
      clearable: true,
      filterable: false,
      multiple: true,
      checkStrictly: false, //可选择任意一级选项，默认不开启
      showAllLevels: true, //显示完整路径
      required: false,
      requiredHint: '',
      customRule: '',
      customRuleHint: '',
      //-------------------

      labelIconClass: null,
      labelIconPosition: 'rear',
      labelTooltip: null,
      //-------------------
      onCreated: '',
      onMounted: '',
      onChange: '',
      onFocus: '',
      onBlur: '',
      onValidate: ''
    }
  },
  {
    name: '级联单选',
    type: 'cascader-radio',
    formItemFlag: true,
    options: {
      name: '',
      label: '',
      labelAlign: '',
      placeholder: '',
      size: '',
      labelWidth: null,
      labelHidden: false,
      columnWidth: '200px',
      disabled: false,
      hidden: false,
      clearable: true,
      filterable: false,
      checkStrictly: false, //可选择任意一级选项，默认不开启
      showAllLevels: true, //显示完整路径
      required: false,
      requiredHint: '',
      customRule: '',
      customRuleHint: '',
      //-------------------

      labelIconClass: null,
      labelIconPosition: 'rear',
      labelTooltip: null,
      //-------------------
      onCreated: '',
      onMounted: '',
      onChange: '',
      onFocus: '',
      onBlur: '',
      onValidate: ''
    }
  }
]

/**
 * 自定义字段数组
 */
export const customFields = []

/**
 * 添加容器部件模式
 * @param {Object} containerSchema - 容器部件模式对象
 */
export function addContainerWidgetSchema(containerSchema) {
  containers.push(containerSchema)
}

/**
 * 添加基础字段模式
 * @param {Object} fieldSchema - 基础字段模式对象
 */
export function addBasicFieldSchema(fieldSchema) {
  basicFields.push(fieldSchema)
}

/**
 * 添加高级字段模式
 * @param {Object} fieldSchema - 高级字段模式对象
 */
export function addAdvancedFieldSchema(fieldSchema) {
  advancedFields.push(fieldSchema)
}

/**
 * 添加自定义部件模式
 * @param {Object} widgetSchema - 自定义部件模式对象
 */
export function addCustomWidgetSchema(widgetSchema) {
  customFields.push(widgetSchema)
}
