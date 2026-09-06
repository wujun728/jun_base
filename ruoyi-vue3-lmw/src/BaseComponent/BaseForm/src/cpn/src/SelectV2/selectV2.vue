<script setup>
import { useTemplateRef } from 'vue'
import { getOptions } from '../../../utils/index.js'
const props = defineProps({
  item: {
    type: Object,
    required: true,
  },
  allDisabled: {
    type: Boolean,
  },
})
const value = defineModel('value')
const elRef = useTemplateRef('elRef')
const getRef = () => {
  return elRef.value
}
const placeholder = computed(() => {
  if (props.allDisabled) {
    return ''
  }
  if (props.item?.config?.placeholder) {
    return props.item.config.placeholder
  }
  return '请选择' + props.item.label
})
defineExpose({
  getRef,
})
</script>
<template>
  <el-select-v2
    clearable
    ref="elRef"
    :disabled="allDisabled"
    v-model="value"
    :placeholder="placeholder"
    :options="getOptions(item)"
    v-bind="item.config"
    v-on="item.eventFunction || {}"
  >
    <template v-for="slotName in item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData"> </slot>
    </template>
  </el-select-v2>
</template>

<style scoped lang="scss"></style>
