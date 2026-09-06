<script setup>
import { getOptions } from '../../../utils/index.js'

const props = defineProps({
  item: { type: Object, required: true },
  allDisabled: { type: Boolean },
})
const value = defineModel('value')
const selectRef = useTemplateRef('selectRef')

const placeholder = computed(() => {
  if (props.allDisabled) return ''
  if (props.item?.config?.placeholder) return props.item.config.placeholder
  return '请选择' + props.item.label
})

const selectConfig = computed(() => ({
  allowClear: true,
  virtual: true,
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
    :placeholder="placeholder"
    :options="getOptions(item)"
    v-bind="selectConfig"
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
