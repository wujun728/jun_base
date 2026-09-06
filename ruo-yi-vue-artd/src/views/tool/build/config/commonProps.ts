/**
 * 表单构建器 - 公共属性配置
 * 所有组件共享的公共属性定义。
 */
import type { PropConfig } from '../types/form'

export const commonProps: PropConfig[] = [
  { name: 'label', label: '标签', type: 'input' },
  { name: 'field', label: '字段', type: 'input' },
  { name: 'placeholder', label: '占位提示', type: 'input' },
  { name: 'defaultValue', label: '默认值', type: 'input' },
  { name: 'required', label: '是否必填', type: 'switch' },
  { name: 'disabled', label: '是否禁用', type: 'switch' },
  { name: 'hidden', label: '是否隐藏', type: 'switch' },
  { name: 'readonly', label: '是否只读', type: 'switch' },
  { name: 'width', label: '宽度', type: 'input', placeholder: '如: 200px' },
  {
    name: 'labelWidth',
    label: '标签宽度',
    type: 'input',
    placeholder: '如: 100px'
  },
  { name: 'className', label: '自定义类名', type: 'input' },
  { name: 'style', label: '自定义样式', type: 'input' }
]
