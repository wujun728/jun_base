/**
 * 表单构建器 - Schema 工具函数
 * 提供节点创建、查找、克隆、移动、删除等纯函数操作。
 * 不依赖 Vue 响应式，可在任意场景复用。
 */
import type { FormNode, ComponentType } from '../types/form'

let idCounter = 0

export function generateId(): string {
  idCounter++
  return `field_${Date.now()}_${idCounter}`
}

export function createDefaultNode(type: ComponentType): FormNode {
  const id = generateId()
  const base: FormNode = {
    id,
    type,
    label: '',
    field: '',
    props: {}
  }
  if (type === 'row') {
    base.label = '行'
    base.field = ''
    base.children = []
  } else if (type === 'col') {
    base.label = '列'
    base.field = ''
    base.children = []
  } else if (type === 'button') {
    base.label = '按钮'
    base.field = ''
  }
  return base
}

export function cloneNode(node: FormNode): FormNode {
  return JSON.parse(JSON.stringify(node))
}

export function findNodeById(nodes: FormNode[], id: string): FormNode | null {
  for (const node of nodes) {
    if (node.id === id) return node
    if (node.children) {
      const found = findNodeById(node.children, id)
      if (found) return found
    }
  }
  return null
}

export function removeNodeById(nodes: FormNode[], id: string): boolean {
  const index = nodes.findIndex((n) => n.id === id)
  if (index !== -1) {
    nodes.splice(index, 1)
    return true
  }
  for (const node of nodes) {
    if (node.children && removeNodeById(node.children, id)) {
      return true
    }
  }
  return false
}

export function moveNodeUp(nodes: FormNode[], id: string): boolean {
  const idx = nodes.findIndex((n) => n.id === id)
  if (idx > 0) {
    ;[nodes[idx - 1], nodes[idx]] = [nodes[idx], nodes[idx - 1]]
    return true
  }
  for (const node of nodes) {
    if (node.children && moveNodeUp(node.children, id)) {
      return true
    }
  }
  return false
}

export function moveNodeDown(nodes: FormNode[], id: string): boolean {
  const idx = nodes.findIndex((n) => n.id === id)
  if (idx !== -1 && idx < nodes.length - 1) {
    ;[nodes[idx], nodes[idx + 1]] = [nodes[idx + 1], nodes[idx]]
    return true
  }
  for (const node of nodes) {
    if (node.children && moveNodeDown(node.children, id)) {
      return true
    }
  }
  return false
}
