<script setup lang="ts">
import { computed, ref } from 'vue'
import type { FormItemTool } from '../../types/index.ts'
import { buildPlaceholder, type TextValue } from '../shared/form.ts'

type Props = {
  item: FormItemTool<'textarea'>
  allDisabled: boolean
  inputId?: string
}
const props = defineProps<Props>()
const emits = defineEmits<{
  keyUpEnter: [event: KeyboardEvent, item: FormItemTool<'textarea'>]
}>()
const value = defineModel<TextValue>('value')
const textareaRef = ref()

const placeholder = computed(() =>
  buildPlaceholder(props.allDisabled, '请输入', props.item.label)
)
const onEnter = (event: KeyboardEvent) => emits('keyUpEnter', event, props.item)
const getRef = () => textareaRef.value
defineExpose({
  getRef,
})
</script>
<template>
  <el-input
    ref="textareaRef"
    clearable
    :disabled="props.allDisabled"
    :id="props.inputId"
    :placeholder="placeholder"
    v-model="value"
    maxlength="150"
    type="textarea"
    show-word-limit
    @keyup.enter="onEnter"
    v-bind="props.item.config"
    v-on="props.item.eventFunction ?? {}"
  >
    <template v-for="slotName in props.item.slotNames" #[slotName]>
      <slot :name="slotName"> </slot>
    </template>
  </el-input>
</template>

<style scoped lang="scss"></style>
