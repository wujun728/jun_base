<!--
 * 表单构建器 - 选项编辑器
 * 用于编辑 Select/Radio/Checkbox/Cascader 的选项列表。
 * 支持新增、删除选项。
 -->
<template>
  <div class="space-y-2">
    <div class="flex items-center justify-between">
      <span class="text-xs text-gray-500">选项列表</span>
      <el-button text @click="addOption">
        <el-icon><Plus /></el-icon>
        新增
      </el-button>
    </div>
    <div class="space-y-1">
      <div
        v-for="(item, index) in options"
        :key="index"
        class="flex items-center gap-2">
        <el-icon class="cursor-grab text-gray-400" :size="14"><Rank /></el-icon>
        <el-input
          :model-value="item.label"
          @update:model-value="(v: any) => (item.label = v)"
          placeholder="名称"
          class="flex-1" />
        <el-input
          :model-value="item.value"
          @update:model-value="(v: any) => (item.value = v)"
          placeholder="值"
          class="flex-1" />
        <el-button text type="danger" @click="removeOption(index)">
          <el-icon><Delete /></el-icon>
        </el-button>
      </div>
    </div>
    <div
      v-if="options.length === 0"
      class="py-2 text-center text-xs text-gray-400">
      暂无选项，点击"新增"添加
    </div>
  </div>
</template>

<script setup lang="ts">
import { Plus, Rank, Delete } from '@element-plus/icons-vue'
import { computed } from 'vue'

const props = defineProps<{
  modelValue: any[]
}>()

const emit = defineEmits<{
  'update:modelValue': [value: any[]]
}>()

const options = computed({
  get: () => props.modelValue || [],
  set: (val) => emit('update:modelValue', val)
})

function addOption() {
  const newOpts = [...options.value, { label: '', value: '' }]
  emit('update:modelValue', newOpts)
}

function removeOption(index: number) {
  const newOpts = options.value.filter((_: any, i: number) => i !== index)
  emit('update:modelValue', newOpts)
}
</script>
