<script setup>
import { ref, isRef, useTemplateRef } from 'vue'
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
defineExpose({
  getRef,
})
</script>
<template>
  <el-switch
    ref="elRef"
    :disabled="allDisabled"
    v-model="value"
    v-bind="item.config"
    v-on="item.eventFunction || {}"
  >
    <template v-for="slotName in item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData"> </slot>
    </template>
  </el-switch>
</template>

<style scoped lang="scss"></style>
