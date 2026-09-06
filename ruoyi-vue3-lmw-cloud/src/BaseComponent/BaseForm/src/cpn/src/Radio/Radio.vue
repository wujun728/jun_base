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
  <el-radio-group
    ref="elRef"
    :disabled="allDisabled"
    v-model="value"
    v-bind="item.config"
    v-on="item.eventFunction || {}"
    v-if="item.isGroup"
  >
    <el-radio
      v-for="option in getOptions(item)"
      :key="option.key ?? option.value"
      :value="option.value"
      v-bind="item.optionConfig ?? {}"
    >
      {{ option.label }}
    </el-radio>
    <template v-for="slotName in item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData"> </slot>
    </template>
  </el-radio-group>
  <template v-else>
    <el-radio
      v-for="option in getOptions(item)"
      v-model="value"
      :key="option.key ?? option.value"
      :disabled="allDisabled"
      v-bind="item.config"
      v-on="item.eventFunction || {}"
    >
      {{ option.label }}
    </el-radio>
  </template>
</template>

<style scoped lang="scss"></style>
