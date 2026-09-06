<!--
 * 表单构建器 - 预览弹窗
 * 使用 Dialog 实时预览表单效果，数据与设计区同步。
 -->
<template>
  <el-dialog
    v-model="visible"
    title="表单预览"
    width="70%"
    :close-on-click-modal="false"
    top="5vh">
    <div class="preview-container">
      <el-form
        :model="formModel"
        :label-width="formConfig.labelWidth + 'px'"
        :label-position="formConfig.labelPosition"
        :size="formConfig.size"
        :inline="formConfig.inline"
        :disabled="formConfig.disabled"
        :hide-required-asterisk="formConfig.hideRequiredAsterisk"
        :status-icon="formConfig.statusIcon"
        :validate-on-rule-change="formConfig.validateOnRuleChange">
        <template v-for="node in designerNodes" :key="node.id">
          <PreviewItem :node="node" :form-model="formModel" />
        </template>
        <el-form-item v-if="formConfig.showFormButton">
          <el-button type="primary" @click.prevent>提交</el-button>
          <el-button @click.prevent>重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-dialog>
</template>

<script setup lang="ts">
import type { FormNode } from '../types/form'

import { ref, reactive, watch } from 'vue'

import { useDesigner } from '../composables/useDesigner'
import PreviewItem from './PreviewItem.vue'

const props = defineProps<{
  modelValue: boolean
}>()

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
}>()

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

import { computed } from 'vue'

const { designerNodes, formConfig } = useDesigner()

const formModel = reactive<Record<string, any>>({})

watch(
  designerNodes,
  () => {
    buildModel(designerNodes.value)
  },
  { deep: true, immediate: true }
)

function buildModel(nodes: FormNode[]) {
  for (const node of nodes) {
    if (node.type === 'row' || node.type === 'col') {
      if (node.children) buildModel(node.children)
      continue
    }
    if (node.type === 'button') continue
    const field = node.field || node.id
    if (!(field in formModel)) {
      formModel[field] = null
    }
  }
}
</script>

<style scoped>
.preview-container {
  max-height: 70vh;
  overflow-y: auto;
}
</style>
