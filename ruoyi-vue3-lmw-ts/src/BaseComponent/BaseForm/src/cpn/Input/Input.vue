<script setup lang="ts">
import { computed, ref } from 'vue'
import type { FormItemTool } from '../../types/index.ts'
import { buildPlaceholder, type InputLikeValue } from '../shared/form.ts'

interface Props {
  item: FormItemTool<'input'>
  allDisabled: boolean
  inputId?: string
}

const props = defineProps<Props>()
const emits = defineEmits<{
  keyUpEnter: [event: KeyboardEvent, item: FormItemTool<'input'>]
}>()
const value = defineModel<InputLikeValue>('value')
const inputRef = ref()

const placeholder = computed(() =>
  buildPlaceholder(props.allDisabled, '请输入', props.item.label)
)

const onEnter = (event: KeyboardEvent) => emits('keyUpEnter', event, props.item)
const getRef = () => inputRef.value

defineExpose({
  getRef,
})
</script>
<template>
  <el-input
    ref="inputRef"
    clearable
    :disabled="props.allDisabled"
    :id="props.inputId"
    :placeholder="placeholder"
    v-model="value"
    v-bind="props.item.config"
    v-on="props.item.eventFunction ?? {}"
    @keyup.enter="onEnter"
  >
    <template v-for="slotName in props.item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData"> </slot>
    </template>
  </el-input>
</template>

<style scoped lang="scss"></style>
