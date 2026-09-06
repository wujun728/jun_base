<script setup lang="ts">
import { computed, ref } from 'vue'
import { getOptions } from '../../utils/index.ts'
import type { FormItemTool } from '../../types/index.ts'
import {
  toOptionKey,
  toOptionLabel,
  toOptionValueByField,
} from '../shared/option.ts'
import { buildPlaceholder, type SelectValue } from '../shared/form.ts'

interface Props {
  item: FormItemTool<'select'>
  allDisabled: boolean
  inputId?: string
}

const props = defineProps<Props>()
const value = defineModel<SelectValue>('value')
const selectRef = ref()

const placeholder = computed(() =>
  buildPlaceholder(
    props.allDisabled,
    '请选择',
    props.item.label,
    props.item.config?.placeholder as string | undefined
  )
)

const getRef = () => selectRef.value
const toSelectOptionValue = (option: Record<string, unknown>) => {
  const candidate = toOptionValueByField(option, props.item.setValue)
  if (
    typeof candidate === 'string' ||
    typeof candidate === 'number' ||
    typeof candidate === 'boolean'
  ) {
    return candidate
  }
  if (candidate && typeof candidate === 'object' && !Array.isArray(candidate)) {
    return candidate
  }
  return ''
}

defineExpose({
  getRef,
})
</script>
<template>
  <el-select
    clearable
    ref="selectRef"
    :disabled="props.allDisabled"
    :id="props.inputId"
    :placeholder="placeholder"
    v-model="value"
    v-bind="props.item.config"
    v-on="props.item.eventFunction ?? {}"
  >
    <el-option
      v-for="option in getOptions(props.item)"
      :value="toSelectOptionValue(option)"
      :label="toOptionLabel(option, props.item.setLabel)"
      :key="toOptionKey(option)"
      v-on="props.item.optionFunction ?? {}"
    >
    </el-option>
    <template v-for="slotName in props.item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData"> </slot>
    </template>
  </el-select>
</template>

<style scoped lang="scss"></style>
