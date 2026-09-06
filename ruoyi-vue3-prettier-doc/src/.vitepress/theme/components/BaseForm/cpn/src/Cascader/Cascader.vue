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
const value = defineModel('value')
const elRef = ref(null)
const getRef = () => {
  return elRef.value
}

defineExpose({
  getRef,
})
</script>
<template>
  <el-cascader
    clearable
    ref="elRef"
    :disabled="allDisabled"
    :placeholder="'请选择' + item.label"
    :options="getOptions(item)"
    v-model="value"
    v-bind="item.config"
    v-on="item.eventFunction || {}"
  >
    <template v-for="slotName in item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData"> </slot>
    </template>
  </el-cascader>
</template>

<style scoped lang="scss"></style>
