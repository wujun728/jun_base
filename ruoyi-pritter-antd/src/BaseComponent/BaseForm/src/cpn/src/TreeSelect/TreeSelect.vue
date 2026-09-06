<script setup>
import { defaultTreeFieldNames, getOptions } from '../../../utils/index.js'

const props = defineProps({
  item: { type: Object, required: true },
  allDisabled: { type: Boolean },
})
const value = defineModel('value')
const treeSelectRef = useTemplateRef('treeSelectRef')

const treeSelectConfig = computed(() => ({
  allowClear: true,
  placeholder: '请选择' + props.item.label,
  fieldNames: props.item.config?.fieldNames ?? defaultTreeFieldNames,
  ...props.item.config,
  treeData: props.item.config?.treeData ?? getOptions(props.item),
}))

const getRef = () => treeSelectRef.value
defineExpose({ getRef })
</script>

<template>
  <a-tree-select
    ref="treeSelectRef"
    v-model:value="value"
    class="base-form-tree-select"
    :disabled="allDisabled"
    v-bind="treeSelectConfig"
    :id="item.field"
    v-on="item.eventFunction || {}"
  >
    <template v-for="slotName in item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData" />
    </template>
  </a-tree-select>
</template>

<style scoped lang="scss">
.base-form-tree-select {
  width: 100%;
}
</style>
