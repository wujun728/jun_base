<script setup>
import getLogViewConfig from '../config/logViewConfig'
const props = defineProps({
  viewFormData: {
    type: Object,
    default: () => ({}),
  },
})
const dialogVisible = defineModel('dialogVisible')
const logViewConfig = getLogViewConfig()
const logMaxHeight = window.innerHeight - 130
const isSmall = window.isSmallScreen
</script>
<template>
  <el-dialog
    :width="getWidth('700px')"
    v-model="dialogVisible"
    :fullscreen="isSmall"
    :close-on-click-modal="false"
    title="调度日志详细"
    destroy-on-close
    draggable
  >
    <el-scrollbar :max-height="logMaxHeight" always>
      <BaseForm :data="viewFormData" v-bind="logViewConfig">
        <template #statusCustom="{ backData }">
          <div v-if="backData.data == 0">正常</div>
          <div v-else-if="backData.data == 1">失败</div>
        </template>
      </BaseForm>
      <template #footer>
        <el-button @click="dialogVisible = false">关闭</el-button>
      </template>
    </el-scrollbar>
  </el-dialog>
</template>

<style scoped lang="scss"></style>
