<script setup lang="ts">
import { computed, ref } from 'vue'
import { getOptions } from '../../utils/index.ts'
import type { FormItemTool } from '../../types/index.ts'
import {
  type PrimitiveValue,
  toOptionGroupValue,
  toOptionKey,
  toOptionPrimitiveValue,
} from '../shared/option.ts'

type Props = {
  item: FormItemTool<'checkBox'>
  allDisabled: boolean
  inputId?: string
}
const props = defineProps<Props>()
type CheckboxValue = PrimitiveValue
type CheckboxModel = CheckboxValue | CheckboxValue[] | undefined
type GroupCheckboxValue = string | number
const value = defineModel<CheckboxModel>('value')
const checkboxRef = ref()

const options = computed(() => getOptions(props.item))
const getRef = () => checkboxRef.value
const groupValue = computed<GroupCheckboxValue[]>({
  get: () =>
    Array.isArray(value.value)
      ? value.value.filter(
          (item): item is GroupCheckboxValue =>
            typeof item === 'string' || typeof item === 'number'
        )
      : [],
  set: (nextValue) => {
    value.value = nextValue
  },
})
const singleValue = computed<CheckboxValue | undefined>({
  get: () => (Array.isArray(value.value) ? undefined : value.value),
  set: (nextValue) => {
    value.value = nextValue
  },
})
const normalizedGroupConfig = computed(() => {
  const config = (props.item.config ?? {}) as Record<string, unknown>
  const { modelValue, value, ...rest } = config
  return rest
})
defineExpose({
  getRef,
})
</script>
<template>
  <div :class="props.item.field + 'InnerClass'" class="innerClass">
    <el-checkbox-group
      ref="checkboxRef"
      :id="props.inputId"
      :disabled="props.allDisabled"
      v-model="groupValue"
      v-bind="normalizedGroupConfig"
      v-on="props.item.eventFunction ?? {}"
      v-if="props.item.isGroup"
    >
      <el-checkbox
        v-for="option in options"
        :key="toOptionKey(option)"
        :value="toOptionGroupValue(option)"
        v-bind="props.item.optionConfig ?? {}"
      >
        {{ option.label }}
      </el-checkbox>
      <template v-for="slotName in props.item.slotNames" #[slotName]="slotData">
        <slot :name="slotName" :slotData="slotData"> </slot>
      </template>
    </el-checkbox-group>
    <template v-else>
      <el-checkbox
        v-for="option in options"
        v-model="singleValue"
        :key="toOptionKey(option)"
        :disabled="props.allDisabled"
        :value="toOptionPrimitiveValue(option)"
        v-bind="props.item.config"
        v-on="props.item.eventFunction ?? {}"
      >
        {{ option.label }}
      </el-checkbox>
    </template>
  </div>
</template>

<style scoped lang="scss">
.innerClass {
  width: 100%;
}
</style>
