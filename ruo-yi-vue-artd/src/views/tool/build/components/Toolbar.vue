<!--
 * 表单构建器 - 顶部工具栏
 * 提供预览、生成代码、复制代码、导入JSON、导出JSON、清空等操作。
 -->
<template>
  <div
    class="flex items-center justify-between border-b border-gray-200 bg-white px-4 pb-3">
    <div class="flex items-center gap-2">
      <span class="text-base font-medium text-gray-700">表单构建</span>
    </div>
    <div class="flex items-center gap-2">
      <el-button :icon="View" @click="$emit('preview')">预览</el-button>
      <el-button :icon="Document" type="primary" @click="$emit('generate')">
        生成代码
      </el-button>
      <el-button :icon="CopyDocument" @click="$emit('copy')">
        复制代码
      </el-button>
      <el-upload
        :show-file-list="false"
        :before-upload="handleImport"
        accept=".json"
        style="display: inline-block">
        <el-button :icon="Upload">导入JSON</el-button>
      </el-upload>
      <el-button :icon="Download" @click="$emit('export')">导出JSON</el-button>
      <el-button :icon="Delete" type="danger" plain @click="$emit('clear')">
        清空
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import {
  View,
  Document,
  CopyDocument,
  Upload,
  Download,
  Delete
} from '@element-plus/icons-vue'

const emit = defineEmits<{
  preview: []
  generate: []
  copy: []
  import: [data: string]
  export: []
  clear: []
}>()

function handleImport(file: File): boolean {
  const reader = new FileReader()
  reader.onload = (e) => {
    const text = e.target?.result as string
    emit('import', text)
  }
  reader.readAsText(file)
  return false
}
</script>
