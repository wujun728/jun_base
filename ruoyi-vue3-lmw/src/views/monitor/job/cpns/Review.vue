<script setup>
import getViewDialogConfig from '../config/viewDialogConfig'
const props = defineProps({
  viewFormData: {
    type: Object,
    default: () => ({}),
  },
  jobGroupFormat: {
    type: Function,
    default: () => {},
  },
})
const isSmall = window.isSmallScreen
const dialogVisible = defineModel('dialogVisible')
const viewDialogConfig = getViewDialogConfig()
const reviewMaxHeight = window.innerHeight - 130
</script>
<template>
  <el-dialog
    v-model="dialogVisible"
    :fullscreen="isSmall"
    :close-on-click-modal="false"
    title="任务详情"
    destroy-on-close
    draggable
  >
    <el-scrollbar :max-height="reviewMaxHeight" always>
      <BaseForm
        :data="viewFormData"
        v-bind="viewDialogConfig"
        class="mt10 mb20"
      >
        <template #jopGroupCustom="{ backData }">
          {{ jobGroupFormat(backData.data) }}
        </template>
        <template #nextValidTimeCustom="{ backData }">
          {{ backData.data }}
        </template>
        <template #statusCustom="{ backData }">
          <div v-if="backData.data == 0">正常</div>
          <div v-else-if="backData.data == 1">暂停</div>
        </template>
        <template #concurrentCustom="{ backData }">
          <div v-if="backData.data == 0">允许</div>
          <div v-else-if="backData.data == 1">禁止</div>
        </template>
        <template #misfirePolicyCustom="{ backData }">
          <div v-if="backData.data == 0">默认策略</div>
          <div v-else-if="backData.data == 1">立即执行</div>
          <div v-else-if="backData.data == 2">执行一次</div>
          <div v-else-if="backData.data == 3">放弃执行</div>
        </template>
      </BaseForm>
    </el-scrollbar>
    <template #footer>
      <el-button @click="dialogVisible = false">关闭</el-button>
    </template>
  </el-dialog>
</template>

<style scoped lang="scss"></style>
