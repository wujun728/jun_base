<script setup>
const props = defineProps({
  item: { type: Object, required: true },
  allDisabled: { type: Boolean },
})
const value = defineModel('value')
const dateRef = useTemplateRef('dateRef')

const isRange = computed(() => props.item.config?.range === true)

const dateConfig = computed(() => ({
  placeholder: isRange.value ? void 0 : '请选择' + props.item.label,
  valueFormat: 'YYYY-MM-DD',
  format: 'YYYY-MM-DD',
  inputReadOnly: true,
  ...props.item.config,
}))

const getRef = () => dateRef.value
defineExpose({ getRef })
</script>

<template>
  <a-range-picker
    v-if="isRange"
    ref="dateRef"
    v-model:value="value"
    class="base-form-date"
    :disabled="allDisabled"
    v-bind="dateConfig"
    :id="item.field"
    v-on="item.eventFunction || {}"
  >
    <template v-for="slotName in item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData" />
    </template>
  </a-range-picker>
  <a-date-picker
    v-else
    ref="dateRef"
    v-model:value="value"
    class="base-form-date"
    :disabled="allDisabled"
    v-bind="dateConfig"
    :id="item.field"
    v-on="item.eventFunction || {}"
  >
    <template v-for="slotName in item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData" />
    </template>
  </a-date-picker>
</template>

<style scoped lang="scss">
.base-form-date {
  width: 100%;
}
</style>
