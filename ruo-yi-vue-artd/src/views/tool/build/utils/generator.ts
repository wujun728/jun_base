/**
 * 表单构建器 - 代码生成器
 * 根据设计区节点树和表单配置，生成完整的 Vue3 + Element Plus 代码。
 * 支持所有组件类型、布局嵌套、自定义命名、提交/重置按钮。
 */
import type { FormNode, FormConfig } from '../types/form'

/** 获取选项字段名（用于生成 options 变量） */
function getOptionFieldName(type: string): string {
  if (
    ['select', 'multi-select', 'radio', 'checkbox', 'cascader'].includes(type)
  )
    return 'options'
  return ''
}

/** 缩进工具 */
function indent(level: number): string {
  return '  '.repeat(level)
}

/** 生成组件属性字符串 */
function genProps(props: Record<string, unknown>, level: number): string {
  const lines: string[] = []
  for (const [key, value] of Object.entries(props)) {
    if (['options', 'data', 'children', 'text'].includes(key)) continue
    if (value === undefined || value === null || value === '') continue
    if (typeof value === 'boolean') {
      if (value) lines.push(indent(level) + key)
    } else if (typeof value === 'string') {
      lines.push(indent(level) + key + '="' + value + '"')
    } else if (typeof value === 'number') {
      lines.push(indent(level) + ':' + key + '="' + value + '"')
    }
  }
  return lines.join('\n')
}

