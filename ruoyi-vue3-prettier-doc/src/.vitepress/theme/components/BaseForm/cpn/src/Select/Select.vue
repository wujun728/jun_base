<script setup>
import { ref } from 'vue'
import { getOptions, capitalizeFirstLetter } from '../../../utils/index.js'
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
  <el-select
    clearable
    ref="elRef"
    :disabled="allDisabled"
    :placeholder="'请选择' + item.label"
    v-model="value"
    v-bind="item.config"
    v-on="item.eventFunction || {}"
  >
    <el-option
      v-for="option in getOptions(item)"
      :value="item.setValue ? option[item.setValue] : option.value"
      :label="item.setLabel ? option[item.setLabel] : option.label"
      :key="option.key ?? option.value"
      v-on="item.optionFunction || {}"
    >
      <template v-for="slotName in item.optionSlots" #[slotName]>
        <slot
          :name="item.field + capitalizeFirstLetter(slotName) + 'Option'"
          :slotData="{ ...item, option }"
        >
        </slot>
      </template>
    </el-option>
    <template v-for="slotName in item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData"> </slot>
    </template>
  </el-select>
</template>

<style scoped lang="scss"></style>
