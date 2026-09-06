<script setup>
import { defaultTreeFieldNames, getOptions } from '../../../utils/index.js'

const props = defineProps({
  item: { type: Object, required: true },
  allDisabled: { type: Boolean },
})
const value = defineModel('value')
const treeRef = useTemplateRef('treeRef')

const treeConfig = computed(() => {
  const config = props.item.config ?? {}
  const { treeData, fieldNames, ...restConfig } = config

  return {
    style: { width: '100%' },
    fieldNames: fieldNames ?? defaultTreeFieldNames,
    treeData: treeData ?? getOptions(props.item),
    ...restConfig,
  }
})

const getRef = () => treeRef.value
defineExpose({ getRef })
</script>

<template>
  <a-tree
    ref="treeRef"
    v-model:checkedKeys="value"
    v-bind="treeConfig"
    :id="item.field"
    v-on="item.eventFunction || {}"
  >
    <template v-for="slotName in item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData" />
    </template>
  </a-tree>
</template>
