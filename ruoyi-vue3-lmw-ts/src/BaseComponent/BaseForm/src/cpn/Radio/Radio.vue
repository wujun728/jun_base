<script setup lang="ts">
import { computed, ref } from 'vue'
import { getOptions } from '../../utils/index.ts'
import type { FormItemTool } from '../../types/index.ts'
import {
  type PrimitiveValue,
  toOptionKey,
  toOptionPrimitiveValue,
} from '../shared/option.ts'

type Props = {
  item: FormItemTool<'radio'>
  allDisabled: boolean
  inputId?: string
}
const props = defineProps<Props>()
type RadioValue = PrimitiveValue | undefined
const value = defineModel<RadioValue>('value')
const radioRef = ref()

const options = computed(() => getOptions(props.item))
const getRef = () => radioRef.value
defineExpose({
  getRef,
})
</script>
<template>
  <el-radio-group
    ref="radioRef"
    :id="props.inputId"
    :disabled="props.allDisabled"
    v-model="value"
    v-bind="props.item.config"
    v-on="props.item.eventFunction ?? {}"
    v-if="props.item.isGroup"
  >
    <el-radio
      v-for="option in options"
      :key="toOptionKey(option)"
      :value="toOptionPrimitiveValue(option)"
      v-bind="props.item.optionConfig ?? {}"
    >
      {{ option.label }}
    </el-radio>
    <template v-for="slotName in props.item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData"> </slot>
    </template>
  </el-radio-group>
  <template v-else>
    <el-radio
      v-for="option in options"
      v-model="value"
      :key="toOptionKey(option)"
      :disabled="props.allDisabled"
      :value="toOptionPrimitiveValue(option)"
      v-bind="props.item.config"
      v-on="props.item.eventFunction ?? {}"
    >
      {{ option.label }}
    </el-radio>
  </template>
</template>

<style scoped lang="scss"></style>
