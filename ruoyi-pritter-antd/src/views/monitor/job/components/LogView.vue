<script setup>
import getLogViewConfig from '../config/logViewConfig'

defineProps({
  viewFormData: {
    type: Object,
    default: () => ({}),
  },
})

const open = defineModel('open', { type: Boolean })
const logViewConfig = getLogViewConfig()
const isSmall = window.isSmallScreen
</script>

<template>
  <a-modal
    v-model:open="open"
    title="调度日志详细"
    :width="getWidth('700px')"
    :wrap-class-name="isSmall ? 'full-modal' : ''"
    :footer="null"
    destroy-on-close
  >
    <BaseForm
      class="view-form"
      :data="viewFormData"
      v-bind="logViewConfig"
      :all-disabled="true"
    >
      <template #statusCustom="{ backData }">
        {{ backData.formData.status === '0' ? '正常' : '失败' }}
      </template>
    </BaseForm>
    <div class="modal-footer">
      <a-button @click="open = false">关闭</a-button>
    </div>
  </a-modal>
</template>

<style scoped lang="scss">
.view-form {
  margin: 10px 0 20px;
}

.modal-footer {
  text-align: right;
}
</style>
