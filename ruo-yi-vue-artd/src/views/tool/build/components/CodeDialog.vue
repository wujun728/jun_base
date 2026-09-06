<!--
 * 表单构建器 - 代码展示弹窗
 * 展示生成的 Vue3 + Element Plus 完整代码，支持复制。
 -->
<template>
  <el-dialog
    v-model="visible"
    title="生成代码"
    width="680px"
    top="5vh"
    :close-on-click-modal="false">
    <div class="relative">
      <el-button
        dark
        class="absolute top-2 right-4 z-10"
        :icon="CopyDocument"
        @click="copyCode">
        复制
      </el-button>
      <pre
        class="max-h-[70vh] overflow-auto rounded-lg bg-gray-900 p-4 text-sm leading-relaxed text-gray-100"><code>{{ code }}</code></pre>
    </div>
  </el-dialog>
</template>

<script setup lang="ts">
import { CopyDocument } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { computed } from 'vue'

import { copyToClipboard } from '../utils/clipboard'

const props = defineProps<{
  modelValue: boolean
  code: string
}>()

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
}>()

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

async function copyCode() {
  const ok = await copyToClipboard(props.code)
  if (ok) {
    ElMessage.success('代码已复制')
  } else {
    ElMessage.error('复制失败')
  }
}
</script>
