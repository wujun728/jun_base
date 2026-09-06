import type { FormNode, FormConfig, ComponentType } from '../types/form'
import type { ComponentConfig } from '../types/form'

/**
 * 表单构建器 - 核心设计器状态管理
 * 使用模块级 ref/reactive 实现全局单例状态。
 * 职责：管理设计区节点列表、选中状态、表单全局配置、增删改查导入导出。
 */
import { ref, reactive, computed } from 'vue'

import {
  basicComponents,
  layoutComponents,
  actionComponents
} from '../config/componentConfig'
import {
  generateId,
  createDefaultNode,
  cloneNode,
  removeNodeById,
  moveNodeUp,
  moveNodeDown
} from '../utils/schema'

const designerNodes = ref<FormNode[]>([])
const selectedId = ref<string | null>(null)
const formConfig = reactive<FormConfig>({
  labelWidth: 100,
  labelPosition: 'left',
  size: 'default',
  inline: false,
  disabled: false,
  hideRequiredAsterisk: false,
  statusIcon: false,
  validateOnRuleChange: true,
  showFormButton: true,
  formRefName: 'formRef',
  formModelName: 'formModel',
  rulesName: 'rules'
})

const selectedNode = computed(() => {
  if (!selectedId.value) return null
  return findNode(designerNodes.value, selectedId.value)
})

function findNode(nodes: FormNode[], id: string): FormNode | null {
  for (const node of nodes) {
    if (node.id === id) return node
    if (node.children) {
      const found = findNode(node.children, id)
      if (found) return found
    }
  }
  return null
}

function findNodeParent(
  nodes: FormNode[],
  id: string
): { parent: FormNode[]; index: number } | null {
  const index = nodes.findIndex((n) => n.id === id)
  if (index !== -1) return { parent: nodes, index }
  for (const node of nodes) {
    if (node.children) {
      const result = findNodeParent(node.children, id)
      if (result) return result
    }
  }
  return null
}

function createDesignerNode(type: ComponentType): FormNode {
  const config = getAllComponents().find((c) => c.type === type)
  const node = createDefaultNode(type)
  if (config) {
    node.props = { ...config.defaultProps }
    node.label = config.label
  }
  if (type === 'row') {
    const col = createDefaultNode('col')
    col.props = { span: 12 }
    col.label = '列'
    node.children = [col]
  }
  return node
}

function addNode(type: ComponentType, parentId?: string) {
  const node = createDesignerNode(type)
  if (parentId) {
    const parent = findNode(designerNodes.value, parentId)
    if (parent) {
      if (!parent.children) parent.children = []
      parent.children.push(node)
    }
  } else {
    designerNodes.value.push(node)
  }
  selectedId.value = node.id
}

function selectNode(id: string | null) {
  selectedId.value = id
}

function deleteNode(id: string) {
  removeNodeById(designerNodes.value, id)
  if (selectedId.value === id) selectedId.value = null
}

function copyNode(id: string) {
  const info = findNodeParent(designerNodes.value, id)
  if (!info) return
  const { parent, index } = info
  const copy = cloneNode(parent[index])
  copy.id = generateId()
  parent.splice(index + 1, 0, copy)
  selectedId.value = copy.id
}

function moveUp(id: string) {
  moveNodeUp(designerNodes.value, id)
}

function moveDown(id: string) {
  moveNodeDown(designerNodes.value, id)
}

function getAllComponents(): ComponentConfig[] {
  return [...basicComponents, ...layoutComponents, ...actionComponents]
}

function getComponentConfig(type: string): ComponentConfig | undefined {
  return getAllComponents().find((c) => c.type === type)
}

function clearAll() {
  designerNodes.value = []
  selectedId.value = null
}

function importJson(json: string) {
  try {
    const data = JSON.parse(json)
    if (Array.isArray(data)) {
      designerNodes.value = data
      selectedId.value = null
    }
  } catch {
    throw new Error('JSON 格式错误')
  }
}

function exportJson(): string {
  return JSON.stringify(designerNodes.value, null, 2)
}

export function useDesigner() {
  return {
    designerNodes,
    selectedId,
    selectedNode,
    formConfig,
    createDesignerNode,
    addNode,
    selectNode,
    deleteNode,
    copyNode,
    moveUp,
    moveDown,
    getAllComponents,
    getComponentConfig,
    clearAll,
    importJson,
    exportJson
  }
}
