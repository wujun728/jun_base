<script setup>
import getViewDialogConfig from '../config/viewDialogConfig'

defineProps({
  viewFormData: {
    type: Object,
    default: () => ({}),
  },
  jobGroupFormat: {
    type: Function,
    default: () => '',
  },
})

const open = defineModel('open', { type: Boolean })
const viewDialogConfig = getViewDialogConfig()
const isSmall = window.isSmallScreen
</script>

<template>
  <a-modal
    v-model:open="open"
    title="任务详情"
    :width="getWidth('700px')"
    :wrap-class-name="isSmall ? 'full-modal' : ''"
    :footer="null"
    destroy-on-close
  >
    <BaseForm
      class="view-form"
      :data="viewFormData"
      v-bind="viewDialogConfig"
      :all-disabled="true"
    >
      <template #jobGroupCustom="{ backData }">
        {{ jobGroupFormat(backData.formData) }}
      </template>
      <template #statusCustom="{ backData }">
        {{ backData.formData.status === '0' ? '正常' : '暂停' }}
      </template>
      <template #concurrentCustom="{ backData }">
        {{ backData.formData.concurrent === '0' ? '允许' : '禁止' }}
      </template>
      <template #misfirePolicyCustom="{ backData }">
        {{
          {
            0: '默认策略',
            1: '立即执行',
            2: '执行一次',
            3: '放弃执行',
          }[backData.formData.misfirePolicy] ?? backData.formData.misfirePolicy
        }}
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
