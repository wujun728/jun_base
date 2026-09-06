<script setup>
import { ref } from 'vue'
const props = defineProps({
  item: {
    type: Object,
    required: true,
  },
  allDisabled: {
    type: Boolean,
  },
})
const emits = defineEmits(['keyUpEnter'])
const value = defineModel('value')
const keyUpEnter = ($event, item) => {
  emits('keyUpEnter', $event, item)
}
const elRef = ref(null)
const getRef = () => {
  return elRef.value
}
defineExpose({
  getRef,
})
</script>
<template>
  <el-input-number
    ref="elRef"
    clearable
    :disabled="allDisabled"
    v-model="value"
    @keyup.enter="keyUpEnter($event, item)"
    v-bind="item.config"
    v-on="item.eventFunction || {}"
  >
    <template v-for="slotName in item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData"> </slot>
    </template>
  </el-input-number>
</template>

<style scoped lang="scss"></style>
