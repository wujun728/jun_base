<script setup lang="ts">
import { computed, ref } from 'vue'
import type { FormItemTool } from '../../types/index.ts'
import { buildPlaceholder, type TextValue } from '../shared/form.ts'

interface Props {
  item: FormItemTool<'autocomplete'>
  allDisabled: boolean
  inputId?: string
}
const props = defineProps<Props>()
const emits = defineEmits<{
  keyUpEnter: [event: KeyboardEvent, item: FormItemTool<'autocomplete'>]
}>()
const value = defineModel<TextValue>('value')
const autocompleteRef = ref()

const placeholder = computed(() =>
  buildPlaceholder(props.allDisabled, '请输入', props.item.label)
)
const onEnter = (event: KeyboardEvent) => emits('keyUpEnter', event, props.item)
const getRef = () => autocompleteRef.value
defineExpose({
  getRef,
})
</script>
<template>
  <el-autocomplete
    ref="autocompleteRef"
    clearable
    :disabled="props.allDisabled"
    :id="props.inputId"
    :placeholder="placeholder"
    v-model="value"
    @keyup.enter="onEnter"
    v-bind="props.item.config"
    v-on="props.item.eventFunction ?? {}"
  >
    <template v-for="slotName in props.item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData"> </slot>
    </template>
  </el-autocomplete>
</template>

<style scoped lang="scss"></style>
