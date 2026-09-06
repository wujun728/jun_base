<script setup lang="ts">
import { ref } from 'vue'
import type { FormItemTool } from '../../types/index.ts'
import type { NumericValue } from '../shared/form.ts'

type Props = {
  item: FormItemTool<'inputNumber'>
  allDisabled: boolean
  inputId?: string
}
const props = defineProps<Props>()
const emits = defineEmits<{
  keyUpEnter: [event: KeyboardEvent, item: FormItemTool<'inputNumber'>]
}>()
const value = defineModel<NumericValue>('value')
const inputNumberRef = ref()

const onEnter = (event: KeyboardEvent) => emits('keyUpEnter', event, props.item)
const getRef = () => inputNumberRef.value
defineExpose({
  getRef,
})
</script>
<template>
  <el-input-number
    ref="inputNumberRef"
    clearable
    :disabled="props.allDisabled"
    :id="props.inputId"
    v-model="value"
    @keyup.enter="onEnter"
    v-bind="props.item.config"
    v-on="props.item.eventFunction ?? {}"
  >
    <template v-for="slotName in props.item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData"> </slot>
    </template>
  </el-input-number>
</template>

<style scoped lang="scss"></style>
