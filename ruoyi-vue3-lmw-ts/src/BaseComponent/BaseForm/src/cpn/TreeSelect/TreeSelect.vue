<script setup lang="ts">
import { computed, ref } from 'vue'
import { getOptions } from '../../utils/index.ts'
import type { FormItemTool } from '../../types/index.ts'
import { buildPlaceholder, type SelectValue } from '../shared/form.ts'

type Props = {
  item: FormItemTool<'treeSelect'>
  allDisabled: boolean
  inputId?: string
}
const props = defineProps<Props>()
const value = defineModel<SelectValue>('value')
const treeSelectRef = ref()

const placeholder = computed(() =>
  buildPlaceholder(
    props.allDisabled,
    '请选择',
    props.item.label,
    props.item.config?.placeholder as string | undefined
  )
)
const getRef = () => treeSelectRef.value
defineExpose({
  getRef,
})
</script>
<template>
  <el-tree-select
    clearable
    ref="treeSelectRef"
    :disabled="props.allDisabled"
    :id="props.inputId"
    :placeholder="placeholder"
    :data="getOptions(props.item)"
    v-model="value"
    v-bind="props.item.config"
    v-on="props.item.eventFunction ?? {}"
  >
    <template v-for="slotName in props.item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData"> </slot>
    </template>
  </el-tree-select>
</template>

<style scoped lang="scss"></style>
