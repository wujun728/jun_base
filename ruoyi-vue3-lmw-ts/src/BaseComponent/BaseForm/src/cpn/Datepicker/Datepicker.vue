<script setup lang="ts">
import { computed, ref } from 'vue'
import type { DatePickerInstance } from 'element-plus'
import type { FormItemTool } from '../../types/index.ts'
import { buildPlaceholder } from '../shared/form.ts'

type Props = {
  item: FormItemTool<'datepicker'>
  allDisabled: boolean
  inputId?: string
}
const props = defineProps<Props>()
const value = defineModel<string | string[] | Date | Date[]>('value')
const datepickerRef = ref<DatePickerInstance>()

const placeholder = computed(() =>
  buildPlaceholder(
    props.allDisabled,
    '请选择',
    props.item.label,
    props.item.config?.placeholder as string | undefined
  )
)
const getRef = () => datepickerRef.value
defineExpose({
  getRef,
})
</script>
<template>
  <el-date-picker
    ref="datepickerRef"
    :disabled="props.allDisabled"
    :id="[props.inputId + 'Start', props.inputId + 'End']"
    :placeholder="placeholder"
    v-model="value"
    valueFormat="YYYY-MM-DD"
    v-bind="props.item.config"
    v-on="props.item.eventFunction ?? {}"
  >
    <template v-for="slotName in props.item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData"> </slot>
    </template>
  </el-date-picker>
</template>

<style scoped lang="scss"></style>
