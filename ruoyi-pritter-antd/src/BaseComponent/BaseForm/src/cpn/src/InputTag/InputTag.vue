<script setup>
const props = defineProps({
  item: { type: Object, required: true },
  allDisabled: { type: Boolean },
})
const value = defineModel('value')
const selectRef = useTemplateRef('selectRef')

const tagConfig = computed(() => ({
  mode: 'tags',
  allowClear: true,
  placeholder: '请输入' + props.item.label,
  ...props.item.config,
}))

const getRef = () => selectRef.value
defineExpose({ getRef })
</script>

<template>
  <a-select
    ref="selectRef"
    v-model:value="value"
    class="base-form-select"
    :disabled="allDisabled"
    v-bind="tagConfig"
    :id="item.field"
    v-on="item.eventFunction || {}"
  >
    <template v-for="slotName in item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData" />
    </template>
  </a-select>
</template>

<style scoped lang="scss">
.base-form-select {
  width: 100%;
}
</style>
