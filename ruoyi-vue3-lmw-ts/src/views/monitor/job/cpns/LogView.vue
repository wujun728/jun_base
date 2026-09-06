<script setup lang="ts">
import getLogViewConfig from '../config/logViewConfig'
type Props = {
  viewFormData: anyObj
}
const { viewFormData = {} } = defineProps<Props>()
const dialogVisible = defineModel<boolean>('dialogVisible')
const emits = defineEmits(['update:dialogVisible'])
const logViewConfig = getLogViewConfig()
const logMaxHeight = window.innerHeight - 130
const isSmall = window.isSmallScreen
</script>
<template>
  <el-dialog
    :width="getWidth('700px')"
    v-model="dialogVisible"
    title="调度日志详细"
    destroy-on-close
    append-to-body
    :fullscreen="isSmall"
    draggable
  >
    <el-scrollbar :max-height="logMaxHeight">
      <BaseForm :data="viewFormData" v-bind="logViewConfig">
        <template #statusCustom="{ backData }">
          <div v-if="backData.data == 0">正常</div>
          <div v-else-if="backData.data == 1">失败</div>
        </template>
      </BaseForm>
    </el-scrollbar>
    <template #footer>
      <el-button @click="dialogVisible = false">关闭</el-button>
    </template>
  </el-dialog>
</template>

<style scoped lang="scss"></style>
