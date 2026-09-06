<!--
 * 表单构建器 - 拖拽画布
 * 使用 vue-draggable-plus 实现组件拖入、排序。
 * 区分外部拖入（从左侧面板）和内部排序。
 -->
<template>
  <div class="form-canvas">
    <el-scrollbar height="100%">
      <VueDraggable
        v-model="designerNodes"
        group="form-components"
        :animation="200"
        ghost-class="ghost"
        class="h-full space-y-2 pt-4"
        @add="handleAdd">
        <CanvasItem
          v-for="node in designerNodes"
          :key="node.id"
          :node="node"
          :depth="0"
          @select="selectNode" />
      </VueDraggable>

      <!-- Empty state -->
      <div
        v-if="designerNodes.length === 0"
        class="absolute-center pointer-events-none flex flex-col items-center justify-center py-20 text-gray-400">
        <el-icon :size="48" class="mb-4"><Plus /></el-icon>
        <p class="text-sm">拖拽组件到这里开始设计表单</p>
      </div>
    </el-scrollbar>
  </div>
</template>

<script setup lang="ts">
import type { FormNode } from '../types/form'

import { Plus } from '@element-plus/icons-vue'
import { VueDraggable } from 'vue-draggable-plus'

import { useDesigner } from '../composables/useDesigner'
import { cloneNode } from '../utils/schema'
import CanvasItem from './CanvasItem.vue'

const { designerNodes, selectNode } = useDesigner()

const canvasGroup = {
  name: 'form-components',
  pull: true,
  put: true
}

function cloneCanvasNode(node: FormNode) {
  return cloneNode(node)
}

function handleAdd(event) {
  console.log(event, 'handleAdd')
  if (event.clonedData?.id) {
    selectNode(event.clonedData.id)
  }
}
</script>

<style scoped>
.form-canvas {
  transition: all 0.2s ease;
  height: 100%;
  position: relative;
}
.canvas-list {
  height: calc(100% - 16px);
  padding-bottom: 80px;
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
:deep(.el-scrollbar__view) {
  height: 100%;
}
</style>
