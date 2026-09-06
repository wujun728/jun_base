<!--
 * 表单构建器 - 左侧组件库面板
 * 按分类展示所有可用组件，支持拖拽到设计区。
 * 分类：基础组件、布局组件、操作组件。
 -->
<template>
  <div
    class="flex h-full w-[270px] min-w-[270px] flex-col overflow-y-auto border-r border-gray-200 bg-white">
    <el-scrollbar height="100%">
      <div class="border-b border-gray-100 px-4 py-3">
        <h3 class="text-sm font-medium text-gray-700">组件库</h3>
      </div>
      <div class="flex-1 space-y-4 p-3 pr-6">
        <div v-for="group in groups" :key="group.title">
          <div class="mb-2 px-1 text-xs text-gray-400">{{ group.title }}</div>
          <VueDraggable
            v-model="group.components"
            :group="componentGroup"
            :sort="false"
            :clone="cloneComponent"
            :animation="200"
            chosen-class="sortable-chosen"
            drag-class="sortable-drag"
            ghost-class="ghost"
            class="grid grid-cols-2 gap-2">
            <div
              v-for="comp in group.components"
              :key="comp.type"
              class="component-library-field-item flex cursor-grab items-center gap-2 rounded-lg border border-gray-200 px-3 py-2.5 text-xs transition-all duration-200 hover:border-primary hover:bg-primary/5 hover:text-primary active:cursor-grabbing">
              <el-icon :size="14">
                <component :is="getIcon(comp.icon)" />
              </el-icon>
              <span>{{ comp.label }}</span>
            </div>
          </VueDraggable>
        </div>
      </div>
    </el-scrollbar>
  </div>
</template>

<script setup lang="ts">
import type { ComponentConfig } from '../types/form'

import * as Icons from '@element-plus/icons-vue'
import { markRaw } from 'vue'
import { VueDraggable } from 'vue-draggable-plus'

import { useDesigner } from '../composables/useDesigner'
import {
  basicComponents,
  layoutComponents,
  actionComponents
} from '../config/componentConfig'

const { createDesignerNode } = useDesigner()

const componentGroup: any = {
  name: 'form-components',
  pull: 'clone',
  put: false
}

const groups = [
  { title: '基础组件', components: basicComponents },
  { title: '布局组件', components: layoutComponents },
  { title: '操作组件', components: actionComponents }
]

function getIcon(name: string) {
  return markRaw((Icons as Record<string, any>)[name] || Icons.Edit)
}

function cloneComponent(component: ComponentConfig) {
  const node = createDesignerNode(component.type)
  console.log(node, 'cloneComponent')
  return node
}
</script>

<style scoped>
.ghost {
  opacity: 0.65;
  background: #ecf5ff;
  border: 1px dashed #409eff;
  border-radius: 8px;
}
:deep(.sortable-chosen) {
  opacity: 0.85;
}
:deep(.sortable-drag) {
  box-shadow: 0 8px 24px rgb(64 158 255 / 18%);
}
</style>
