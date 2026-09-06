/**
 * 表单构建器 - 类型定义
 * 核心类型体系，所有组件、配置、节点均基于此文件定义。
 * 新增组件类型时，需在此文件添加 ComponentType 枚举值。
 */
export type ComponentCategory = 'basic' | 'layout' | 'action'

export type ComponentType =
  | 'input'
  | 'textarea'
  | 'input-number'
  | 'password'
  | 'select'
  | 'multi-select'
  | 'radio'
  | 'checkbox'
  | 'switch'
  | 'date'
  | 'daterange'
  | 'time'
  | 'datetime'
  | 'cascader'
  | 'tree-select'
  | 'slider'
  | 'rate'
  | 'color-picker'
  | 'upload'
  | 'row'
  | 'col'
  | 'button'

export interface OptionItem {
  label: string
  value: string | number
  children?: OptionItem[]
}

export interface FormNode {
  id: string
  type: ComponentType
  label: string
  field: string
  props: Record<string, unknown>
  children?: FormNode[]
}

export interface PropConfig {
  name: string
  label: string
  type:
    | 'input'
    | 'select'
    | 'switch'
    | 'number'
    | 'color'
    | 'slider'
    | 'option-editor'
  options?: { label: string; value: string | number }[]
  default?: unknown
  placeholder?: string
}

export interface ComponentConfig {
  type: ComponentType
  label: string
  icon: string
  category: ComponentCategory
  defaultProps: Record<string, unknown>
  propsConfig: PropConfig[]
  renderTag: string
  canHaveChildren?: boolean
  allowedChildren?: ComponentType[]
  noFormItem?: boolean
  noLabel?: boolean
}

export interface FormConfig {
  labelWidth: number
  labelPosition: 'left' | 'right' | 'top'
  size: 'default' | 'small' | 'large'
  inline: boolean
  disabled: boolean
  hideRequiredAsterisk: boolean
  statusIcon: boolean
  validateOnRuleChange: boolean
  showFormButton: boolean
  formRefName: string
  formModelName: string
  rulesName: string
}

export interface GeneratedCode {
  template: string
  script: string
  full: string
}
