<!--
 * 表单构建器 - 画布单项
 * 递归渲染组件节点，支持 Row/Col 无限嵌套。
 * 提供选中高亮、Hover 边框、上移/下移/复制/删除操作。
 -->
<template>
  <div
    class="canvas-draggable-item canvas-item group relative"
    :class="{
      'is-selected': isSelected,
      'is-hover': isHover,
      'is-row': node.type === 'row',
      'is-col': node.type === 'col',
      'is-button': node.type === 'button'
    }"
    @click.stop="selectNode(node.id)"
    @mouseenter="isHover = true"
    @mouseleave="isHover = false">
    <!-- Row -->
    <template v-if="node.type === 'row'">
      <div class="px-3 py-2">
        <div class="mb-2 flex items-center gap-2">
          <el-icon :size="14"><Grid /></el-icon>
          <span class="text-xs font-medium text-gray-500">行容器</span>
          <span v-if="node.props.gutter" class="text-xs text-gray-400">
            gutter: {{ node.props.gutter }}
          </span>
        </div>
        <VueDraggable
          v-model="node.children!"
          group="form-components"
          :animation="200"
          :clone="cloneCanvasNode"
          :empty-insert-threshold="60"
          ghost-class="ghost"
          chosen-class="sortable-chosen"
          drag-class="sortable-drag"
          class="row-canvas flex min-h-[60px] flex-wrap items-start"
          :style="rowGutterStyle"
          @add="handleAdd">
          <div
            v-for="col in node.children!"
            :key="col.id"
            class="col-wrapper"
            :style="getColStyle(col)">
            <div
              class="relative min-h-[50px] rounded-md border border-dashed border-gray-300 bg-gray-50/50 p-2"
              :class="{ 'is-col-selected': selectedId === col.id }"
              @click.stop="selectNode(col.id)">
              <div class="mb-1 flex items-center gap-1">
                <el-icon :size="12"><Grid /></el-icon>
                <span class="text-xs text-gray-400">
                  列 {{ col.props.span || 24 }}
                </span>
              </div>
              <VueDraggable
                v-model="col.children!"
                group="form-components"
                :animation="200"
                :clone="cloneCanvasNode"
                :empty-insert-threshold="60"
                ghost-class="ghost"
                chosen-class="sortable-chosen"
                drag-class="sortable-drag"
                class="nested-canvas-list min-h-[30px] space-y-1"
                @add="handleAdd">
                <CanvasItem
                  v-for="child in col.children!"
                  :key="child.id"
                  :node="child"
                  :depth="(depth ?? 0) + 1"
                  @select="(id: string) => $emit('select', id)" />
                <div
                  v-if="!col.children?.length"
                  class="py-2 text-center text-xs text-gray-300">
                  拖入组件
                </div>
              </VueDraggable>
              <div
                v-if="selectedId === col.id"
                class="absolute -top-3 right-1 z-10 flex items-center gap-1">
                <el-button circle size="small" @click.stop="copyNode(col.id)">
                  <el-icon><CopyDocument /></el-icon>
                </el-button>
                <el-button
                  circle
                  size="small"
                  type="danger"
                  @click.stop="deleteNode(col.id)">
                  <el-icon><Delete /></el-icon>
                </el-button>
              </div>
            </div>
          </div>
          <div
            v-if="!node.children?.length"
            class="w-full py-4 text-center text-xs text-gray-300">
            拖入列容器
          </div>
        </VueDraggable>
      </div>
    </template>

    <!-- Col (standalone) -->
    <template v-else-if="node.type === 'col'">
      <div class="p-2">
        <div class="mb-1 flex items-center gap-1">
          <el-icon :size="12"><Grid /></el-icon>
          <span class="text-xs text-gray-400">
            列 {{ node.props.span || 24 }}
          </span>
        </div>
        <VueDraggable
          v-model="node.children!"
          group="form-components"
          :animation="200"
          :clone="cloneCanvasNode"
          :empty-insert-threshold="60"
          ghost-class="ghost"
          chosen-class="sortable-chosen"
          drag-class="sortable-drag"
          class="nested-canvas-list min-h-[30px] space-y-1"
          @add="handleAdd">
          <CanvasItem
            v-for="child in node.children || []"
            :key="child.id"
            :node="child"
            :depth="(depth ?? 0) + 1"
            @select="(id: string) => $emit('select', id)" />
          <div
            v-if="!node.children?.length"
            class="py-2 text-center text-xs text-gray-300">
            拖入组件
          </div>
        </VueDraggable>
      </div>
    </template>

    <!-- Button -->
    <template v-else-if="node.type === 'button'">
      <div class="flex items-center justify-between px-4 py-3">
        <div class="flex items-center gap-2">
          <el-icon :size="16"><Plus /></el-icon>
          <span class="text-sm">
            {{ (node.props.text as string) || '按钮' }}
          </span>
          <el-tag type="info" effect="plain" class="ml-1">
            {{ node.props.type || 'primary' }}
          </el-tag>
        </div>
      </div>
    </template>

    <!-- Form field -->
    <template v-else>
      <div class="flex items-center justify-between px-4 py-3">
        <div class="flex min-w-0 flex-1 items-center gap-3">
          <!-- <el-icon :size="16" class="drag-handle cursor-grab text-gray-400">
            <Rank />
          </el-icon> -->
          <div class="flex min-w-0 items-center gap-2">
            <span class="truncate text-sm font-medium text-gray-700">
              {{ node.label || '未命名' }}
            </span>
            <el-tag effect="plain" class="shrink-0">
              {{ getTypeLabel(node.type) }}
            </el-tag>
          </div>
        </div>
        <div class="flex items-center gap-1">
          <span v-if="node.field" class="mr-2 text-xs text-gray-400">
            {{ node.field }}
          </span>
        </div>
      </div>
    </template>

    <!-- Selection border -->
    <div
      v-if="isSelected"
      class="pointer-events-none absolute inset-0 rounded-lg border-2 border-primary"></div>

    <!-- Actions -->
    <div
      v-if="isHover || isSelected"
      class="absolute -top-4 right-2 z-500 flex items-center gap-1 rounded-lg px-1 py-0.5">
      <el-button circle size="small" @click.stop="moveUp(node.id)">
        <el-icon><ArrowUp /></el-icon>
      </el-button>
      <el-button circle size="small" @click.stop="moveDown(node.id)">
        <el-icon><ArrowDown /></el-icon>
      </el-button>
      <el-button circle size="small" @click.stop="copyNode(node.id)">
        <el-icon><CopyDocument /></el-icon>
      </el-button>
      <el-button
        circle
        size="small"
        type="danger"
        @click.stop="deleteNode(node.id)">
        <el-icon><Delete /></el-icon>
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { FormNode } from '../types/form'

