/** * 表单构建器 - 主页面 * 三栏布局：左侧组件库、中间设计区、右侧属性面板。 *
顶部工具栏：预览、生成代码、复制、导入导出、清空。 */
<template>
  <div class="art-full-height">
    <div class="art-card box-border h-full p-4">
      <Toolbar
        @preview="showPreview = true"
        @generate="handleGenerate"
        @copy="handleCopy"
        @import="handleImport"
        @export="handleExport"
        @clear="handleClear" />
      <div class="flex h-[calc(100%-50px)] flex-1 overflow-hidden">
        <LeftPanel />
        <DesignPanel />
        <RightPanel />
      </div>
    </div>
    <PreviewDialog v-model="showPreview" />
    <CodeDialog v-model="showCode" :code="generatedCode" />
  </div>
</template>

<script setup lang="ts">
import { ElMessage, ElMessageBox } from 'element-plus'
import { ref } from 'vue'

import CodeDialog from './components/CodeDialog.vue'
import DesignPanel from './components/DesignPanel.vue'
import LeftPanel from './components/LeftPanel.vue'
import PreviewDialog from './components/PreviewDialog.vue'
import RightPanel from './components/RightPanel.vue'
import Toolbar from './components/Toolbar.vue'
import { useDesigner } from './composables/useDesigner'
import { copyToClipboard } from './utils/clipboard'
import { generateCode } from './utils/generator'

defineOptions({ name: 'Build' })

const { designerNodes, formConfig, clearAll, importJson, exportJson } =
  useDesigner()

const showPreview = ref(false)
const showCode = ref(false)
const generatedCode = ref('')

function handleGenerate() {
  if (designerNodes.value.length === 0) {
    ElMessage.warning('请先添加表单组件')
    return
  }
  generatedCode.value = generateCode(designerNodes.value, formConfig)
  showCode.value = true
}

async function handleCopy() {
  if (designerNodes.value.length === 0) {
    ElMessage.warning('请先添加表单组件')
    return
  }
  const code = generateCode(designerNodes.value, formConfig)
  const ok = await copyToClipboard(code)
  if (ok) {
    ElMessage.success('代码已复制到剪贴板')
  } else {
    ElMessage.error('复制失败')
  }
}

function handleImport(text: string) {
  try {
    importJson(text)
    ElMessage.success('导入成功')
  } catch (e: any) {
    ElMessage.error(e.message || '导入失败')
  }
}

function handleExport() {
  const json = exportJson()
  const blob = new Blob([json], { type: 'application/json' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = 'form-schema.json'
  a.click()
  URL.revokeObjectURL(url)
}

function handleClear() {
  if (designerNodes.value.length === 0) return
  ElMessageBox.confirm('确定要清空所有组件吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      clearAll()
      ElMessage.success('已清空')
    })
    .catch(() => {})
}
</script>

<style scoped>
:deep(.el-tabs__header) {
  margin-bottom: 12px;
}
</style>
