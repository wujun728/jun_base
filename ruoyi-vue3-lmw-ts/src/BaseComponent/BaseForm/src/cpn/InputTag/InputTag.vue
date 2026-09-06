<script setup lang="ts">
import type { InputInstance } from 'element-plus'
import { computed, nextTick, ref } from 'vue'
import type { FormItemTool } from '../../types/index.ts'

interface Props {
  item: FormItemTool<'inputTag'>
  allDisabled: boolean
  inputId?: string
}

const props = defineProps<Props>()
const value = defineModel<string[]>('value')
const tagInputRef = ref()
const inlineInputRefs = ref<Record<string, InputInstance | null>>({})
const editIndex = ref(-1)
const editValue = ref('')

const placeholder = computed(() =>
  props.allDisabled ? '' : `请输入${props.item.label ?? ''}`
)
const hasCustomTagSlot = computed(() => props.item.slotNames?.includes('tag'))

const setInlineRef = (el: InputInstance | null, key: string) => {
  inlineInputRefs.value[key] = el
}

const handleTagClick = (value: string, index: number) => {
  editIndex.value = index
  editValue.value = value
  nextTick(() => {
    inlineInputRefs.value[value]?.focus()
  })
}

const onInlineEnter = (key: string) => {
  inlineInputRefs.value[key]?.blur()
}

const handleInlineBlur = (index: number) => {
  const values = [...(value.value ?? [])]
  values[index] = editValue.value
  value.value = values
  editValue.value = ''
  editIndex.value = -1
  nextTick(() => {
    tagInputRef.value?.focus?.()
  })
}

const getRef = () => tagInputRef.value
defineExpose({
  getRef,
})
</script>
<template>
  <el-input-tag
    ref="tagInputRef"
    :disabled="props.allDisabled"
    :id="props.inputId"
    :placeholder="placeholder"
    v-model="value"
    v-bind="props.item.config"
    v-on="props.item.eventFunction ?? {}"
  >
    <template #tag="{ value, index }" v-if="!hasCustomTagSlot">
      <div @click="handleTagClick(value, index)" v-show="index !== editIndex">
        <span>{{ value }}</span>
      </div>
      <el-input
        v-show="index === editIndex"
        :ref="(el) => setInlineRef(el as InputInstance | null, value)"
        size="small"
        v-model="editValue"
        @keyup.enter="onInlineEnter(value)"
        @blur="handleInlineBlur(index)"
      >
      </el-input>
    </template>

    <template v-for="slotName in props.item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData"> </slot>
    </template>
  </el-input-tag>
</template>

<style scoped lang="scss"></style>