import {
  Grid,
  Plus,
  ArrowUp,
  ArrowDown,
  CopyDocument,
  Delete
} from '@element-plus/icons-vue'
import { ref, computed } from 'vue'
import { VueDraggable } from 'vue-draggable-plus'

import { useDesigner } from '../composables/useDesigner'
import { cloneNode } from '../utils/schema'

const props = defineProps<{
  node: FormNode
  depth?: number
}>()

defineEmits<{
  select: [id: string]
}>()

const isHover = ref(false)

const {
  selectedId,
  selectNode,
  deleteNode,
  copyNode,
  moveUp,
  moveDown,
  getComponentConfig
} = useDesigner()

const isSelected = computed(() => selectedId.value === props.node.id)

const rowGutterStyle = computed(() => {
  if (props.node.type !== 'row') return {}
  const g = (props.node.props.gutter as number) || 0
  if (!g) return {}
  return {
    marginLeft: `${-g / 2}px`,
    marginRight: `${-g / 2}px`
  }
})

function getColStyle(col: FormNode) {
  const span = (col.props.span as number) || 12
  const gutter = (props.node.props.gutter as number) || 0
  return {
    flex: `0 0 ${(span / 24) * 100}%`,
    paddingLeft: `${gutter / 2}px`,
    paddingRight: `${gutter / 2}px`
  }
}

const canvasGroup = {
  name: 'form-components',
  pull: true,
  put: true
}

function getTypeLabel(type: string): string {
  const config = getComponentConfig(type)
  return config?.label || type
}

function cloneCanvasNode(node: FormNode) {
  return cloneNode(node)
}

function handleAdd(event) {
  if (event.clonedData?.id) {
    selectNode(event.clonedData.id)
  }
}
</script>

<style scoped>
.canvas-item {
  outline: 2px solid transparent;
  outline-offset: -2px;
  border-radius: 8px;
  transition: outline-color 0.2s ease;
}
.canvas-item.is-hover:not(.is-selected) {
  outline-color: #409eff;
}
.is-col-selected {
  border-color: #409eff !important;
  background-color: #ecf5ff !important;
  outline: 2px solid #409eff;
  outline-offset: 1px;
}
.nested-canvas-list {
  padding-bottom: 4px;
}
.ghost {
  min-height: 44px;
  opacity: 0.75;
  background: #ecf5ff;
  border: 2px dashed #409eff;
  border-radius: 8px;
}
:deep(.sortable-chosen) {
  opacity: 0.85;
}
:deep(.sortable-drag) {
  box-shadow: 0 8px 24px rgb(64 158 255 / 18%);
}
.drag-handle {
  cursor: grab;
}
.row-canvas {
  overflow: hidden;
}
.col-wrapper {
  min-width: 0;
}
.drag-handle:active {
  cursor: grabbing;
}
</style>
