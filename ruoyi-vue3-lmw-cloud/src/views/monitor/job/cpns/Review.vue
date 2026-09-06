<script setup>
import getViewDialogConfig from '../config/viewDialogConfig'
const props = defineProps({
  viewFormData: {
    type: Object,
    default: () => ({}),
  },
  dialogVisible: {
    type: Boolean,
    required: true,
  },
  jobGroupFormat: {
    type: Function,
    default: () => {},
  },
})
const emits = defineEmits(['update:dialogVisible'])
const viewDialogConfig = getViewDialogConfig()
const handleValueChange = (value) => {
  emits('update:dialogVisible', value)
}
</script>
<template>
  <el-dialog
    :model-value="dialogVisible"
    @update:modelValue="handleValueChange($event)"
    title="任务详情"
    destroy-on-close
    append-to-body
  >
    <el-scrollbar max-height="420px">
      <BaseForm :data="viewFormData" v-bind="viewDialogConfig">
        <template #jopGroupCustom="{ backData }">
          {{ jobGroupFormat(backData.data) }}
        </template>
        <template #nextValidTimeCustom="{ backData }">
          {{ parseTime(backData.data) }}
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
      <template #footer>
        <el-button @click="handleValueChange(false)">关闭</el-button>
      </template>
    </el-scrollbar>
  </el-dialog>
</template>

<style scoped lang="scss"></style>
