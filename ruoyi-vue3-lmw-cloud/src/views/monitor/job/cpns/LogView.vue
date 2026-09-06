<script setup>
import getLogViewConfig from '../config/logViewConfig'
const props = defineProps({
  viewFormData: {
    type: Object,
    default: () => ({}),
  },
  dialogVisible: {
    type: Boolean,
    required: true,
  },
})
const emits = defineEmits(['update:dialogVisible'])
const logViewConfig = getLogViewConfig()
const handleValueChange = (value) => {
  emits('update:dialogVisible', value)
}
</script>
<template>
  <el-dialog
    width="700px"
    :model-value="dialogVisible"
    @update:modelValue="handleValueChange($event)"
    title="调度日志详细"
    destroy-on-close
    append-to-body
  >
    <el-scrollbar max-height="420px">
      <BaseForm :data="viewFormData" v-bind="logViewConfig">
        <template #statusCustom="{ backData }">
          <div v-if="backData.data == 0">正常</div>
          <div v-else-if="backData.data == 1">失败</div>
        </template>
      </BaseForm>
      <template #footer>
        <el-button @click="handleValueChange(false)">关闭</el-button>
      </template>
    </el-scrollbar>
  </el-dialog>
</template>

<style scoped lang="scss"></style>
