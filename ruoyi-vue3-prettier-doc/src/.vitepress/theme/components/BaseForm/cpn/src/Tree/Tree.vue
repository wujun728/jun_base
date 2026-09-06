<script setup>
import { ref } from 'vue'
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
const emits = defineEmits(['keyUpEnter'])
const elRef = ref(null)
const getRef = () => {
  return elRef.value
}
defineExpose({
  getRef,
})
</script>
<template>
  <el-tree
    ref="elRef"
    :data="getOptions(item)"
    :style="{
      width: '100%',
    }"
    v-bind="item.config"
    v-on="item.eventFunction || {}"
  >
    <template v-for="slotName in item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData"> </slot>
    </template>
  </el-tree>
</template>

<style scoped lang="scss"></style>