/** 递归生成单个节点的模板代码 */
function genNode(node: FormNode, level: number, varName: string): string {
  const lines: string[] = []

  if (node.type === 'button') {
    const text = (node.props.text as string) || '按钮'
    const btnType = (node.props.type as string) || 'primary'
    lines.push(indent(level) + '<el-button type="' + btnType + '"')
    if (node.props.size)
      lines.push(indent(level + 1) + 'size="' + node.props.size + '"')
    if (node.props.icon)
      lines.push(indent(level + 1) + 'icon="' + node.props.icon + '"')
    if (node.props.loading) lines.push(indent(level + 1) + 'loading')
    if (node.props.plain) lines.push(indent(level + 1) + 'plain')
    if (node.props.round) lines.push(indent(level + 1) + 'round')
    if (node.props.circle) lines.push(indent(level + 1) + 'circle')
    if (node.props.disabled) lines.push(indent(level + 1) + 'disabled')
    if (node.props.clickEvent)
      lines.push(indent(level + 1) + '@click="' + node.props.clickEvent + '"')
    lines.push(indent(level) + '>' + text + '</el-button>')
    return lines.join('\n')
  }

  if (node.type === 'row') {
    const gutter = node.props.gutter
      ? ' :gutter="' + node.props.gutter + '"'
      : ''
    lines.push(indent(level) + '<el-row' + gutter + '>')
    if (node.children) {
      for (const child of node.children) {
        lines.push(genNode(child, level + 1, varName))
      }
    }
    lines.push(indent(level) + '</el-row>')
    return lines.join('\n')
  }

  if (node.type === 'col') {
    const span = node.props.span || 24
    const offset = node.props.offset
      ? ' :offset="' + node.props.offset + '"'
      : ''
    lines.push(indent(level) + '<el-col :span="' + span + '"' + offset + '>')
    if (node.children) {
      for (const child of node.children) {
        lines.push(genNode(child, level + 1, varName))
      }
    }
    lines.push(indent(level) + '</el-col>')
    return lines.join('\n')
  }

  const field = node.field || node.id
  const label = node.label || ''

  lines.push(
    indent(level) + '<el-form-item label="' + label + '" prop="' + field + '"'
  )
  if (node.props.required) lines.push(indent(level + 1) + 'required')
  if (node.props.labelWidth)
    lines.push(
      indent(level + 1) + 'label-width="' + node.props.labelWidth + '"'
    )
  lines.push(indent(level) + '>')

  const tagProps = genProps(node.props, level + 1)

  if (node.type === 'textarea') {
    lines.push(
      indent(level + 1) +
        '<el-input type="textarea" v-model="' +
        varName +
        '.' +
        field +
        '"'
    )
    if (tagProps) lines.push(tagProps)
    lines.push(indent(level + 1) + '/>')
  } else if (node.type === 'password') {
    lines.push(
      indent(level + 1) +
        '<el-input type="password" v-model="' +
        varName +
        '.' +
        field +
        '" show-password'
    )
    if (tagProps) lines.push(tagProps)
    lines.push(indent(level + 1) + '/>')
  } else if (node.type === 'select' || node.type === 'multi-select') {
    const optName = field + 'Options'
    lines.push(
      indent(level + 1) + '<el-select v-model="' + varName + '.' + field + '"'
    )
    if (tagProps) lines.push(tagProps)
    lines.push(indent(level + 1) + '>')
    lines.push(
      indent(level + 2) +
        '<el-option v-for="item in ' +
        optName +
        '" :key="item.value" :label="item.label" :value="item.value" />'
    )
    lines.push(indent(level + 1) + '</el-select>')
  } else if (node.type === 'radio') {
    const optName = field + 'Options'
    lines.push(
      indent(level + 1) +
        '<el-radio-group v-model="' +
        varName +
        '.' +
        field +
        '">'
    )
    lines.push(
      indent(level + 2) +
        '<el-radio v-for="item in ' +
        optName +
        '" :key="item.value" :label="item.value">{{ item.label }}</el-radio>'
    )
    lines.push(indent(level + 1) + '</el-radio-group>')
  } else if (node.type === 'checkbox') {
    const optName = field + 'Options'
    lines.push(
      indent(level + 1) +
        '<el-checkbox-group v-model="' +
        varName +
        '.' +
        field +
        '">'
    )
    lines.push(
      indent(level + 2) +
        '<el-checkbox v-for="item in ' +
        optName +
        '" :key="item.value" :label="item.value">{{ item.label }}</el-checkbox>'
    )
    lines.push(indent(level + 1) + '</el-checkbox-group>')
  } else if (node.type === 'cascader') {
    const optName = field + 'Options'
    lines.push(
      indent(level + 1) +
        '<el-cascader v-model="' +
        varName +
        '.' +
        field +
        '" :options="' +
        optName +
        '"'
    )
    if (tagProps) lines.push(tagProps)
    lines.push(indent(level + 1) + '/>')
  } else if (node.type === 'tree-select') {
    const dataName = field + 'Data'
    lines.push(
      indent(level + 1) +
        '<el-tree-select v-model="' +
        varName +
        '.' +
        field +
        '" :data="' +
        dataName +
        '"'
    )
    if (tagProps) lines.push(tagProps)
    lines.push(indent(level + 1) + '/>')
  } else if (node.type === 'switch') {
    lines.push(
      indent(level + 1) + '<el-switch v-model="' + varName + '.' + field + '"'
    )
    if (tagProps) lines.push(tagProps)
    lines.push(indent(level + 1) + '/>')
  } else if (node.type === 'upload') {
    lines.push(
      indent(level + 1) + '<el-upload v-model="' + varName + '.' + field + '"'
    )
    if (tagProps) lines.push(tagProps)
    lines.push(indent(level + 1) + '/>')
  } else if (node.type === 'daterange') {
    lines.push(
      indent(level + 1) +
        '<el-date-picker v-model="' +
        varName +
        '.' +
        field +
        '" type="daterange"'
    )
    if (tagProps) lines.push(tagProps)
    lines.push(indent(level + 1) + '/>')
  } else {
    const tag =
      node.type === 'datetime'
        ? 'el-date-picker'
        : node.type === 'date'
          ? 'el-date-picker'
          : node.type === 'time'
            ? 'el-time-picker'
            : node.type === 'input-number'
              ? 'el-input-number'
              : node.type === 'color-picker'
                ? 'el-color-picker'
                : node.type === 'slider'
                  ? 'el-slider'
                  : node.type === 'rate'
                    ? 'el-rate'
                    : 'el-input'
    lines.push(
      indent(level + 1) + '<' + tag + ' v-model="' + varName + '.' + field + '"'
    )
    if (tagProps) lines.push(tagProps)
    lines.push(indent(level + 1) + '/>')
  }

  lines.push(indent(level) + '</el-form-item>')
  return lines.join('\n')
}

