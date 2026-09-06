<!--
 * 表单构建器 - 动态属性编辑器
 * 根据组件配置动态渲染属性编辑表单。
 * 支持 input/select/switch/number/option-editor 等类型。
 -->
<template>
  <div class="space-y-4">
    <template v-if="node">
      <!-- Common props -->
      <div class="space-y-3">
        <div class="text-xs font-medium tracking-wider text-gray-500 uppercase">
          基本属性
        </div>
        <el-form label-position="top">
          <el-form-item label="标签">
            <el-input v-model="node!.label" placeholder="请输入标签" />
          </el-form-item>
          <el-form-item label="字段">
            <el-input v-model="node!.field" placeholder="请输入字段名" />
          </el-form-item>
          <el-form-item v-if="hasProp('placeholder')" label="占位提示">
            <el-input
              :model-value="node!.props.placeholder as string"
              @update:model-value="(v: any) => (node!.props.placeholder = v)"
              placeholder="请输入占位提示" />
          </el-form-item>
          <el-form-item v-if="hasProp('defaultValue')" label="默认值">
            <el-input
              :model-value="node!.props.defaultValue as string"
              @update:model-value="(v: any) => (node!.props.defaultValue = v)"
              placeholder="请输入默认值" />
          </el-form-item>
          <el-form-item label="是否必填">
            <el-switch
              :model-value="!!node!.props.required"
              @update:model-value="(v: any) => (node!.props.required = v)" />
          </el-form-item>
          <el-form-item label="是否禁用">
            <el-switch
              :model-value="!!node!.props.disabled"
              @update:model-value="(v: any) => (node!.props.disabled = v)" />
          </el-form-item>
          <el-form-item label="是否隐藏">
            <el-switch
              :model-value="!!node!.props.hidden"
              @update:model-value="(v: any) => (node!.props.hidden = v)" />
          </el-form-item>
          <el-form-item label="是否只读">
            <el-switch
              :model-value="!!node!.props.readonly"
              @update:model-value="(v: any) => (node!.props.readonly = v)" />
          </el-form-item>
        </el-form>
      </div>

      <el-divider />

      <!-- Component-specific props -->
      <div class="space-y-3">
        <div class="text-xs font-medium tracking-wider text-gray-500 uppercase">
          组件属性
        </div>
        <el-form label-position="top">
          <template v-for="prop in componentProps" :key="prop.name">
            <el-form-item :label="prop.label">
              <!-- Input -->
              <el-input
                v-if="prop.type === 'input'"
                :model-value="node!.props[prop.name] as string"
                @update:model-value="(v: any) => (node!.props[prop.name] = v)"
                :placeholder="prop.placeholder" />
              <!-- Number -->
              <el-input-number
                v-else-if="prop.type === 'number'"
                :model-value="node!.props[prop.name] as number"
                @update:model-value="(v: any) => (node!.props[prop.name] = v)"
                :min="0"
                :max="9999"
                style="width: 100%" />
              <!-- Switch -->
              <el-switch
                v-else-if="prop.type === 'switch'"
                :model-value="!!node!.props[prop.name]"
                @update:model-value="
                  (v: any) => (node!.props[prop.name] = v)
                " />
              <!-- Select -->
              <el-select
                v-else-if="prop.type === 'select'"
                :model-value="node!.props[prop.name] as string"
                @update:model-value="(v: any) => (node!.props[prop.name] = v)"
                style="width: 100%">
                <el-option
                  v-for="opt in prop.options"
                  :key="opt.value"
                  :label="opt.label"
                  :value="opt.value" />
              </el-select>
              <!-- Option Editor -->
              <OptionEditor
                v-else-if="prop.type === 'option-editor'"
                :model-value="node!.props[prop.name] as any[]"
                @update:model-value="
                  (v: any[]) => (node!.props[prop.name] = v)
                " />
            </el-form-item>
          </template>
        </el-form>
      </div>
    </template>

    <div
      v-else
      class="flex flex-col items-center justify-center py-12 text-gray-400">
      <el-icon :size="32" class="mb-2"><Setting /></el-icon>
      <p class="text-sm">选择一个组件以编辑属性</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { FormNode } from '../types/form'

import { Setting } from '@element-plus/icons-vue'
import { computed } from 'vue'

import { useDesigner } from '../composables/useDesigner'
import OptionEditor from './OptionEditor.vue'

const props = defineProps<{
  node: FormNode | null
}>()

const { getComponentConfig } = useDesigner()

const config = computed(() => {
  if (!props.node) return null
  return getComponentConfig(props.node.type)
})

const componentProps = computed(() => {
  if (!config.value) return []
  return config.value.propsConfig || []
})

function hasProp(name: string): boolean {
  return componentProps.value.some((p) => p.name === name)
}
</script>
