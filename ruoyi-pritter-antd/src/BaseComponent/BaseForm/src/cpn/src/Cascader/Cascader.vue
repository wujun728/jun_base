<script setup>
import { getOptions } from '../../../utils/index.js'

const props = defineProps({
  item: { type: Object, required: true },
  allDisabled: { type: Boolean },
})
const value = defineModel('value')
const cascaderRef = useTemplateRef('cascaderRef')

const cascaderConfig = computed(() => ({
  allowClear: true,
  placeholder: '请选择' + props.item.label,
  ...props.item.config,
  options: props.item.config?.options ?? getOptions(props.item),
}))

const getRef = () => cascaderRef.value
defineExpose({ getRef })
</script>

<template>
  <a-cascader
    ref="cascaderRef"
    v-model:value="value"
    class="base-form-cascader"
    :disabled="allDisabled"
    v-bind="cascaderConfig"
    :id="item.field"
    v-on="item.eventFunction || {}"
  >
    <template v-for="slotName in item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData" />
    </template>
  </a-cascader>
</template>

<style scoped lang="scss">
.base-form-cascader {
  width: 100%;
}
</style>