export function generateCode(nodes: FormNode[], config: FormConfig): string {
  const varName = config.formModelName || 'formModel'
  const refName = config.formRefName || 'formRef'
  const rulesName = config.rulesName || 'rules'
  const lines: string[] = []

  lines.push('<template>')
  lines.push(indent(1) + '<el-form')
  lines.push(indent(2) + 'ref="' + refName + '"')
  lines.push(indent(2) + ':model="' + varName + '"')
  lines.push(indent(2) + ':rules="' + rulesName + '"')
  lines.push(indent(2) + 'label-width="' + config.labelWidth + 'px"')
  if (config.labelPosition !== 'left')
    lines.push(indent(2) + 'label-position="' + config.labelPosition + '"')
  if (config.size !== 'default')
    lines.push(indent(2) + 'size="' + config.size + '"')
  if (config.inline) lines.push(indent(2) + 'inline')
  if (config.disabled) lines.push(indent(2) + 'disabled')
  if (config.hideRequiredAsterisk)
    lines.push(indent(2) + 'hide-required-asterisk')
  if (config.statusIcon) lines.push(indent(2) + 'status-icon')
  lines.push(indent(2) + '>')

  for (const node of nodes) {
    lines.push('')
    lines.push(genNode(node, 2, varName))
  }

  if (config.showFormButton) {
    lines.push('')
    lines.push(indent(2) + '<el-form-item>')
    lines.push(
      indent(3) +
        '<el-button type="primary" @click="submitForm">提交</el-button>'
    )
    lines.push(indent(3) + '<el-button @click="resetForm">重置</el-button>')
    lines.push(indent(2) + '</el-form-item>')
  }

  lines.push('')
  lines.push(indent(1) + '</el-form>')
  lines.push('</template>')
  lines.push('')
  lines.push('<script setup lang="ts">')
  lines.push('import { ref, reactive } from "vue"')
  lines.push('import type { FormInstance } from "element-plus"')
  lines.push('')

  const formFields: string[] = []
  const rules: Record<string, unknown[]> = {}
  const optionsMap: Record<string, unknown[]> = {}

  function collectFields(ns: FormNode[]) {
    for (const node of ns) {
      if (node.type === 'row' || node.type === 'col') {
        if (node.children) collectFields(node.children)
        continue
      }
      if (node.type === 'button') continue
      const field = node.field || node.id
      formFields.push(field)
      if (node.props.required) {
        rules[field] = [
          {
            required: true,
            message: '请输入' + (node.label || field),
            trigger: 'blur'
          }
        ]
      }
      const optField = getOptionFieldName(node.type)
      if (optField && node.props[optField]) {
        optionsMap[field + 'Options'] = node.props[optField] as unknown[]
      }
      if (node.type === 'tree-select' && node.props.data) {
        optionsMap[field + 'Data'] = node.props.data as unknown[]
      }
    }
  }

  collectFields(nodes)

  const formModel: Record<string, unknown> = {}
  for (const f of formFields) {
    formModel[f] = null
  }

  lines.push('const ' + refName + ' = ref<FormInstance>()')
  lines.push('')
  lines.push(
    'const ' +
      varName +
      ' = reactive(' +
      JSON.stringify(formModel).replace(/"null"/g, 'null') +
      ')'
  )
  lines.push('')
  if (Object.keys(rules).length > 0) {
    lines.push('const ' + rulesName + ' = ' + JSON.stringify(rules, null, 2))
  } else {
    lines.push('const ' + rulesName + ' = {}')
  }
  lines.push('')

  for (const [key, val] of Object.entries(optionsMap)) {
    lines.push('const ' + key + ' = ' + JSON.stringify(val, null, 2))
  }

  if (Object.keys(optionsMap).length > 0) lines.push('')

  lines.push('const submitForm = async () => {')
  lines.push('  if (!' + refName + '.value) return')
  lines.push('  await ' + refName + '.value.validate((valid) => {')
  lines.push('    if (valid) {')
  lines.push('      console.log(' + varName + ')')
  lines.push('    }')
  lines.push('  })')
  lines.push('}')
  lines.push('')
  lines.push('const resetForm = () => {')
  lines.push('  ' + refName + '.value?.resetFields()')
  lines.push('}')
  lines.push('</script>')

  return lines.join('\n')
}

export function generatePreviewHtml(
  nodes: FormNode[],
  config: FormConfig
): string {
  const varName = 'formModel'
  const lines: string[] = []

  lines.push(
    '<el-form :model="' +
      varName +
      '" label-width="' +
      config.labelWidth +
      'px"'
  )
  if (config.labelPosition !== 'left')
    lines.push(indent(1) + 'label-position="' + config.labelPosition + '"')
  if (config.size !== 'default')
    lines.push(indent(1) + 'size="' + config.size + '"')
  if (config.inline) lines.push(indent(1) + 'inline')
  if (config.disabled) lines.push(indent(1) + 'disabled')
  lines.push('>')

  for (const node of nodes) {
    lines.push('')
    lines.push(genNode(node, 1, varName))
  }

  lines.push('')
  lines.push('</el-form>')
  return lines.join('\n')
}
